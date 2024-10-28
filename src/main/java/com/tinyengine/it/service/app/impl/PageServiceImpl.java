
package com.tinyengine.it.service.app.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.enums.Enums;
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.config.log.SystemServiceLog;
import com.tinyengine.it.mapper.AppExtensionMapper;
import com.tinyengine.it.mapper.AppMapper;
import com.tinyengine.it.mapper.BlockHistoryMapper;
import com.tinyengine.it.mapper.BlockMapper;
import com.tinyengine.it.mapper.I18nEntryMapper;
import com.tinyengine.it.mapper.PageHistoryMapper;
import com.tinyengine.it.mapper.PageMapper;
import com.tinyengine.it.model.dto.*;
import com.tinyengine.it.model.entity.App;
import com.tinyengine.it.model.entity.AppExtension;
import com.tinyengine.it.model.entity.Block;
import com.tinyengine.it.model.entity.Page;
import com.tinyengine.it.model.entity.PageHistory;
import com.tinyengine.it.model.entity.User;
import com.tinyengine.it.service.app.AppService;
import com.tinyengine.it.service.app.PageHistoryService;
import com.tinyengine.it.service.app.PageService;
import com.tinyengine.it.service.app.UserService;
import com.tinyengine.it.service.app.impl.v1.AppV1ServiceImpl;
import com.tinyengine.it.service.material.impl.BlockServiceImpl;

import lombok.extern.slf4j.Slf4j;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * The type Page service.
 *
 * @since 2024-10-20
 */
@Service
@Slf4j
public class PageServiceImpl implements PageService {
    /**
     * The Page mapper.
     */
    @Autowired
    private PageMapper pageMapper;

    /**
     * The App service.
     */
    @Autowired
    private AppService appService;

    /**
     * The App mapper.
     */
    @Autowired
    private AppMapper appMapper;

    /**
     * The User service.
     */
    @Autowired
    private UserService userService;

    /**
     * The Block mapper.
     */
    @Autowired
    private BlockMapper blockMapper;

    /**
     * The Block service.
     */
    @Autowired
    private BlockServiceImpl blockServiceImpl;

    /**
     * The Page history service.
     */
    @Autowired
    private PageHistoryService pageHistoryService;

    /**
     * The Page history mapper.
     */
    @Autowired
    private PageHistoryMapper pageHistoryMapper;

    /**
     * The App v 1 service.
     */
    @Autowired
    private AppV1ServiceImpl appV1ServiceImpl;

    /**
     * The Block history mapper.
     */
    @Autowired
    private BlockHistoryMapper blockHistoryMapper;

    /**
     * The App extension mapper.
     */
    @Autowired
    private AppExtensionMapper appExtensionMapper;

    /**
     * The 18 n entry mapper.
     */
    @Autowired
    private I18nEntryMapper i18nEntryMapper;

    /**
     * 通过appId查询page所有数据实现方法
     *
     * @param aid the aid
     * @return Page
     */
    @Override
    @SystemServiceLog(description = "通过appId查询page所有数据实现方法")
    public List<Page> queryAllPage(Integer aid) {
        return pageMapper.queryPageByApp(aid);
    }

    /**
     * 根据主键id查询表t_page信息
     *
     * @param id id
     * @return query result
     */
    @Override
    @SystemServiceLog(description = "通过Id查询page数据实现方法")
    public Page queryPageById(@Param("id") Integer id) {
        Page pageInfo = pageMapper.queryPageById(id);
        // 获取schemaMeta进行获取materialHistory中的framework进行判断
        String framework = appMapper.queryAppById(pageInfo.getApp()).getFramework();
        if (framework.isEmpty()) {
            throw new ServiceException(ExceptionEnum.CM312.getResultCode(), ExceptionEnum.CM312.getResultMsg());
        }
        if (pageInfo.getIsPage()) {
            // 这里不保证能成功获取区块的列表，没有区块或获取区块列表不成功返回 {}
            Map<String, List<String>> blockAssets = blockServiceImpl.getBlockAssets(pageInfo.getPageContent(),
                    framework);
            pageInfo.setAssets(blockAssets);
            return addIsHome(pageInfo);
        }
        return pageInfo;
    }

    /**
     * 根据条件查询表t_page数据
     *
     * @param page page
     * @return query result
     */
    @Override
    @SystemServiceLog(description = "通过条件查询page数据实现方法")
    public List<Page> queryPageByCondition(Page page) {
        return pageMapper.queryPageByCondition(page);
    }

    /**
     * 通过appId删除page数据实现方法
     *
     * @param id the id
     * @return deleted page
     */
    @Override
    @SystemServiceLog(description = "通过appId删除page数据实现方法")
    public Result<Page> delPage(Integer id) {
        // 获取页面信息
        Page pages = queryPageById(id);
        // 判断是页面还是文件夹
        if (!pages.getIsPage()) {
            // 如果是文件夹，调folder service的处理逻辑
            return del(id);
        }
        // 保护默认页面
        protectDefaultPage(pages, id);

        // 删除
        Page pageResult = pageMapper.queryPageById(id);
        int result = pageMapper.deletePageById(id);
        if (result < 1) {
            return Result.failed(ExceptionEnum.CM001);
        }
        return Result.success(pageResult);
    }

    /**
     * 新增表t_page数据
     *
     * @param page page
     * @return created page
     */
    @Override
    @SystemServiceLog(description = "createPage 创建页面实现方法")
    public Result<Page> createPage(Page page) {
        // 判断isHome 为true时，parentId 不为0，禁止创建
        if (page.getIsHome() && !"0".equals(page.getParentId())) {
            return Result.failed("Homepage can only be set in the root directory");
        }
        // 将前端创建页面传递过来的staic/public 设置为 staticPages/publicPages
        if (!page.getGroup().isEmpty() && Arrays.asList("static", "public").contains(page.getGroup())) {
            page.setGroup(page.getGroup() + "Pages");
        }
        String userId = "1";
        page.setOccupierBy(userId);
        page.setIsDefault(false);
        page.setDepth(0);

        List<Page> pageResult = queryPages(page);
        if (!pageResult.isEmpty()) {
            return Result.failed(ExceptionEnum.CM003);
        }
        int result = pageMapper.createPage(page);
        if (result < 1) {
            return Result.failed(ExceptionEnum.CM001);
        }
        Page pageInfo = queryPageById(page.getId());
        // 对isHome 字段进行特殊处理
        if (page.getIsHome()) {
            boolean res = setAppHomePage(Math.toIntExact(page.getApp()), pageInfo.getId());
            if (!res) {
                return Result.failed(ExceptionEnum.CM001);
            }
        }

        pageInfo.setIsHome(page.getIsHome());
        return Result.success(pageInfo);
    }

    private List<Page> queryPages(Page page) {
        Page pageParam = new Page();
        pageParam.setName(page.getName());
        pageParam.setApp(page.getApp());
        return pageMapper.queryPageByCondition(pageParam);
    }

    /**
     * 创建文件夹实现方法
     *
     * @param page the page
     * @return Page
     */
    @Override
    @SystemServiceLog(description = "createFolder 创建文件夹实现方法")
    public Result<Page> createFolder(Page page) {
        String parentId = page.getParentId();
        // 通过parentId 计算depth
        Result<Integer> depthResult = getDepth(parentId);
        if (!depthResult.isSuccess()) {
            return Result.failed(depthResult.getMessage());
        }
        int depth = depthResult.getData();
        page.setDepth(depth + 1);
        page.setGroup("staticPages");
        page.setIsDefault(false);
        page.setIsBody(true);
        // needTODO 获取user的ID
        String userId = "1";
        page.setOccupierBy(userId);
        List<Page> pageResult = queryPages(page);
        if (!pageResult.isEmpty()) {
            return Result.failed(ExceptionEnum.CM003);
        }
        int result = pageMapper.createPage(page);
        if (result < 1) {
            return Result.failed(ExceptionEnum.CM001);
        }
        Page pageInfo = queryPageById(page.getId());
        return Result.success(pageInfo);
    }

    /**
     * 更新页面实现方法
     *
     * @param page the page
     * @return Page
     */
    @SystemServiceLog(description = "updatePage 更新页面实现方法")
    @Override
    public Result<Page> updatePage(Page page) {
        int id = page.getId();
        boolean isHomeVal = false;
        Page pageTemp = queryPageById(page.getId());
        if (!validateIsHome(page, pageTemp)) {
            return Result.failed("isHome parameter error");
        }
        int appId = pageTemp.getApp();
        // 保护默认页面
        protectDefaultPage(pageTemp, appId);

        // 针对参数中isHome的传值进行isHome字段的判定
        if (page.getIsHome()) {
            setAppHomePage(appId, id);
            isHomeVal = true;
        } else {
            // 判断页面原始信息中是否为首页
            if (pageTemp.getIsHome()) {
                setAppHomePage(appId, 0);
            }
        }

        page.setIsHome(isHomeVal);
        pageTemp.setMessage(page.getMessage());
        // 保存成功，异步生成页面历史记录快照,不保证生成成功
        PageHistory pageHistory = new PageHistory();

        // 把Pages中的属性值赋值到PagesHistories中
        BeanUtils.copyProperties(pageTemp, pageHistory);
        pageHistory.setPage(pageTemp.getId());
        pageHistory.setId(null);
        int result = pageHistoryService.createPageHistory(pageHistory);
        if (result < 1) {
            return Result.failed(ExceptionEnum.CM001);
        }

        return checkUpdate(page);
    }

    /**
     * 更新页面文件夹
     *
     * @param page Page
     * @return Page
     */
    @SystemServiceLog(description = "update 更新文件夹实现方法")
    @Override
    public Result<Page> update(Page page) {
        String parentId = page.getParentId();
        // 校验parentId 带来的深度改变
        if (parentId != null) {
            Result<Integer> depthInfo = verifyParentId(parentId);
            if (!depthInfo.isSuccess()) {
                return Result.failed("parentId is invalid");
            }
            page.setDepth(depthInfo.getData() + 1);
        }
        // getFolder 获取父类信息
        Page parentInfo = pageMapper.queryPageById(page.getId());
        // 当更新参数中没有depth 或 depth没有发生改变时
        if (page.getDepth().equals(parentInfo.getDepth())) {
            int result = pageMapper.updatePageById(page);
            if (result < 1) {
                return Result.failed(ExceptionEnum.CM001);
            }
            Page pagesResult = queryPageById(page.getId());
            return Result.success(pagesResult);
        }
        return Result.failed(ExceptionEnum.CM002);
    }

    /**
     * 获取 页面（虽然这个接口数据跟页面没啥关系）/区块的预览元数据
     *
     * @param previewParam previewParam
     * @return { PreviewDto }
     */
    @SystemServiceLog(description = "getPreviewMetaData 获取预览元数据实现方法")
    @Override
    public PreviewDto getPreviewMetaData(PreviewParam previewParam) {
        String type = previewParam.getType();
        PreviewDto previewDto;
        if (Enums.Schema2CodeType.BLOCK.getValue().equals(type)) {
            previewDto = getBlockPreviewMetaData(previewParam);
            return previewDto;
        }
        previewDto = appService.getAppPreviewMetaData(previewParam.getApp());
        return previewDto;
    }

    /**
     * Sets app home page.
     *
     * @param appId  the app id
     * @param pageId the page id
     * @return the app home page
     */
    public boolean setAppHomePage(int appId, int pageId) {
        // updateApp
        App app = new App();
        app.setId(appId);
        app.setHomePage(pageId);

        int result = appMapper.updateAppById(app);
        return result >= 1;
    }

    /**
     * 获取文件夹深度
     *
     * @param parentId the parent id
     * @return depth
     */
    public Result<Integer> getDepth(String parentId) {
        int parent = Integer.parseInt(parentId);
        if (parent == 0) {
            return Result.success(0);
        }
        // getFolder 获取父类信息
        Page parentInfo = pageMapper.queryPageById(parent);
        int depth = parentInfo.getDepth();
        if (depth < 5) {
            return Result.success(depth);
        }
        return Result.failed("Exceeded depth");
    }

    /**
     * Add is home page.
     *
     * @param pageInfo the page info
     * @return the page
     */
    public Page addIsHome(Page pageInfo) {
        if (pageInfo == null) {
            return pageInfo;
        }
        int appId = pageInfo.getApp();
        int appHomePageId = getAppHomePageId(appId);
        pageInfo.setIsHome((pageInfo.getId()) == appHomePageId);

        return pageInfo;
    }

    /**
     * Gets app home page id.
     *
     * @param appId the app id
     * @return the app home page id
     */
    public int getAppHomePageId(int appId) {
        App appInfo = appMapper.queryAppById(appId);
        // appHomePageId 存在为null的情况，即app没有设置首页
        Integer homePage = appInfo.getHomePage();

        // 将 homePage 转换为整数，如果为空则默认为 0
        int id;
        if (homePage == null) {
            id = 0;
            return id;
        }
        id = homePage;
        return id;
    }

    /**
     * Del result.
     *
     * @param id the id
     * @return the result
     */
    public Result<Page> del(Integer id) {
        // 这里只需要判断page表中是否存在子节点即可
        Page page = new Page();
        page.setParentId(id.toString());
        List<Page> subFolders = pageMapper.queryPageByCondition(page);
        if (!subFolders.isEmpty()) {
            return Result.failed("此文件夹不是空文件夹，不能删除！");
        }
        return checkDelete(id);
    }

    /**
     * Check delete result.
     *
     * @param id the id
     * @return the result
     */
    public Result<Page> checkDelete(Integer id) {
        // needTODO 从缓存中获取的user信息
        User user = userService.queryUserById(1);
        Page page = pageMapper.queryPageById(id);
        User occupier = page.getOccupier();

        // 如果当前页面没人占用 或者是自己占用 可以删除该页面
        if (iCanDoIt(occupier, user)) {
            pageMapper.deletePageById(id);

            return Result.success(page);
        }

        return Result.failed("The current page is being edited by" + occupier.getUsername());
    }

    /**
     * 判断是否能删除
     *
     * @param occupier the occupier
     * @param user     the user
     * @return boolean
     */
    public boolean iCanDoIt(User occupier, User user) {
        if ("public".equals(user.getUsername())) {
            return true;
        }

        return occupier == null || occupier.getId().equals(user.getId());
    }

    /**
     * 保护默认页面
     *
     * @param pages the pages
     * @param id    the id
     */
    public void protectDefaultPage(Page pages, Integer id) {
        if (pages.getIsDefault()) {
            // 查询是否是模板应用，不是的话不能删除或修改
            App app = appMapper.queryAppById(id);
            if (app.getTemplateType() == null) {
                Result.failed(ExceptionEnum.CM310.getResultCode());
            }
        }
    }

    /**
     * Validate is home boolean.
     *
     * @param param    the param
     * @param pageInfo the page info
     * @return the boolean
     */
    public boolean validateIsHome(Page param, Page pageInfo) {
        boolean isHomeOld = pageInfo.getIsHome();
        int parentIdOld = Integer.parseInt(pageInfo.getParentId());
        boolean isHome = param.getIsHome();
        int parentId = Integer.parseInt(param.getParentId());
        // 当isHome 为 true ， 但是 parentId 大于0时 非法
        if (isHome && parentId > 0) {
            return false;
        }
        // 当isHome 为 true parentId 不存在 parentIdOld 大于0时 非法
        if (isHome && parentId == -1 && parentIdOld > 0) {
            return false;
        }
        // 当isHome 不存在 且 isHomeOld 为true时 将parentId 设为其他id 时非法
        return isHome || !isHomeOld || parentId <= 0;
    }

    /**
     * Check update result.
     *
     * @param page the page
     * @return the result
     */
    public Result<Page> checkUpdate(Page page) {
        // 获取占用着occupier todo 获取的时候从page实体类中获取是个对象
        User occupier = userService.queryUserById(Integer.parseInt(page.getOccupierBy()));
        // 当前页面没有被锁定就请求更新页面接口，提示无权限
        if (occupier == null) {
            Result.failed("Please unlock the page before editing the page");
        }
        // 当页面被人锁定时，如果提交update请求的人不是当前用户，提示无权限
        // needTODO 从缓存中获取登录用户信息
        User user = userService.queryUserById(1);
        if (!user.getId().equals(occupier.getId())) {
            Result.failed("The current page is being edited by" + occupier.getUsername());
        }
        pageMapper.updatePageById(page);
        // 修改完返回页面还是返回dto，为了下次修改每次参数属性一致
        Page pagesResult = queryPageById(page.getId());
        return Result.success(pagesResult);
    }

    /**
     * 校验parentId
     *
     * @param parentId the parent id
     * @return map
     */
    public Result<Integer> verifyParentId(String parentId) {
        if (Pattern.matches("^[0-9]+$", parentId)) {
            return getDepth(parentId);
        }

        return Result.failed("parentId is invalid");
    }

    /**
     * 主函数
     *
     * @param pid    the pid
     * @param target the target
     * @return update tree
     */
    public TreeNodeCollection getUpdateTree(int pid, int target) {
        TreeNodeCollection collection = new TreeNodeCollection();
        TreeNodeDto treeNodeDto = new TreeNodeDto();
        treeNodeDto.setPids(new ArrayList<>(Arrays.asList(pid)));
        treeNodeDto.setLevel(target + 1);
        treeNodeDto.setCollection(collection);

        TreeNodeCollection getTreeNodesResult = getTreeNodes(treeNodeDto);
        if (getTreeNodesResult.getRange().isEmpty()) {
            return rangeData;
        }
        return getTreeNodesResult;
    }

    /**
     * 计算当前parent的深度信息
     *
     * @param treeNodeDto the treeNodeDto
     * @return the tree nodes
     */
    public TreeNodeCollection getTreeNodes(TreeNodeDto treeNodeDto) {
        int level = treeNodeDto.getLevel();
        TreeNodeCollection collection = treeNodeDto.getCollection();
        List<Integer> pids = treeNodeDto.getPids();

        // 没有子节点，返回收集的节点信息
        if (pids.isEmpty()) {
            return rangeData;
        }
        // 当前的节点深度超过 配置的最大深度，返回失败信息
        if (level > 5) {
            throw new ServiceException("400", "Exceeded depth");
        }
        // 获取子节点的id
        List<Integer> childrenId = getChildrenId(pids);
        // 收集 id depth 信息
        List<NodeData> dps =
            childrenId.stream().map(id -> new NodeData(id, level)).collect(Collectors.toList());
        // 使用 addAll 方法将 childrenId 追加到 range
        rangeData.getRange().addAll(childrenId);
        rangeData.getData().addAll(dps);

        // 递归
        TreeNodeDto treeNodeParam = new TreeNodeDto();
        treeNodeParam.setPids(childrenId);
        treeNodeParam.setLevel(level + 1);
        treeNodeParam.setCollection(collection);
        return getTreeNodes(treeNodeParam);
    }

    /**
     * 获取 parentId 数组下的所有子节点
     *
     * @param pids the pids
     * @return the children id
     */
    public List<Integer> getChildrenId(List<Integer> pids) {
        // 构建查询条件
        QueryWrapper<Page> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("parent_id", pids);
        List<Page> children = pageMapper.selectList(queryWrapper);
        if (children.isEmpty()) {
            return new ArrayList<>();
        }

        return children.stream().map(Page::getId).collect(Collectors.toList());
    }

    /**
     * 获取区块预览元数据
     *
     * @param previewParam the preview param
     * @return PreviewDto block preview meta data
     */
    public PreviewDto getBlockPreviewMetaData(PreviewParam previewParam) {
        Block block = blockMapper.queryBlockById(previewParam.getId());
        // 拼装工具类
        AppExtension appExtension = new AppExtension();
        appExtension.setApp(previewParam.getApp());
        List<AppExtension> extensionsList = appExtensionMapper.queryAppExtensionByCondition(appExtension);
        Map<String, List<SchemaUtils>> extensions = appV1ServiceImpl.getSchemaExtensions(extensionsList);
        List<SchemaUtils> utils = extensions.get("utils");
        // 拼装数据源
        Map<String, Object> dataSource = (Map<String, Object>) block.getContent().get("dataSource");
        // 拼装国际化词条
        List<I18nEntryDto> i18ns = i18nEntryMapper.findI18nEntriesByHostandHostType(previewParam.getId(), "block");
        SchemaI18n i18n =
            appService.formatI18nEntrites(i18ns, Enums.E_i18Belongs.BLOCK.getValue(), previewParam.getId());

        PreviewDto previewDto = new PreviewDto();
        previewDto.setDataSource(dataSource);
        previewDto.setI18n(i18n);
        previewDto.setUtils(utils);
        return previewDto;
    }
}

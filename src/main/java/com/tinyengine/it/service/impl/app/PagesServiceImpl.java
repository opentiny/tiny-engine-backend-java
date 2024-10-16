package com.tinyengine.it.service.impl.app;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tinyengine.it.config.Enums;
import com.tinyengine.it.config.SystemServiceLog;
import com.tinyengine.it.exception.ExceptionEnum;
import com.tinyengine.it.exception.ServiceException;
import com.tinyengine.it.mapper.*;
import com.tinyengine.it.model.dto.Collection;
import com.tinyengine.it.model.dto.*;
import com.tinyengine.it.model.entity.*;
import com.tinyengine.it.service.app.AppsService;
import com.tinyengine.it.service.app.PagesHistoriesService;
import com.tinyengine.it.service.app.PagesService;
import com.tinyengine.it.service.app.UsersPermissionsUserService;
import com.tinyengine.it.service.impl.app.v1.AppsV1ServiceImpl;
import com.tinyengine.it.service.impl.material.BlocksServiceImpl;
import com.tinyengine.it.utils.ExecuteJavaScript;
import com.tinyengine.it.utils.Utils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PagesServiceImpl implements PagesService {

    @Autowired
    PagesMapper pagesMapper;
    @Autowired
    AppsService appsService;
    @Autowired
    UsersPermissionsUserService usersPermissionsUserService;
    @Autowired
    AppsV1ServiceImpl appsV1Service;
    @Autowired
    BlocksMapper blocksMapper;
    @Autowired
    BlocksServiceImpl blocksServiceImpl;
    @Autowired
    PagesHistoriesService pagesHistoriesService;
    @Autowired
    PagesHistoriesMapper pagesHistoriesMapper;
    @Autowired
    AppsV1ServiceImpl appsV1ServiceImpl;
    @Autowired
    BlockHistoriesMapper blockHistoriesMapper;
    @Autowired
    AppExtensionsMapper appExtensionsMapper;
    @Autowired
    I18nEntriesMapper i18nEntriesMapper;
    @Value("${path.dslScriptPath}")
    private String scriptPath;

    /**
     * 根据主键id查询表pages信息
     *
     * @param id
     */
    @Override
    public PageDto findPagesById(@Param("id") Integer id) throws ServiceException {
        return pagesMapper.findPagesById(id);
    }

    /**
     * 根据条件查询表pages数据
     *
     * @param pages
     */
    @Override
    public List<PageDto> findPagesByCondition(Pages pages) throws ServiceException {
        return pagesMapper.findPagesByCondition(pages);
    }


    /**
     * 创建页面
     *
     * @param pages
     */
    @SystemServiceLog(description = "createPage 创建页面 实现类")
    @Override
    public PageDto createPage(Pages pages) {
        // 判断isHome 为true时，parentId 不为0，禁止创建
        if (pages.getIsHome() && !pages.getParentId().equals("0")) {
            throw new RuntimeException("Homepage can only be set in the root directory");
        }
        // 将前端创建页面传递过来的staic/public 设置为 staticPages/publicPages
        if (!pages.getGroup().isEmpty() && Arrays.asList("static", "public").contains(pages.getGroup())) {
            pages.setGroup(pages.getGroup() + "Pages");

        }
        int occupier = 86; // todo 获取的是user.id
        pages.setOccupier(occupier);
        pages.setIsDefault(false);
        pages.setDepth(0);
        pagesMapper.createPages(pages);
        PageDto pageInfo = findPagesById(pages.getId());
        // 对isHome 字段进行特殊处理
        if (pages.getIsHome()) {
            boolean res = setAppHomePage((int) pages.getApp().longValue(), pageInfo.getId());
            if (!res) {
                // return res 待商议
                return null;

            }
        }

        pageInfo.setIsHome(pages.getIsHome());
        return pageInfo;

    }

    public boolean setAppHomePage(int appId, int pageId) {
        // updateApp
        Apps apps = new Apps();
        apps.setId(appId);
        apps.setHomePage((long) pageId);

        updateApp(apps);
        // 如果更新extend_config字段，从platform获取数据，继承非route部分
        return true;
    }

    public void updateApp(Apps apps) {
        // app 的update
        // todo 判断是超级管理员等
        appsService.updateAppsById(apps);

    }

    /**
     * 创建文件夹
     *
     * @param pages
     */
    @SystemServiceLog(description = "createFolder 创建文件夹 实现类")
    @Override
    public PageDto createFolder(Pages pages) {
        String parentId = pages.getParentId();
        // 通过parentId 计算depth
        Map<String, Object> depthResult = getDepth(parentId);
        if (depthResult.get("error") != null) {
            // todo 有错误信息直接返回depthResult，但java中不能返回两种类型，暂时返回null
            return null;
        }
        int depth = (int) depthResult.get("depth");
        pages.setDepth(depth + 1);
        pages.setGroup("staticPages");
        pages.setIsDefault(false);
        pages.setIsBody(true);
        // todo 获取user的ID
        int occupier = 86;
        pages.setOccupier(occupier);
        pagesMapper.createPages(pages);
        return findPagesById(pages.getId());

    }


    /**
     * 获取文件夹深度
     *
     * @param parentId
     * @return
     */
    public Map<String, Object> getDepth(String parentId) {
        int parentIdInt = Integer.parseInt(String.valueOf(parentId));
        Map<String, Object> result = new HashMap<>();
        if (parentIdInt == 0) {
            result.put("depth", 0);
            return result;
        }
        // getFolder 获取父类信息
        PageDto parentInfo = pagesMapper.findPagesById(parentIdInt);
        int depth = parentInfo.getDepth();
        if (depth < 5) {
            result.put("depth", depth);
            return result;
        }
        result.put("error", "Exceeded depth");

        return result;
    }

    /**
     * 删除页面
     *
     * @param id
     * @return
     * @throws ServiceException
     */
    @SystemServiceLog(description = "delPage 删除页面 实现类")
    @Override
    public Result<PageDto> delPage(@Param("id") Integer id) throws Exception {
        // 获取页面信息
        PageDto pages = getPageById(id);
        // 判断是页面还是文件夹
        if (!pages.getIsPage()) {
            // 如果是文件夹，调folder service的处理逻辑
            return del(id);
        }
        // 保护默认页面
        protectDefaultPage(pages, id);

        // 删除
        PageDto pageResult = pagesMapper.findPagesById(id);
        pagesMapper.deletePagesById(id);

        return Result.success(pageResult);
    }


    /**
     * 根据页面id去获取页面里的页面信息
     *
     * @param id
     * @return
     * @throws Exception
     */
    @SystemServiceLog(description = "getPageById 根据页面id去获取页面里的页面信息 实现类")
    @Override
    public PageDto getPageById(Integer id) throws Exception {
        PageDto pageInfo = pagesMapper.findPagesById(id);
        //  获取schemaMeta进行获取materialHistory中的framework进行判断
        String framework = appsV1Service.setMeta(pageInfo.getApp().intValue()).getMaterialHistory().getFramework();

        if (framework.isEmpty()) {
            throw new ServiceException(ExceptionEnum.CM312.getResultCode(), ExceptionEnum.CM312.getResultMsg());
        }
        if (pageInfo.getIsPage()) {
            // 这里不保证能成功获取区块的列表，没有区块或获取区块列表不成功返回 {}
            Map<String, List<String>> blockAssets = blocksServiceImpl.getBlockAssets(pageInfo.getPageContent(), framework);
            pageInfo.setAssets(blockAssets);
            return addIsHome(pageInfo);
        }

        return pageInfo;
    }


    public PageDto addIsHome(PageDto pageInfo) {
        if (pageInfo == null) {
            return pageInfo;
        }
        long appId = pageInfo.getApp();
        long appHomePageId = getAppHomePageId(appId);
        pageInfo.setIsHome((pageInfo.getId()) == appHomePageId);

        return pageInfo;
    }

    public Long getAppHomePageId(Long appId) {
        Apps appInfo = appsService.findAppsById(appId.intValue());
        // appHomePageId 存在为null的情况，即app没有设置首页
        Long homePage = appInfo.getHomePage();

        // 将 homePage 转换为整数，如果为空则默认为 0
        long id;
        if (homePage == null) {
            id = 0L;
            return id;
        }
        id = homePage;
        return id;
    }

    public Result<PageDto> del(Integer id) {
        // 这里只需要判断page表中是否存在子节点即可
        Pages pages = new Pages();
        pages.setParentId(id.toString());
        List<PageDto> subFolders = pagesMapper.findPagesByCondition(pages);
        if (!subFolders.isEmpty()) {
            return Result.failed("此文件夹不是空文件夹，不能删除！");
        }
        Result<PageDto> result = checkDelete(id);
        return result;

    }


    public Result<PageDto> checkDelete(Integer id) {
        // todo 从缓存中获取的user信息
        UsersPermissionsUser user = usersPermissionsUserService.findUsersPermissionsUserById(86);
        UsersPermissionsUser occupier = pagesMapper.findPagesById(id).getOccupier();
        // 如果当前页面没人占用 或者是自己占用 可以删除该页面
        if (iCanDoIt(occupier, user)) {
            PageDto pages = pagesMapper.findPagesById(id);
            pagesMapper.deletePagesById(id);

            return Result.success(pages);
        }

        return Result.failed("The current page is being edited by" + occupier.getUsername() + occupier.getResetPasswordToken());
    }

    /**
     * 判断是否能删除
     *
     * @param occupier
     * @param user
     * @return
     */
    public boolean iCanDoIt(UsersPermissionsUser occupier, UsersPermissionsUser user) {
        if (user.getResetPasswordToken().equals("p_webcenter")) {
            return true;

        }

        return occupier != null ? occupier.getId().equals(user.getId()) : true;
    }

    /**
     * 保护默认页面
     *
     * @param pages
     * @param id
     */
    public void protectDefaultPage(PageDto pages, Integer id) {
        if (pages.getIsDefault()) {
            // 查询是否是模板应用，不是的话不能删除或修改
            Apps apps = appsService.findAppsById(id);
            if (apps.getTemplateType() == null) {
                Result.failed(ExceptionEnum.CM310.getResultCode());
            }

        }
    }

    /**
     * 更新页面
     *
     * @param pages
     * @return
     * @throws Exception
     */
    @SystemServiceLog(description = "updatePage 更新页面 实现类")
    @Override
    public Result<PageDto> updatePage(Pages pages) throws Exception {
        int id = pages.getId();
        boolean isHomeVal = false;
        PageDto pageTemp = getPageById(pages.getId());
        if (!validateIsHome(pages, pageTemp)) {
            return Result.failed("isHome parameter error");
        }
        int appId = pageTemp.getApp().intValue();
        // 保护默认页面
        protectDefaultPage(pageTemp, appId);

        // 针对参数中isHome的传值进行isHome字段的判定
        if (pages.getIsHome() == null) {
            long res = getAppHomePageId(pageTemp.getApp());
            isHomeVal = id == res;

        } else if (pages.getIsHome()) {
            setAppHomePage(appId, id);
            isHomeVal = true;
        } else {
            // 判断页面原始信息中是否为首页
            if (pageTemp.getIsHome()) {
                setAppHomePage(appId, 0);

            }
        }

        pages.setIsHome(isHomeVal);
        pageTemp.setMessage(pages.getMessage());
        // 保存成功，异步生成页面历史记录快照,不保证生成成功
        PagesHistories pagesHistories = new PagesHistories();

        BeanUtils.copyProperties(pageTemp, pagesHistories); // 把Pages中的属性值赋值到PagesHistories中
        pagesHistories.setPage(Long.valueOf(pageTemp.getId()));
        pagesHistories.setId(null);
        pagesHistoriesService.createPagesHistories(pagesHistories);

        return checkUpdate(pages);
    }


    public boolean validateIsHome(Pages param, PageDto pageInfo) {
        boolean isHomeOld = pageInfo.getIsHome();
        int parentIdOld = Integer.parseInt(pageInfo.getParentId());
        boolean isHome = param.getIsHome();
        int parentId = Integer.parseInt(param.getParentId());
        // 当isHome 为 true ， 但是 parentId 大于0时 非法
        if (isHome && parentId > 0) {
            return false;
        }
        // 当isHome 为 true parentId 不存在  parentIdOld 大于0时 非法
        if (isHome && parentId == -1 && parentIdOld > 0) {
            return false;
        }
        // 当isHome 不存在 且 isHomeOld 为true时 将parentId 设为其他id 时非法
        if (!isHome && isHomeOld && parentId > 0) {
            return false;
        }
        return true;

    }

    public Result<PageDto> checkUpdate(Pages pages) throws Exception {
        // 获取占用着occupier todo 获取的时候从page实体类中获取是个对象
        UsersPermissionsUser occupier = usersPermissionsUserService.findUsersPermissionsUserById(pages.getOccupier());
        // 当前页面没有被锁定就请求更新页面接口，提示无权限
        if (occupier == null) {
            Result.failed("Please unlock the page before editing the page");
        }
        // 当页面被人锁定时，如果提交update请求的人不是当前用户，提示无权限
        // todo 从缓存中获取登录用户信息
        UsersPermissionsUser user = usersPermissionsUserService.findUsersPermissionsUserById(86);
        if (!user.getId().equals(occupier.getId())) {
            Result.failed("The current page is being edited by" + occupier.getUsername() + "(" + occupier.getResetPasswordToken() + ")");
        }
        pagesMapper.updatePagesById(pages);
        // 修改完返回页面还是返回dto，为了下次修改每次参数属性一致
        PageDto pagesResult = getPageById(pages.getId());
        return Result.success(pagesResult);
    }


    /**
     * 更新页面文件夹
     *
     * @param pages
     * @return
     */
    @SystemServiceLog(description = "update 更新页面文件夹 实现类")
    @Override
    public Result<PageDto> update(Pages pages) throws Exception {
        String parentId = pages.getParentId();
        // 校验parentId 带来的深度改变
        if (parentId != null) {
            Map<String, Object> depthInfo = verifyParentId(parentId);
            if (depthInfo.get("error") != null) {
                return Result.failed("parentId is invalid");
            }
            pages.setDepth((int) depthInfo.get("depth") + 1);
        }
        // getFolder 获取父类信息
        PageDto parentInfo = pagesMapper.findPagesById(pages.getId());
        // 当更新参数中没有depth 或 depth没有发生改变时
        if (pages.getDepth().equals(parentInfo.getDepth())) {
            pagesMapper.updatePagesById(pages);
            PageDto pagesResult = getPageById(pages.getId());
            return Result.success(pagesResult);
        }
        // 当文件夹改变父级且没有平级移动时
        Collection collection = getUpdateTree(pages.getId(), pages.getDepth());
        if (collection == null) {
            return checkUpdate(pages);
        }

        return null;
    }


    /**
     * 校验parentId
     *
     * @param parentId
     * @return
     */
    public Map<String, Object> verifyParentId(String parentId) {
        if (Pattern.matches("^[0-9]+$", parentId.toString())) {
            return getDepth(parentId);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("error", "parentId is invalid");
        return result;
    }


    /**
     * 主函数
     *
     * @param pid
     * @param target
     * @return
     * @throws Exception
     */
    public Collection getUpdateTree(int pid, int target) throws Exception {
        Collection collection = new Collection();
        Map<String, Object> param = new HashMap<>();
        param.put("collection", collection);
        param.put("pids", Collections.singletonList(pid));
        param.put("level", target + 1);
        Collection getTreeNodesResult = getTreeNodes(param);
        if (getTreeNodesResult.getRange().isEmpty()) {
            return null;
        }
        return getTreeNodesResult;

    }


    /**
     * 计算当前parent的深度信息
     *
     * @param map
     */
    public Collection getTreeNodes(Map<String, Object> map) throws Exception {
        int level = (int) map.get("level");
        Collection collection = new Collection();
        Object obj = map.get("pids");
        List<Integer> pids = new ArrayList<>();

        if (obj instanceof List<?>) {
            List<?> pidsList = (List<?>) obj;
            pids = pidsList.stream()
                    .map(element -> (Integer) element)
                    .collect(Collectors.toList());

        }
        // 没有子节点，返回收集的节点信息
        if (pids.isEmpty()) {
            return collection;
        }
        // 当前的节点深度超过 配置的最大深度，返回失败信息
        if (level > 5) {
            throw new Exception("Exceeded depth");
        }
        // 获取子节点的id
        List<Integer> childrenId = getChildrenId(pids);
        // 收集 id depth 信息
        List<SimpleEntry<Integer, Integer>> dps = childrenId.stream()
                .map(id -> new SimpleEntry<>(id, level)) // 或者使用 SimpleEntry
                .collect(Collectors.toList());
        // 使用 addAll 方法将 childrenId 追加到 range
        collection.getRange().addAll(childrenId);
        collection.getData().addAll(dps);

        // 递归
        Map<String, Object> mapParam = new HashMap<>();
        mapParam.put("pids", childrenId);
        mapParam.put("level", level + 1);
        mapParam.put("collection", collection);

        return getTreeNodes(mapParam);
    }


    /**
     * 获取 parentId 数组下的所有子节点
     *
     * @param pids
     */
    public List<Integer> getChildrenId(List<Integer> pids) {
        // 构建查询条件
        QueryWrapper<Pages> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("parent_id", pids);
        List<Pages> children = pagesMapper.selectList(queryWrapper);
        if (children.isEmpty()) {
            return new ArrayList<>();
        }

        return children.stream()
                .map(Pages::getId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> schema2Code(SchemaCodeParam schemaCodeParam) {
        String type = Enums.E_Schema2CodeType.PAGE.getValue();
        List<Map<String, Object>> schemaCode = translateSchema(schemaCodeParam, type);
        return schemaCode;
    }

    /**
     * 获取 页面（虽然这个接口数据跟页面没啥关系）/区块的预览元数据
     *
     * @param { E_Schema2CodeType } type 页面还是区块
     * @param { number|string } id 页面或区块id
     * @param { number|string } app 应用id
     * @return { PreviewDto }
     */
    @SystemServiceLog(description = "getPreviewMetaData 获取预览元数据")
    @Override
    public PreviewDto getPreviewMetaData(PreviewParam previewParam) {
        String type = previewParam.getType();
        PreviewDto previewDto;
        if (Enums.E_Schema2CodeType.BLOCK.getValue().equals(type)) {
            previewDto = getBlockPreviewMetaData(previewParam);
            return previewDto;
        }
        previewDto = appsService.getAppPreviewMetaData(previewParam.getApp());
        return previewDto;
    }

    /**
     * 调用dsl转换页面或区块的schema为代码
     *
     * @param dslCodeParam
     * @returns List<Map < String, Object>> dsl库返回解析代码的对象
     */
    @SystemServiceLog(description = "dslCode 调用dsl转换页面或区块的schema为代码")
    @Override
    public List<Map<String, Object>> dslCode(DslCodeParam dslCodeParam) {
        String type = dslCodeParam.getType();
        Map<String, Object> schema;
        String name;
        // 如果history参数存在，则要获取对应历史记录的schema
        if (Enums.E_Schema2CodeType.BLOCK.getValue().equals(type)) {
            if (dslCodeParam.getHistory() != null) {
                BlockHistories blockHistories = blockHistoriesMapper.findBlockHistoriesById(dslCodeParam.getHistory());
                schema = blockHistories.getContent();
                name = blockHistories.getLabel();
            } else {
                Blocks blocks = blocksMapper.findBlocksById(dslCodeParam.getId());
                schema = blocks.getContent();
                name = blocks.getLabel();
            }
        } else {
            if (dslCodeParam.getHistory() != null) {
                PagesHistories pagesHistories = pagesHistoriesMapper.findPagesHistoriesById(dslCodeParam.getHistory());
                schema = pagesHistories.getPageContent();
                name = pagesHistories.getName();
            } else {
                PageDto pages = pagesMapper.findPagesById(dslCodeParam.getId());
                schema = pages.getPageContent();
                name = pages.getName();
            }
        }
        SchemaCodeParam schemaCodeParam = new SchemaCodeParam();
        schemaCodeParam.setApp(dslCodeParam.getApp());
        Map<String, Object> pageInfo = new HashMap<>();
        pageInfo.put("schema", schema);
        pageInfo.put("name", name);
        schemaCodeParam.setPageInfo(pageInfo);
        schemaCodeParam.setApp(dslCodeParam.getApp());
        return translateSchema(schemaCodeParam, type);
    }

    /**
     * 获取区块预览元数据
     *
     * @param previewParam
     * @return PreviewDto
     */
    public PreviewDto getBlockPreviewMetaData(PreviewParam previewParam) {
        Blocks blocks = blocksMapper.findBlocksById(previewParam.getId());
        // 拼装工具类
        AppExtensions appExtension = new AppExtensions();
        appExtension.setApp(previewParam.getApp());
        List<AppExtensions> extensionsList = appExtensionsMapper.findAppExtensionsByCondition(appExtension);
        Map<String, Object> extensions = appsV1ServiceImpl.getSchemaExtensions(extensionsList);
        List<Map<String, Object>> utils = (List<Map<String, Object>>) extensions.get("utils");
        // 拼装数据源
        Map<String, Object> dataSource = (Map<String, Object>) blocks.getContent().get("dataSource");
        // 拼装国际化词条
        List<I18nEntriesDto> i18ns = i18nEntriesMapper.findI18nEntriesByHostandHostType(previewParam.getId(), "block");
        Map<String, Map<String, String>> i18n = appsService.formatI18nEntrites(i18ns, Enums.E_i18Belongs.BLOCK.getValue(), previewParam.getId());

        PreviewDto previewDto = new PreviewDto();
        previewDto.setDataSource(dataSource);
        previewDto.setI18n(i18n);
        previewDto.setUtils(utils);
        return previewDto;
    }

    /**
     * 通过dsl 将页面/区块schema数据生成对应代码
     *
     * @param { I_TranslateSchemaParam } params
     * @return {Promise<I_Response>} dsl函数返回数据
     */
    public List<Map<String, Object>> translateSchema(SchemaCodeParam schemaCodeParam, String type) {

        // 页面/区块 预览只需将页面、区块路径和区块构建产物路径统一设置为 ./components 即可
        String defaultMain = "./components";

        Map<String, Object> dslParam = getDslParamByAppId(schemaCodeParam.getApp());
        MetaDto metaDto = new MetaDto();
        metaDto.setBlockHistories((List<BlockHistories>) dslParam.get("blockHistories"));
        metaDto.setMaterialHistory((MaterialHistories) dslParam.get("materialHistory"));

        List<Map<String, Object>> componentsMap = appsV1ServiceImpl.getSchemaComponentsMap(metaDto);
        for (Map<String, Object> component : componentsMap) {
            if (component.get("main") != null) {
                component.put("main", defaultMain);
            }
        }
        // 如果类型是 PAGE，则添加新的组件
        if (Enums.E_Schema2CodeType.PAGE.getValue().equals(type)) {
            Map<String, Object> pageComponent = new HashMap<>();
            pageComponent.put("name", schemaCodeParam.getPageInfo().get("name"));
            pageComponent.put("main", defaultMain);
            componentsMap.add(pageComponent);
        }
        List<BlockHistories> blocksData = (List<BlockHistories>) dslParam.get("blockHistories");
        ExecuteJavaScript executeJavaScript = new ExecuteJavaScript();
        // 指定 npm 的完整路径
        String npmCommand = "node";
        List<String> command = new ArrayList<>();
        command.add(npmCommand);
        command.add(scriptPath);
        command.add(schemaCodeParam.getPageInfo().toString());
        command.add(blocksData.toString());
        if (!componentsMap.isEmpty()) {
            command.add(componentsMap.toString());
        }
        List<Map<String, Object>> code = executeJavaScript.executeDslJavaScript(command);
        return code;
    }

    public List<BlockVersionDto> getAllBlockHistories(Integer id) {
        PageDto pageInfo = findPagesById(id);
        // 先查出该区块的全部子区块content_blocks 数据
        List<BlockVersionDto> blockHistories = new ArrayList<>();
        if (pageInfo.getContentBlocks().isEmpty()) {
            return blockHistories;
        }
        List<BlockVersionDto> contentBlocks = pageInfo.getContentBlocks();
        List<BlockVersionDto> allContentBlocks = findContentBlockHistories(contentBlocks);
        // 根据content_blocks 获取区块的全部子区块的构建产物数据
        List<Integer> historiesId = appsV1ServiceImpl.getBlockHistoryIdBySemver(allContentBlocks);

        return blockHistories;
    }

    private List<BlockVersionDto> findContentBlockHistories(List<BlockVersionDto> contentBlocks) {

        List<Integer> historiesId = appsV1ServiceImpl.getBlockHistoryIdBySemver(contentBlocks);
        List<BlockHistories> blockHistories = blockHistoriesMapper.findBlockHistoriesByIds(historiesId);
        List<BlockVersionDto> subContentBlocks = blockHistories.stream()
                .map(BlockHistories::getContentBlocks) // 提取 content_blocks
                .filter(BlockHistories -> BlockHistories != null && !BlockHistories.isEmpty()) // 过滤非空集合
                .flatMap(List::stream) // 扁平化
                .collect(Collectors.toList()); // 收集到 List 中
        List<BlockVersionDto> mergedBlocks = Stream.concat(subContentBlocks.stream(), contentBlocks.stream())
                .collect(Collectors.toList());
        // 对全部的content_blocks 进行去重合并
        if (!subContentBlocks.isEmpty()) {
            return deduplicateContentBlocks(mergedBlocks);
        }
        return contentBlocks;
    }

    public List<BlockVersionDto> deduplicateContentBlocks(List<BlockVersionDto> contentBlocks) {
        Map<Integer, BlockVersionDto> resMap = new HashMap<>();

        for (BlockVersionDto blockVersionDto : contentBlocks) {
            BlockVersionDto existing = resMap.get(blockVersionDto.getBlock());
            if (existing != null) {
                if (!"x".equals(existing.getVersion())) {
                    if (Utils.versionAGteVersionB(blockVersionDto.getVersion(), existing.getVersion())) {
                        resMap.put(blockVersionDto.getBlock(), blockVersionDto);
                    }
                }
            } else {
                resMap.put(blockVersionDto.getBlock(), blockVersionDto);
            }
        }

        return new ArrayList<>(resMap.values());
    }


    /**
     * 通过appId 获取 dsl 必须的 components 和 blocksData数据
     *
     * @param { string|number } appId 应用id
     * @return {any} 返回对应参数
     */
    public Map<String, Object> getDslParamByAppId(Integer appId) {
        MetaDto metaDto = appsV1ServiceImpl.setMeta(appId);
        Map<String, Object> dslParam = new HashMap<>();
        dslParam.put("blockHistories", metaDto.getBlockHistories());
        dslParam.put("framework", metaDto.getMaterialHistory().getFramework());
        dslParam.put("components", metaDto.getMaterialHistory().getComponents());
        dslParam.put("materialHistory", metaDto.getMaterialHistory());
        return dslParam;
    }
}

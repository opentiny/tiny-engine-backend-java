package com.tinyengine.it.service.impl.app;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tinyengine.it.config.Enums;
import com.tinyengine.it.config.SystemServiceLog;
import com.tinyengine.it.exception.ServiceException;
import com.tinyengine.it.mapper.AppsMapper;
import com.tinyengine.it.mapper.BlocksCarriersRelationsMapper;
import com.tinyengine.it.mapper.I18nEntriesMapper;
import com.tinyengine.it.model.dto.*;
import com.tinyengine.it.model.entity.Apps;
import com.tinyengine.it.model.entity.Blocks;
import com.tinyengine.it.model.entity.BlocksCarriersRelations;
import com.tinyengine.it.model.entity.I18nEntries;
import com.tinyengine.it.service.app.AppsService;
import com.tinyengine.it.service.app.I18nEntriesService;
import com.tinyengine.it.service.impl.app.v1.AppsV1ServiceImpl;
import com.tinyengine.it.service.impl.material.BlockGroupsServiceImpl;
import com.tinyengine.it.service.impl.material.BlocksServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AppsServiceImpl implements AppsService {

    @Autowired
    AppsMapper appsMapper;
    @Autowired
    I18nEntriesMapper i18nEntriesMapper;
    @Autowired
    I18nEntriesService i18nEntriesService;
    @Autowired
    AppsV1ServiceImpl appsV1ServiceImpl;
    @Autowired
    BlocksCarriersRelationsMapper blocksCarriersRelationsMapper;
    @Autowired
    BlocksServiceImpl blocksServiceImpl;
    @Autowired
    BlockGroupsServiceImpl blockGroupsServiceImpl;

    /**
     * 查询表apps所有数据
     */
    @Override
    public List<Apps> findAllApps() throws ServiceException {
        return appsMapper.findAllApps();
    }

    /**
     * 根据主键id查询表apps信息
     *
     * @param id
     */
    @Override
    public Apps findAppsById(@Param("id") Integer id) throws ServiceException {
        return appsMapper.findAppsById(id);
    }

    /**
     * 根据条件查询表apps数据
     *
     * @param apps
     */
    @Override
    public List<Apps> findAppsByCondition(Apps apps) throws ServiceException {
        return appsMapper.findAppsByCondition(apps);
    }

    /**
     * 根据主键id删除表apps数据
     *
     * @param id
     */
    @Override
    public Integer deleteAppsById(@Param("id") Integer id) throws ServiceException {
        return appsMapper.deleteAppsById(id);
    }

    /**
     * 根据主键id更新表apps数据
     *
     * @param apps
     */
    @Override
    public Integer updateAppsById(Apps apps) throws ServiceException {
        return appsMapper.updateAppsById(apps);
    }

    /**
     * 新增表apps数据
     *
     * @param apps
     */
    @Override
    public Integer createApps(Apps apps) throws ServiceException {
        return appsMapper.createApps(apps);
    }

    /**
     * 计算应用必要元素的hash值，并返回应用的schema 等信息
     *
     * @param appId
     */
    @Override
    public AppComplexInfoDto calculateHashValue(Integer appId) {

    /*    Map<String,Object> schema = appSchemaService.getSchema(appId);
        Map<String,Object> meta = appSchemaService.meta;
        Map<String,Object> extendIds = new HashMap<>();
        Apps detail = this.findAppsById(appId);
        Map<String,Object> appExtendConfig = detail.getExtendConfig();

        // 回填应用 data_hash数据会更新应用的update_at字段，需要删除 应用schema中的更新时间的字段 gmt_modified
        Map<String,Object> schemaCopy = DeepCopy.deepCopy(schema.toString());
        if (schemaCopy.containsKey("meat")) {
            Map<String, Object> meat = (Map<String, Object>) schemaCopy.get("meat");
            meat.remove("gmt_modified"); // 删除元素
        }

        Map<String,Object> hashData = new HashMap<>();
        hashData.put("schema",schemaCopy);
        hashData.put("extendIds", extendIds);
        hashData.put("appExtendConfig",appExtendConfig);
        String hash = HashCalculator.calculateMD5Hash(hashData);

        String dataHash = detail.getDataHash();
        boolean isChanged = !hash.equals(dataHash);
        log.info("ID:"+ appId+ "应用关键数据hash, 新("+hash+")旧("+dataHash+")是否不同:"+isChanged);
        if (isChanged) {
            // 更新应用表中的hash值
            Apps updateApp = new Apps();
            updateApp.setId(appId);
            updateApp.setDataHash(hash);
            this.updateAppsById(updateApp);
        }

        AppComplexInfoDto calculateHashDto = new AppComplexInfoDto();
        calculateHashDto.setHash(hash);
        calculateHashDto.setMeta(meta);
        calculateHashDto.setAppInfo(detail);
        calculateHashDto.setSchema(schema);
        calculateHashDto.setIsChanged(isChanged);
        return calculateHashDto;*/
        return null;
    }

    /**
     * 序列化国际化词条
     *
     * @param i18nEntries 国际化词条标准请求返回数据
     * @param userdIn     国际化词条从属单元 （应用或区块）
     * @param id          应用id或区块id
     */
    @SystemServiceLog(description = "对应用id或区块id获取序列化国际化词条")
    @Override
    public Map<String, Map<String, String>> formatI18nEntrites(List<I18nEntriesDto> i18nEntries, Integer userdIn, Integer id) {

        if (i18nEntries.isEmpty()) {
            Map<String, Map<String, String>> relationLangs = new HashMap<>();
            I18nEntries i18n = new I18nEntries();
            // 没有词条的时候，查询应用和区块对应的国家化关联，把默认空的关联分组返回
            if (userdIn == Enums.E_i18Belongs.APP.getValue()) {

                i18n.setHostType("app");

            } else {
                i18n.setHostType("block");
            }
            List<I18nEntriesDto> i18ns = i18nEntriesMapper.findI18nEntriesByHostandHostType(id, i18n.getHostType());
            relationLangs = i18nEntriesService.formatEntriesList(i18ns);
            return relationLangs;
        }
        return i18nEntriesService.formatEntriesList(i18nEntries);
    }

    @SystemServiceLog(description = "getAppPreviewMetaData 获取预览元数据")
    @Override
    public PreviewDto getAppPreviewMetaData(Integer id) {
        MetaDto metaDto = appsV1ServiceImpl.setMeta(id);
        Map<String, Object> dataSource = new HashMap<>();
        // 拼装数据源
        dataSource.put("list", metaDto.getSource());
        Map<String, Object> dataHandler = metaDto.getApp().getDataSourceGlobal();
        dataSource.putAll(dataHandler);
        // 拼装工具类
        Map<String, Object> extensions = appsV1ServiceImpl.getSchemaExtensions(metaDto.getExtension());
        List<Map<String, Object>> utils = (List<Map<String, Object>>) extensions.get("utils");
        // 拼装国际化词条
        Map<String, Map<String, String>> i18n = formatI18nEntrites(metaDto.getI18n(), Enums.E_i18Belongs.APP.getValue(), id);
        PreviewDto previewDto = new PreviewDto();
        previewDto.setDataSource(dataSource);
        previewDto.setI18n(i18n);
        previewDto.setUtils(utils);
        previewDto.setGlobalState(metaDto.getApp().getGlobalState());
        return previewDto;
    }


    /**
     * 关联应用信息
     *
     * @param map
     */
    @SystemServiceLog(description = "实现类里associateBlocksInApps关联应用信息")
    @Override
    public List<AppsDto> associateBlocksInApps(Map<String, Object> map) {
        // 获取关联的应用列表,todo 因不知道传的什么参数，先按照查询全部apps处理
        List<AppsDto> appsList = appsMapper.findAllAppsAndBlockGroupAndBlockCate();
        if (appsList.isEmpty()) {
            return new ArrayList<>();
        }

        // 汇总相关的区块分组的id
        List<Integer> blockGroupIds = appsList.stream()
                .flatMap(apps -> apps.getBlock_groups().stream())
                .map(BlockGroupsDto::getId)
                .collect(Collectors.toList());

        // 获取关联的区块id和版本
        QueryWrapper<BlocksCarriersRelations> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("host", blockGroupIds)
                .eq("host_type", "blockGroup");
        List<BlocksCarriersRelations> blocksVersionlist = blocksCarriersRelationsMapper.selectList(queryWrapper);

        // 获取区块列表
        List<Integer> blockIds = blocksVersionlist.stream().map(BlocksCarriersRelations::getBlock).distinct().collect(Collectors.toList());
        List<Blocks> blocksList = blocksServiceImpl.getBlocks(blockIds);
        appsList.forEach(apps -> {
            if (apps.getBlock_groups() != null) {
                apps.getBlock_groups().forEach(blockGroup -> {
                    List<Integer> blocksIds = blocksVersionlist.stream().filter(blockVersions -> blockVersions.getHost().equals(blockGroup.getId()))
                            .map(BlocksCarriersRelations::getBlock)
                            .collect(Collectors.toList());

                    List<Blocks> blocksInGroup = blocksList.stream().filter(block -> blocksIds.contains(block.getId())).collect(Collectors.toList());
                    blockGroup.setBlocks(blocksInGroup);
                });
            }
        });

        return appsList;

    }


    /**
     * 获取应用下的全部国际化词条
     *
     * @param appId
     * @return
     */

    @SystemServiceLog(description = "getI18n 获取应用下的全部国际化词条")
    @Override
    public Map<String, Object> getI18n(int appId) {
        // 获取应用下的词条
        List<I18nEntriesDto> i18nEntriesList = i18nEntriesMapper.findI18nEntriesByHostandHostType(appId, "app");
        Map<String, Map<String, String>> appEntries = formatI18nEntrites(i18nEntriesList, Enums.E_i18Belongs.APP.getValue(), appId);

        // 获取区块词条
        // （区块在被没有放入区块组时不能被页面使用，所以获取到区块组即可得到该应用关联的全部区块）
        Map<String, Object> getBlockGroupI18nEntriesResult = blockGroupsServiceImpl.getBlockGroupI18nEntries(appId);
        List<Integer> blockIds = (List<Integer>) getBlockGroupI18nEntriesResult.get("blockIds");
        List<I18nEntriesDto> blockEntriesData = (List<I18nEntriesDto>) getBlockGroupI18nEntriesResult.get("entries");
        Map<String, Map<String, String>> blockEntries = formatI18nEntritesByBlockIds(blockEntriesData, Enums.E_i18Belongs.BLOCK.getValue(), blockIds);
        // 合并应用 区块词条
        Map<String, Object> temp = new HashMap<>();

        Map<String, Map<String, String>> entries = appsV1ServiceImpl.mergeEntries(appEntries, blockEntries);
        temp.putAll(entries);
        // 获取默认语言
        Apps apps = appsMapper.findAppsById(appId);
        temp.put("default_lang", apps.getDefaultLang());
        return temp;
    }

    /**
     * 序列化国际化词条
     *
     * @param i18nEntries 国际化词条标准请求返回数据
     * @param userdIn     国际化词条从属单元 （应用或区块）
     * @param ids         区块id集合
     * @return
     */
    @SystemServiceLog(description = "formatI18nEntritesByBlockIds 处理区块id集合获取序列化国际化词条")
    @Override
    public Map<String, Map<String, String>> formatI18nEntritesByBlockIds(List<I18nEntriesDto> i18nEntries, Integer userdIn, List<Integer> ids) {
        if (i18nEntries.isEmpty()) {
            Map<String, Map<String, String>> relationLangs = new HashMap<>();

            // 没有词条的时候，查询应用和区块对应的国家化关联，把默认空的关联分组返回
            if (userdIn == Enums.E_i18Belongs.BLOCK.getValue()) {
                List<I18nEntriesDto> i18ns = i18nEntriesMapper.findI18nEntriesByHostsandHostType(ids, "block");
                relationLangs = i18nEntriesService.formatEntriesList(i18ns);
                return relationLangs;
            }

        }
        return i18nEntriesService.formatEntriesList(i18nEntries);
    }

}

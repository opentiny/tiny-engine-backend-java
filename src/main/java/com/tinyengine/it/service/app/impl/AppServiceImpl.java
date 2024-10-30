package com.tinyengine.it.service.app.impl;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.enums.Enums;
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.config.log.SystemServiceLog;
import com.tinyengine.it.mapper.AppMapper;
import com.tinyengine.it.mapper.I18nEntryMapper;
import com.tinyengine.it.model.dto.I18nEntryDto;
import com.tinyengine.it.model.dto.MetaDto;
import com.tinyengine.it.model.dto.PreviewDto;
import com.tinyengine.it.model.dto.SchemaI18n;
import com.tinyengine.it.model.dto.SchemaUtils;
import com.tinyengine.it.model.entity.App;
import com.tinyengine.it.model.entity.I18nEntry;
import com.tinyengine.it.model.entity.Platform;
import com.tinyengine.it.service.app.AppService;
import com.tinyengine.it.service.app.I18nEntryService;
import com.tinyengine.it.service.app.impl.v1.AppV1ServiceImpl;
import com.tinyengine.it.service.material.impl.BlockGroupServiceImpl;
import com.tinyengine.it.service.material.impl.BlockServiceImpl;
import com.tinyengine.it.service.platform.PlatformService;

import lombok.extern.slf4j.Slf4j;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type App service.
 *
 * @since 2024-10-20
 */
@Service
@Slf4j
public class AppServiceImpl implements AppService {
    /**
     * The App mapper.
     */
    @Autowired
    private AppMapper appMapper;

    /**
     * The Platform service.
     */
    @Autowired
    private PlatformService platformService;

    /**
     * The 18 n entry service.
     */
    @Autowired
    private I18nEntryService i18nEntryService;

    /**
     * The 18 n entry mapper.
     */
    @Autowired
    private I18nEntryMapper i18nEntryMapper;

    /**
     * The App v 1 service.
     */
    @Autowired
    private AppV1ServiceImpl appV1ServiceImpl;

    /**
     * The Block service.
     */
    @Autowired
    private BlockServiceImpl blockServiceImpl;

    /**
     * The Block group service.
     */
    @Autowired
    private BlockGroupServiceImpl blockGroupServiceImpl;

    /**
     * 查询表t_app所有数据
     *
     * @return App
     */
    @Override
    public List<App> queryAllApp() {
        return appMapper.queryAllApp();
    }

    /**
     * 根据主键id查询表t_app信息
     *
     * @param id id
     * @return App
     */
    @Override
    @SystemServiceLog(description = "通过id查询应用实现方法")
    public Result<App> queryAppById(@Param("id") Integer id) {
        App app = appMapper.queryAppById(id);
        if (app == null) {
            return Result.failed(ExceptionEnum.CM009);
        }
        return Result.success(app);
    }

    /**
     * 根据条件查询表t_app数据
     *
     * @param app app
     * @return App
     */
    @Override
    public List<App> queryAppByCondition(App app) {
        return appMapper.queryAppByCondition(app);
    }

    /**
     * 根据主键id删除表t_app数据
     *
     * @param id id
     * @return App
     */
    @Override
    @SystemServiceLog(description = "应用删除实现方法")
    public Result<App> deleteAppById(@Param("id") Integer id) {
        App app = appMapper.queryAppById(id);
        int result = appMapper.deleteAppById(id);
        if (result < 1) {
            return Result.failed(ExceptionEnum.CM009);
        }
        return Result.success(app);
    }

    /**
     * 根据主键id更新表t_app数据
     *
     * @param app app
     * @return App
     */
    @Override
    @SystemServiceLog(description = "应用修改实现方法")
    public Result<App> updateAppById(App app) {
        // 如果更新extend_config字段，从platform获取数据，继承非route部分
        if (!app.getExtendConfig().isEmpty()) {
            App appResult = appMapper.queryAppById(app.getId());
            Platform platform = platformService.queryPlatformById(appResult.getPlatformId());
            Map<String, Object> appExtendConfig = platform.getAppExtendConfig();
            appExtendConfig.remove("route");
            app.getExtendConfig().putAll(appExtendConfig);
        }
        int result = appMapper.updateAppById(app);
        if (result < 1) {
            return Result.failed(ExceptionEnum.CM001);
        }
        App selectedApp = appMapper.queryAppById(app.getId());
        return Result.success(selectedApp);
    }

    /**
     * 新增表t_app数据
     *
     * @param app app
     * @return App
     */
    @Override
    @SystemServiceLog(description = "应用创建实现方法")
    public Result<App> createApp(App app) {
        List<App> appResult = appMapper.queryAppByCondition(app);
        if (!appResult.isEmpty()) {
            return Result.failed(ExceptionEnum.CM003);
        }
        int result = appMapper.createApp(app);
        if (result < 1) {
            return Result.failed(ExceptionEnum.CM001);
        }
        return Result.success(app);
    }

    /**
     * 序列化国际化词条
     *
     * @param i18nEntries 国际化词条标准请求返回数据
     * @param userdIn     国际化词条从属单元 （应用或区块）
     * @param id          应用id或区块id
     * @return Entries List
     */
    @SystemServiceLog(description = "对应用id或区块id获取序列化国际化词条")
    @Override
    public SchemaI18n formatI18nEntrites(List<I18nEntryDto> i18nEntries, Integer userdIn, Integer id) {
        if (i18nEntries.isEmpty()) {
            I18nEntry i18n = new I18nEntry();
            // 没有词条的时候，查询应用和区块对应的国家化关联，把默认空的关联分组返回
            if (userdIn == Enums.I18Belongs.APP.getValue()) {
                i18n.setHostType("app");
            } else {
                i18n.setHostType("block");
            }
            List<I18nEntryDto> i18ns = i18nEntryMapper.findI18nEntriesByHostandHostType(id, i18n.getHostType());
            return i18nEntryService.formatEntriesList(i18ns);
        }
        return i18nEntryService.formatEntriesList(i18nEntries);
    }

    /**
     * 获取预览元数据
     *
     * @param id 应用id
     * @return PreviewDto
     */
    @SystemServiceLog(description = "getAppPreviewMetaData 获取预览元数据")
    @Override
    public PreviewDto getAppPreviewMetaData(Integer id) {
        MetaDto metaDto = appV1ServiceImpl.getMetaDto(id);
        Map<String, Object> dataSource = new HashMap<>();
        // 拼装数据源
        dataSource.put("list", metaDto.getSource());
        Map<String, Object> dataHandler = metaDto.getApp().getDataSourceGlobal();
        dataSource.putAll(dataHandler);
        // 拼装工具类
        Map<String, List<SchemaUtils>> extensions = appV1ServiceImpl.getSchemaExtensions(metaDto.getExtension());
        // 拼装国际化词条
        SchemaI18n i18n = formatI18nEntrites(metaDto.getI18n(),
                Enums.I18Belongs.APP.getValue(), id);
        PreviewDto previewDto = new PreviewDto();
        previewDto.setDataSource(dataSource);
        previewDto.setI18n(i18n);
        previewDto.setUtils(extensions.get("utils"));
        previewDto.setGlobalState(metaDto.getApp().getGlobalState());
        return previewDto;
    }
}

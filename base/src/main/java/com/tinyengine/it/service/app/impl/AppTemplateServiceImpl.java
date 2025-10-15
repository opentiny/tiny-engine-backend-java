package com.tinyengine.it.service.app.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.mapper.AppExtensionMapper;
import com.tinyengine.it.mapper.AppMapper;
import com.tinyengine.it.mapper.DatasourceMapper;
import com.tinyengine.it.mapper.I18nEntryMapper;
import com.tinyengine.it.mapper.PageMapper;
import com.tinyengine.it.model.dto.I18nEntryDto;
import com.tinyengine.it.model.entity.App;
import com.tinyengine.it.model.entity.AppExtension;
import com.tinyengine.it.model.entity.Datasource;
import com.tinyengine.it.model.entity.I18nEntry;
import com.tinyengine.it.model.entity.Page;
import com.tinyengine.it.service.app.AppService;
import com.tinyengine.it.service.app.AppTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppTemplateServiceImpl extends ServiceImpl<AppMapper, App> implements AppTemplateService {
    /**
     * The App service.
     */
    @Autowired
    private AppService appService;

    /**
     * The page mapper.
     */
    @Autowired
    private PageMapper pageMapper;

    @Autowired
    private AppExtensionMapper appExtensionMapper;

    @Autowired
    private DatasourceMapper datasourceMapper;

    @Autowired
    private I18nEntryMapper i18nEntryMapper;

    /**
     * 查询表应用模版所有信息
     *
     * @return the list
     */
    @Override
    public List<App> queryAllAppTemplate() {
        return this.baseMapper.queryAllAppTemplate();
    }

    /**
     * 根据主键id查询应用模版信息
     *
     * @param id the id
     * @return the result
     */
    @Override
    public Result<App> queryAppTemplateById(Integer id) {
        App app = baseMapper.queryAppTemplateById(id);
        if (app == null) {
            return Result.failed(ExceptionEnum.CM009);
        }
        return Result.success(app);
    }

    /**
     * 通过模版应用创建应用
     *
     * @param app the app
     * @return the result
     */
    @Override
    public Result<App> createAppByTemplate(App app) {

        if (app.getId() == null) {
            return Result.failed(ExceptionEnum.CM002);
        }
        int templateId = app.getId();
        app.setId(null);
        app.setIsTemplate(false);
        app.setSetTemplateBy(null);
        Result<App> result = appService.createApp(app);
        int appId = result.getData().getId();
        copyData(templateId, appId);

        return result;
    }

    private void createPage(int templateId, int appId) {
        List<Page> pages = pageMapper.queryPageByApp(templateId);
        if (pages.isEmpty()) {
            throw new ServiceException(ExceptionEnum.CM009.getResultCode(), ExceptionEnum.CM009.getResultMsg());
        }
        for (Page page : pages) {
            page.setId(null);
            page.setCreatedBy(null);
            page.setCreatedTime(null);
            page.setLastUpdatedBy(null);
            page.setLastUpdatedTime(null);
            page.setApp(appId);
            pageMapper.createPage(page);
        }
    }

    private void copyData(int templateId, int appId) {
        createPage(templateId, appId);
        createAppExtension(templateId, appId);
        createDataSource(templateId, appId);
        createAppExtension(templateId, appId);
        createI18n(templateId, appId);
    }

    private void createAppExtension(int templateId, int appId) {
        AppExtension queryParam = new AppExtension();
        queryParam.setApp(templateId);
        List<AppExtension> appExtensions = appExtensionMapper.queryAppExtensionByCondition(queryParam);
        if (appExtensions.isEmpty()) {
            throw new ServiceException(ExceptionEnum.CM009.getResultCode(), ExceptionEnum.CM009.getResultMsg());
        }
        for (AppExtension appExtension : appExtensions) {
            appExtension.setId(null);
            appExtension.setCreatedBy(null);
            appExtension.setCreatedTime(null);
            appExtension.setLastUpdatedBy(null);
            appExtension.setLastUpdatedTime(null);
            appExtension.setApp(appId);
            appExtensionMapper.createAppExtension(appExtension);
        }
    }

    private void createDataSource(int templateId, int appId) {
        Datasource queryParam = new Datasource();
        queryParam.setApp(templateId);
        List<Datasource> datasources = datasourceMapper.queryDatasourceByCondition(queryParam);
        if (datasources.isEmpty()) {
            throw new ServiceException(ExceptionEnum.CM009.getResultCode(), ExceptionEnum.CM009.getResultMsg());
        }
        for (Datasource datasource : datasources) {
            datasource.setId(null);
            datasource.setCreatedBy(null);
            datasource.setCreatedTime(null);
            datasource.setLastUpdatedBy(null);
            datasource.setLastUpdatedTime(null);
            datasource.setApp(appId);
            datasourceMapper.createDatasource(datasource);
        }
    }

    private void createI18n(int templateId, int appId) {
        List<I18nEntryDto> i18nEntries = i18nEntryMapper.findI18nEntriesByHostandHostType(templateId, "app");
        if (i18nEntries.isEmpty()) {
            throw new ServiceException(ExceptionEnum.CM009.getResultCode(), ExceptionEnum.CM009.getResultMsg());
        }
        for (I18nEntryDto i18nEntrieDto : i18nEntries) {

            i18nEntrieDto.setId(null);
            i18nEntrieDto.setCreatedBy(null);
            i18nEntrieDto.setCreatedTime(null);
            i18nEntrieDto.setLastUpdatedBy(null);
            i18nEntrieDto.setLastUpdatedTime(null);
            i18nEntrieDto.setHost(appId);
            I18nEntry i18nEntry = new I18nEntry();
            BeanUtil.copyProperties(i18nEntrieDto, i18nEntry);
            i18nEntryMapper.createI18nEntry(i18nEntry);
        }
    }
}

/**
 * Copyright (c) 2023 - present TinyEngine Authors.
 * Copyright (c) 2023 - present Huawei Cloud Computing Technologies Co., Ltd.
 *
 * Use of this source code is governed by an MIT-style license.
 *
 * THE OPEN SOURCE SOFTWARE IN THIS PRODUCT IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL,
 * BUT WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS FOR
 * A PARTICULAR PURPOSE. SEE THE APPLICABLE LICENSES FOR MORE DETAILS.
 *
 */

package com.tinyengine.it.service.app.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.context.LoginUserContext;
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.mapper.AppExtensionMapper;
import com.tinyengine.it.mapper.AppMapper;
import com.tinyengine.it.mapper.DatasourceMapper;
import com.tinyengine.it.mapper.I18nEntryMapper;
import com.tinyengine.it.mapper.ModelMapper;
import com.tinyengine.it.mapper.PageHistoryMapper;
import com.tinyengine.it.model.dto.AppDto;
import com.tinyengine.it.model.dto.I18nEntryDto;
import com.tinyengine.it.model.entity.App;
import com.tinyengine.it.model.entity.AppExtension;
import com.tinyengine.it.model.entity.Datasource;
import com.tinyengine.it.model.entity.I18nEntry;
import com.tinyengine.it.model.entity.Model;
import com.tinyengine.it.model.entity.Page;
import com.tinyengine.it.model.entity.PageHistory;
import com.tinyengine.it.service.app.AppTemplateService;
import com.tinyengine.it.service.app.PageService;
import com.tinyengine.it.service.material.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
;
import java.util.List;
import java.util.UUID;

@Service
public class AppTemplateServiceImpl extends ServiceImpl<AppMapper, App> implements AppTemplateService {
    /**
     * The App service.
     */
    @Autowired
    private AppMapper appMapper;

    /**
     * The page mapper.
     */
    @Autowired
    private PageService pageService;

    /**
     * The page history mapper.
     */
    @Autowired
    private PageHistoryMapper pageHistoryMapper;

    /**
     * The app extension mapper.
     */
    @Autowired
    private AppExtensionMapper appExtensionMapper;

    /**
     * The data source mapper.
     */
    @Autowired
    private DatasourceMapper datasourceMapper;

    /**
     * The i18n entry mapper.
     */
    @Autowired
    private I18nEntryMapper i18nEntryMapper;

    /**
     * The model service.
     */
    @Autowired
    private ModelService modelService;

    /**
     * The model mapper.
     */
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private LoginUserContext loginUserContext;

    /**
     * 分页查询应用模版所有信息
     * @param currentPage the currentPage
     * @param  pageSize the pageSize
     * @param  orderBy the orderBy
     * @param app the app
     * @return the AppDto
     */
    @Override
    public AppDto queryAllAppTemplate(Integer currentPage, Integer pageSize, String orderBy, App app) {
        if (currentPage < 1) {
            currentPage = 1;  // 默认第一页
        }
        if (pageSize < 1) {
            pageSize = 10;    // 默认每页10条
        }
        if (pageSize > 1000) {
            pageSize = 1000;  // 限制最大页大小
        }
        int offset = (currentPage - 1) * pageSize;

        List<App> apps = this.baseMapper.queryAllAppTemplate(pageSize, offset, app.getName(),
            app.getIndustryId(), app.getSceneId(), app.getFramework(), orderBy, app.getCreatedBy(),
            loginUserContext.getTenantId());
        Integer total = this.baseMapper.queryAppTemplateTotal(loginUserContext.getTenantId());
        AppDto appDto = new AppDto();
        appDto.setApps(apps);
        appDto.setTotal(total);
        return appDto;
    }

    /**
     * 根据主键id查询应用模版信息
     *
     * @param id the id
     * @return the result
     */
    @Override
    public Result<App> queryAppTemplateById(Integer id) {
        App app = baseMapper.queryAppTemplateById(id, loginUserContext.getTenantId());
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
    public App createAppByTemplate(App app) {

        if (app.getId() == null) {
            throw new ServiceException(ExceptionEnum.CM002.getResultCode(), ExceptionEnum.CM002.getResultMsg());
        }
        int templateId = app.getId();
        app.setId(null);
        app.setIsTemplate(false);
        app.setSetTemplateBy(null);
        int result = appMapper.createApp(app);
        if (result < 1) {
            throw new ServiceException(ExceptionEnum.CM001.getResultCode(), ExceptionEnum.CM001.getResultMsg());
        }
        copyData(templateId, app.getId());
        return appMapper.queryAppById(app.getId(), loginUserContext.getTenantId());
    }

    private void copyData(int templateId, int appId) {
        createPage(templateId, appId);
        createPageHistory(templateId, appId);
        createAppExtension(templateId, appId);
        createDataSource(templateId, appId);
        createI18n(templateId, appId);
        createModel(templateId, appId);
    }

    private void createPage(int templateId, int appId) {
        List<Page> pages = pageService.queryAllPage(templateId);
        if (pages.isEmpty()) {
            return;
        }
        for (Page page : pages) {
            page.setId(null);
            page.setCreatedBy(null);
            page.setCreatedTime(null);
            page.setLastUpdatedBy(null);
            page.setLastUpdatedTime(null);
            page.setApp(appId);
            pageService.createPage(page);
        }
    }

    private void createPageHistory(int templateId, int appId) {
        List<PageHistory> pageHistories = pageHistoryMapper.queryPageHistoryByAppId(templateId);
        if (pageHistories.isEmpty()) {
            return;
        }
        for (PageHistory pageHistory : pageHistories) {
            pageHistory.setId(null);
            pageHistory.setCreatedBy(null);
            pageHistory.setCreatedTime(null);
            pageHistory.setLastUpdatedBy(null);
            pageHistory.setLastUpdatedTime(null);
            pageHistory.setApp(appId);
            pageHistoryMapper.createPageHistory(pageHistory);
        }
    }

    private void createAppExtension(int templateId, int appId) {
        AppExtension queryParam = new AppExtension();
        queryParam.setApp(templateId);
        List<AppExtension> appExtensions = appExtensionMapper.queryAppExtensionByCondition(queryParam);
        if (appExtensions.isEmpty()) {
            return;
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
            return;
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
            return;
        }
        for (I18nEntryDto i18nEntrieDto : i18nEntries) {

            String key = i18nEntrieDto.getKey() + "_" + UUID.randomUUID();
            i18nEntrieDto.setId(null);
            i18nEntrieDto.setKey(key);
            i18nEntrieDto.setCreatedBy(null);
            i18nEntrieDto.setCreatedTime(null);
            i18nEntrieDto.setLastUpdatedBy(null);
            i18nEntrieDto.setLastUpdatedTime(null);
            i18nEntrieDto.setHost(appId);
            i18nEntrieDto.setLang(null);
            I18nEntry i18nEntry = new I18nEntry();
            BeanUtil.copyProperties(i18nEntrieDto, i18nEntry);
            i18nEntryMapper.createI18nEntry(i18nEntry);
        }
    }

    private void createModel(int templateId, int appId) {
        Model queryModel = new Model();
        queryModel.setAppId(templateId);
        List<Model> models = modelMapper.queryModelByCondition(queryModel);
        if (models.isEmpty()) {
            return;
        }
        for (Model model : models) {
            model.setId(null);
            model.setCreatedBy(null);
            model.setCreatedTime(null);
            model.setLastUpdatedBy(null);
            model.setLastUpdatedTime(null);
            model.setAppId(appId);
            modelService.createModel(model);
        }
    }
}

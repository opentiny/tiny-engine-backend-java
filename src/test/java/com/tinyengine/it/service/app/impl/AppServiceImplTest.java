package com.tinyengine.it.service.app.impl;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.mapper.AppMapper;
import com.tinyengine.it.mapper.I18nEntryMapper;
import com.tinyengine.it.model.dto.I18nEntryDto;
import com.tinyengine.it.model.dto.MetaDto;
import com.tinyengine.it.model.dto.PreviewDto;
import com.tinyengine.it.model.dto.SchemaI18n;
import com.tinyengine.it.model.dto.SchemaUtils;
import com.tinyengine.it.model.entity.App;
import com.tinyengine.it.model.entity.Platform;
import com.tinyengine.it.service.app.I18nEntryService;
import com.tinyengine.it.service.app.impl.v1.AppV1ServiceImpl;
import com.tinyengine.it.service.material.impl.BlockGroupServiceImpl;
import com.tinyengine.it.service.material.impl.BlockServiceImpl;
import com.tinyengine.it.service.platform.PlatformService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * test case
 *
 * @since 2024-10-29
 */
class AppServiceImplTest {
    @Mock
    private AppMapper appMapper;
    @Mock
    private PlatformService platformService;
    @Mock
    private I18nEntryService i18nEntryService;
    @Mock
    private I18nEntryMapper i18nEntryMapper;
    @Mock
    private AppV1ServiceImpl appV1ServiceImpl;
    @Mock
    private BlockServiceImpl blockServiceImpl;
    @Mock
    private BlockGroupServiceImpl blockGroupServiceImpl;
    @InjectMocks
    private AppServiceImpl appServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testQueryAllApp() {
        List<App> mockData = Arrays.<App>asList(new App());
        when(appMapper.queryAllApp()).thenReturn(mockData);

        List<App> result = appServiceImpl.queryAllApp();
        Assertions.assertEquals(mockData, result);
    }

    @Test
    void testQueryAppById() {
        App app = new App();
        when(appMapper.queryAppById(1)).thenReturn(app);

        Result<App> result = appServiceImpl.queryAppById(1);
        Assertions.assertEquals(app, result.getData());
    }

    @Test
    void testQueryAppByCondition() {
        List<App> mockData = Arrays.<App>asList(new App());
        when(appMapper.queryAppByCondition(any(App.class))).thenReturn(mockData);

        List<App> result = appServiceImpl.queryAppByCondition(new App());
        Assertions.assertEquals(mockData, result);
    }

    @Test
    void testDeleteAppById() {
        App app = new App();
        when(appMapper.queryAppById(1)).thenReturn(app);
        when(appMapper.deleteAppById(1)).thenReturn(2);

        Result<App> result = appServiceImpl.deleteAppById(1);
        Assertions.assertEquals(app, result.getData());
    }

    @Test
    void testUpdateAppById() {
        App resultApp = new App();
        resultApp.setPlatformId(2);
        HashMap<String, Object> mockConfig = new HashMap<>();

        resultApp.setExtendConfig(mockConfig);
        int appId = 1;
        when(appMapper.queryAppById(appId)).thenReturn(resultApp);
        App param = new App();
        param.setId(appId);

        when(appMapper.updateAppById(param)).thenReturn(1);
        Platform platform = new Platform();
        platform.setAppExtendConfig(new HashMap<>());
        when(platformService.queryPlatformById(any())).thenReturn(platform);
        HashMap<String, Object> config = new HashMap<>();
        config.put("test","test");
        param.setExtendConfig(config);
        Result<App> result = appServiceImpl.updateAppById(param);
        Assertions.assertEquals(resultApp, result.getData());
    }

    @Test
    void testCreateApp() {
        List<App> appResult = new ArrayList<>();
        App param = new App();
        when(appMapper.queryAppByCondition(param)).thenReturn(appResult);
        when(appMapper.createApp(param)).thenReturn(1);

        Result<App> result = appServiceImpl.createApp(param);
        Assertions.assertEquals(param, result.getData());
    }

    @Test
    void testFormatI18nEntrites() {
        SchemaI18n mockData = new SchemaI18n();
        when(i18nEntryService.formatEntriesList(any(List.class))).thenReturn(mockData);

        List<I18nEntryDto> param = new ArrayList<>();
        SchemaI18n result = appServiceImpl.formatI18nEntrites(param, 1, 2);
        Assertions.assertEquals(mockData, result);
    }

    @Test
    void testGetAppPreviewMetaData() {
        Integer paramId = 123;
        when(i18nEntryService.formatEntriesList(any(List.class))).thenReturn(new SchemaI18n());
        when(i18nEntryMapper.findI18nEntriesByHostandHostType(anyInt(), anyString()))
                .thenReturn(Arrays.<I18nEntryDto>asList(new I18nEntryDto()));
        MetaDto metaDto = new MetaDto();
        App app = new App();

        HashMap<String, Object> dataSourceGlobal = new HashMap<>();
        dataSourceGlobal.put("key","datasource");
        app.setDataSourceGlobal(dataSourceGlobal);
        metaDto.setApp(app);
        metaDto.setI18n(new ArrayList<>());
        when(appV1ServiceImpl.getMetaDto(paramId)).thenReturn(metaDto);
        HashMap<String, List<SchemaUtils>> extensionsResponse = new HashMap<String, List<SchemaUtils>>() {{
            put("utils", Arrays.asList(new SchemaUtils()));
        }};
        when(appV1ServiceImpl.getSchemaExtensions(any())).thenReturn(extensionsResponse);

        PreviewDto result = appServiceImpl.getAppPreviewMetaData(paramId);
        Assertions.assertEquals(1, result.getUtils().size());
        Assertions.assertEquals("datasource", result.getDataSource().get("key"));
    }
}

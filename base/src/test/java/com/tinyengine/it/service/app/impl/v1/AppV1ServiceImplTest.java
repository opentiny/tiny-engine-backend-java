package com.tinyengine.it.service.app.impl.v1;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

import com.tinyengine.it.mapper.AppExtensionMapper;
import com.tinyengine.it.mapper.AppMapper;
import com.tinyengine.it.mapper.BlockGroupMapper;
import com.tinyengine.it.mapper.BlockHistoryMapper;
import com.tinyengine.it.mapper.DatasourceMapper;
import com.tinyengine.it.mapper.I18nEntryMapper;
import com.tinyengine.it.mapper.MaterialHistoryMapper;
import com.tinyengine.it.mapper.PageMapper;
import com.tinyengine.it.model.dto.BlockHistoryDto;
import com.tinyengine.it.model.dto.BlockVersionDto;
import com.tinyengine.it.model.dto.ComponentTree;
import com.tinyengine.it.model.dto.I18nEntryDto;
import com.tinyengine.it.model.dto.MetaDto;
import com.tinyengine.it.model.dto.SchemaDto;
import com.tinyengine.it.model.dto.SchemaI18n;
import com.tinyengine.it.model.dto.SchemaUtils;
import com.tinyengine.it.model.entity.App;
import com.tinyengine.it.model.entity.AppExtension;
import com.tinyengine.it.model.entity.BlockGroup;
import com.tinyengine.it.model.entity.BlockHistory;
import com.tinyengine.it.model.entity.Datasource;
import com.tinyengine.it.model.entity.MaterialHistory;
import com.tinyengine.it.model.entity.Page;
import com.tinyengine.it.model.entity.Platform;
import com.tinyengine.it.service.app.I18nEntryService;
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
import java.util.Map;

/**
 * test case
 *
 * @since 2024-10-29
 */
class AppV1ServiceImplTest {
    @Mock
    private AppMapper appMapper;

    @Mock
    private I18nEntryMapper i18nEntryMapper;

    @Mock
    private I18nEntryService i18nEntryService;

    @Mock
    private AppExtensionMapper appExtensionMapper;

    @Mock
    private DatasourceMapper datasourceMapper;

    @Mock
    private PageMapper pageMapper;

    @Mock
    private BlockHistoryMapper blockHistoryMapper;

    @Mock
    private BlockGroupMapper blockGroupMapper;

    @Mock
    private MaterialHistoryMapper materialHistoryMapper;

    @Mock
    private PlatformService platformService;

    @InjectMocks
    private AppV1ServiceImpl appV1ServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAppSchema() {
        App app = new App();
        int appId = 2;
        app.setId(appId);
        app.setHomePage(1);
        when(appMapper.queryAppById(anyInt())).thenReturn(app);
        Page page = new Page();
        page.setIsPage(true);
        page.setPageContent(new HashMap<>());
        List<Page> pageList = Arrays.asList(page);
        when(pageMapper.queryPageByApp(appId)).thenReturn(pageList);

        List<I18nEntryDto> i18nEntryDtos = Arrays.<I18nEntryDto>asList(new I18nEntryDto());
        when(i18nEntryMapper.findI18nEntriesByHostandHostType(appId, "app")).thenReturn(i18nEntryDtos);
        when(blockGroupMapper.queryBlockGroupByApp(appId)).thenReturn(Arrays.asList(new BlockGroup()));
        MaterialHistory materialHistory = new MaterialHistory();
        materialHistory.setComponents(new ArrayList<>());
        when(materialHistoryMapper.queryMaterialHistoryById(anyInt())).thenReturn(materialHistory);
        when(materialHistoryMapper.queryBlockHistoryBymaterialHistoryId(anyInt()))
                .thenReturn(Arrays.<Integer>asList(Integer.valueOf(0)));

        Platform platform = new Platform();
        platform.setMaterialHistoryId(3);

        when(platformService.queryPlatformById(anyInt())).thenReturn(platform);

        SchemaDto result = appV1ServiceImpl.appSchema(appId);
        Assertions.assertEquals("2", result.getMeta().getAppId());
    }

    @Test
    void testMergeEntries() {
        SchemaI18n result = appV1ServiceImpl.mergeEntries(new SchemaI18n(), new SchemaI18n());
        Assertions.assertEquals(0, result.getEn_US().size());
    }

    @Test
    void testGetMetaDto() {
        App app = new App();
        when(appMapper.queryAppById(anyInt())).thenReturn(app);
        when(i18nEntryMapper.findI18nEntriesByHostandHostType(anyInt(), anyString()))
                .thenReturn(Arrays.asList(new I18nEntryDto()));
        when(appExtensionMapper.queryAppExtensionByCondition(any(AppExtension.class)))
                .thenReturn(Arrays.asList(new AppExtension()));
        when(datasourceMapper.queryDatasourceByCondition(any(Datasource.class)))
                .thenReturn(Arrays.<Datasource>asList(new Datasource()));
        when(pageMapper.queryPageByApp(anyInt())).thenReturn(Arrays.<Page>asList(new Page()));
        when(blockGroupMapper.queryBlockGroupByApp(anyInt())).thenReturn(Arrays.asList(new BlockGroup()));
        when(materialHistoryMapper.queryMaterialHistoryById(anyInt())).thenReturn(new MaterialHistory());
        when(materialHistoryMapper.queryBlockHistoryBymaterialHistoryId(anyInt()))
                .thenReturn(Arrays.<Integer>asList(Integer.valueOf(0)));
        when(platformService.queryPlatformById(anyInt())).thenReturn(new Platform());

        MetaDto result = appV1ServiceImpl.getMetaDto(Integer.valueOf(0));
        Assertions.assertEquals(app, result.getApp());
    }

    @Test
    void testGetBlockHistoryIdBySemver() {
        BlockHistoryDto historyDto = new BlockHistoryDto();
        historyDto.setVersion("1");
        List<BlockHistoryDto> historyDtos = Arrays.asList(historyDto);
        when(blockHistoryMapper.queryMapByIds(any(List.class))).thenReturn(historyDtos);

        List<Integer> result = appV1ServiceImpl.getBlockHistoryIdBySemver(Arrays.asList(new BlockVersionDto()));
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void testGetSchemaComponentsTree() {
        MetaDto metaDto = new MetaDto();
        metaDto.setPages(new ArrayList<>());
        List<ComponentTree> result = appV1ServiceImpl.getSchemaComponentsTree(metaDto);
        Assertions.assertEquals(0, result.size());
    }

    @Test
    void testGetSchemaComponentsMap() {
        MetaDto metaDto = new MetaDto();
        ArrayList<BlockHistory> blockHistories = new ArrayList<>();
        BlockHistory history = new BlockHistory();
        history.setContent(new HashMap<>());
        history.setVersion("v1");
        blockHistories.add(history);
        metaDto.setBlockHistories(blockHistories);
        MaterialHistory materialHistory = new MaterialHistory();
        materialHistory.setComponents(new ArrayList<>());
        metaDto.setMaterialHistory(materialHistory);
        List<Map<String, Object>> result = appV1ServiceImpl.getSchemaComponentsMap(metaDto);
        Assertions.assertEquals("v1", result.get(0).get("version"));
    }

    @Test
    void testGetSchemaExtensions() {
        AppExtension appExtension = new AppExtension();
        appExtension.setName("name");
        List<AppExtension> extensionList = Arrays.asList(appExtension);
        Map<String, List<SchemaUtils>> result = appV1ServiceImpl.getSchemaExtensions(extensionList);
        Assertions.assertEquals("name", result.get("utils").get(0).getName());
    }

    @Test
    void testFormatDataFields() {
        Map<String, Object> result = appV1ServiceImpl.formatDataFields(new HashMap<String, Object>() {
            {
                put("data", "data");
            }
        }, Arrays.<String>asList("fields"), true);
        Assertions.assertEquals(new HashMap<String, Object>() {
            {
                put("data", "data");
            }
        }, result);
    }
}


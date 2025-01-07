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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.mapper.AppExtensionMapper;
import com.tinyengine.it.mapper.AppMapper;
import com.tinyengine.it.mapper.BlockHistoryMapper;
import com.tinyengine.it.mapper.BlockMapper;
import com.tinyengine.it.mapper.I18nEntryMapper;
import com.tinyengine.it.mapper.PageHistoryMapper;
import com.tinyengine.it.mapper.PageMapper;
import com.tinyengine.it.model.dto.PreviewDto;
import com.tinyengine.it.model.dto.PreviewParam;
import com.tinyengine.it.model.dto.TreeNodeCollection;
import com.tinyengine.it.model.dto.TreeNodeDto;
import com.tinyengine.it.model.entity.App;
import com.tinyengine.it.model.entity.Block;
import com.tinyengine.it.model.entity.Page;
import com.tinyengine.it.model.entity.PageHistory;
import com.tinyengine.it.model.entity.User;
import com.tinyengine.it.service.app.AppService;
import com.tinyengine.it.service.app.PageHistoryService;
import com.tinyengine.it.service.app.UserService;
import com.tinyengine.it.service.app.impl.v1.AppV1ServiceImpl;
import com.tinyengine.it.service.material.impl.BlockServiceImpl;

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
class PageServiceImplTest {
    @Mock
    private PageMapper pageMapper;
    @Mock
    private AppService appService;
    @Mock
    private AppMapper appMapper;
    @Mock
    private UserService userService;
    @Mock
    private BlockMapper blockMapper;
    @Mock
    private BlockServiceImpl blockServiceImpl;
    @Mock
    private PageHistoryService pageHistoryService;
    @Mock
    private PageHistoryMapper pageHistoryMapper;
    @Mock
    private AppV1ServiceImpl appV1ServiceImpl;
    @Mock
    private BlockHistoryMapper blockHistoryMapper;
    @Mock
    private AppExtensionMapper appExtensionMapper;
    @Mock
    private I18nEntryMapper i18nEntryMapper;
    @InjectMocks
    private PageServiceImpl pageServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testQueryPageById() {
        int queryPageId = 1;

        Page returnPage = new Page();
        returnPage.setApp(333);
        returnPage.setIsPage(true);
        returnPage.setId(queryPageId);
        when(pageMapper.queryPageById(queryPageId)).thenReturn(returnPage);

        App app = new App();
        app.setFramework("Vue");
        app.setHomePage(1);
        // not home page
        when(appMapper.queryAppById(333)).thenReturn(app);

        Page result = pageServiceImpl.queryPageById(1);
        assertEquals(returnPage, result);
    }

    @Test
    void testQueryPageByCondition() {
        Page mockData = new Page();
        Page param = new Page();
        when(pageMapper.queryPageByCondition(param)).thenReturn(Arrays.asList(mockData));

        List<Page> result = pageServiceImpl.queryPageByCondition(param);
        assertEquals(mockData, result.get(0));
    }

    @Test
    void testCreatePage() {
        Page param = new Page();
        int pageId = 123;
        param.setId(pageId);
        param.setIsHome(false);
        param.setParentId("1");
        param.setOccupierBy("555");
        param.setGroup("static");

        when(pageMapper.queryPageByCondition(any())).thenReturn(new ArrayList<>());
        when(pageMapper.createPage(param)).thenReturn(1);

        Page returnPage = new Page();
        returnPage.setApp(333);
        returnPage.setIsPage(true);
        returnPage.setId(pageId);
        returnPage.setIsHome(false);
        when(pageMapper.queryPageById(pageId)).thenReturn(returnPage);

        App app = new App();
        app.setFramework("Vue");
        app.setHomePage(1);
        // not home page
        when(appMapper.queryAppById(333)).thenReturn(app);
        when(pageHistoryService.createPageHistory(any(PageHistory.class))).thenReturn(1);

        Result<Page> result = pageServiceImpl.createPage(param);
        assertEquals(returnPage, result.getData());
    }

    @Test
    void testCreateFolder() {
        Page param = new Page();
        param.setParentId("1");
        int queryPageId = 123;
        param.setId(queryPageId);

        Page parentPage = new Page();
        parentPage.setDepth(2);
        when(pageMapper.queryPageById(1)).thenReturn(parentPage);

        Page page = new Page();
        page.setId(queryPageId);
        List<Page> pageList = Arrays.<Page>asList(page);
        when(pageMapper.queryPageByCondition(param)).thenReturn(pageList);

        Page returnPage = new Page();
        returnPage.setApp(333);
        returnPage.setIsPage(true);
        returnPage.setId(queryPageId);
        when(pageMapper.queryPageById(queryPageId)).thenReturn(returnPage);

        when(pageMapper.createPage(param)).thenReturn(1);
        App app = new App();
        app.setFramework("Vue");
        app.setHomePage(1);
        // not home page
        when(appMapper.queryAppById(333)).thenReturn(app);
        HashMap<String, List<String>> blockAsset = new HashMap<String, List<String>>() {
            {
                put("blockAsset", Arrays.asList("getBlockAssetsResponse"));
            }
        };
        when(blockServiceImpl.getBlockAssets(any(Map.class), anyString())).thenReturn(blockAsset);

        Result<Page> result = pageServiceImpl.createFolder(param);
        assertEquals(returnPage, result.getData());
    }

    @Test
    void testUpdatePage() {
        when(pageHistoryService.createPageHistory(any(PageHistory.class))).thenReturn(1);

        Page param = new Page();
        int pageId = 123;
        param.setId(pageId);
        param.setIsHome(false);
        param.setParentId("1");
        param.setOccupierBy("555");

        Page queryPage = new Page();
        queryPage.setApp(222);
        queryPage.setIsPage(false);
        queryPage.setIsHome(false);
        queryPage.setParentId("1");
        queryPage.setIsDefault(false);
        when(pageMapper.queryPageById(pageId)).thenReturn(queryPage);

        App app = new App();
        app.setFramework("Vue");
        app.setHomePage(1);
        // not home page
        when(appMapper.queryAppById(222)).thenReturn(app);
        User occupier = new User();
        occupier.setId(111);
        when(userService.queryUserById(555)).thenReturn(occupier);
        User currentUser = new User();
        currentUser.setId(111);
        when(userService.queryUserById(1)).thenReturn(currentUser);

        Result<Page> result = pageServiceImpl.updatePage(param);
        assertEquals(queryPage, result.getData());
    }

    @Test
    void testUpdate() {
        Page parent = new Page();
        parent.setDepth(3);

        when(pageMapper.queryPageById(1)).thenReturn(parent);

        when(pageMapper.updatePageById(any(Page.class))).thenReturn(1);
        App app = new App();
        app.setFramework("Vue");
        app.setHomePage(1);
        when(appMapper.queryAppById(222)).thenReturn(app);
        Page parentInfo = new Page();
        parentInfo.setDepth(4);
        parentInfo.setIsPage(false);
        parentInfo.setApp(222);
        when(pageMapper.queryPageById(123)).thenReturn(parentInfo);

        Page param = new Page();
        param.setIsHome(true);
        param.setParentId("1");
        param.setId(123);
        param.setDepth(2);
        param.setApp(222);
        Result<Page> result = pageServiceImpl.update(param);
        assertEquals(parentInfo, result.getData());
    }

    @Test
    void testSetAppHomePage() {
        when(appMapper.updateAppById(any(App.class))).thenReturn(1);

        boolean isResult = pageServiceImpl.setAppHomePage(1, 2);
        assertTrue(isResult);
    }

    @Test
    void testGetDepth() {
        Page page = new Page();
        page.setDepth(5);
        when(pageMapper.queryPageById(1)).thenReturn(page);

        Result<Integer> result = pageServiceImpl.getDepth("1");
        Assertions.assertFalse(result.isSuccess());
        assertEquals("Exceeded depth", result.getMessage());
    }

    @Test
    void testDel() {
        Page mockData = new Page();
        when(pageMapper.queryPageById(1)).thenReturn(mockData);

        when(pageMapper.queryPageByCondition(any(Page.class))).thenReturn(new ArrayList<>());

        User user = new User();
        user.setUsername("public");
        when(userService.queryUserById(1)).thenReturn(user);

        Result<Page> result = pageServiceImpl.del(1);
        assertEquals(mockData, result.getData());
    }

    @Test
    void testCheckDelete() {
        Page mockData = new Page();
        when(pageMapper.queryPageById(1)).thenReturn(mockData);

        User user = new User();
        user.setUsername("public");
        when(userService.queryUserById(1)).thenReturn(user);

        Result<Page> result = pageServiceImpl.checkDelete(1);
        assertEquals(mockData, result.getData());
    }

    @Test
    void testValidateIsHome() {
        Page param = new Page();
        param.setIsHome(true);
        param.setParentId("0");
        Page pageInfo = new Page();
        pageInfo.setIsHome(true);
        pageInfo.setParentId("1");
        boolean isResult = pageServiceImpl.validateIsHome(param, pageInfo);
        assertTrue(isResult);
    }

    @Test
    void testVerifyParentId() {
        Page parent = new Page();
        parent.setDepth(3);
        when(pageMapper.queryPageById(anyInt())).thenReturn(parent);

        Result<Integer> result = pageServiceImpl.verifyParentId("123");
        assertEquals(3, result.getData());
    }

    @Test
    void testGetTreeNodes() {
        when(pageMapper.selectList(any(Wrapper.class))).thenReturn(new ArrayList());

        TreeNodeDto param = new TreeNodeDto();
        param.setLevel(1);
        TreeNodeCollection collection = new TreeNodeCollection();
        collection.setData(new ArrayList<>());
        collection.setRange(new ArrayList<>());
        param.setCollection(collection);
        param.setPids(Arrays.asList(1, 2));

        TreeNodeCollection result = pageServiceImpl.getTreeNodes(param);
        assertTrue(result.getRange().isEmpty());
        assertTrue(result.getData().isEmpty());
    }

    @Test
    void testGetChildrenId() {
        List<Integer> idList = Arrays.asList(1);
        List<Integer> result = pageServiceImpl.getChildrenId(idList);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetBlockPreviewMetaData() {
        PreviewParam param = new PreviewParam();
        param.setId(1);
        Block block = new Block();
        HashMap<String, Object> content = new HashMap<>();
        HashMap<Object, Object> datasource = new HashMap<>();
        content.put("dataSource", datasource);
        block.setContent(content);
        when(blockMapper.queryBlockById(1)).thenReturn(block);
        when(appV1ServiceImpl.getSchemaExtensions(any(List.class))).thenReturn(new HashMap());

        PreviewDto result = pageServiceImpl.getBlockPreviewMetaData(param);
        assertEquals(datasource, result.getDataSource());
    }
}

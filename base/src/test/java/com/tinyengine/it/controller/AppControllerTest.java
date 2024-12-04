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

package com.tinyengine.it.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.mapper.AppMapper;
import com.tinyengine.it.model.entity.App;
import com.tinyengine.it.service.app.AppService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * test case
 *
 * @since 2024-11-05
 */
class AppControllerTest {
    @Mock
    private AppService appService;
    @Mock
    private AppMapper appMapper;
    @InjectMocks
    private AppController appController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllApp() {
        List<App> mockData = Arrays.<App>asList(new App());
        when(appService.queryAllApp()).thenReturn(mockData);

        Result<List<App>> result = appController.getAllApp();
        Assertions.assertEquals(mockData, result.getData());
    }

    @Test
    void testGetAppById() {
        App mockData = new App();
        when(appService.queryAppById(anyInt())).thenReturn(Result.success(mockData));

        Result<App> result = appController.getAppById(Integer.valueOf(0));
        Assertions.assertEquals(mockData, result.getData());
    }

    @Test
    void testCreateApp() {
        App mockData = new App();
        when(appService.createApp(any(App.class))).thenReturn(Result.success(mockData));

        Result<App> result = appController.createApp(new App());
        Assertions.assertEquals(mockData, result.getData());
    }

    @Test
    void testUpdateApp() {
        App mockData = new App();
        when(appService.updateAppById(any(App.class))).thenReturn(Result.success(mockData));

        Result<App> result = appController.updateApp(Integer.valueOf(0), new App());
        Assertions.assertEquals(mockData, result.getData());
    }

    @Test
    void testDeleteApp() {
        App mockData = new App();
        when(appService.deleteAppById(anyInt())).thenReturn(Result.success(mockData));

        Result<App> result = appController.deleteApp(Integer.valueOf(0));
        Assertions.assertEquals(mockData, result.getData());
    }

    @Test
    void testDetail() {
        App mockData = new App();
        when(appService.queryAppById(anyInt())).thenReturn(Result.success(mockData));

        Result<App> result = appController.detail(Integer.valueOf(0));
        Assertions.assertEquals(mockData, result.getData());
    }

    @Test
    void testUpdateI18n() {
        HashMap<String, Object> param = new HashMap<String, Object>() {{
            int[] arrayInt = {1, 2};
            put("i18n_langs", arrayInt);
        }};
        Result<App> result = appController.updateI18n(Integer.valueOf(0), param);
        Assertions.assertFalse(result.isSuccess());
    }
}
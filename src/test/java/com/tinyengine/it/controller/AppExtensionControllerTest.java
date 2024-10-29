package com.tinyengine.it.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.model.entity.AppExtension;
import com.tinyengine.it.service.app.AppExtensionService;

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
 * @since 2024-10-29
 */
class AppExtensionControllerTest {
    @Mock
    AppExtensionService appExtensionService;
    @InjectMocks
    AppExtensionController appExtensionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllAppExtension() {
        List<AppExtension> mockData = Arrays.asList(new AppExtension());
        when(appExtensionService.findAppExtensionByCondition(any(AppExtension.class))).thenReturn(mockData);

        Result<List<AppExtension>> result = appExtensionController.getAllAppExtension("1", "category");
        Assertions.assertEquals(mockData, result.getData());
    }

    @Test
    void testGetAppExtensionById() {
        AppExtension appExtension = new AppExtension();
        appExtension.setApp(2);
        List<AppExtension> mockData = Arrays.asList(appExtension);
        when(appExtensionService.findAppExtensionByCondition(any(AppExtension.class))).thenReturn(mockData);

        HashMap<String, String> param = new HashMap<String, String>() {{
            put("app", "1");
        }};
        Result<AppExtension> result = appExtensionController.getAppExtensionById(param);
        Assertions.assertEquals(2, result.getData().getApp());
    }

    @Test
    void testCreateAppExtension() {
        Result<AppExtension> mockData = new Result<>();
        when(appExtensionService.createAppExtension(any(AppExtension.class))).thenReturn(mockData);

        Result<AppExtension> result = appExtensionController.createAppExtension(new AppExtension());
        Assertions.assertEquals(mockData, result);
    }

    @Test
    void testUpdateAppExtension() {
        Result<AppExtension> mockData = new Result<>();
        when(appExtensionService.updateAppExtensionById(any(AppExtension.class))).thenReturn(mockData);

        Result<AppExtension> result = appExtensionController.updateAppExtension(new AppExtension());
        Assertions.assertEquals(mockData, result);
    }

    @Test
    void testDeleteAppExtension() {
        Result<AppExtension> mockData = new Result<>();
        when(appExtensionService.deleteAppExtensionById(anyInt())).thenReturn(mockData);

        Result<AppExtension> result = appExtensionController.deleteAppExtension(Integer.valueOf(0));
        Assertions.assertEquals(mockData, result);
    }
}


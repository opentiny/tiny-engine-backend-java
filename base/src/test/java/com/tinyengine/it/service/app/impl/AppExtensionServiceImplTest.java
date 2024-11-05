package com.tinyengine.it.service.app.impl;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.mapper.AppExtensionMapper;
import com.tinyengine.it.model.entity.AppExtension;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * test case
 *
 * @since 2024-10-29
 */
class AppExtensionServiceImplTest {
    @Mock
    private AppExtensionMapper appExtensionMapper;
    @InjectMocks
    private AppExtensionServiceImpl appExtensionServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllAppExtension() {
        List<AppExtension> mockData = Arrays.asList(new AppExtension());
        when(appExtensionMapper.queryAllAppExtension()).thenReturn(mockData);

        Result<List<AppExtension>> result = appExtensionServiceImpl.findAllAppExtension();
        Assertions.assertEquals(mockData, result.getData());
    }

    @Test
    void testFindAppExtensionById() {
        AppExtension mockData = new AppExtension();
        when(appExtensionMapper.queryAppExtensionById(1)).thenReturn(mockData);

        AppExtension result = appExtensionServiceImpl.findAppExtensionById(1);
        Assertions.assertEquals(mockData, result);
    }

    @Test
    void testFindAppExtensionByCondition() {
        List<AppExtension> mockData = Arrays.<AppExtension>asList(new AppExtension());
        AppExtension param = new AppExtension();
        when(appExtensionMapper.queryAppExtensionByCondition(param)).thenReturn(mockData);

        List<AppExtension> result = appExtensionServiceImpl.findAppExtensionByCondition(param);
        Assertions.assertEquals(mockData, result);
    }

    @Test
    void testDeleteAppExtensionById() {
        AppExtension mockData = new AppExtension();
        when(appExtensionMapper.queryAppExtensionById(1)).thenReturn(mockData);

        Result<AppExtension> result = appExtensionServiceImpl.deleteAppExtensionById(1);
        Assertions.assertEquals(mockData, result.getData());
    }

    @Test
    void testUpdateAppExtensionById() {
        AppExtension mockData = new AppExtension();
        AppExtension param = new AppExtension();
        int id = 1;
        param.setId(id);

        when(appExtensionMapper.queryAppExtensionById(id)).thenReturn(mockData);
        when(appExtensionMapper.updateAppExtensionById(any(AppExtension.class))).thenReturn(id);

        Result<AppExtension> result = appExtensionServiceImpl.updateAppExtensionById(param);
        Assertions.assertEquals(mockData, result.getData());
    }

    @Test
    void testCreateAppExtension() {
        List<AppExtension> mockData = new ArrayList<>();
        when(appExtensionMapper.queryAppExtensionByCondition(any(AppExtension.class))).thenReturn(mockData);
        AppExtension param = new AppExtension();
        when(appExtensionMapper.createAppExtension(param)).thenReturn(1);

        Result<AppExtension> result = appExtensionServiceImpl.createAppExtension(param);
        Assertions.assertEquals(param, result.getData());
    }
}

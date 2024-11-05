package com.tinyengine.it.service.platform.impl;

import static org.mockito.Mockito.when;

import com.tinyengine.it.mapper.PlatformMapper;
import com.tinyengine.it.model.entity.Platform;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

/**
 * test case
 *
 * @since 2024-10-29
 */
class PlatformServiceImplTest {
    @Mock
    private PlatformMapper platformMapper;
    @InjectMocks
    private PlatformServiceImpl platformServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testQueryAllPlatform() {
        List<Platform> mockData = Arrays.<Platform>asList(new Platform());
        when(platformMapper.queryAllPlatform()).thenReturn(mockData);

        List<Platform> result = platformServiceImpl.queryAllPlatform();
        Assertions.assertEquals(mockData, result);
    }

    @Test
    void testQueryPlatformById() {
        Platform mockData = new Platform();
        when(platformMapper.queryPlatformById(1)).thenReturn(mockData);

        Platform result = platformServiceImpl.queryPlatformById(1);
        Assertions.assertEquals(mockData, result);
    }

    @Test
    void testQueryPlatformByCondition() {
        Platform param = new Platform();
        List<Platform> mockData = Arrays.asList(param);
        when(platformMapper.queryPlatformByCondition(param)).thenReturn(mockData);

        List<Platform> result = platformServiceImpl.queryPlatformByCondition(param);
        Assertions.assertEquals(mockData, result);
    }

    @Test
    void testDeletePlatformById() {
        when(platformMapper.deletePlatformById(1)).thenReturn(2);

        Integer result = platformServiceImpl.deletePlatformById(1);
        Assertions.assertEquals(2, result);
    }

    @Test
    void testUpdatePlatformById() {
        Platform param = new Platform();
        when(platformMapper.updatePlatformById(param)).thenReturn(1);

        Integer result = platformServiceImpl.updatePlatformById(param);
        Assertions.assertEquals(1, result);
    }

    @Test
    void testCreatePlatform() {
        Platform param = new Platform();
        when(platformMapper.createPlatform(param)).thenReturn(1);

        Integer result = platformServiceImpl.createPlatform(param);
        Assertions.assertEquals(1, result);
    }
}
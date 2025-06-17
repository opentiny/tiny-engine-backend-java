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

package com.tinyengine.it.service.platform.impl;

import static org.mockito.Mockito.when;

import cn.hutool.core.util.ReflectUtil;
import com.tinyengine.it.common.base.Result;
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
        ReflectUtil.setFieldValue(platformServiceImpl, "baseMapper", platformMapper);
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
        Platform mockData = new Platform();
        mockData.setId(1);
        when(platformMapper.queryPlatformById(1)).thenReturn(mockData);
        when(platformMapper.deletePlatformById(1)).thenReturn(1);

        Result<Platform> result = platformServiceImpl.deletePlatformById(1);
        Assertions.assertEquals(mockData, result.getData());
    }

    @Test
    void testUpdatePlatformById() {
        Platform param = new Platform();
        param.setId(1);
        when(platformMapper.updatePlatformById(param)).thenReturn(1);
        Platform mockData = new Platform();
        when(platformMapper.queryPlatformById(1)).thenReturn(mockData);
        Result<Platform> result = platformServiceImpl.updatePlatformById(param);
        Assertions.assertEquals(mockData, result.getData());
    }

    @Test
    void testCreatePlatform() {
        Platform param = new Platform();
        param.setName("testPlatform");
        when(platformMapper.createPlatform(param)).thenReturn(1);

        Result<Platform> result = platformServiceImpl.createPlatform(param);
        Assertions.assertEquals(param, result.getData());
    }
}
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

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.mapper.DatasourceMapper;
import com.tinyengine.it.model.entity.Datasource;

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
class DatasourceServiceImplTest {
    @Mock
    private DatasourceMapper datasourceMapper;
    @InjectMocks
    private DatasourceServiceImpl datasourceServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testQueryDatasourceById() {
        Datasource mockData = new Datasource();
        when(datasourceMapper.queryDatasourceById(1)).thenReturn(mockData);

        Datasource result = datasourceServiceImpl.queryDatasourceById(1);
        Assertions.assertEquals(mockData, result);
    }

    @Test
    void testQueryDatasourceByCondition() {
        List<Datasource> mockData = Arrays.asList(new Datasource());
        when(datasourceMapper.queryDatasourceByCondition(any(Datasource.class))).thenReturn(mockData);

        List<Datasource> result = datasourceServiceImpl.queryDatasourceByCondition(new Datasource());
        Assertions.assertEquals(mockData, result);
    }

    @Test
    void testDeleteDatasourceById() {
        Datasource mockData = new Datasource();
        when(datasourceMapper.queryDatasourceById(anyInt())).thenReturn(mockData);
        when(datasourceMapper.deleteDatasourceById(anyInt())).thenReturn(1);

        Result<Datasource> result = datasourceServiceImpl.deleteDatasourceById(1);
        Assertions.assertEquals(mockData, result.getData());
    }

    @Test
    void testUpdateDatasourceById() {
        Integer dataSourceId = 1;
        Datasource mockData = new Datasource();
        when(datasourceMapper.queryDatasourceById(dataSourceId)).thenReturn(mockData);
        when(datasourceMapper.updateDatasourceById(any(Datasource.class))).thenReturn(1);

        Datasource param = new Datasource();
        param.setId(dataSourceId);
        Result<Datasource> result = datasourceServiceImpl.updateDatasourceById(param);
        Assertions.assertEquals(mockData, result.getData());
    }

    @Test
    void testCreateDatasource() {
        Datasource param = new Datasource();
        param.setId(1);
        param.setApp(1);
        param.setName("name");
        Datasource mockData = new Datasource();
        when(datasourceMapper.queryDatasourceById(1)).thenReturn(mockData);
        when(datasourceMapper.createDatasource(param)).thenReturn(1);

        Result<Datasource> result = datasourceServiceImpl.createDatasource(param);
        Assertions.assertEquals(mockData, result.getData());
    }
}

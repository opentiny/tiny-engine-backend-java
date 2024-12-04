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

package com.tinyengine.it.service.material.impl;

import static org.mockito.Mockito.when;

import com.tinyengine.it.mapper.MaterialMapper;
import com.tinyengine.it.model.entity.Material;

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
class MaterialServiceImplTest {
    @Mock
    private MaterialMapper materialMapper;
    @InjectMocks
    private MaterialServiceImpl materialServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testQueryAllMaterial() {
        Material mockData = new Material();
        when(materialMapper.queryAllMaterial()).thenReturn(Arrays.<Material>asList(mockData));

        List<Material> result = materialServiceImpl.queryAllMaterial();
        Assertions.assertEquals(mockData, result.get(0));
    }

    @Test
    void testQueryMaterialById() {
        Material mockData = new Material();
        when(materialMapper.queryMaterialById(1)).thenReturn(mockData);

        Material result = materialServiceImpl.queryMaterialById(1);
        Assertions.assertEquals(mockData, result);
    }

    @Test
    void testQueryMaterialByCondition() {
        Material material = new Material();
        Material param = new Material();
        when(materialMapper.queryMaterialByCondition(param)).thenReturn(Arrays.<Material>asList(material));

        List<Material> result = materialServiceImpl.queryMaterialByCondition(param);
        Assertions.assertEquals(material, result.get(0));
    }

    @Test
    void testDeleteMaterialById() {
        when(materialMapper.deleteMaterialById(1)).thenReturn(2);

        Integer result = materialServiceImpl.deleteMaterialById(1);
        Assertions.assertEquals(2, result);
    }

    @Test
    void testUpdateMaterialById() {
        Material param = new Material();
        when(materialMapper.updateMaterialById(param)).thenReturn(1);

        Integer result = materialServiceImpl.updateMaterialById(param);
        Assertions.assertEquals(1, result);
    }

    @Test
    void testCreateMaterial() {
        Material param = new Material();
        when(materialMapper.createMaterial(param)).thenReturn(1);

        Integer result = materialServiceImpl.createMaterial(param);
        Assertions.assertEquals(1, result);
    }
}

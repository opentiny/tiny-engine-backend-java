package com.tinyengine.it.service.material.impl;

import static org.mockito.Mockito.when;

import com.tinyengine.it.mapper.MaterialHistoryMapper;
import com.tinyengine.it.model.entity.MaterialHistory;

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
class MaterialHistoryServiceImplTest {
    @Mock
    private MaterialHistoryMapper materialHistoryMapper;
    @InjectMocks
    private MaterialHistoryServiceImpl materialHistoryServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllMaterialHistory() {
        MaterialHistory mockData = new MaterialHistory();
        when(materialHistoryMapper.queryAllMaterialHistory()).thenReturn(Arrays.asList(mockData));

        List<MaterialHistory> result = materialHistoryServiceImpl.findAllMaterialHistory();
        Assertions.assertEquals(mockData, result.get(0));
    }

    @Test
    void testFindMaterialHistoryById() {
        MaterialHistory mockData = new MaterialHistory();
        when(materialHistoryMapper.queryMaterialHistoryById(1)).thenReturn(mockData);

        MaterialHistory result = materialHistoryServiceImpl.findMaterialHistoryById(1);
        Assertions.assertEquals(mockData, result);
    }

    @Test
    void testFindMaterialHistoryByCondition() {
        MaterialHistory mockData = new MaterialHistory();
        MaterialHistory param = new MaterialHistory();
        when(materialHistoryMapper.queryMaterialHistoryByCondition(param)).thenReturn(Arrays.asList(mockData));

        List<MaterialHistory> result = materialHistoryServiceImpl.findMaterialHistoryByCondition(param);
        Assertions.assertEquals(mockData, result.get(0));
    }

    @Test
    void testDeleteMaterialHistoryById() {
        when(materialHistoryMapper.deleteMaterialHistoryById(1)).thenReturn(123);

        Integer result = materialHistoryServiceImpl.deleteMaterialHistoryById(1);
        Assertions.assertEquals(123, result);
    }

    @Test
    void testUpdateMaterialHistoryById() {
        MaterialHistory param = new MaterialHistory();
        when(materialHistoryMapper.updateMaterialHistoryById(param)).thenReturn(123);

        Integer result = materialHistoryServiceImpl.updateMaterialHistoryById(param);
        Assertions.assertEquals(123, result);
    }

    @Test
    void testCreateMaterialHistory() {
        MaterialHistory param = new MaterialHistory();
        when(materialHistoryMapper.createMaterialHistory(param)).thenReturn(123);

        Integer result = materialHistoryServiceImpl.createMaterialHistory(param);
        Assertions.assertEquals(123, result);
    }
}

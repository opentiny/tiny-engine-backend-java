package com.tinyengine.it.service.material.impl;

import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

import com.tinyengine.it.mapper.BusinessCategoryMapper;
import com.tinyengine.it.model.entity.BusinessCategory;

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
class BusinessCategoryServiceImplTest {
    @Mock
    private BusinessCategoryMapper businessCategoryMapper;
    @InjectMocks
    private BusinessCategoryServiceImpl businessCategoryServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testQueryAllBusinessCategory() {
        BusinessCategory mockData = new BusinessCategory();
        when(businessCategoryMapper.queryAllBusinessCategory()).thenReturn(Arrays.<BusinessCategory>asList(mockData));

        List<BusinessCategory> result = businessCategoryServiceImpl.queryAllBusinessCategory();
        Assertions.assertEquals(mockData, result.get(0));
    }

    @Test
    void testQueryBusinessCategoryById() {
        BusinessCategory mockData = new BusinessCategory();
        when(businessCategoryMapper.queryBusinessCategoryById(1)).thenReturn(mockData);

        BusinessCategory result = businessCategoryServiceImpl.queryBusinessCategoryById(1);
        Assertions.assertEquals(mockData, result);
    }

    @Test
    void testQueryBusinessCategoryByCondition() {
        BusinessCategory mockData = new BusinessCategory();
        BusinessCategory param = new BusinessCategory();
        when(businessCategoryMapper.queryBusinessCategoryByCondition(param)).thenReturn(Arrays.asList(mockData));

        List<BusinessCategory> result = businessCategoryServiceImpl.queryBusinessCategoryByCondition(param);
        Assertions.assertEquals(mockData, result.get(0));
    }

    @Test
    void testDeleteBusinessCategoryById() {
        when(businessCategoryMapper.deleteBusinessCategoryById(1)).thenReturn(2);

        Integer result = businessCategoryServiceImpl.deleteBusinessCategoryById(1);
        Assertions.assertEquals(2, result);
    }

    @Test
    void testUpdateBusinessCategoryById() {
        BusinessCategory param = new BusinessCategory();
        when(businessCategoryMapper.updateBusinessCategoryById(param)).thenReturn(1);

        Integer result = businessCategoryServiceImpl.updateBusinessCategoryById(param);
        Assertions.assertEquals(1, result);
    }

    @Test
    void testCreateBusinessCategory() {
        BusinessCategory param = new BusinessCategory();
        when(businessCategoryMapper.createBusinessCategory(param)).thenReturn(1);

        Integer result = businessCategoryServiceImpl.createBusinessCategory(param);
        Assertions.assertEquals(1, result);
    }
}

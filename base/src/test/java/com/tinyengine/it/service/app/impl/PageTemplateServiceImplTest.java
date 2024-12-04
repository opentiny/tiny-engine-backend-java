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
import static org.mockito.Mockito.when;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.mapper.PageTemplateMapper;
import com.tinyengine.it.model.entity.PageTemplate;

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
class PageTemplateServiceImplTest {
    @Mock
    private PageTemplateMapper pageTemplateMapper;
    @InjectMocks
    private PageTemplateServiceImpl pageTemplateServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testQueryAllPageTemplate() {
        PageTemplate mockData = new PageTemplate();
        String type = "type";
        when(pageTemplateMapper.queryAllPageTemplate(type)).thenReturn(Arrays.asList(mockData));

        String name = "name";
        when(pageTemplateMapper.queryPageTemplateByName(name, type)).thenReturn(Arrays.<PageTemplate>asList(mockData));

        Result<List<PageTemplate>> result = pageTemplateServiceImpl.queryAllPageTemplate(name, type);
        Assertions.assertEquals(mockData, result.getData().get(0));

        result = pageTemplateServiceImpl.queryAllPageTemplate(null, type);
        Assertions.assertEquals(mockData, result.getData().get(0));
    }

    @Test
    void testQueryPageTemplateById() {
        PageTemplate mockData = new PageTemplate();
        when(pageTemplateMapper.queryPageTemplateById(1)).thenReturn(mockData);

        Result<PageTemplate> result = pageTemplateServiceImpl.queryPageTemplateById(1);
        Assertions.assertEquals(mockData, result.getData());
    }

    @Test
    void testQueryPageTemplateByCondition() {
        PageTemplate mockData = new PageTemplate();
        PageTemplate param = new PageTemplate();
        when(pageTemplateMapper.queryPageTemplateByCondition(param)).thenReturn(Arrays.asList(mockData));

        List<PageTemplate> result = pageTemplateServiceImpl.queryPageTemplateByCondition(param);
        Assertions.assertEquals(mockData, result.get(0));
    }

    @Test
    void testDeletePageTemplateByIds() {
        List<Integer> param = Arrays.asList(1);
        when(pageTemplateMapper.deletePageTemplateByIds(param)).thenReturn(1);

        Result<Integer> result = pageTemplateServiceImpl.deletePageTemplateByIds(param);
        Assertions.assertEquals(1, result.getData());
    }

    @Test
    void testUpdatePageTemplateById() {
        PageTemplate param = new PageTemplate();
        when(pageTemplateMapper.updatePageTemplateById(param)).thenReturn(2);

        Integer result = pageTemplateServiceImpl.updatePageTemplateById(param);
        Assertions.assertEquals(2, result);
    }

    @Test
    void testCreatePageTemplate() {
        List<PageTemplate> templateList = new ArrayList<>();
        when(pageTemplateMapper.queryPageTemplateByCondition(any(PageTemplate.class))).thenReturn(templateList);
        PageTemplate param = new PageTemplate();
        param.setId(1);
        when(pageTemplateMapper.createPageTemplate(param)).thenReturn(1);

        PageTemplate mockData = new PageTemplate();
        when(pageTemplateMapper.queryPageTemplateById(1)).thenReturn(mockData);
        Result<PageTemplate> result = pageTemplateServiceImpl.createPageTemplate(param);
        Assertions.assertEquals(mockData, result.getData());
    }
}


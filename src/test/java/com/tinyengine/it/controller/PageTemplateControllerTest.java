package com.tinyengine.it.controller;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.model.entity.PageTemplate;
import com.tinyengine.it.service.app.PageTemplateService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

/**
 * test case
 *
 * @since 2024-10-29
 */
class PageTemplateControllerTest {
    @Mock
    private PageTemplateService pageTemplateService;
    @InjectMocks
    private PageTemplateController pageTemplateController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePageTemplate() {
        when(pageTemplateService.createPageTemplate(any(PageTemplate.class))).thenReturn(new Result<PageTemplate>());

        Result<PageTemplate> result = pageTemplateController.createPageTemplate(new PageTemplate());
        Assertions.assertEquals(new Result<PageTemplate>(), result);
    }

    @Test
    void testDeletePageTemplate() {
        List<Integer> param = asList(1, 2);
        Result<Integer> mockData = new Result<>();
        when(pageTemplateService.deletePageTemplateByIds(param)).thenReturn(mockData);

        Result<Integer> result = pageTemplateController.deletePageTemplate(param);
        Assertions.assertEquals(mockData, result);
    }

    @Test
    void testDetail() {
        when(pageTemplateService.queryPageTemplateById(1)).thenReturn(new Result<PageTemplate>());

        Result<PageTemplate> result = pageTemplateController.detail(1);
        Assertions.assertEquals(new Result<PageTemplate>(), result);
    }

    @Test
    void testFindAllPageTemplate() {
        when(pageTemplateService.queryAllPageTemplate(anyString(), anyString())).thenReturn(new Result<List<PageTemplate>>());

        Result<List<PageTemplate>> result = pageTemplateController.findAllPageTemplate("name", "type");
        Assertions.assertEquals(new Result<List<PageTemplate>>(), result);
    }
}


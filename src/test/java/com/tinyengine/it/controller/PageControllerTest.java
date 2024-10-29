package com.tinyengine.it.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.model.dto.PreviewDto;
import com.tinyengine.it.model.dto.PreviewParam;
import com.tinyengine.it.model.entity.Page;
import com.tinyengine.it.service.app.PageService;

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
class PageControllerTest {
    @Mock
    PageService pageService;
    @InjectMocks
    PageController pageController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPage() {
        List<Page> mockData = Arrays.<Page>asList(new Page());
        when(pageService.queryAllPage(anyInt())).thenReturn(mockData);

        Result<List<Page>> result = pageController.getAllPage(Integer.valueOf(0));
        Assertions.assertEquals(1, result.getData().size());
    }

    @Test
    void testGetPageById() throws Exception {
        Page mockData = new Page();
        when(pageService.queryPageById(anyInt())).thenReturn(mockData);

        Result<Page> result = pageController.getPageById(Integer.valueOf(0));
        Assertions.assertEquals(mockData, result.getData());

    }

    @Test
    void testCreatePage() throws Exception {
        when(pageService.createPage(any(Page.class))).thenReturn(new Result<Page>());

        Page page = new Page();
        page.setIsPage(true);
        Result<Page> result = pageController.createPage(page);
        Assertions.assertNull(result.getData());
    }

    @Test
    void testUpdatePage() throws Exception {
        when(pageService.updatePage(any(Page.class))).thenReturn(new Result<Page>());

        Page page = new Page();
        page.setIsPage(true);
        Result<Page> result = pageController.updatePage(page);
        Assertions.assertNull(result.getData());
    }

    @Test
    void testDeletePage() throws Exception {
        when(pageService.delPage(anyInt())).thenReturn(new Result<Page>());

        Result<Page> result = pageController.deletePage(Integer.valueOf(0));
        Assertions.assertEquals(new Result<Page>(), result);
    }

    @Test
    void testPreviewData() {
        PreviewDto mockData = new PreviewDto();
        when(pageService.getPreviewMetaData(any(PreviewParam.class))).thenReturn(mockData);

        Result<PreviewDto> result = pageController.previewData(new PreviewParam());
        Assertions.assertEquals(mockData, result.getData());
    }
}


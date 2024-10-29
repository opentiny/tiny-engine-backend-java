package com.tinyengine.it.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.model.entity.PageHistory;
import com.tinyengine.it.service.app.PageHistoryService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * test case
 *
 * @since 2024-10-29
 */
class PageHistoryControllerTest {
    @Mock
    PageHistoryService pageHistoryService;
    @InjectMocks
    PageHistoryController pageHistoryController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPageHistory() {
        PageHistory mockData = new PageHistory();
        when(pageHistoryService.findPageHistoryByCondition(any(PageHistory.class))).thenReturn(Arrays.<PageHistory>asList(mockData));

        Result<List<PageHistory>> result = pageHistoryController.getAllPageHistory(Integer.valueOf(0));
        Assertions.assertEquals(mockData, result.getData().get(0));
    }

    @Test
    void testGetPageHistoryById() {
        PageHistory mockData = new PageHistory();
        when(pageHistoryService.findPageHistoryById(anyInt())).thenReturn(mockData);

        Result<PageHistory> result = pageHistoryController.getPageHistoryById(Integer.valueOf(0));
        Assertions.assertEquals(mockData, result.getData());
    }

    @Test
    void testCreatePageHistory() {
        PageHistory mockData = new PageHistory();
        mockData.setPage(1);
        mockData.setId(1);
        mockData.setPageContent(new HashMap<>());
        when(pageHistoryService.findPageHistoryById(anyInt())).thenReturn(mockData);
        when(pageHistoryService.createPageHistory(any(PageHistory.class))).thenReturn(Integer.valueOf(0));

        Result<PageHistory> result = pageHistoryController.createPageHistory(mockData);
        Assertions.assertEquals(mockData, result.getData());
    }

    @Test
    void testDeletePageHistory() {
        PageHistory mockData = new PageHistory();
        when(pageHistoryService.findPageHistoryById(anyInt())).thenReturn(mockData);
        when(pageHistoryService.deletePageHistoryById(anyInt())).thenReturn(Integer.valueOf(0));

        Result<PageHistory> result = pageHistoryController.deletePageHistory(Integer.valueOf(0));
        Assertions.assertEquals(mockData, result.getData());
    }
}


package com.tinyengine.it.service.app.impl;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.tinyengine.it.mapper.PageHistoryMapper;
import com.tinyengine.it.model.entity.PageHistory;

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
class PageHistoryServiceImplTest {
    @Mock
    private PageHistoryMapper pageHistoryMapper;
    @InjectMocks
    private PageHistoryServiceImpl pageHistoryServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllPageHistory() {
        PageHistory mockData = new PageHistory();
        when(pageHistoryMapper.queryAllPageHistory()).thenReturn(Arrays.asList(mockData));

        List<PageHistory> result = pageHistoryServiceImpl.findAllPageHistory();
        Assertions.assertEquals(mockData, result.get(0));
    }

    @Test
    void testFindPageHistoryById() {
        PageHistory mockData = new PageHistory();
        when(pageHistoryMapper.queryPageHistoryById(1)).thenReturn(mockData);

        PageHistory result = pageHistoryServiceImpl.findPageHistoryById(1);
        Assertions.assertEquals(mockData, result);
    }

    @Test
    void testFindPageHistoryByCondition() {
        List<PageHistory> mockData = Arrays.asList(new PageHistory());
        when(pageHistoryMapper.queryPageHistoryByCondition(any(PageHistory.class))).thenReturn(mockData);

        List<PageHistory> result = pageHistoryServiceImpl.findPageHistoryByCondition(new PageHistory());
        Assertions.assertEquals(mockData, result);
    }

    @Test
    void testDeletePageHistoryById() {
        when(pageHistoryMapper.deletePageHistoryById(1)).thenReturn(2);

        Integer result = pageHistoryServiceImpl.deletePageHistoryById(1);
        Assertions.assertEquals(2, result);
    }

    @Test
    void testUpdatePageHistoryById() {
        PageHistory param = new PageHistory();
        when(pageHistoryMapper.updatePageHistoryById(param)).thenReturn(1);

        Integer result = pageHistoryServiceImpl.updatePageHistoryById(param);
        Assertions.assertEquals(1, result);
    }

    @Test
    void testCreatePageHistory() {
        PageHistory param = new PageHistory();
        when(pageHistoryMapper.createPageHistory(param)).thenReturn(1);

        Integer result = pageHistoryServiceImpl.createPageHistory(param);
        Assertions.assertEquals(1, result);
    }
}

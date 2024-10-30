package com.tinyengine.it.service.material.impl;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

import com.tinyengine.it.mapper.BlockHistoryMapper;
import com.tinyengine.it.model.entity.BlockHistory;

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
class BlockHistoryServiceImplTest {
    @Mock
    private BlockHistoryMapper blockHistoryMapper;
    @InjectMocks
    private BlockHistoryServiceImpl blockHistoryServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllBlockHistory() {
        BlockHistory mockData = new BlockHistory();
        when(blockHistoryMapper.queryAllBlockHistory()).thenReturn(Arrays.asList(mockData));

        List<BlockHistory> result = blockHistoryServiceImpl.findAllBlockHistory();
        Assertions.assertEquals(mockData, result.get(0));
    }

    @Test
    void testFindBlockHistoryById() {
        BlockHistory mockData = new BlockHistory();
        when(blockHistoryMapper.queryBlockHistoryById(1)).thenReturn(mockData);

        BlockHistory result = blockHistoryServiceImpl.findBlockHistoryById(1);
        Assertions.assertEquals(mockData, result);
    }

    @Test
    void testFindBlockHistoryByCondition() {
        BlockHistory param = new BlockHistory();
        BlockHistory mockData = new BlockHistory();
        when(blockHistoryMapper.queryBlockHistoryByCondition(param)).thenReturn(Arrays.asList(mockData));

        List<BlockHistory> result = blockHistoryServiceImpl.findBlockHistoryByCondition(param);
        Assertions.assertEquals(mockData, result.get(0));
    }

    @Test
    void testDeleteBlockHistoryById() {
        when(blockHistoryMapper.deleteBlockHistoryById(1)).thenReturn(2);

        Integer result = blockHistoryServiceImpl.deleteBlockHistoryById(1);
        Assertions.assertEquals(2, result);
    }

    @Test
    void testUpdateBlockHistoryById() {
        BlockHistory param = new BlockHistory();
        when(blockHistoryMapper.updateBlockHistoryById(param)).thenReturn(2);

        Integer result = blockHistoryServiceImpl.updateBlockHistoryById(param);
        Assertions.assertEquals(2, result);
    }

    @Test
    void testCreateBlockHistory() {
        BlockHistory param = new BlockHistory();
        when(blockHistoryMapper.createBlockHistory(param)).thenReturn(2);

        Integer result = blockHistoryServiceImpl.createBlockHistory(param);
        Assertions.assertEquals(2, result);
    }
}

package com.tinyengine.it.service.material.impl;

import static org.mockito.Mockito.when;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.mapper.BlockGroupMapper;
import com.tinyengine.it.model.dto.BlockGroupDto;
import com.tinyengine.it.model.entity.BlockGroup;

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
class BlockGroupServiceImplTest {
    @Mock
    private BlockGroupMapper blockGroupMapper;
    @InjectMocks
    private BlockGroupServiceImpl blockGroupServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllBlockGroup() {
        BlockGroup mockData = new BlockGroup();
        when(blockGroupMapper.queryAllBlockGroup()).thenReturn(Arrays.<BlockGroup>asList(mockData));

        List<BlockGroup> result = blockGroupServiceImpl.findAllBlockGroup();
        Assertions.assertEquals(mockData, result.get(0));
    }

    @Test
    void testFindBlockGroupById() {
        BlockGroup mockData = new BlockGroup();
        when(blockGroupMapper.queryBlockGroupById(1)).thenReturn(mockData);

        BlockGroup result = blockGroupServiceImpl.findBlockGroupById(1);
        Assertions.assertEquals(mockData, result);
    }

    @Test
    void testFindBlockGroupByCondition() {
        BlockGroup param = new BlockGroup();
        BlockGroupDto blockGroup = new BlockGroupDto();
        when(blockGroupMapper.queryBlockGroupByCondition(param)).thenReturn(Arrays.asList(blockGroup));

        List<BlockGroupDto> result = blockGroupServiceImpl.findBlockGroupByCondition(param);
        Assertions.assertEquals(blockGroup, result.get(0));
    }

    @Test
    void testDeleteBlockGroupById() {
        when(blockGroupMapper.deleteBlockGroupById(1)).thenReturn(2);

        Integer result = blockGroupServiceImpl.deleteBlockGroupById(1);
        Assertions.assertEquals(2, result);
    }

    @Test
    void testUpdateBlockGroupById() {
        BlockGroup param = new BlockGroup();
        when(blockGroupMapper.updateBlockGroupById(param)).thenReturn(1);

        Integer result = blockGroupServiceImpl.updateBlockGroupById(param);
        Assertions.assertEquals(1, result);
    }

    @Test
    void testCreateBlockGroup() {
        BlockGroup param = new BlockGroup();
        when(blockGroupMapper.createBlockGroup(param)).thenReturn(1);
        BlockGroup blockGroupParam = new BlockGroup();
        blockGroupParam.setId(1);
        Result<List<BlockGroupDto>> result = blockGroupServiceImpl.createBlockGroup(blockGroupParam);
        Assertions.assertNotNull(result.getData());
    }
}

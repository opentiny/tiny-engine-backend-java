package com.tinyengine.it.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.mapper.BlockGroupMapper;
import com.tinyengine.it.model.dto.BlockGroupDto;
import com.tinyengine.it.model.entity.BlockGroup;
import com.tinyengine.it.service.material.BlockGroupService;

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
 * @since 2024-11-05
 */
class BlockGroupControllerTest {
    @Mock
    private BlockGroupService blockGroupService;
    @Mock
    private BlockGroupMapper blockGroupMapper;
    @InjectMocks
    private BlockGroupController blockGroupController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBlockGroups() {
        BlockGroupDto mockData = new BlockGroupDto();
        when(blockGroupService.getBlockGroupByIdsOrAppId(any(List.class), anyInt()))
                .thenReturn(Arrays.<BlockGroupDto>asList(mockData));

        Result<List<BlockGroupDto>> result =
                blockGroupController.getAllBlockGroups(Arrays.<Integer>asList(Integer.valueOf(0)), Integer.valueOf(0));
        Assertions.assertEquals(mockData, result.getData().get(0));
    }

    @Test
    void testCreateBlockGroups() {
        when(blockGroupService.createBlockGroup(any(BlockGroup.class))).thenReturn(new Result<List<BlockGroupDto>>());

        Result<List<BlockGroupDto>> result = blockGroupController.createBlockGroups(new BlockGroup());
        Assertions.assertEquals(new Result<List<BlockGroupDto>>(), result);
    }

    @Test
    void testUpdateBlockGroups() {
        when(blockGroupService.updateBlockGroupById(any(BlockGroup.class))).thenReturn(Integer.valueOf(0));
        BlockGroupDto blockGroupDto = new BlockGroupDto();
        when(blockGroupMapper.getBlockGroupsById(anyInt())).thenReturn(Arrays.<BlockGroupDto>asList(blockGroupDto));

        Result<List<BlockGroupDto>> result =
                blockGroupController.updateBlockGroups(Integer.valueOf(0), new BlockGroup());
        Assertions.assertEquals(blockGroupDto, result.getData().get(0));
    }

    @Test
    void testDeleteBlockGroups() {
        BlockGroupDto mockData = new BlockGroupDto();
        mockData.setId(1);
        when(blockGroupService.findBlockGroupById(anyInt())).thenReturn(mockData);
        when(blockGroupService.deleteBlockGroupById(anyInt())).thenReturn(Integer.valueOf(0));
        BlockGroupDto resultData = new BlockGroupDto();
        when(blockGroupMapper.getBlockGroupsById(anyInt())).thenReturn(Arrays.<BlockGroupDto>asList(resultData));

        Result<List<BlockGroupDto>> result = blockGroupController.deleteBlockGroups(1);
        Assertions.assertEquals(resultData, result.getData().get(0));
    }
}

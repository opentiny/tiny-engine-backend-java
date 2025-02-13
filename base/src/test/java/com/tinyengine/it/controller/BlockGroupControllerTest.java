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

package com.tinyengine.it.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.mapper.BlockGroupMapper;
import com.tinyengine.it.model.entity.BlockGroup;
import com.tinyengine.it.service.material.BlockGroupService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
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
        BlockGroup mockData = new BlockGroup();
        when(blockGroupService.getBlockGroupByIdsOrAppId(any(List.class), anyInt(), any()))
                .thenReturn(Arrays.asList(mockData));

        Result<List<BlockGroup>> result =
                blockGroupController.getAllBlockGroups(
                        Arrays.<Integer>asList(Integer.valueOf(0)), Integer.valueOf(0), "block");
        Assertions.assertEquals(mockData, result.getData().get(0));
    }

    @Test
    void testCreateBlockGroups() {
        when(blockGroupService.createBlockGroup(any(BlockGroup.class))).thenReturn(new Result<List<BlockGroup>>());

        Result<List<BlockGroup>> result = blockGroupController.createBlockGroups(new BlockGroup());
        Assertions.assertEquals(new Result<List<BlockGroup>>(), result);
    }

    @Test
    void testUpdateBlockGroups() {
        when(blockGroupService.updateBlockGroupById(any(BlockGroup.class))).thenReturn(1);
        BlockGroup blockGroup = new BlockGroup();
        when(blockGroupService.findBlockGroupById(1)).thenReturn(blockGroup);

        Result<List<BlockGroup>> result =
                blockGroupController.updateBlockGroups(1, new BlockGroup());
        Assertions.assertEquals("200", result.getCode());
    }

    @Test
    void testDeleteBlockGroups() {
        BlockGroup mockData = new BlockGroup();
        mockData.setId(1);
        when(blockGroupService.findBlockGroupById(anyInt())).thenReturn(mockData);
        when(blockGroupService.deleteBlockGroupById(anyInt())).thenReturn(Integer.valueOf(0));

        Result<List<BlockGroup>> result = blockGroupController.deleteBlockGroups(1);
        Assertions.assertEquals("200", result.getCode());
    }
}

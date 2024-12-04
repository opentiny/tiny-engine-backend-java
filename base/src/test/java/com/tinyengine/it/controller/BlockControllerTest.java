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
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.mapper.BlockMapper;
import com.tinyengine.it.mapper.TenantMapper;
import com.tinyengine.it.model.dto.BlockDto;
import com.tinyengine.it.model.dto.BlockParamDto;
import com.tinyengine.it.model.entity.Block;
import com.tinyengine.it.model.entity.Tenant;
import com.tinyengine.it.model.entity.User;
import com.tinyengine.it.service.material.BlockService;
import com.tinyengine.it.service.material.TaskRecordService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * test case
 *
 * @since 2024-11-05
 */
class BlockControllerTest {
    @Mock
    private BlockService blockService;
    @Mock
    private TenantMapper tenantMapper;
    @Mock
    private BlockMapper blockMapper;
    @Mock
    private TaskRecordService taskRecordService;
    @InjectMocks
    private BlockController blockController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBlocks() {
        IPage<Block> page = new Page<Block>();
        ArrayList<Block> mockData = new ArrayList<>();
        page.setRecords(mockData);
        when(blockService.findBlocksByPagetionList(any(BlockParamDto.class))).thenReturn(page);

        Result<List<Block>> result = blockController.getAllBlocks(new BlockParamDto());
        Assertions.assertEquals(mockData, result.getData());
    }

    @Test
    void testGetCountByCondition() {
        Block block = new Block();
        List<Block> mockData = Arrays.<Block>asList(block);
        when(blockMapper.findBlocksByNameCnAndDes(anyString(), anyString())).thenReturn(mockData);

        Result<Integer> result = blockController.getCountByCondition("nameCn", "description");
        Assertions.assertEquals(1, result.getData());
    }

    @Test
    void testGetBlocksById() {
        BlockDto mockData = new BlockDto();
        when(blockMapper.findBlockAndGroupAndHistoByBlockId(anyInt())).thenReturn(mockData);

        Result<BlockDto> result = blockController.getBlocksById(Integer.valueOf(0));
        Assertions.assertEquals(mockData, result.getData());
    }

    @Test
    void testCreateBlocks() {
        BlockDto mockData = new BlockDto();
        when(blockService.createBlock(any(BlockDto.class))).thenReturn(Result.success(mockData));

        Result<BlockDto> result = blockController.createBlocks(new BlockDto());
        Assertions.assertEquals(mockData, result.getData());
    }

    @Test
    void testDeleteBlocks() {
        BlockDto queryDto = new BlockDto();
        when(blockMapper.findBlockAndGroupAndHistoByBlockId(anyInt())).thenReturn(queryDto);

        Result<BlockDto> result = blockController.deleteBlocks(Integer.valueOf(0));
        Assertions.assertEquals(queryDto, result.getData());
    }

    @Test
    void testFind() {
        IPage<Block> page = new Page<Block>();
        ArrayList<Block> mockData = new ArrayList<>();
        Block block = new Block();
        block.setId(1);
        mockData.add(block);
        page.setRecords(mockData);
        when(blockService.findBlocksByPagetionList(any(BlockParamDto.class))).thenReturn(page);
        BlockDto returnBlock = new BlockDto();
        when(blockMapper.findBlockAndHistorByBlockId(anyInt())).thenReturn(Arrays.<BlockDto>asList(returnBlock));

        Result<List<BlockDto>> result = blockController.find(new BlockParamDto());
        Assertions.assertEquals(returnBlock, result.getData().get(0));
    }

    @Test
    void testAllTags() {
        String mockData = "allTagsResponse";
        when(blockService.allTags()).thenReturn(Arrays.<String>asList(mockData));

        Result<List<String>> result = blockController.allTags();
        Assertions.assertEquals(mockData, result.getData().get(0));
    }

    @Test
    void testGetBlocks() {
        IPage<Block> page = new Page<Block>();
        ArrayList<Block> mockData = new ArrayList<>();
        mockData.add(new Block());
        page.setRecords(mockData);
        when(blockService.findBlocksByConditionPagetion(any(Map.class))).thenReturn(page);

        Result<IPage<Block>> result = blockController.getBlocks(new HashMap<>());
        Assertions.assertEquals(mockData, result.getData().getRecords());
    }

    @Test
    void testAllTenant() {
        Tenant mockData = new Tenant();
        when(tenantMapper.queryAllTenant()).thenReturn(Arrays.<Tenant>asList(mockData));

        Result<List<Tenant>> result = blockController.allTenant();
        Assertions.assertEquals(mockData, result.getData().get(0));
    }

    @Test
    void testAllAuthor() {
        User user = new User();
        when(blockService.getUsers(any(List.class))).thenReturn(Arrays.<User>asList(user));

        Result<List<User>> result = blockController.allAuthor();
        Assertions.assertEquals(user, result.getData().get(0));
    }

    @Test
    void testGetAllBlockCategories() {
        List<Block> mockData = new ArrayList<>();
        when(blockService.listNew(anyString(), anyString())).thenReturn(Result.success(mockData));

        Result<List<Block>> result = blockController.getBlockGroups(anyString(), anyString());
        Assertions.assertEquals(mockData, result.getData());
    }

    @Test
    void testUpdateBlocks() {
        when(blockService.updateBlockById(any(BlockDto.class))).thenReturn(1);
        BlockDto returnData = new BlockDto();
        when(blockMapper.findBlockAndGroupAndHistoByBlockId(anyInt())).thenReturn(returnData);

        Result<BlockDto> result = blockController.updateBlocks(returnData, Integer.valueOf(0));
        Assertions.assertEquals(returnData, result.getData());
    }
}

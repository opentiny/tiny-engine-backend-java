/**
 * Copyright (c) 2023 - present TinyEngine Authors.
 * Copyright (c) 2023 - present Huawei Cloud Computing Technologies Co., Ltd.
 * <p>
 * Use of this source code is governed by an MIT-style license.
 * <p>
 * THE OPEN SOURCE SOFTWARE IN THIS PRODUCT IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL,
 * BUT WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS FOR
 * A PARTICULAR PURPOSE. SEE THE APPLICABLE LICENSES FOR MORE DETAILS.
 */

package com.tinyengine.it.service.material.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.context.LoginUserContext;
import com.tinyengine.it.mapper.BlockCarriersRelationMapper;
import com.tinyengine.it.mapper.BlockGroupBlockMapper;
import com.tinyengine.it.mapper.BlockGroupMapper;
import com.tinyengine.it.model.dto.BlockGroupDto;
import com.tinyengine.it.model.entity.Block;
import com.tinyengine.it.model.entity.BlockGroup;

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
class BlockGroupServiceImplTest {
    @Mock
    private BlockGroupMapper blockGroupMapper;

    @InjectMocks
    private BlockGroupServiceImpl blockGroupServiceImpl;

    @Mock
    private BlockCarriersRelationMapper blockCarriersRelationMapper;

    @Mock
    private BlockGroupBlockMapper blockGroupBlockMapper;

    @Mock
    private LoginUserContext loginUserContext;

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
        when(blockGroupMapper.queryBlockGroupAndBlockById(any(), any(), any())).thenReturn(mockData);
        when(loginUserContext.getLoginUserId()).thenReturn("1");

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
        when(blockGroupMapper.deleteBlockGroupById(any())).thenReturn(1);

        Integer result = blockGroupServiceImpl.deleteBlockGroupById(1);
        Assertions.assertEquals(1, result);
    }

    @Test
    void testUpdateBlockGroupById() {
        BlockGroup param = new BlockGroup();
        param.setId(1);
        param.setBlocks(new ArrayList<>());
        when(blockGroupMapper.updateBlockGroupById(param)).thenReturn(1);
        when(blockGroupBlockMapper.deleteBlockGroupBlockByGroupId(null)).thenReturn(1);
        when(blockCarriersRelationMapper.deleteBlockCarriersRelation(null, null, null)).thenReturn(1);
        Integer result = blockGroupServiceImpl.updateBlockGroupById(param);
        Assertions.assertEquals(1, result);
    }

    @Test
    void testCreateBlockGroup() {
        BlockGroup param = new BlockGroup();
        Block block = new Block();
        block.setId(1);
        List<Block> blockList = new ArrayList<>();
        blockList.add(block);
        when(blockGroupMapper.createBlockGroup(param)).thenReturn(1);
        when(loginUserContext.getLoginUserId()).thenReturn("1");
        when(blockGroupMapper.queryBlockGroupAndBlockById(1, null, loginUserContext.getLoginUserId())).thenReturn(
            param);
        BlockGroup blockGroupParam = new BlockGroup();
        blockGroupParam.setId(1);
        Result<BlockGroup> result = blockGroupServiceImpl.createBlockGroup(blockGroupParam);
        Assertions.assertNotNull(result.getData());
    }

    @Test
    void testGetBlockGroupByIdsOrAppId() {
        Integer appId = 1;
        String from = "block";
        List<Integer> paramIdList = new ArrayList<>();
        List<BlockGroup> mockData = new ArrayList<>();
        when(blockGroupMapper.queryBlockGroupAndBlockById(any(), any(), any())).thenReturn(new BlockGroup());
        when(blockGroupMapper.queryBlockGroupByAppId(any(), any(), any())).thenReturn(mockData);
        when(loginUserContext.getLoginUserId()).thenReturn("1");

        // not empty param
        List<BlockGroup> result = blockGroupServiceImpl.getBlockGroupByIdsOrAppId(paramIdList, appId, from);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());

        // empty param
        when(blockGroupMapper.queryAllBlockGroupAndBlock(any(), any())).thenReturn(mockData);
        result = blockGroupServiceImpl.getBlockGroupByIdsOrAppId(null, null, null);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }
}

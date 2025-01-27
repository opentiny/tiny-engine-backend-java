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

package com.tinyengine.it.service.material.impl;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.mapper.AppMapper;
import com.tinyengine.it.mapper.BlockGroupMapper;
import com.tinyengine.it.mapper.BlockMapper;
import com.tinyengine.it.mapper.UserMapper;
import com.tinyengine.it.model.dto.BlockDto;
import com.tinyengine.it.model.dto.BlockParam;
import com.tinyengine.it.model.dto.BlockParamDto;
import com.tinyengine.it.model.dto.NotGroupDto;
import com.tinyengine.it.model.entity.App;
import com.tinyengine.it.model.entity.Block;
import com.tinyengine.it.model.entity.BlockGroup;
import com.tinyengine.it.model.entity.User;

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
 * @since 2024-10-29
 */
class BlockServiceImplTest {
    @Mock
    private BlockMapper blockMapper;

    @InjectMocks
    private BlockServiceImpl blockServiceImpl;

    @Mock
    private UserMapper userMapper;
    @Mock
    private AppMapper appMapper;
    @Mock
    private BlockGroupMapper blockGroupMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testQueryAllBlock() {
        Block mockData = new Block();
        when(blockMapper.queryAllBlock()).thenReturn(Arrays.<Block>asList(mockData));

        List<Block> result = blockServiceImpl.queryAllBlock();
        Assertions.assertEquals(mockData, result.get(0));
    }

    @Test
    void testQueryBlockById() {
        BlockDto mockData = new BlockDto();
        when(blockMapper.findBlockAndGroupAndHistoByBlockId(1)).thenReturn(mockData);

        BlockDto result = blockServiceImpl.queryBlockById(1);
        Assertions.assertEquals(mockData, result);
    }

    @Test
    void testQueryBlockByCondition() {
        Block mockData = new Block();
        Block param = new Block();
        when(blockMapper.queryBlockByCondition(param)).thenReturn(Arrays.<Block>asList(mockData));

        List<Block> result = blockServiceImpl.queryBlockByCondition(param);
        Assertions.assertEquals(mockData, result.get(0));
    }

    @Test
    void testDeleteBlockById() {
        when(blockMapper.deleteBlockById(123)).thenReturn(1);

        Integer result = blockServiceImpl.deleteBlockById(123);
        Assertions.assertEquals(1, result);
    }

    @Test
    void testCreateBlock() {
        when(blockMapper.createBlock(any())).thenReturn(1);
        BlockDto t = new BlockDto();
        t.setName("test");
        when(blockMapper.findBlockAndGroupAndHistoByBlockId(any())).thenReturn(t);
        BlockParam blockParam = new BlockParam();
        blockParam.setId(0);
        Result<BlockDto> result = blockServiceImpl.createBlock(blockParam);
        Assertions.assertEquals("test", result.getData().getName());
    }

    @Test
    void testUpdateBlockById() {
        BlockParam blockParam = new BlockParam();
        when(blockMapper.updateBlockById(any())).thenReturn(1);
        when(blockMapper.findBlockAndGroupAndHistoByBlockId(anyInt())).thenReturn(new BlockDto());
        Block block = new Block();
        block.setAppId(1);
        when(blockMapper.queryBlockById(blockParam.getId())).thenReturn(block);

        Result<BlockDto> result = blockServiceImpl.updateBlockById(blockParam, 1);
        Assertions.assertEquals(null, result.getData());
    }

    @Test
    void testGetBlockAssets() {
        Map<String, Object> assetMap = new HashMap<>();
        assetMap.put("material", asList("material"));
        assetMap.put("scripts", asList("scripts"));
        assetMap.put("styles", asList("styles"));

        Block block = new Block();
        block.setAssets(assetMap);
        when(blockMapper.selectList(any(Wrapper.class))).thenReturn(asList(block));

        HashMap<String, Object> pageContent = new HashMap<String, Object>() {
            {
                put("componentName", "blockName1");
                put("componentType", "Block");
            }
        };

        Map<String, List<String>> result = blockServiceImpl.getBlockAssets(pageContent, "framework");
        Assertions.assertEquals("material", result.get("material").get(0));
        Assertions.assertEquals("scripts", result.get("scripts").get(0));
        Assertions.assertEquals("styles", result.get("styles").get(0));
    }

    @Test
    void testGetBlockInfo() {
        List<Object> mockData = new ArrayList<>();
        when(blockMapper.selectList(any(Wrapper.class))).thenReturn(mockData);

        List<String> param = asList("block");
        List<Block> result = blockServiceImpl.getBlockInfo(param, "framework");
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testTraverseBlocks() throws JsonProcessingException {
        // map
        String mapContent = "{\n"
                + "            \"componentName\":\"blockName1\",\n"
                + "            \"componentType\":\"Block\"\n"
                + "        }";
        List<String> nameList = new ArrayList<>();
        nameList.add("blockName");
        blockServiceImpl.traverseBlocks(mapContent, nameList);
        assertTrue(nameList.contains("blockName"));
        assertTrue(nameList.contains("blockName1"));

        String listContent = " [\n"
                + "        {\"componentName\":\"blockName2\", \"componentType\":\"Block\"},\n"
                + "        {\"componentName\":\"blockName3\", \"componentType\":\"Block\"}"
                + "        ]";
        nameList = new ArrayList<>();
        nameList.add("blockName");
        blockServiceImpl.traverseBlocks(listContent, nameList);
        assertTrue(nameList.contains("blockName2"));
        assertTrue(nameList.contains("blockName3"));
        assertTrue(nameList.contains("blockName"));
    }

    @Test
    void testIsBlock() {
        boolean hasResult = blockServiceImpl.isBlock(new HashMap() {
            {
                put("componentType", "Block");
            }
        });
        Assertions.assertEquals(true, hasResult);
    }

    @Test
    void testFindBlocksByPagetionList() {
        IPage<Block> page = new Page<>();
        when(blockMapper.selectPage(any(), any(Wrapper.class))).thenReturn(page);
        BlockParamDto param = new BlockParamDto();
        param.setAppId("1");
        param.setName("name");
        param.setDescription("desc");
        param.setLabel("label");
        param.setSort("sort:sort");
        param.setLimit(1);
        param.setStart(1);

        IPage<Block> result = blockServiceImpl.findBlocksByPagetionList(param);
        Assertions.assertEquals(page, result);
    }


    @Test
    void testAllTags() {
        List<Block> mockData = new ArrayList<>();
        Block block = new Block();
        block.setTags(asList("tag"));
        mockData.add(block);
        when(blockMapper.selectList(any(Wrapper.class))).thenReturn(mockData);

        List<String> result = blockServiceImpl.allTags();
        Assertions.assertEquals("tag", result.get(0));
    }

    @Test
    void testGetNotInGroupBlocks() {
        BlockDto blockDto = new BlockDto();
        List<BlockDto> mockData = Arrays.asList(blockDto);
        NotGroupDto notGroupDto = new NotGroupDto();
        List<BlockGroup> blockGroups = new ArrayList<>();
        when(blockMapper.findBlocksReturn(notGroupDto)).thenReturn(mockData);
        when(userMapper.queryUserById(anyInt())).thenReturn(new User());
        when(blockGroupMapper.findBlockGroupByBlockId(blockDto.getId(), blockDto.getCreatedBy())).thenReturn(blockGroups);

        List<BlockDto> result = blockServiceImpl.getNotInGroupBlocks(notGroupDto);
        Assertions.assertEquals(new ArrayList<>(), result);
    }

    @Test
    void testFindBlocksByConditionPagetion() {
        when(blockMapper.selectList(any(Wrapper.class))).thenReturn(Arrays.asList(new Block()));

        IPage<Block> page = new Page<>();
        when(blockMapper.selectPage(any(), any(Wrapper.class))).thenReturn(page);

        HashMap<String, String> request = new HashMap<>();
        request.put("public_scope_mode", "request");
        request.put("name_cn", "name_cn");
        request.put("description", "description");
        IPage<Block> result = blockServiceImpl.findBlocksByConditionPagetion(request);
        Assertions.assertEquals(page, result);
    }

    @Test
    void testListNewWhenGroupIdNotEmpty() {
        List<Block> blocksList = new ArrayList<>();
        when(blockMapper.findBlockByBlockGroupId(anyInt(), any())).thenReturn(blocksList);

        App app = new App();
        app.setId(1);
        when(appMapper.queryAppById(anyInt())).thenReturn(app);

        Result<List<Block>> result = blockServiceImpl.listNew("1", "1");
        Assertions.assertEquals(blocksList, result.getData());
    }

    @Test
    void testListNewWhenGroupIdIsEmpty() {
        List<Block> blocksList1 = new ArrayList<>();
        blocksList1.add(new Block());
        when(blockMapper.queryBlockByCondition(any(Block.class))).thenReturn(blocksList1);

        List<Block> blocksList2 = new ArrayList<>();
        when(blockMapper.findBlocksByBlockGroupIdAppId(anyInt())).thenReturn(blocksList2);

        App app = new App();
        app.setId(1);
        when(appMapper.queryAppById(anyInt())).thenReturn(app);

        HashMap<String, String> param = new HashMap<>();

        param.put("appId", "1");
        param.put("description", "description");
        Result<List<Block>> result = blockServiceImpl.listNew("1", null);
        Assertions.assertEquals(1, result.getData().size());
    }
}

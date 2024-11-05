package com.tinyengine.it.service.material.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.mapper.BlockMapper;
import com.tinyengine.it.model.dto.BlockDto;
import com.tinyengine.it.model.entity.Block;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

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
        Block mockData = new Block();
        when(blockMapper.queryBlockById(1)).thenReturn(mockData);

        Block result = blockServiceImpl.queryBlockById(1);
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
        BlockDto blockDto = new BlockDto();
        blockDto.setId(1);
        blockDto.setScreenshot("aa");
        blockDto.setLabel("bb");
        blockDto.setFramework("cc");
        blockDto.setPlatformId(1);
        blockDto.setAppId(1);
        blockDto.setGroups(Arrays.asList(1));
        blockDto.setName("testBlock");
        Result<BlockDto> result = blockServiceImpl.createBlock(blockDto);
        Assertions.assertEquals("test", result.getData().getName());
    }

    @Test
    void testUpdateBlockById() {
        when(blockMapper.updateBlockById(any())).thenReturn(1);
        BlockDto blockDto = new BlockDto();
        blockDto.setId(1);
        blockDto.setName("BlockTest1");
        blockDto.setScreenshot("aa");
        blockDto.setLabel("bb");
        Integer result = blockServiceImpl.updateBlockById(blockDto);
        Assertions.assertEquals(1, result);
    }

    @Test
    void testGetBlockAssets() {
        Map<String, Object> assetMap = new HashMap<>();
        assetMap.put("material", Arrays.asList("material"));
        assetMap.put("scripts", Arrays.asList("scripts"));
        assetMap.put("styles", Arrays.asList("styles"));

        Block block = new Block();
        block.setAssets(assetMap);
        when(blockMapper.selectList(any(Wrapper.class))).thenReturn(Arrays.asList(block));

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

        List<String> param = Arrays.asList("block");
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
        boolean result = blockServiceImpl.isBlock(new HashMap() {
            {
                put("componentType", "Block");
            }
        });
        Assertions.assertEquals(true, result);
    }
}

package com.tinyengine.it.service.material.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.mapper.BlockMapper;
import com.tinyengine.it.model.entity.Block;
import com.tinyengine.it.service.material.BlockService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The type Block service.
 */
@Service
@Slf4j
public class BlockServiceImpl implements BlockService {

    @Autowired
    private BlockMapper blockMapper;

    /**
     * 查询表t_block所有数据
     */
    @Override
    public List<Block> queryAllBlock() throws ServiceException {
        return blockMapper.queryAllBlock();
    }

    /**
     * 根据主键id查询表t_block信息
     *
     * @param id
     */
    @Override
    public Block queryBlockById(@Param("id") Integer id) throws ServiceException {
        return blockMapper.queryBlockById(id);
    }

    /**
     * 根据条件查询表t_block数据
     *
     * @param block
     */
    @Override
    public List<Block> queryBlockByCondition(Block block) throws ServiceException {
        return blockMapper.queryBlockByCondition(block);
    }

    /**
     * 根据主键id删除表t_block数据
     *
     * @param id
     */
    @Override
    public Integer deleteBlockById(@Param("id") Integer id) throws ServiceException {
        return blockMapper.deleteBlockById(id);
    }

    /**
     * 根据主键id更新表t_block数据
     *
     * @param block
     */
    @Override
    public Integer updateBlockById(Block block) throws ServiceException {
        return blockMapper.updateBlockById(block);
    }

    /**
     * 新增表t_block数据
     *
     * @param block
     */
    @Override
    public Integer createBlock(Block block) throws ServiceException {
        return blockMapper.createBlock(block);
    }

    /**
     * Gets block assets.
     *
     * @param pageContent the page content
     * @param framework   the framework
     * @return the block assets
     * @throws Exception the exception
     */
    public Map<String, List<String>> getBlockAssets(Map<String, Object> pageContent, String framework)
        throws Exception {
        List<String> block = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        traverseBlocks(objectMapper.writeValueAsString(pageContent), block);
        if (block.isEmpty()) {
            return Collections.emptyMap();
        }
        // 获取区块列表
        List<Block> blocksList = getBlockInfo(block, framework);
        // 对获取区块列表后的数据处理
        Map<String, List<String>> mergedAssets = new HashMap<>();
        mergedAssets.put("material", new ArrayList<>());
        mergedAssets.put("scripts", new ArrayList<>());
        mergedAssets.put("styles", new ArrayList<>());

        // Merge the assets using streams
        return blocksList.stream().map(Block::getAssets).map(assetsMap -> {
            Map<String, List<String>> tempMap = new HashMap<>();
            tempMap.put("material", (List<String>)assetsMap.getOrDefault("material", new ArrayList<>()));
            tempMap.put("scripts", (List<String>)assetsMap.getOrDefault("scripts", new ArrayList<>()));
            tempMap.put("styles", (List<String>)assetsMap.getOrDefault("styles", new ArrayList<>()));
            return tempMap;
        }).reduce(mergedAssets, (acc, curr) -> {
            acc.get("material").addAll(curr.get("material"));
            acc.get("scripts").addAll(curr.get("scripts"));
            acc.get("styles").addAll(curr.get("styles"));
            return acc;
        }, (map1, map2) -> {
            map1.get("material").addAll(map2.get("material"));
            map1.get("scripts").addAll(map2.get("scripts"));
            map1.get("styles").addAll(map2.get("styles"));
            return map1;
        });
    }

    /**
     * Gets block info.
     *
     * @param block     the block
     * @param framework the framework
     * @return the block info
     */
    public List<Block> getBlockInfo(List<String> block, String framework) {
        // 创建 QueryWrapper 实例
        QueryWrapper<Block> queryWrapper = new QueryWrapper<>();
        if (block != null && !block.isEmpty()) {

            // 处理 blockLabelName 为数组的情况
            String labelsCondition =
                block.stream().map(name -> "label = '" + name + "'").collect(Collectors.joining(" OR "));

            // 添加标签条件
            queryWrapper.and(wrapper -> wrapper.apply(labelsCondition));

            // 添加框架条件
            queryWrapper.eq("framework", framework);

        }

        // 执行查询并返回结果
        return blockMapper.selectList(queryWrapper);
    }

    /**
     * Traverse blocks.
     *
     * @param content the content
     * @param block   the block
     * @throws JsonProcessingException the json processing exception
     */
    public void traverseBlocks(String content, List<String> block) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(content);
        // 判断传过来的参数是JSON数组还是JSON对象
        if (content != null && jsonNode.isArray()) {
            List<String> schema = objectMapper.readValue(content, List.class);
            for (Object prop : (List<?>)schema) {
                traverseBlocks(objectMapper.writeValueAsString(prop), block);
            }
        } else {
            Map<String, Object> schema = objectMapper.readValue(content, Map.class);
            Map<?, ?> schemaMap = (Map<?, ?>)schema;
            if (isBlock(schemaMap) && !block.contains(schemaMap.get("componentName"))) {
                block.add((String)schemaMap.get("componentName"));
            }
            if (schemaMap.containsKey("children") && schemaMap.get("children") instanceof List) {
                traverseBlocks(objectMapper.writeValueAsString(schemaMap.get("children")), block);
            }
        }

    }

    /**
     * Is block boolean.
     *
     * @param schema the schema
     * @return the boolean
     */
    public boolean isBlock(Map<?, ?> schema) {
        return schema != null && "Block".equals(schema.get("componentType"));
    }

}

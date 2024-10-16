package com.tinyengine.it.service.impl.material;

import com.tinyengine.it.config.Enums;
import com.tinyengine.it.exception.ServiceException;
import com.tinyengine.it.mapper.BlockHistoriesMapper;
import com.tinyengine.it.mapper.BlocksMapper;
import com.tinyengine.it.mapper.TaskRecordMapper;
import com.tinyengine.it.model.dto.BlockBuildDto;
import com.tinyengine.it.model.entity.BlockHistories;
import com.tinyengine.it.model.entity.Blocks;
import com.tinyengine.it.model.entity.TaskRecord;
import com.tinyengine.it.service.material.BlockBuildService;
import com.tinyengine.it.service.material.BlocksService;
import com.tinyengine.it.utils.ExecuteJavaScript;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class BlockBuildServiceImpl implements BlockBuildService {
    @Autowired
    BlocksService blocksService;
    @Autowired
    BlockHistoriesMapper blockHistoriesMapper;
    @Autowired
    TaskRecordMapper taskRecordMapper;
    @Autowired
    BlocksMapper blocksMapper;
    @Autowired
    ExecuteJavaScript executeJavaScript;

    private static final Logger logger = LoggerFactory.getLogger(BlockBuildServiceImpl.class);

    @Override
    public CompletableFuture<Void> start(int blockId, int taskId, BlockBuildDto blockBuildDto) {
        return CompletableFuture.runAsync(() -> {
            logger.info("开始区块构建", blockId, taskId);
            Blocks blocks = blocksService.findBlocksById(blockId);
            TaskRecord taskRecord = new TaskRecord();
            taskRecord.setId(taskId);
            taskRecord.setProgress("generating code");
            taskRecord.setTaskStatus(Enums.E_TaskStatus.RUNNING.getValue());
            taskRecordMapper.updateTaskRecordById(taskRecord);
            // 获取dsl转换的源码
            List<Map<String, Object>> sourceCode = this.translate(blocks);
        });

    }


    private String kebabToPascalCase(String label) {
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;

        for (char c : label.toCharArray()) {
            if (c == '-') {
                capitalizeNext = true;
            } else {
                if (capitalizeNext) {
                    result.append(Character.toUpperCase(c));
                    capitalizeNext = false;
                } else {
                    result.append(c);
                }
            }
        }

        return result.toString();
    }

    /**
     * @param blocks 区块记录
     * @returns 转换后的代码
     */
    public List<Map<String, Object>> translate(Blocks blocks) {
        if (blocks.getContent().isEmpty()) {
            throw new ServiceException("block undefined, check block content format");
        }
        String label = blocks.getLabel();

        // 使用历史记录中的schema做转换
        Map<String, Object> content = blocks.getContent();
        if (content.isEmpty()) {
            throw new ServiceException("unexpected history content");
        }

        // 获取嵌套的区块
        List<Blocks> innerBlocks = new ArrayList<>();
        List<String> innerBlocksLabel = this.traverseBlocks(content);
        if (!innerBlocksLabel.isEmpty()) {
            try {
                innerBlocks = blocksMapper.findBlocksByLables(innerBlocksLabel);
            } catch (Exception e) {
                throw new ServiceException("strapi query blocks by label failed");
            }
        }

        List<Map<String, Object>> blocksData = innerBlocks.stream()
                .filter(b -> !b.getLabel().equals(label))
                .map(b -> {
                    Map<String, Object> blockMap = new HashMap<>();
                    blockMap.put("content", b.getContent());
                    blockMap.put("label", b.getLabel());
                    return blockMap;
                })
                .collect(Collectors.toList());

        // 调用 executeJavaScript 方法执行 JavaScript 脚本
        Map<String, Object> pageInfo = new HashMap<>();
        pageInfo.put("name", label);
        pageInfo.put("schema", content);
        List<Map<String, Object>> componentsMap = new ArrayList<>();
        // List<Map<String, Object>> result = executeJavaScript.executeJavaScript(pageInfo, blocksData, componentsMap);
        return null;
    }

    public List<String> traverseBlocks(Map<String, Object> schema) {
        List<String> innerBlocks = new ArrayList<>();
        traverse(schema, innerBlocks);
        return innerBlocks;
    }

    private void traverse(Map<String, Object> schema, List<String> innerBlocks) {
        if (schema.containsKey("children") && schema.get("children") instanceof List) {
            List<Map<String, Object>> children = (List<Map<String, Object>>) schema.get("children");
            for (Map<String, Object> child : children) {
                traverse(child, innerBlocks);
            }
        }

        if (isBlock(schema)) {
            String componentName = (String) schema.get("componentName");
            if (!innerBlocks.contains(componentName)) {
                innerBlocks.add(componentName);
            }
        }
    }

    public boolean isBlock(Map<String, Object> schema) {
        return schema != null && schema.containsKey("componentType") && "Block".equals(schema.get("componentType"));
    }

    @Override
    public int ensureBlockId(Blocks blocks) {
        if (blocks.getId() != null) {
            return blocks.getId();
        }
        // 查询当前用户信息
        int userId = 86;
        Blocks querBlock = new Blocks();
        querBlock.setLabel(blocks.getLabel());
        querBlock.setFramework(blocks.getFramework());
        querBlock.setCreatedBy(86);
        List<Blocks> blockList = blocksService.findBlocksByCondition(querBlock);
        if (blockList.isEmpty()) {
            blocksService.createBlocks(blocks);
            return blocks.getId();
        }

        return blockList.get(0).getId();

    }

    @Override
    public List<BlockHistories> isHistoryExisted(Integer id, String version) {
        BlockHistories query = new BlockHistories();
        query.setBlockId(id);
        query.setVersion(version);
        List<BlockHistories> blockHistories = blockHistoriesMapper.findBlockHistoriesByCondition(query);

        return blockHistories;
    }

}

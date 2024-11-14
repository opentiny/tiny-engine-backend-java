package com.tinyengine.it.service.material.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.enums.Enums;
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.common.log.SystemServiceLog;
import com.tinyengine.it.mapper.AppMapper;
import com.tinyengine.it.mapper.BlockMapper;
import com.tinyengine.it.mapper.UserMapper;
import com.tinyengine.it.model.dto.BlockDto;
import com.tinyengine.it.model.dto.BlockParamDto;
import com.tinyengine.it.model.entity.App;
import com.tinyengine.it.model.entity.Block;
import com.tinyengine.it.model.entity.BlockGroup;
import com.tinyengine.it.model.entity.User;
import com.tinyengine.it.service.material.BlockService;

import lombok.extern.slf4j.Slf4j;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The type Block service.
 *
 * @since 2024-10-20
 */
@Service
@Slf4j
public class BlockServiceImpl implements BlockService {
    @Autowired
    private BlockMapper blockMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AppMapper appMapper;

    /**
     * 查询表t_block所有数据
     *
     * @return Block
     */
    @Override
    public List<Block> queryAllBlock() {
        return blockMapper.queryAllBlock();
    }

    /**
     * 根据主键id查询表t_block信息
     *
     * @param id id
     * @return block
     */
    @Override
    public Block queryBlockById(@Param("id") Integer id) {
        return blockMapper.queryBlockById(id);
    }

    /**
     * 根据条件查询表t_block数据
     *
     * @param block block
     * @return block
     */
    @Override
    public List<Block> queryBlockByCondition(Block block) {
        return blockMapper.queryBlockByCondition(block);
    }

    /**
     * 根据主键id删除表t_block数据
     *
     * @param id id
     * @return execute success data number
     */
    @Override
    public Integer deleteBlockById(@Param("id") Integer id) {
        return blockMapper.deleteBlockById(id);
    }

    /**
     * 根据主键id更新表t_block数据
     *
     * @param blockDto blockDto
     * @return blockDto
     */
    @Override
    public Integer updateBlockById(BlockDto blockDto) {
        // public 不是部分公开, 则public_scope_tenants为空数组
        // 把前端传参赋值给实体
        Block blocks = new Block();
        BeanUtils.copyProperties(blockDto, blocks);
        blocks.setOccupierBy(String.valueOf(1));
        // 处理区块截图
        if (!blockDto.getScreenshot().isEmpty() && !blockDto.getLabel().isEmpty()) {
            // 图片上传,此处给默认值空字符
            blocks.setScreenshot("");
        }
        List<Integer> groups = blockDto.getGroups().stream()
                .filter(obj -> obj instanceof Integer) // 过滤出 Integer 类型的对象
                .map(obj -> (Integer) obj) // 转换为 Integer 类型
                .collect(Collectors.toList()); // 收集为 List<Integer>;
        if (!groups.isEmpty()) {
            int groupId = groups.get(0);
            blocks.setBlockGroupId(groupId);
        }
        return blockMapper.updateBlockById(blocks);
    }

    /**
     * 新增表t_block数据
     *
     * @param blockDto the block dto
     * @return execute success the result
     */
    @Override
    public Result<BlockDto> createBlock(BlockDto blockDto) {
        // 对接收到的参数occupier为对应的一个对象，进行特殊处理并重新赋值
        Block blocks = new Block();
        if (blockDto.getOccupier() != null) {
            blocks.setOccupierBy(String.valueOf(blockDto.getOccupier().getId()));
        }
        BeanUtils.copyProperties(blockDto, blocks);
        blocks.setIsDefault(false);
        blocks.setIsOfficial(false);
        blocks.setPlatformId(1); // 新建区块给默认值
        List<Object> groups = blockDto.getGroups();
        if (!groups.isEmpty() && groups.get(0) instanceof Integer) {
            Integer groupId = (Integer) groups.get(0); // 强制类型转换
            blocks.setBlockGroupId(groupId);
        }
        int result = blockMapper.createBlock(blocks);
        if (result < 1) {
            return Result.failed(ExceptionEnum.CM001);
        }
        int id = blocks.getId();
        BlockDto blocksResult = blockMapper.findBlockAndGroupAndHistoByBlockId(id);
        return Result.success(blocksResult);
    }

    /**
     * Gets block assets.
     *
     * @param pageContent the page content
     * @param framework   the framework
     * @return the block assets
     */
    public Map<String, List<String>> getBlockAssets(Map<String, Object> pageContent, String framework) {
        List<String> block = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            traverseBlocks(objectMapper.writeValueAsString(pageContent), block);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
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
            tempMap.put("material", (List<String>) assetsMap.getOrDefault("material", new ArrayList<>()));
            tempMap.put("scripts", (List<String>) assetsMap.getOrDefault("scripts", new ArrayList<>()));
            tempMap.put("styles", (List<String>) assetsMap.getOrDefault("styles", new ArrayList<>()));
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
            String labelsCondition = block.stream()
                    .map(name -> "label = '" + name + "'")
                    .collect(Collectors.joining(" OR "));

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
            for (Object prop : schema) {
                traverseBlocks(objectMapper.writeValueAsString(prop), block);
            }
        } else {
            Map<?, ?> schemaMap = objectMapper.readValue(content, Map.class);
            if (isBlock(schemaMap) && !block.contains(schemaMap.get("componentName"))) {
                if (schemaMap.get("componentName") instanceof String) {
                    block.add((String) schemaMap.get("componentName"));
                }
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

    /**
     * 生态中心区块列表分页查询
     *
     * @param blockParamDto blockParamDto
     * @return block
     */
    @Override
    public IPage<Block> findBlocksByPagetionList(BlockParamDto blockParamDto) {
        String appId = blockParamDto.getAppId();
        // 如果 appId 存在并且不匹配指定的正则表达式，则删除它
        if (appId != null && !Pattern.matches("^[1-9]+[0-9]*$", appId)) {
            blockParamDto.setAppId(null); // 设置成null达到map中remove的效果
        }
        // 获取查询条件
        String nameCn = blockParamDto.getName();
        String description = blockParamDto.getDescription();
        String label = blockParamDto.getLabel();

        QueryWrapper<Block> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(nameCn)) {
            queryWrapper.like("name", nameCn);
        }
        if (StringUtils.isNotEmpty(description)) {
            queryWrapper.or().like("description", description);
        }
        if (StringUtils.isNotEmpty(label)) {
            queryWrapper.or().eq("label", label);
        }
        String sort = blockParamDto.getSort(); // nodejs中页面传参"updated_at:DESC"
        // 获取是按升序还是降序排列
        if (sort != null) {
            String[] temp = sort.split(":");
            String sortOrder = temp[1];
            if ("ASC".equalsIgnoreCase(sortOrder)) {
                queryWrapper.orderByAsc("created_time");
            } else {
                queryWrapper.orderByDesc("last_updated_time");
            }
        }
        // 把start、limit转为java分页的pageNum、pageSize
        int limit = blockParamDto.getLimit() != null ? Integer.parseInt(blockParamDto.getLimit()) : 0;
        int start = blockParamDto.getStart() != null ? Integer.parseInt(blockParamDto.getStart()) : 0;
        int pageNum = start == 0 && limit == 0 ? 1 : (start / limit) + 1;
        int pageSize = limit == 0 ? 10 : limit;
        Page<Block> page = new Page<>(pageNum, pageSize);
        return blockMapper.selectPage(page, queryWrapper);
    }

    /**
     * 查找表中所有tags
     *
     * @return list
     */
    @SystemServiceLog(description = "allTags 查找表中所有tags 实现类")
    @Override
    public List<String> allTags() {
        QueryWrapper<Block> queryWrapper = new QueryWrapper<>();
        ObjectMapper objectMapper = new ObjectMapper();
        queryWrapper.select("tags").isNotNull("tags");
        List<Block> allBlocksList = blockMapper.selectList(queryWrapper);
        return allBlocksList.stream()
                .flatMap(blocks -> blocks.getTags().stream())
                .collect(Collectors.toList());
    }

    /**
     * 获取不在分组内的区块
     *
     * @param groupId groupId
     * @return the list
     */
    @SystemServiceLog(description = "getNotInGroupBlocks 获取不在分组内的区块 实现类")
    @Override
    public List<BlockDto> getNotInGroupBlocks(Integer groupId) {
        // 获取缓存中的登录用户
        int userId = 1;
        User user = userMapper.queryUserById(userId);
        List<BlockDto> blocksList = blockMapper.findBlocksReturn();
        return blocksList.stream()
                .filter(item -> {
                    // 过滤已发布的
                    if (item.getLastBuildInfo() == null || item.getContent() == null || item.getAssets() == null) {
                        return false;
                    }
                    // 组过滤
                    if (item.getGroups() != null && item.getGroups().stream()
                            .anyMatch(group -> group instanceof BlockGroup
                                    && ((BlockGroup) group).getId().equals(groupId))) {
                        return false;
                    }
                    // 公开范围过滤
                    if (item.getPublicStatus() == Enums.Scope.FULL_PUBLIC.getValue()) {
                        return true;
                    }
                    return user != null && item.getPublicStatus() == Enums.Scope.PUBLIC_IN_TENANTS.getValue();
                })
                .collect(Collectors.toList());
    }

    /**
     * 根据条件进行分页查询
     *
     * @param request request
     * @return the ipage
     */
    @Override
    public IPage<Block> findBlocksByConditionPagetion(Map<String, String> request) {
        String publicScopeMode = request.get("public_scope_mode");
        if (publicScopeMode != null) {
            request.remove("public_scope_mode");
        }

        // 获取查询条件
        String nameCn = request.get("name_cn");
        String description = request.get("description");

        QueryWrapper<Block> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper ->
                wrapper.like(StringUtils.isNotEmpty(nameCn), "name", nameCn)
                        .or()
                        .like(StringUtils.isNotEmpty(description), "description", description)
        );
        List<Block> blocksList = blockMapper.selectList(queryWrapper);
        Page<Block> page = new Page<>(1, blocksList.size());
        return blockMapper.selectPage(page, queryWrapper);
    }

    /**
     * 获取用户
     *
     * @param blocksList the block list
     * @return the list
     */
    @Override
    public List<User> getUsers(List<Block> blocksList) {
        Set<String> userSet = new HashSet<>();

        // 提取 createdBy 列表中的唯一值
        blocksList.forEach(item -> {
            if (item.getCreatedBy() != null && !userSet.contains(item.getCreatedBy())) {
                userSet.add(String.valueOf(item.getCreatedBy()));
            }
        });

        List<String> userIdsList = new ArrayList<>(userSet);

        // 模拟执行 SQL 查询并返回结果
        return userMapper.selectUsersByIds(userIdsList);
    }

    /**
     * 获取区块
     *
     * @param map the map
     * @return the list
     */
    @Override
    public Result<List<Block>> listNew(Map<String, String> map) {
        int groupId = 0;
        int appId = 0;
        if (map.get("groupId") != null && !map.get("groupId").isEmpty()) {
            groupId = Integer.parseInt(map.get("groupId"));
        }
        if (map.get("appId") != null && !map.get("appId").isEmpty()) {
            appId = Integer.parseInt(map.get("appId"));
        }
        App apps = appMapper.queryAppById(appId);
        if (groupId != 0) {
            if (!apps.getId().equals(appId)) {
                return Result.failed(ExceptionEnum.CM206);
            }
        }
        List<Block> blocksList = new ArrayList<>();
        // 如果有 groupId, 只查group下的block
        if (groupId != 0) {
            blocksList = blockMapper.findBlocksByBlockGroupId(groupId);
            return Result.success(blocksList);
        }
        // 如果没有 groupId
        // 1. 查询和 app 相关的所有 group
        // 2. 组合 groups 下的所有 block
        // 3. 查询个人创建的 blocks
        // 4. 将个人的和 groups 下的 blocks 合并去重
        blocksList = blockMapper.findBlocksByBlockGroupIdAppId(appId);
        List<Block> appBlocks = blocksList;
        // 通过createBy查询区块表数据
        Block blocks = new Block();
        List<Block> personalBlocks = queryBlockByCondition(blocks);
        List<Block> retBlocks = new ArrayList<>();
        // 合并 personalBlocks 和 appBlocks 数组
        List<Block> combinedBlocks = Stream.concat(personalBlocks.stream(), appBlocks.stream())
                .collect(Collectors.toList());
        // 遍历合并后的数组，检查是否存在具有相同 id 的元素
        combinedBlocks.forEach(block -> {
            boolean isFind = retBlocks.stream()
                    .anyMatch(retBlock -> Objects.equals(retBlock.getId(), block.getId()));
            if (!isFind) {
                retBlocks.add(block);
            }
        });
        // 给is_published赋值
        List<Block> result = retBlocks.stream()
                .map(b -> {
                    boolean isPublished = b.getLastBuildInfo() != null
                            && b.getLastBuildInfo().get("result") instanceof Boolean
                            ? (Boolean) b.getLastBuildInfo().get("result") : Boolean.FALSE;
                    b.setIsPublished(isPublished);
                    return b;
                })
                .collect(Collectors.toList());
        return Result.success(result);
    }
}

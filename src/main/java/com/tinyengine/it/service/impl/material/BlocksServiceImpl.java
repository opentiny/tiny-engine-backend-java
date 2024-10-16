package com.tinyengine.it.service.impl.material;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinyengine.it.config.Enums;
import com.tinyengine.it.config.SystemServiceLog;
import com.tinyengine.it.exception.ServiceException;
import com.tinyengine.it.mapper.BlockHistoriesMapper;
import com.tinyengine.it.mapper.BlocksCategoriesBlockCategoriesBlocksMapper;
import com.tinyengine.it.mapper.BlocksMapper;
import com.tinyengine.it.mapper.UsersPermissionsUserMapper;
import com.tinyengine.it.model.dto.BlockDto;
import com.tinyengine.it.model.entity.BlockHistories;
import com.tinyengine.it.model.entity.Blocks;
import com.tinyengine.it.model.entity.BlocksCategoriesBlockCategoriesBlocks;
import com.tinyengine.it.model.entity.UsersPermissionsUser;
import com.tinyengine.it.service.material.BlocksService;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Component
public class BlocksServiceImpl implements BlocksService {
    private static final Logger logger = LoggerFactory.getLogger(BlocksServiceImpl.class);

    @Autowired
    BlocksMapper blocksMapper;
    @Autowired
    UsersPermissionsUserMapper usersPermissionsUserMapper;
    @Autowired
    BlockHistoriesMapper blockHistoriesMapper;
    @Autowired
    BlocksCategoriesBlockCategoriesBlocksMapper blocksCategoriesBlockCategoriesBlocksMapper;


    /**
     * 根据主键id查询表blocks信息
     *
     * @param id
     */
    @Override
    public Blocks findBlocksById(@Param("id") Integer id) throws ServiceException {
        return blocksMapper.findBlocksById(id);
    }

    /**
     * 根据条件查询表blocks数据
     *
     * @param blocks
     */
    @Override
    public List<Blocks> findBlocksByCondition(Blocks blocks) throws ServiceException {
        return blocksMapper.findBlocksByCondition(blocks);
    }

    /**
     * 根据主键id删除表blocks数据
     *
     * @param id
     */
    @Override
    public Integer deleteBlocksById(@Param("id") Integer id) throws ServiceException {
        return blocksMapper.deleteBlocksById(id);
    }

    /**
     * 根据主键id更新表blocks数据
     *
     * @param blockDto
     */
    @SystemServiceLog(description = "区块修改实现类")
    @Override
    public BlockDto updateBlocksById(BlockDto blockDto) {
        // public 不是部分公开, 则public_scope_tenants为空数组
        // 把前端传参赋值给实体
        Blocks blocks = new Blocks();
        blocks.setId(blockDto.getId());
        blocks.setContent(blockDto.getContent());
        blocks.setDescription(blockDto.getDescription());
        blocks.setLabel(blockDto.getLabel());
        blocks.setNameCn(blockDto.getNameCn());
        blocks.setIsPublic(blockDto.getIsPublic());
        blocks.setPublic_scope_tenants(blockDto.getPublic_scope_tenants());
        blocks.setScreenshot(blockDto.getScreenshot());
        blocks.setTags(blockDto.getTags());
        blocks.setOccupier(86); //todo 获取用户信息
        // 处理区块截图
        if (!blockDto.getScreenshot().isEmpty() && !blockDto.getLabel().isEmpty()) {
            // 图片上传,此处给默认值空字符
            blocks.setScreenshot("");
        }

        blocksMapper.updateBlocksById(blocks);

        // 更新区块时去获取关联的分类id,进而去更新区块和分类关联表
        List<Integer> categories = blockDto.getCategories().stream()
                .filter(obj -> obj instanceof Integer) // 过滤出 Integer 类型的对象
                .map(obj -> (Integer) obj) // 转换为 Integer 类型
                .collect(Collectors.toList()); // 收集为 List<Integer>;
        if (categories.isEmpty()) {
            return blocksMapper.fingBlockAndLangAngGroupAndHistoByBlockId(blockDto.getId());
        }
        int categoriesId = categories.get(0);
        BlocksCategoriesBlockCategoriesBlocks blocksCategoriesBlockCategoriesBlocks = new BlocksCategoriesBlockCategoriesBlocks();
        blocksCategoriesBlockCategoriesBlocks.setBlockId(blockDto.getId());
        blocksCategoriesBlockCategoriesBlocks.setBlockCategoryId(categoriesId);
        blocksCategoriesBlockCategoriesBlocksMapper.updateBlocksCategoriesBlockCategoriesBlocksById(blocksCategoriesBlockCategoriesBlocks);
        return blocksMapper.fingBlockAndLangAngGroupAndHistoByBlockId(blockDto.getId());
    }

    /**
     * 新增表blocks数据
     *
     * @param blocks
     */
    @SystemServiceLog(description = "createBlocks 创建区块 实现类")
    @Override
    public Integer createBlocks(Blocks blocks) throws ServiceException {
        List<String> TINY_ACCOUNTS = Arrays.asList("p_webcenter");
        // 假设 user 是一个对象，其中包含 resetPasswordToken 属性  待获取
        String resetPasswordToken = "p_webcenter"; // 假设 resetPasswordToken 是一个字符串

        // 检查 resetPasswordToken 是否在 TINY_ACCOUNTS 中
        boolean isTokenInTinyAccounts = TINY_ACCOUNTS.contains(resetPasswordToken);
        // todo 先暂时TinyReserved的值给默认值为false
        blocks.setTinyReserved(false);
        return blocksMapper.createBlocks(blocks);
    }

    /**
     * 查找表中所有tags
     *
     * @return
     */
    @SystemServiceLog(description = "allTags 查找表中所有tags 实现类")
    @Override
    public List<String> allTags() {
        QueryWrapper<Blocks> queryWrapper = new QueryWrapper<>();
        ObjectMapper objectMapper = new ObjectMapper();
        queryWrapper.select("tags").isNotNull("tags");
        List<Blocks> allBlocksList = blocksMapper.selectList(queryWrapper);
        List<String> allTagsList = allBlocksList.stream()
                .flatMap(blocks -> blocks.getTags().stream())
                .collect(Collectors.toList());

        return allTagsList;
    }

    /**
     * @return
     */
    @SystemServiceLog(description = "listNew 区块列表 实现类")
    @Override
    public List<Blocks> listNew(int categoryId, int appId) {
        // 获取缓存里的user 先写死 待获取
        int createdBy = 86;
        List<Blocks> blocksList = new ArrayList<>();
        // 如果有 categoryId, 只查category下的blocks
        if (categoryId != 0) {
            blocksList = blocksMapper.findBlocksByBlockCategoriesId(categoryId);
            return blocksList;
        }
        // 如果没有 categoryId
        // 1. 查询和 app 相关的所有 category
        // 2. 组合 categories 下的所有 blocks
        // 3. 查询个人创建的 blocks
        // 4. 将个人的和 categories 下的 blocks 合并去重
        blocksList = blocksMapper.findBlocksByBlockCategoriesAppId(appId);
        List<Blocks> appBlocks = blocksList;
        // 通过createBy查询区块表数据
        Blocks blocks = new Blocks();
        blocks.setCreatedBy(createdBy);
        List<Blocks> personalBlocks = findBlocksByCondition(blocks);
        List<Blocks> retBlocks = new ArrayList<>();
        // 合并 personalBlocks 和 appBlocks 数组
        List<Blocks> combinedBlocks = Stream.concat(personalBlocks.stream(), appBlocks.stream())
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
        List<Blocks> result = retBlocks.stream()
                .map(b -> {
                    boolean isPublished = b.getLastBuildInfo() != null && b.getLastBuildInfo().get("result") instanceof Boolean
                            ? (Boolean) b.getLastBuildInfo().get("result") : Boolean.FALSE;
                    b.setIs_published(isPublished);
                    return b;
                })
                .collect(Collectors.toList());

        return result;
    }

    /**
     * @param request
     * @return
     */
    @SystemServiceLog(description = "findBlocksByConditionPagetion 根据条件查询区块分页 实现类")
    @Override
    public IPage<Blocks> findBlocksByConditionPagetion(Map<String, String> request) {
        String public_scope_mode = request.get("public_scope_mode");
        if (public_scope_mode != null) {
            request.remove("public_scope_mode");
        }

        // 获取查询条件
        String nameCn = request.get("name_cn");
        String description = request.get("description");

        QueryWrapper<Blocks> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(wrapper ->
                wrapper.like(StringUtils.isNotEmpty(nameCn), "name_cn", nameCn)
                        .or()
                        .like(StringUtils.isNotEmpty(description), "description", description)
        );
        List<Blocks> blocksList = blocksMapper.selectList(queryWrapper);
        Page<Blocks> page = new Page<>(1, blocksList.size());
        return blocksMapper.selectPage(page, queryWrapper);
    }

    /**
     * 生态中心区块列表分页查询
     *
     * @param request
     * @return
     */
    @SystemServiceLog(description = "findBlocksByPagetionList 生态中心区块列表分页查询 实现类")
    @Override
    public IPage<Blocks> findBlocksByPagetionList(Map<String, String> request) {
        // 获取查询条件
        String sort = request.get("sort"); // nodejs中页面传参"updated_at:DESC"
        String nameCn = request.get("name_cn");
        String description = request.get("description");
        String label = request.get("label");
        int limit = request.get("limit") != null ? Integer.parseInt(request.get("limit")) : 0;
        int start = request.get("start") != null ? Integer.parseInt(request.get("start")) : 0;
        // 把start、limit转为java分页的pageNum、pageSize
        int pageNum = start == 0 && limit == 0 ? 1 : (start / limit) + 1;
        int pageSize = limit == 0 ? 10 : limit;

        QueryWrapper<Blocks> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(nameCn)) {
            queryWrapper.like("name_cn", nameCn);
        }
        if (StringUtils.isNotEmpty(description)) {
            queryWrapper.or().like("description", description);
        }
        if (StringUtils.isNotEmpty(label)) {
            queryWrapper.or().eq("label", label);
        }

        // 获取是按升序还是降序排列
        if (sort != null) {
            String[] temp = sort.split(":");
            String sortOrder = temp[1];
            if ("ASC".equalsIgnoreCase(sortOrder)) {
                queryWrapper.orderByAsc("updated_at");
            } else if ("DESC".equalsIgnoreCase(sortOrder)) {
                queryWrapper.orderByDesc("updated_at");
            }
        }

        Page<Blocks> page = new Page<>(pageNum, pageSize);
        return blocksMapper.selectPage(page, queryWrapper);
    }

    /**
     * @param blocksList
     * @return
     */
    @SystemServiceLog(description = "getUsers 获取用户 实现类")
    @Override
    public List<UsersPermissionsUser> getUsers(List<Blocks> blocksList) {

        Set<String> userSet = new HashSet<>();

        // 提取 createdBy 列表中的唯一值
        blocksList.forEach(item -> {
            if (item.getCreatedBy() != null && !userSet.contains(item.getCreatedBy())) {
                userSet.add(String.valueOf(item.getCreatedBy()));
            }
        });

        List<String> userIdsList = new ArrayList<>(userSet);

        // 模拟执行 SQL 查询并返回结果
        List<UsersPermissionsUser> users = usersPermissionsUserMapper.selectUsersByIds(userIdsList);
        //  return (data && data[0]) || [];
        return users;
    }

    /**
     * 转换后的代码
     *
     * @param blocks 区块记录
     * @return
     */
    @SystemServiceLog(description = "translate 转换后的代码 实现类")
    @Override
    public List<Blocks> translate(Blocks blocks) throws Exception {
        if (blocks == null) {
            throw new Exception("block undefined, check block content format");
        }
        // 区块关联的历史记录
        BlockHistories blockHistories = blockHistoriesMapper.findBlockHistoriesById(blocks.getCurrentHistory());
        if (blockHistories.getContent().isEmpty()) {
            throw new Exception("unexpected history content");
        }
        // 获取嵌套的区块
        List<String> innerBlocksLabel = new ArrayList<>();
        List<Blocks> innerBlocks = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(blockHistories.getContent());
        traverseBlocks(json, innerBlocksLabel);
        if (!innerBlocksLabel.isEmpty()) {
            try {
                innerBlocks = blocksMapper.findBlocksByLabels(innerBlocksLabel);
            } catch (RuntimeException e) {
                logger.error("query blocks by label failed");
                throw new RuntimeException("Failed to process blocks: " + e.getMessage(), e);
            }

        }
        List<Blocks> blocksData = innerBlocks.stream()
                .map(block -> new Blocks(block.getContent(), block.getLabel()))
                .filter(blockData -> !blockData.getLabel().equals(blocks.getLabel()))
                .collect(Collectors.toList());

        String type = Enums.E_TYPES.valueOf(blocks.getFramework()).getValue();  // vue-tiny
        ObjectMapper mapper = new ObjectMapper();

        // 获取配置文件的属性值
        ClassLoader classLoader = BlocksServiceImpl.class.getClassLoader();
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream("src/main/resources/config.properties")) {
            properties.load(inputStream);

            // 获取单个属性值
            String dslPkgCore = properties.getProperty("config.dsl." + type + ".dslPkgCore");
            downloadNpmDepedencyAndRun(blockHistories.getContent(), blocks.getLabel(), blocksData, dslPkgCore);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @param map
     */
    @SystemServiceLog(description = "getNotInGroupBlocks 获取不在分组内的区块 实现类")
    @Override
    public List<BlockDto> getNotInGroupBlocks(Map<String, String> map) {
        // 获取缓存中的登录用户 TODO
        UsersPermissionsUser user = usersPermissionsUserMapper.findUsersPermissionsUserById(86);
        int groupId = Integer.parseInt(map.get("groupId"));
        if (map.get("tenant") != null) {
            map.remove("tenant");
        }
        int limit = Integer.parseInt(map.get("limit"));
        int start = 0;
        List<BlockDto> blocksList = blocksMapper.findBlocksReturnByBlockGroupId(groupId);
        return blocksList.stream()
                .filter(item -> {
                    // 过滤已发布的
                    if (item.getLastBuildInfo() == null || item.getContent() == null || item.getAssets() == null) {
                        return false;
                    }
                    // 组过滤
                    if (item.getGroups() != null && item.getGroups().stream().anyMatch(row -> row.getId() == groupId)) {
                        return false;
                    }
                    // 公开范围过滤
                    if (item.getIsPublic() == Enums.PUBLIC_SCOPE.FULL_PUBLIC.getValue()) {
                        return true;
                    }
                    if (user != null && item.getIsPublic() == Enums.PUBLIC_SCOPE.PUBLIC_IN_TENANTS.getValue()) {
                        return true;
                    }
                    return false;
                })
                .map(entity -> {
                    // Blocks block = (Blocks) Strapi.sanitizeEntity(entity, Strapi.models.block);
                    ObjectMapper objectMapper = new ObjectMapper();
                    Map<String, Object> mapTemp = objectMapper.convertValue(entity, Map.class);
                    if (mapTemp.get("content") != null) {
                        mapTemp.remove("content");
                    }
                    if (mapTemp.get("assets") != null) {
                        mapTemp.remove("assets");
                    }
                    Blocks blocks = new Blocks();
                    try {
                        BeanUtils.copyProperties(map, blocks);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    return entity;
                })
                .skip(start)
                .limit(limit)
                .collect(Collectors.toList());

    }


    public void traverseBlocks(String content, List<String> blocks) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(content);
        // 判断传过来的参数是JSON数组还是JSON对象
        if (content != null && jsonNode.isArray()) {
            List<String> schema = objectMapper.readValue(content, List.class);
            for (Object prop : (List<?>) schema) {
                traverseBlocks(objectMapper.writeValueAsString(prop), blocks);
            }
        } else {
            Map<String, Object> schema = objectMapper.readValue(content, Map.class);
            Map<?, ?> schemaMap = (Map<?, ?>) schema;
            if (isBlock(schemaMap) && !blocks.contains(schemaMap.get("componentName"))) {
                blocks.add((String) schemaMap.get("componentName"));
            }
            if (schemaMap.containsKey("children") && schemaMap.get("children") instanceof List) {
                traverseBlocks(objectMapper.writeValueAsString(schemaMap.get("children")), blocks);
            }
        }

    }

    public boolean isBlock(Map<?, ?> schema) {
        return schema != null && "Block".equals(schema.get("componentType"));
    }


    // java中下载npm依赖并在代码中导入执行nodejs代码

    public void downloadNpmDepedencyAndRun(Object content, String label, List<Blocks> blocksData, String dslPkgCore) throws Exception {
        // 1、下载npm依赖
        ProcessBuilder processBuilder = new ProcessBuilder("npm", "install", "@opentiny/tiny-engine-dsl-vue");
        // 设置工作目录
        processBuilder.directory(new File("D:/zjcIdeaWork/develop/tiny-engine-webservice-java"));
        Process process = processBuilder.start();
        int exitCode = process.waitFor();
        // 打印结果
        if (exitCode == 0) {
            System.out.println("npm package installed successfully.");
        } else {
            System.err.println("npm command failed with exit code: " + exitCode);
        }

        // 2. 执行 Node.js 代码
        // 创建 ProcessBuilder 对象
        ProcessBuilder processBuilderTemp = new ProcessBuilder("node", "-e", "const { generateCode } = require(" + dslPkgCore + ");\n" +
                "    const result = generateCode({ pageInfo: { schema: " + content + ", name: " + label + "}, " + blocksData + " });");

        // 设置工作目录
        processBuilderTemp.directory(new File("D:/zjcIdeaWork/develop/tiny-engine-webservice-java"));

        // 启动进程并等待其完成
        Process processTemp = processBuilderTemp.start();

        // 读取 Node.js 进程的输出
        BufferedReader reader = new BufferedReader(new InputStreamReader(processTemp.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        // 等待进程结束
        int exitCodeTemp = process.waitFor();
        System.out.println("Node.js process exited with code: " + exitCodeTemp);
    }

    public Map<String, List<String>> getBlockAssets(Map<String, Object> pageContent, String framework) throws Exception {
        List<String> blocks = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        traverseBlocks(objectMapper.writeValueAsString(pageContent), blocks);
        if (blocks.isEmpty()) {
            return Collections.emptyMap();
        }
        // 获取区块列表
        List<Blocks> blocksList = getBlockInfo(blocks, framework);
        // 对获取区块列表后的数据处理
        Map<String, List<String>> mergedAssets = new HashMap<>();
        mergedAssets.put("material", new ArrayList<>());
        mergedAssets.put("scripts", new ArrayList<>());
        mergedAssets.put("styles", new ArrayList<>());

        // Merge the assets using streams
        return blocksList.stream()
                .map(Blocks::getAssets)
                .map(assetsMap -> {
                    Map<String, List<String>> tempMap = new HashMap<>();
                    tempMap.put("material", (List<String>) assetsMap.getOrDefault("material", new ArrayList<>()));
                    tempMap.put("scripts", (List<String>) assetsMap.getOrDefault("scripts", new ArrayList<>()));
                    tempMap.put("styles", (List<String>) assetsMap.getOrDefault("styles", new ArrayList<>()));
                    return tempMap;
                })
                .reduce(
                        mergedAssets,
                        (acc, curr) -> {
                            acc.get("material").addAll(curr.get("material"));
                            acc.get("scripts").addAll(curr.get("scripts"));
                            acc.get("styles").addAll(curr.get("styles"));
                            return acc;
                        },
                        (map1, map2) -> {
                            map1.get("material").addAll(map2.get("material"));
                            map1.get("scripts").addAll(map2.get("scripts"));
                            map1.get("styles").addAll(map2.get("styles"));
                            return map1;
                        }
                );
    }

    public List<Blocks> getBlockInfo(List<String> blocks, String framework) {
        // 创建 QueryWrapper 实例
        QueryWrapper<Blocks> queryWrapper = new QueryWrapper<>();
        if (blocks != null && !blocks.isEmpty()) {

            // 处理 blockLabelName 为数组的情况
            String labelsCondition = blocks.stream()
                    .map(name -> "label = '" + name + "'")
                    .collect(Collectors.joining(" OR "));

            // 添加标签条件
            queryWrapper.and(wrapper -> wrapper.apply(labelsCondition));

            // 添加框架条件
            queryWrapper.eq("framework", framework);

        }

        // 执行查询并返回结果
        return blocksMapper.selectList(queryWrapper);
    }


    /**
     * 根据blockIds去获取区块信息
     *
     * @param blockIds
     * @return
     */
    public List<Blocks> getBlocks(List<Integer> blockIds) {
        QueryWrapper<Blocks> queryWrapperBlocks = new QueryWrapper<>();
        queryWrapperBlocks.in("id", blockIds);
        List<Blocks> blocksList = blocksMapper.selectList(queryWrapperBlocks);
        return blocksList.stream().map(item -> {
                    // 获取区块历史histories
                    List<BlockHistories> blockHistoriesList = blockHistoriesMapper.findBlockHistoriesByBlockId(item.getId());
                    // 计算 histories 的长度
                    long historiesLength = blockHistoriesList.stream()
                            .filter(history -> history.getVersion() != null && !history.getVersion().equals("N/A"))
                            .count();
                    item.setHistories_length((int) historiesLength);
                    return item;
                }

        ).collect(Collectors.toList());
    }

}

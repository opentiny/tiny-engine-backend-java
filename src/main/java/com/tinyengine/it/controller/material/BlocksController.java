package com.tinyengine.it.controller.material;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinyengine.it.config.Enums;
import com.tinyengine.it.config.SystemControllerLog;
import com.tinyengine.it.exception.ExceptionEnum;
import com.tinyengine.it.mapper.BlocksCategoriesBlockCategoriesBlocksMapper;
import com.tinyengine.it.mapper.BlocksMapper;
import com.tinyengine.it.mapper.TenantsMapper;
import com.tinyengine.it.model.dto.BlockBuildDto;
import com.tinyengine.it.model.dto.BlockDto;
import com.tinyengine.it.model.dto.Result;
import com.tinyengine.it.model.entity.*;
import com.tinyengine.it.service.material.BlockBuildService;
import com.tinyengine.it.service.material.BlocksService;
import com.tinyengine.it.service.material.TaskRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 区块api
 */
@Validated
@RestController
@CrossOrigin
@RequestMapping("/material-center/api")
public class BlocksController {
    private static final Logger logger = LoggerFactory.getLogger(BlocksController.class);

    @Autowired
    BlocksService blocksService;
    @Autowired
    TenantsMapper tenantsMapper;
    @Autowired
    BlocksMapper blocksMapper;
    @Autowired
    BlockBuildService blockBuildService;
    @Autowired
    TaskRecordService taskRecordService;
    @Autowired
    BlocksCategoriesBlockCategoriesBlocksMapper blocksCategoriesBlockCategoriesBlocksMapper;

    /**
     * 获取block列表信息
     *
     * @param
     * @return block列表信息
     */
    @Operation(summary = "获取区块列表信息",
            description = "获取区块列表信息",
            parameters = {
                    @Parameter(name = "request", description = "入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Blocks.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "获取区块列表api")
    @GetMapping("/block/list")
    public Result<List<Blocks>> getAllBlocks(@RequestParam Map<String, String> request) {
        String appId = request.get("appId");
        // 如果 appId 存在并且不匹配指定的正则表达式，则删除它
        if (appId != null && !Pattern.matches("^[1-9]+[0-9]*$", appId)) {
            request.remove("appId");
        }
        IPage<Blocks> blocksList = blocksService.findBlocksByPagetionList(request);
        List<Blocks> result = blocksList.getRecords();
        return Result.success(result);
    }

    /**
     * 获取区块列表满足查询条件下的条数
     *
     * @param nameCn
     * @param description
     * @return
     */
    @Operation(summary = "获取区块列表满足查询条件下的条数",
            description = "获取区块列表满足查询条件下的条数",
            parameters = {
                    @Parameter(name = "nameCn", description = "nameCn区块中文名称"),
                    @Parameter(name = "description", description = "区块描述")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema())),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "获取区块列表满足查询条件下的条数")
    @GetMapping("/block/count")
    public Result<Integer> getCountByCondition(@RequestParam(value = "name_cn", required = false) String nameCn,
                                               @RequestParam(value = "description", required = false) String description) {
        // 获取查询条件name_cn、description,若为空查的是全部数据，若不为空按条件查询
        List<Blocks> blocksList = (nameCn == null && description == null)
                ? blocksMapper.findBlocksByNameCnAndDes(null, null)
                : blocksMapper.findBlocksByNameCnAndDes(nameCn, description);
        return Result.success(blocksList.size());
    }

    /**
     * 根据id查询表block信息
     *
     * @param id
     * @return block信息
     */
    @Operation(summary = "查询区块详情",
            description = "根据id查询表block信息并返回",
            parameters = {
                    @Parameter(name = "id", description = "区块Id")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回区块信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Blocks.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "获取区块详情api")
    @GetMapping("/block/detail/{id}")
    public Result<BlockDto> getBlocksById(@PathVariable Integer id) {
        BlockDto blocks = blocksMapper.fingBlockAndLangAngGroupAndHistoByBlockId(id);
        return Result.success(blocks);
    }

    /**
     * 创建block
     *
     * @param map
     * @return
     * @throws Exception
     */
    @Operation(summary = "创建block",
            description = "创建block",
            parameters = {
                    @Parameter(name = "map", description = "入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Blocks.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "区块创建api")
    @PostMapping("/block/create")
    public Result<BlockDto> createBlocks(@Valid @RequestBody Map<String, Object> map) throws Exception {
        // 对接收到的参数occupier为对应的一个对象，进行特殊处理并重新赋值
        ObjectMapper objectMapper = new ObjectMapper();
        if (!(map.get("occupier") instanceof Map && ((Map<?, ?>) map.get("occupier")).isEmpty())) {
            int occupier = (int) objectMapper.convertValue(map.get("occupier"), Map.class).get("id");
            map.put("occupier", occupier);
        } else {
            map.put("occupier", null);
        }
        Map<String, Object> mapTemp = new HashMap<>();
        mapTemp.putAll(map);
        // 判断创建区块时是否关联了分类
        if (mapTemp.containsKey("categories")) {
            mapTemp.remove("categories");
        }
        // 把map里的值对应赋值到实体类中
        Blocks blocks = objectMapper.convertValue(mapTemp, Blocks.class);
        blocks.setIsPublic((Integer) map.get("public"));
        blocks.setIsDefault(false);
        blocks.setIsOfficial(false);
        blocks.setNameCn((String) map.get("name_cn"));
        blocks.setCreatedApp(Integer.parseInt((String) map.get("created_app")));
        // 在nodejs中不是数据库的属性也可以保存成功，会自动过滤，public_scope_tenants在java中不是数据库属性无需处理
        blocks.setTags((List<String>) map.get("tags"));
        blocksService.createBlocks(blocks);
        int id = blocks.getId();
        // 判断创建区块时是否关联了分类,如果有，插入区块分类关联表数据
        if (map.containsKey("categories")) {
            Object categoriesObj = map.get("categories");
            if (categoriesObj instanceof List) {
                List<Integer> categories = (List<Integer>) categoriesObj;
                int cateId = categories.get(0); // 获取区块关联的分类id
                BlocksCategoriesBlockCategoriesBlocks blocksCategoriesBlockCategoriesBlocks = new BlocksCategoriesBlockCategoriesBlocks();
                blocksCategoriesBlockCategoriesBlocks.setBlockCategoryId(cateId);
                blocksCategoriesBlockCategoriesBlocks.setBlockId(id);
                blocksCategoriesBlockCategoriesBlocksMapper.createBlocksCategoriesBlockCategoriesBlocks(blocksCategoriesBlockCategoriesBlocks);
            }
        }
        BlockDto blocksResult = blocksMapper.fingBlockAndLangAngGroupAndHistoByBlockId(id);
        return Result.success(blocksResult);
    }


    /**
     * 删除blocks信息
     *
     * @param id
     * @return blocks信息
     */
    @Operation(summary = "删除blocks信息",
            description = "删除blocks信息",
            parameters = {
                    @Parameter(name = "id", description = "区块id")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Blocks.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "删除blocks信息")
    @GetMapping("/block/delete/{id}")
    public Result<BlockDto> deleteBlocks(@PathVariable Integer id) {
        // 页面返回数据
        BlockDto result = blocksMapper.fingBlockAndLangAngGroupAndHistoByBlockId(id);
        blocksService.deleteBlocksById(id);
        return Result.success(result);
    }

    /**
     * 生态中心区块列表分页查询
     *
     * @param request
     * @return
     */
    @Operation(summary = "生态中心区块列表分页查询",
            description = "生态中心区块列表分页查询",
            parameters = {
                    @Parameter(name = "request", description = "入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema())),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "生态中心区块列表分页查询api")
    @GetMapping("/block")
    public Result<List<BlockDto>> find(@RequestParam Map<String, String> request) {

        IPage<Blocks> blocksIPage = blocksService.findBlocksByPagetionList(request);
        List<Blocks> blocksList = blocksIPage.getRecords();
        List<BlockDto> result = new ArrayList<>();
        for (Blocks blocks : blocksList) {
            List<BlockDto> blockDto = blocksMapper.fingBlockAngCateAndHistorByBlockId(blocks.getId());
            result.addAll(blockDto);
        }
        return Result.success(result);
    }

    /**
     * 查找表中所有tags
     *
     * @return
     */
    @Operation(summary = "查找表中所有tags",
            description = "查找表中所有tags",
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema())),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "查找表中所有tags")
    @GetMapping("/block/tags")
    public Result<List<String>> allTags() {
        List<String> allTagsList = blocksService.allTags();
        return Result.success(allTagsList);
    }

    @Operation(summary = "查找不在分组内的区块",
            description = "查找不在分组内的区块",
            parameters = {
                    @Parameter(name = "groupId", description = "分组id"),
                    @Parameter(name = "map", description = "入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Blocks.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "查找不在分组内的区块api")
    @GetMapping("/block/notgroup/{groupId}")
    public Result<List<BlockDto>> findBlocksNotInGroup(@PathVariable Integer groupId, @RequestParam Map<String, String> map) {
        map.put("groupId", String.valueOf(groupId));

        List<BlockDto> blocksList = blocksService.getNotInGroupBlocks(map);
        return Result.success(blocksList);
    }

    @Operation(summary = "获取区块列表list2",
            description = "获取区块列表list2",
            parameters = {
                    @Parameter(name = "request", description = "入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema())),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "获取区块列表api")
    @GetMapping("/block/list2")
    public Result<IPage<Blocks>> getBlocks(@RequestBody Map<String, String> request) {

        IPage<Blocks> BlocksList = blocksService.findBlocksByConditionPagetion(request);
        return Result.success(BlocksList);
    }


    /**
     * 获取所有租户
     *
     * @return
     */
    @Operation(summary = "获取所有租户",
            description = "获取所有租户",
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Tenants.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "获取所有租户api")
    @GetMapping("/block/tenants")
    public Result<List<Tenants>> allTenant() {
        List<Tenants> tenantsList = tenantsMapper.findAllTenants();
        return Result.success(tenantsList);
    }


    /**
     * 获取所有用户
     *
     * @return
     */
    @Operation(summary = "获取所有用户",
            description = "获取所有用户",
            parameters = {
                    @Parameter(name = "blocks", description = "入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UsersPermissionsUser.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "获取所有用户api")
    @GetMapping("/block/users")
    public Result<List<UsersPermissionsUser>> allAuthor() {

        List<Blocks> blocksList = blocksMapper.findAllBlocks();
        List<UsersPermissionsUser> userList = blocksService.getUsers(blocksList);
        return Result.success(userList);
    }


    /**
     * 获取源代码
     *
     * @param id
     * @throws Exception
     */
    @Operation(summary = "获取区块源代码",
            description = "获取区块源代码",
            parameters = {
                    @Parameter(name = "id", description = "区块id")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema())),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "获取区块源代码api")
    @GetMapping("/block/code/{id}")
    public void getSourceCode(@PathVariable Integer id) throws Exception {
        Blocks blocks = blocksService.findBlocksById(id);
        if (blocks.getCurrentHistory() == null) {
            logger.error("no related history");
            return;
        }

        // 调用DSL转换方法生成代码
        blocksService.translate(blocks);

    }

    /**
     * 修改block
     *
     * @param blockDto
     * @return blcok信息
     */
    @Operation(summary = "修改区块",
            description = "修改区块",
            parameters = {
                    @Parameter(name = "blockDto", description = "入参对象"),
                    @Parameter(name = "id", description = "区块id"),
                    @Parameter(name = "appId", description = "appId")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Blocks.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "区块修改api")
    @PostMapping("/block/update/{id}")
    public Result<BlockDto> updateBlocks(@Valid @RequestBody BlockDto blockDto, @PathVariable Integer id, @RequestParam Integer appId) {
        blockDto.setId(id);
        BlockDto blocksResult = blocksService.updateBlocksById(blockDto);
        return Result.success(blocksResult);
    }

    /**
     * block发布
     *
     * @param blockBuildDto
     * @return blcok信息
     */
    @Operation(summary = "区块发布",
            description = "区块发布",
            parameters = {
                    @Parameter(name = "blockBuildDto", description = "入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TaskRecord.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "区块发布api")
    @PostMapping("/block/deploy")
    public Result<TaskRecord> build(@Valid @RequestBody BlockBuildDto blockBuildDto) {
        Map<String, Object> content = blockBuildDto.getBlocks().getContent();
        if (content.isEmpty()) {
            return Result.failed(ExceptionEnum.CM204);
        }
        // 区块不存在的情况下先创建新区块
        int id = blockBuildService.ensureBlockId(blockBuildDto.getBlocks());
        // 对版本号是否存在进行校验
        List<BlockHistories> hasHistory = blockBuildService.isHistoryExisted(id, blockBuildDto.getVersion());
        if (!hasHistory.isEmpty()) {
            return Result.failed(ExceptionEnum.CM205);
        }
        Blocks blocks = blockBuildDto.getBlocks();
        // 如果有对象存储服务此次逻辑需处理
        blocks.setScreenshot("");
        // 如果有未完成的任务直接返回该任务信息
        TaskRecord task = taskRecordService.getUnfinishedTask(Enums.E_TaskType.BLOCK_BUILD.getValue(), id);
        if (task.getId() != null) {
            return Result.success(task);
        }
        TaskRecord createTask = new TaskRecord();
        createTask.setTaskTypeId(Enums.E_TaskType.BLOCK_BUILD.getValue());
        createTask.setUniqueId(id);
        createTask.setTaskStatus(Enums.E_TaskStatus.INIT.getValue());

        Integer createInfo = taskRecordService.createTaskRecord(createTask);
        TaskRecord taskInfo = taskRecordService.findTaskRecordById(createTask.getId());
        // 执行构建

        blockBuildService.start(id, taskInfo.getId(), blockBuildDto);

        return Result.success(taskInfo);
    }

}

package com.tinyengine.it.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.config.log.SystemControllerLog;
import com.tinyengine.it.mapper.AppMapper;
import com.tinyengine.it.mapper.BlockMapper;
import com.tinyengine.it.mapper.TenantMapper;
import com.tinyengine.it.model.dto.BlockDto;
import com.tinyengine.it.model.entity.*;
import com.tinyengine.it.service.app.AppService;
import com.tinyengine.it.service.material.BlockService;
import com.tinyengine.it.service.material.TaskRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
 * <p>
 * 区块
 * </p>
 *
 * @author zhangjuncao
 * @since 2024-10-30
 */
@Validated
@RestController
@CrossOrigin
@RequestMapping("/material-center/api")
public class BlockController {

    @Autowired
    BlockService blockService;
    @Autowired
    TenantMapper tenantMapper;
    @Autowired
    BlockMapper blockMapper;
    @Autowired
    TaskRecordService taskRecordService;
    @Autowired
    private AppMapper appMapper;

    /**
     * 获取block列表信息
     *
     * @param request request
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
                                    schema = @Schema(implementation = Block.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "获取区块列表api")
    @GetMapping("/block/list")
    public Result<List<Block>> getAllBlocks(@RequestParam Map<String, String> request) {
        String appId = request.get("appId");
        // 如果 appId 存在并且不匹配指定的正则表达式，则删除它
        if (appId != null && !Pattern.matches("^[1-9]+[0-9]*$", appId)) {
            request.remove("appId");
        }
        IPage<Block> blocksList = blockService.findBlocksByPagetionList(request);
        List<Block> result = blocksList.getRecords();
        return Result.success(result);
    }

    /**
     * 获取区块列表满足查询条件下的条数
     *
     * @param nameCn      name
     * @param description description
     * @return the integer
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
        List<Block> blocksList = (nameCn == null && description == null)
                ? blockMapper.findBlocksByNameCnAndDes(null, null)
                : blockMapper.findBlocksByNameCnAndDes(nameCn, description);
        return Result.success(blocksList.size());
    }

    /**
     * 根据id查询表block信息
     *
     * @param id id
     * @return BlockDto
     */
    @Operation(summary = "查询区块详情",
            description = "根据id查询表t_block信息并返回",
            parameters = {
                    @Parameter(name = "id", description = "区块Id")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回区块信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Block.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "获取区块详情api")
    @GetMapping("/block/detail/{id}")
    public Result<BlockDto> getBlocksById(@PathVariable Integer id) {
        BlockDto blocks = blockMapper.findBlockAndGroupAndHistoByBlockId(id);
        return Result.success(blocks);
    }

    /**
     * 创建block
     *
     * @param map map
     * @return BlockDto
     * @throws Exception exception
     */
    @Operation(summary = "创建block",
            description = "创建block",
            parameters = {
                    @Parameter(name = "map", description = "入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Block.class))),
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
        // 判断创建区块时是否关联了分组
        if (mapTemp.containsKey("groups")) {
            mapTemp.remove("groups");
        }
        // 把map里的值对应赋值到实体类中
        Block blocks = objectMapper.convertValue(mapTemp, Block.class);
        blocks.setPublicStatus((Integer) map.get("public"));
        blocks.setIsDefault(false);
        blocks.setIsOfficial(false);
        blocks.setName((String) map.get("name_cn"));
        blocks.setAppId(Integer.parseInt((String) map.get("created_app")));
        blocks.setPlatformId(1); //新建区块给默认值
        // 在nodejs中不是数据库的属性也可以保存成功，会自动过滤，public_scope_tenants在java中不是数据库属性无需处理
        blocks.setTags((List<String>) map.get("tags"));

        // 判断创建区块时是否关联了分组,如果有，插入区块分类关联表数据
        // todo 待前端设计更改成分组，区块表里直接由分组字段，只需把分组字段做更新赋值
        if (map.containsKey("groups")) {
            Object groupsObj = map.get("groups");
            if (groupsObj instanceof List) {
                List<Integer> groups = (List<Integer>) groupsObj;
                int groupId = groups.get(0); // 获取区块关联的分组id
                blocks.setBlockGroupId(groupId);
            }
        }
        blockService.createBlock(blocks);
        int id = blocks.getId();
        BlockDto blocksResult = blockMapper.findBlockAndGroupAndHistoByBlockId(id);
        return Result.success(blocksResult);
    }


    /**
     * 删除blocks信息
     *
     * @param id id
     * @return BlockDto
     */
    @Operation(summary = "删除blocks信息",
            description = "删除blocks信息",
            parameters = {
                    @Parameter(name = "id", description = "区块id")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Block.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "删除blocks信息")
    @GetMapping("/block/delete/{id}")
    public Result<BlockDto> deleteBlocks(@PathVariable Integer id) {
        // 页面返回数据
        BlockDto result = blockMapper.findBlockAndGroupAndHistoByBlockId(id);
        blockService.deleteBlockById(id);
        return Result.success(result);
    }

    /**
     * 生态中心区块列表分页查询
     *
     * @param request request
     * @return BlockDto
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

        IPage<Block> blocksIPage = blockService.findBlocksByPagetionList(request);
        List<Block> blocksList = blocksIPage.getRecords();
        List<BlockDto> result = new ArrayList<>();
        for (Block blocks : blocksList) {
            List<BlockDto> blockDto = blockMapper.findBlockAndHistorByBlockId(blocks.getId());
            result.addAll(blockDto);
        }
        return Result.success(result);
    }

    /**
     * 查找表中所有tags
     *
     * @return the list
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
        List<String> allTagsList = blockService.allTags();
        return Result.success(allTagsList);
    }


    /**
     * 获取区块列表list2
     *
     * @param request request
     * @return the ipage
     */
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
    public Result<IPage<Block>> getBlocks(@RequestBody Map<String, String> request) {

        IPage<Block> BlocksList = blockService.findBlocksByConditionPagetion(request);
        return Result.success(BlocksList);
    }


    /**
     * 获取所有租户
     *
     * @return the list
     */
    @Operation(summary = "获取所有租户",
            description = "获取所有租户",
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Tenant.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "获取所有租户api")
    @GetMapping("/block/tenants")
    public Result<List<Tenant>> allTenant() {
        List<Tenant> tenantsList = tenantMapper.queryAllTenant();
        return Result.success(tenantsList);
    }


    /**
     * 获取所有用户
     *
     * @return the list
     */
    @Operation(summary = "获取所有用户",
            description = "获取所有用户",
            parameters = {
                    @Parameter(name = "blocks", description = "入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "获取所有用户api")
    @GetMapping("/block/users")
    public Result<List<User>> allAuthor() {

        List<Block> blocksList = blockMapper.queryAllBlock();
        List<User> userList = blockService.getUsers(blocksList);
        return Result.success(userList);
    }

    /**
     * 获取区块列表
     *
     * @param map map
     * @return the list
     */
    @Operation(summary = "获取区块列表",
            description = "获取区块列表",
            parameters = {
                    @Parameter(name = "map", description = "入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Block.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "获取区块列表")
    @GetMapping("/blocks")
    public Result<List<Block>> getAllBlockCategories(@Valid @RequestParam Map<String, String> map) {
        int groupId = 0;
        int appId = 0;
        if (map.get("groupId") != null) {
            groupId = Integer.parseInt(map.get("groupId"));
        }
        if (map.get("appId") != null) {
            appId = Integer.parseInt(map.get("appId"));
        }
        App apps = appMapper.queryAppById(appId);
        if (groupId != 0) {
            if (!apps.getId().equals(appId)) {
                return Result.failed(ExceptionEnum.CM206);
            }
        }
        // listNew
        List<Block> blocksList = blockService.listNew(groupId, appId);
        return Result.success(blocksList);
    }


    /**
     * 修改block
     *
     * @param blockDto blockDto
     * @return block dto
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
                                    schema = @Schema(implementation = Block.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "区块修改api")
    @PostMapping("/block/update/{id}")
    public Result<BlockDto> updateBlocks(@Valid @RequestBody BlockDto blockDto, @PathVariable Integer id, @RequestParam Integer appId) {
        blockDto.setId(id);
        BlockDto blocksResult = blockService.updateBlockById(blockDto);
        return Result.success(blocksResult);
    }


}

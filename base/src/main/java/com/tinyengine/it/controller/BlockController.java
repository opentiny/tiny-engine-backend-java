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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.common.log.SystemControllerLog;
import com.tinyengine.it.mapper.BlockMapper;
import com.tinyengine.it.mapper.TenantMapper;
import com.tinyengine.it.model.dto.BlockBuildDto;
import com.tinyengine.it.model.dto.BlockDto;
import com.tinyengine.it.model.dto.BlockParamDto;
import com.tinyengine.it.model.dto.NotGroupDto;
import com.tinyengine.it.model.entity.Block;
import com.tinyengine.it.model.entity.Tenant;
import com.tinyengine.it.model.entity.User;
import com.tinyengine.it.service.material.BlockService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

/**
 * 区块
 *
 * @author zhangjuncao
 * @since 2024-10-30
 */
@Validated
@RestController
@RequestMapping("/material-center/api")
@Tag(name = "区块")
public class BlockController {
    @Autowired
    private BlockService blockService;
    @Autowired
    private TenantMapper tenantMapper;
    @Autowired
    private BlockMapper blockMapper;

    /**
     * 获取block列表信息
     *
     * @param blockParamDto blockParamDto
     * @return block列表信息
     */
    @Operation(summary = "获取区块列表信息",
            description = "获取区块列表信息",
            parameters = {
                    @Parameter(name = "blockParamDto", description = "入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Block.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "获取区块列表api")
    @GetMapping("/block/list")
    public Result<List<Block>> getAllBlocks(@ModelAttribute BlockParamDto blockParamDto) {
        IPage<Block> blocksList = blockService.findBlocksByPagetionList(blockParamDto);
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
    public Result<Integer> getCountByCondition(
            @RequestParam(value = "name_cn", required = false) String nameCn,
            @RequestParam(value = "description", required = false)
            String description) {
        // 获取查询条件name_cn和description,若为空查的是全部数据,若不为空按条件查询
        List<Block> blocksList = blockMapper.findBlocksByNameCnAndDes(nameCn, description);
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
        BlockDto block = blockService.queryBlockById(id);
        return Result.success(block);
    }

    /**
     * 创建block
     *
     * @param blockDto the block dto
     * @return BlockDto
     */
    @Operation(summary = "创建block",
            description = "创建block",
            parameters = {
                    @Parameter(name = "blockDto", description = "入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Block.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "区块创建api")
    @PostMapping("/block/create")
    public Result<BlockDto> createBlocks(@Valid @RequestBody BlockDto blockDto) {
        return blockService.createBlock(blockDto);
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
    public Result<BlockDto> deleteBlock(@PathVariable Integer id) {
        // 页面返回数据
        BlockDto result = blockService.queryBlockById(id);
        blockService.deleteBlockById(id);
        return Result.success(result);
    }

    /**
     * 生态中心区块列表分页查询
     *
     * @param blockParamDto blockParamDto
     * @return BlockDto
     */
    @Operation(summary = "生态中心区块列表分页查询",
            description = "生态中心区块列表分页查询",
            parameters = {
                    @Parameter(name = "blockParamDto", description = "入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema())),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "生态中心区块列表分页查询api")
    @GetMapping("/block")
    public Result<List<BlockDto>> find(@ModelAttribute BlockParamDto blockParamDto) {
        IPage<Block> blocksIPage = blockService.findBlocksByPagetionList(blockParamDto);
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
     * 查找不在分组内的区块
     *
     * @param groupId the groupId
     * @return the list
     */
    @Operation(summary = "查找不在分组内的区块",
            description = "查找不在分组内的区块",
            parameters = {
                    @Parameter(name = "groupId", description = "分组id")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Block.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "查找不在分组内的区块api")
    @GetMapping("/block/notgroup/{groupId}")
    public Result<List<BlockDto>> findBlocksNotInGroup(@PathVariable Integer groupId,
                                                       @RequestParam(value = "label_contains", required = false) String label,
                                                       @RequestParam(value = "tags_contains", required = false) String [] tags,
                                                       @RequestParam(value = "createdBy", required = false) String createdBy) {
        NotGroupDto notGroupDto = new NotGroupDto();
        notGroupDto.setGroupId(groupId);
        notGroupDto.setLabel(label);
        notGroupDto.setCreatedBy(createdBy);
        notGroupDto.setTags(null);

        List<BlockDto> blocksList = blockService.getNotInGroupBlocks(notGroupDto);
        return Result.success(blocksList);
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
        IPage<Block> blocksList = blockService.findBlocksByConditionPagetion(request);
        return Result.success(blocksList);
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
     * @param appId   appId
     * @param groupId groupId
     * @return the list
     */
    @Operation(summary = "获取区块列表",
            description = "获取区块列表",
            parameters = {
                    @Parameter(name = "appId", description = "app主键id"),
                    @Parameter(name = "groupId", description = "区块分组id")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Block.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "获取区块列表")
    @GetMapping("/blocks")
    public Result<List<Block>> getBlockGroups(
            @Valid @RequestParam(required = false) String appId,
            @RequestParam(required = false) String groupId) {
        return blockService.listNew(appId, groupId);
    }


    /**
     * 修改block
     *
     * @param blockDto blockDto
     * @param id       id
     * @return block dto
     */
    @Operation(summary = "修改区块",
            description = "修改区块",
            parameters = {
                    @Parameter(name = "blockDto", description = "入参对象"),
                    @Parameter(name = "id", description = "区块id")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Block.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "区块修改api")
    @PostMapping("/block/update/{id}")
    public Result<BlockDto> updateBlocks(@Valid @RequestBody BlockDto blockDto, @PathVariable Integer id) {
        blockDto.setId(id);
        int result = blockService.updateBlockById(blockDto);
        if (result < 1) {
            return Result.failed(ExceptionEnum.CM001);
        }
        BlockDto blocksResult = blockService.queryBlockById(blockDto.getId());
        return Result.success(blocksResult);
    }

    /**
     * 通过lable和appId查询区块
     *
     * @param label label
     * @param appId appId
     * @return the result
     */
    @Operation(summary = "根据lable和appId查询区块详情",
            description = "根据lable和appId查询区块详情",
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Block.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "根据lable和appId查询区块详情api")
    @GetMapping("/block/label")
    public Result<BlockDto> getBlockByLabel(
            @RequestParam(value = "label") String label,
            @RequestParam(value = "appId") Integer appId) {
        return blockService.getBlockByLabel(label, appId);
    }

    /**
     * block发布
     *
     * @param blockBuildDto block
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
                                    schema = @Schema(implementation = Block.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "区块发布api")
    @PostMapping("/block/deploy")
    public Result<BlockDto> build(@Valid @RequestBody BlockBuildDto blockBuildDto) {
        return blockService.deploy(blockBuildDto);
    }
}

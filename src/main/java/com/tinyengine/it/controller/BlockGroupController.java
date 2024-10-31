package com.tinyengine.it.controller;


import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.config.log.SystemControllerLog;
import com.tinyengine.it.mapper.BlockGroupMapper;
import com.tinyengine.it.model.dto.BlockGroupDto;
import com.tinyengine.it.model.entity.BlockGroup;
import com.tinyengine.it.service.material.BlockGroupService;
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
import java.util.List;

/**
 * <p>
 * 区块分组
 * </p>
 *
 * @author zhangjuncao
 * @since 2024-10-30
 */
@Validated
@RestController
@RequestMapping("/material-center/api")
public class BlockGroupController {
    @Autowired
    BlockGroupService blockGroupService;
    @Autowired
    BlockGroupMapper blockGroupMapper;

    /**
     * 获取区块分组
     *
     * @param ids   ids
     * @param appId appid
     * @return the list
     */
    @Operation(summary = "获取区块分组",
            description = "获取区块分组",
            parameters = {
                    @Parameter(name = "ids", description = "分组ids"),
                    @Parameter(name = "appId", description = "appId")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema())),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "获取区块分组")
    @GetMapping("/block-groups")
    public Result<List<BlockGroupDto>> getAllBlockGroups(@RequestParam(value = "id", required = false) List<Integer> ids, @RequestParam(value = "app", required = false) Integer appId) {
        // 此接收到的两个参数不一定同时存在
        BlockGroup blockGroups = new BlockGroup();
        List<BlockGroupDto> blockGroupsListResult = new ArrayList<>();
        List<BlockGroupDto> blockGroupsListTemp = new ArrayList<>();
        if (ids != null) {
            for (Integer id : ids) {
                blockGroups.setId(id);
                blockGroupsListTemp = blockGroupMapper.getBlockGroupsById(id);
                blockGroupsListResult.addAll(blockGroupsListTemp);
            }

        }
        if (appId != null) {
            blockGroupsListResult = blockGroupMapper.findGroupByAppId(appId);
        }

        return Result.success(blockGroupsListResult);
    }


    /**
     * 创建区块分组
     *
     * @param blockGroups blockGroups
     * @return the list
     */
    @Operation(summary = "创建区块分组",
            description = "创建区块分组",
            parameters = {
                    @Parameter(name = "blockGroups", description = "入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BlockGroup.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "创建区块分组")
    @PostMapping("/block-groups/create")
    public Result<List<BlockGroupDto>> createBlockGroups(@Valid @RequestBody BlockGroup blockGroups) {
        List<BlockGroupDto> blockGroupsList = blockGroupService.findBlockGroupByCondition(blockGroups);
        if (blockGroupsList.isEmpty()) {
            blockGroupService.createBlockGroup(blockGroups);
        } else {
            return Result.failed(ExceptionEnum.CM003);
        }

        // 页面返回数据显示
        List<BlockGroupDto> blockGroupsListResult = blockGroupMapper.getBlockGroupsById(blockGroups.getId());
        return Result.success(blockGroupsListResult);
    }

    /**
     * 修改区块分组
     *
     * @param id          id
     * @param blockGroups blockGroups
     * @return the list
     */
    @Operation(summary = "修改区块分组",
            description = "修改区块分组",
            parameters = {
                    @Parameter(name = "id", description = "分组id"),
                    @Parameter(name = "blockGroups", description = "入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BlockGroup.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "修改区块分组")
    @PostMapping("/block-groups/update/{id}")
    public Result<List<BlockGroupDto>> updateBlockGroups(@Valid @PathVariable Integer id, @RequestBody BlockGroup blockGroups) {
        blockGroups.setId(id);
        blockGroupService.updateBlockGroupById(blockGroups);
        // 页面返回数据显示
        List<BlockGroupDto> blockGroupsListResult = blockGroupMapper.getBlockGroupsById(blockGroups.getId());
        return Result.success(blockGroupsListResult);
    }

    /**
     * 根据id删除区块分组
     *
     * @param id id
     * @return the list
     * @throws ServiceException serviceException
     */
    @Operation(summary = "根据id删除区块分组",
            description = "根据id删除区块分组",
            parameters = {
                    @Parameter(name = "id", description = "分组id")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BlockGroup.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "根据id删除区块分组")
    @GetMapping("/block-groups/delete/{id}")
    public Result<List<BlockGroupDto>> deleteBlockGroups(@PathVariable Integer id) throws ServiceException {
        BlockGroupDto blockGroups = blockGroupService.findBlockGroupById(id);
        if (blockGroups == null) {
            return Result.failed(ExceptionEnum.CM009);
        }

        // 页面返回数据显示
        List<BlockGroupDto> blockGroupsListResult = blockGroupMapper.getBlockGroupsById(blockGroups.getId());
        blockGroupService.deleteBlockGroupById(id);
        return Result.success(blockGroupsListResult);
    }
}

package com.tinyengine.it.controller;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.common.log.SystemControllerLog;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.validation.Valid;

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
@CrossOrigin
@RequestMapping("/material-center/api")
public class BlockGroupController {
    @Autowired
    private BlockGroupService blockGroupService;
    @Autowired
    private BlockGroupMapper blockGroupMapper;

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
    public Result<List<BlockGroupDto>> getAllBlockGroups(
            @RequestParam(value = "id", required = false) List<Integer> ids,
            @RequestParam(value = "app", required = false) Integer appId) {
        List<BlockGroupDto> blockGroupsListResult = blockGroupService.getBlockGroupByIdsOrAppId(ids, appId);
        return Result.success(blockGroupsListResult);
    }


    /**
     * 创建区块分组
     *
     * @param blockGroup blockGroup
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
    public Result<List<BlockGroupDto>> createBlockGroups(@Valid @RequestBody BlockGroup blockGroup) {
        blockGroup.setPlatformId(1);
        return blockGroupService.createBlockGroup(blockGroup);
    }

    /**
     * 修改区块分组
     *
     * @param id         id
     * @param blockGroup blockGroup
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
    public Result<List<BlockGroupDto>> updateBlockGroups(@Valid @PathVariable Integer id,
                                                         @RequestBody BlockGroup blockGroup) {
        blockGroup.setId(id);
        blockGroupService.updateBlockGroupById(blockGroup);
        // 页面返回数据显示
        List<BlockGroupDto> blockGroupsListResult = blockGroupMapper.getBlockGroupsById(blockGroup.getId());
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
        BlockGroup blockGroups = blockGroupService.findBlockGroupById(id);
        if (blockGroups == null) {
            return Result.failed(ExceptionEnum.CM009);
        }
        // 页面返回数据显示
        List<BlockGroupDto> blockGroupsListResult = blockGroupMapper.getBlockGroupsById(blockGroups.getId());
        blockGroupService.deleteBlockGroupById(id);
        return Result.success(blockGroupsListResult);
    }
}

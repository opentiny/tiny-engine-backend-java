package com.tinyengine.it.controller.material;


import com.tinyengine.it.config.SystemControllerLog;
import com.tinyengine.it.exception.ExceptionEnum;
import com.tinyengine.it.exception.ServiceException;
import com.tinyengine.it.mapper.BlockGroupsMapper;
import com.tinyengine.it.model.dto.BlockGroupsDto;
import com.tinyengine.it.model.dto.Result;
import com.tinyengine.it.model.entity.BlockGroups;
import com.tinyengine.it.service.material.BlockGroupsService;
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
 * 前端控制器
 * </p>
 *
 * @author zhangjuncao
 * @since 2024-04-18
 */
@Validated
@RestController
@RequestMapping("/material-center/api")
public class BlockGroupsController {
    @Autowired
    BlockGroupsService blockGroupsService;
    @Autowired
    BlockGroupsMapper blockGroupsMapper;

    /**
     * 获取区块分组
     *
     * @param ids
     * @param appId
     * @return
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
    public Result<List<BlockGroupsDto>> getAllBlockGroups(@RequestParam(value = "id", required = false) List<Integer> ids, @RequestParam(value = "app", required = false) Integer appId) {
        // 此接收到的两个参数不一定同时存在
        BlockGroups blockGroups = new BlockGroups();
        List<BlockGroupsDto> blockGroupsListResult = new ArrayList<>();
        List<BlockGroupsDto> blockGroupsListTemp = new ArrayList<>();
        if (ids != null) {
            for (Integer id : ids) {
                blockGroups.setId(id);
                blockGroupsListTemp = blockGroupsMapper.getBlockGroupsById(id);
                blockGroupsListResult.addAll(blockGroupsListTemp);
            }

        }
        if (appId != null) {
            blockGroupsListResult = blockGroupsMapper.findGroupByAppId(appId);
        }

        return Result.success(blockGroupsListResult);
    }


    /**
     * 创建区块分组
     *
     * @param blockGroups
     * @return
     */
    @Operation(summary = "创建区块分组",
            description = "创建区块分组",
            parameters = {
                    @Parameter(name = "blockGroups", description = "入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BlockGroups.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "创建区块分组")
    @PostMapping("/block-groups/create")
    public Result<List<BlockGroupsDto>> createBlockGroups(@Valid @RequestBody BlockGroups blockGroups) {
        List<BlockGroupsDto> blockGroupsList = blockGroupsService.findBlockGroupsByCondition(blockGroups);
        if (blockGroupsList.isEmpty()) {
            blockGroupsService.createBlockGroups(blockGroups);
        } else {
            return Result.failed(ExceptionEnum.CM003);
        }

        // 页面返回数据显示
        List<BlockGroupsDto> blockGroupsListResult = blockGroupsMapper.getBlockGroupsById(blockGroups.getId());
        return Result.success(blockGroupsListResult);
    }

    /**
     * 修改区块分组
     *
     * @param id
     * @param blockGroups
     * @return
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
                                    schema = @Schema(implementation = BlockGroups.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "修改区块分组")
    @PostMapping("/block-groups/update/{id}")
    public Result<List<BlockGroupsDto>> updateBlockGroups(@Valid @PathVariable Integer id, @RequestBody BlockGroups blockGroups) {
        blockGroups.setId(id);
        blockGroupsService.updateBlockGroupsById(blockGroups);
        // 页面返回数据显示
        List<BlockGroupsDto> blockGroupsListResult = blockGroupsMapper.getBlockGroupsById(blockGroups.getId());
        return Result.success(blockGroupsListResult);
    }

    /**
     * 根据id删除区块分组
     *
     * @param id
     * @return
     * @throws ServiceException
     */
    @Operation(summary = "根据id删除区块分组",
            description = "根据id删除区块分组",
            parameters = {
                    @Parameter(name = "id", description = "分组id")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BlockGroups.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "根据id删除区块分组")
    @GetMapping("/block-groups/delete/{id}")
    public Result<List<BlockGroupsDto>> deleteBlockGroups(@PathVariable Integer id) throws ServiceException {
        BlockGroupsDto blockGroups = blockGroupsService.findBlockGroupsById(id);
        if (blockGroups == null) {
            return Result.failed(ExceptionEnum.CM009);
        }

        // 页面返回数据显示
        List<BlockGroupsDto> blockGroupsListResult = blockGroupsMapper.getBlockGroupsById(blockGroups.getId());
        blockGroupsService.deleteBlockGroupsById(id);
        return Result.success(blockGroupsListResult);
    }
}

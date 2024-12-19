/**
 * Copyright (c) 2023 - present TinyEngine Authors.
 * Copyright (c) 2023 - present Huawei Cloud Computing Technologies Co., Ltd.
 * <p>
 * Use of this source code is governed by an MIT-style license.
 * <p>
 * THE OPEN SOURCE SOFTWARE IN THIS PRODUCT IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL,
 * BUT WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS FOR
 * A PARTICULAR PURPOSE. SEE THE BlockCurrentHistoryLICABLE LICENSES FOR MORE DETAILS.
 */

package com.tinyengine.it.controller;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.log.SystemControllerLog;
import com.tinyengine.it.model.entity.BlockCurrentHistory;
import com.tinyengine.it.model.entity.BlockHistory;
import com.tinyengine.it.service.material.BlockCurrentHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 区块当前使用版本历史
 * </p>
 *
 * @author lu-yg
 * @since 2024-12-17
 */
@Validated
@RestController
@RequestMapping("/material-center/api")
public class BlockCurrentHistoryController {
    @Autowired
    private BlockCurrentHistoryService blockCurrentHistoryService;

    /**
     * 查询表BlockCurrentHistory信息
     *
     * @return BlockCurrentHistory信息 all BlockCurrentHistory
     */
    @Operation(summary = "查询表BlockCurrentHistory信息",
            description = "查询表BlockCurrentHistory信息",
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "BlockCurrentHistorylication/json",
                                    schema = @Schema(implementation = BlockCurrentHistory.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "查询表BlockCurrentHistory信息")
    @GetMapping("/block/current-history/list")
    public Result<List<BlockCurrentHistory>> getAllBlockCurrentHistory() {
        List<BlockCurrentHistory> BlockCurrentHistoryList = blockCurrentHistoryService.queryAllBlockCurrentHistory();
        return Result.success(BlockCurrentHistoryList);
    }

    /**
     * 创建BlockCurrentHistory
     *
     * @param BlockCurrentHistory the BlockCurrentHistory
     * @return BlockCurrentHistory信息 result
     */
    @Operation(summary = "创建BlockCurrentHistory",
            description = "创建BlockCurrentHistory",
            parameters = {
                    @Parameter(name = "BlockCurrentHistory", description = "BlockCurrentHistory入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "BlockCurrentHistorylication/json",
                                    schema = @Schema(implementation = BlockCurrentHistory.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "创建BlockCurrentHistory")
    @PostMapping("/block/current-history/create")
    public Result<BlockCurrentHistory> createBlockCurrentHistory(@Valid @RequestBody BlockCurrentHistory BlockCurrentHistory) {
        return blockCurrentHistoryService.createBlockCurrentHistory(BlockCurrentHistory);
    }

    /**
     * 修改BlockCurrentHistory信息
     *
     * @param id                  the id
     * @param BlockCurrentHistory the BlockCurrentHistory
     * @return BlockCurrentHistory信息 result
     */
    @Operation(summary = "修改单个BlockCurrentHistory信息", description = "修改单个BlockCurrentHistory信息", parameters = {
            @Parameter(name = "id", description = "BlockCurrentHistoryId"),
            @Parameter(name = "BlockCurrentHistory", description = "入参对象")}, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "BlockCurrentHistorylication/json",
                            schema = @Schema(implementation = BlockCurrentHistory.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "修改单个BlockCurrentHistory信息")
    @PostMapping("/block/current-history/update/{id}")
    public Result<BlockCurrentHistory> updateBlockCurrentHistory(@PathVariable Integer id, @RequestBody BlockCurrentHistory BlockCurrentHistory) {
        BlockCurrentHistory.setId(id);
        return blockCurrentHistoryService.updateBlockCurrentHistoryById(BlockCurrentHistory);
    }

    /**
     * 删除BlockCurrentHistory信息
     *
     * @param id the id
     * @return BlockCurrentHistory信息 result
     */
    @Operation(summary = "删除BlockCurrentHistory信息",
            description = "删除BlockCurrentHistory信息",
            parameters = {
                    @Parameter(name = "id", description = "BlockCurrentHistory主键id")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "BlockCurrentHistorylication/json",
                                    schema = @Schema(implementation = BlockCurrentHistory.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "删除BlockCurrentHistory信息")
    @GetMapping("/block/current-history/delete/{id}")
    public Result<BlockCurrentHistory> deleteBlockCurrentHistory(@PathVariable Integer id) {
        return blockCurrentHistoryService.deleteBlockCurrentHistoryById(id);
    }

    /**
     * 获取区块使用版本详情
     *
     * @param id the id
     * @return the result
     */
    @Operation(summary = "获取区块使用版本详情", description = "获取区块使用版本详情", parameters = {
            @Parameter(name = "id", description = "BlockCurrentHistoryId")}, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "BlockCurrentHistorylication/json",
                            schema = @Schema(implementation = BlockCurrentHistory.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "获取应用信息详情")
    @GetMapping("/block/current-history/detail/{id}")
    public Result<BlockHistory> detail(@PathVariable Integer id) {
        BlockHistory blockHistory = blockCurrentHistoryService.queryBlockCurrentHistoryById(id);
        return Result.success(blockHistory);
    }

}

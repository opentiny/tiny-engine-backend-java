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

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.log.SystemControllerLog;
import com.tinyengine.it.model.entity.MaterialHistory;
import com.tinyengine.it.service.material.MaterialHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 物料历史api
 *
 * @since 2025-4-1
 */
@Validated
@RestController
@RequestMapping("/material-center/api")
@Tag(name = "物料历史")
public class MaterialHistoryController {
    /**
     * The MaterialHistory service.
     */
    @Autowired
    private MaterialHistoryService materialHistoryService;

    /**
     * 查询表MaterialHistory信息
     *
     * @return MaterialHistory信息 all materialHistory
     */
    @Operation(summary = "查询表MaterialHistory信息",
            description = "查询表MaterialHistory信息",
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = MaterialHistory.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "查询表MaterialHistory信息")
    @GetMapping("/material-history/list")
    public Result<List<MaterialHistory>> getAllMaterialHistory() {
        List<MaterialHistory> materialHistoryList = materialHistoryService.findAllMaterialHistory();
        return Result.success(materialHistoryList);
    }

    /**
     * 创建MaterialHistory
     *
     * @param materialHistory the materialHistory
     * @return MaterialHistory信息 result
     */
    @Operation(summary = "创建MaterialHistory",
            description = "创建MaterialHistory",
            parameters = {
                    @Parameter(name = "MaterialHistory", description = "MaterialHistory入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = MaterialHistory.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "创建MaterialHistory")
    @PostMapping("/material-history/create")
    public Result<MaterialHistory> createMaterialHistory(@Valid @RequestBody MaterialHistory materialHistory) {
        return materialHistoryService.createMaterialHistory(materialHistory);
    }

    /**
     * 修改MaterialHistory信息
     *
     * @param id  the id
     * @param materialHistory the materialHistory
     * @return MaterialHistory信息 result
     */
    @Operation(summary = "修改单个MaterialHistory信息", description = "修改单个MaterialHistory信息", parameters = {
            @Parameter(name = "id", description = "appId"),
            @Parameter(name = "MaterialHistory", description = "入参对象")}, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MaterialHistory.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "修改单个MaterialHistory信息")
    @PostMapping("/apps/update/{id}")
    public Result<MaterialHistory> updateMaterialHistory(@PathVariable Integer id, @RequestBody MaterialHistory materialHistory) {
        materialHistory.setId(id);
        return materialHistoryService.updateMaterialHistoryById(materialHistory);
    }

    /**
     * 删除MaterialHistory信息
     *
     * @param id the id
     * @return app信息 result
     */
    @Operation(summary = "删除app信息",
            description = "删除app信息",
            parameters = {
                    @Parameter(name = "id", description = "MaterialHistory主键id")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = MaterialHistory.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "删除app信息")
    @GetMapping("/material-history/delete/{id}")
    public Result<MaterialHistory> deleteMaterialHistory(@PathVariable Integer id) {
        return materialHistoryService.deleteMaterialHistoryById(id);
    }

    /**
     * 获取应用信息详情
     *
     * @param id the id
     * @return the result
     */
    @Operation(summary = "获取应用信息详情", description = "获取应用信息详情", parameters = {
            @Parameter(name = "id", description = "appId")}, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MaterialHistory.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "获取应用信息详情")
    @GetMapping("/material-history/detail/{id}")
    public Result<MaterialHistory> detail(@PathVariable Integer id) {
        return materialHistoryService.findMaterialHistoryById(id);
    }
}

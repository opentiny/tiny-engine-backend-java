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
import com.tinyengine.it.model.entity.PlatformHistory;
import com.tinyengine.it.service.platform.PlatformHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 设计器api
 *
 * @since 2024-10-20
 */
@Validated
@RestController
@RequestMapping("/platform-center/api")
@Tag(name = "设计器历史")
public class PlatformHistoryController {
    /**
     * The PlatformHistory service.
     */
    @Autowired
    private PlatformHistoryService platformHistoryService;

    /**
     * 查询表PlatformHistory信息
     *
     * @return PlatformHistory信息 all app
     */
    @Operation(summary = "查询表PlatformHistory信息",
            description = "查询表PlatformHistory信息",
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PlatformHistory.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "查询表PlatformHistory信息")
    @GetMapping("/platform-history/list")
    public Result<List<PlatformHistory>> getAllPlatformHistory() {
        List<PlatformHistory> appList = platformHistoryService.queryAllPlatformHistory();
        return Result.success(appList);
    }

    /**
     * 根据id查询表PlatformHistory信息
     *
     * @param id the id
     * @return PlatformHistory信息 app by id
     */
    @Operation(summary = "根据id查询表PlatformHistory信息",
            description = "根据id查询表PlatformHistory信息",
            parameters = {
                    @Parameter(name = "id", description = "PlatformHistory主键id")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PlatformHistory.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "根据id查询表PlatformHistory信息")
    @GetMapping("/platform-history/{id}")
    public Result<PlatformHistory> getPlatformHistoryById(@PathVariable Integer id) {
        PlatformHistory platformHistory = platformHistoryService.queryPlatformHistoryById(id);
        return Result.success(platformHistory);
    }

    /**
     * 创建PlatformHistory
     *
     * @param platformHistory the platformHistory
     * @return PlatformHistory信息 result
     */
    @Operation(summary = "创建platformHistory",
            description = "创建platformHistory",
            parameters = {
                    @Parameter(name = "platformHistory", description = "PlatformHistory入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PlatformHistory.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "创建platformHistory")
    @PostMapping("/platform-history/create")
    public Result<PlatformHistory> createPlatformHistory(@Valid @RequestBody PlatformHistory platformHistory) {
        return platformHistoryService.createPlatformHistory(platformHistory);
    }

    /**
     * 修改PlatformHistory信息
     *
     * @param id  the id
     * @param platformHistory the platformHistory
     * @return PlatformHistory信息 result
     */
    @Operation(summary = "修改单个PlatformHistory信息", description = "修改单个PlatformHistory信息", parameters = {
            @Parameter(name = "id", description = "appId"),
            @Parameter(name = "PlatformHistory", description = "入参对象")}, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PlatformHistory.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "修改单个PlatformHistory信息")
    @PutMapping("/platform-history/update/{id}")
    public Result<PlatformHistory> updatePlatformHistory(@PathVariable Integer id, @RequestBody PlatformHistory platformHistory) {
        platformHistory.setId(id);
        return platformHistoryService.updatePlatformHistoryById(platformHistory);
    }

    /**
     * 删除PlatformHistory信息
     *
     * @param id the id
     * @return platformHistory信息 result
     */
    @Operation(summary = "删除platformHistory信息",
            description = "删除platformHistory信息",
            parameters = {
                    @Parameter(name = "id", description = "PlatformHistory主键id")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PlatformHistory.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "删除platformHistory信息")
    @DeleteMapping("/platform-history/delete/{id}")
    public Result<PlatformHistory> deletePlatformHistory(@PathVariable Integer id) {
        return platformHistoryService.deletePlatformHistoryById(id);
    }
}

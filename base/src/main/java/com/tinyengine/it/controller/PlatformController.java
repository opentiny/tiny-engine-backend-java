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
import com.tinyengine.it.model.entity.Platform;
import com.tinyengine.it.service.platform.PlatformService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.validation.Valid;


/**
 * 设计器api
 *
 * @since 2024-10-20
 */
@Validated
@RestController
@RequestMapping("/platform-center/api")
@Tag(name = "设计器")
public class PlatformController {
    /**
     * The Platform service.
     */
    @Autowired
    private PlatformService platformService;

    /**
     * 查询表Platform信息
     *
     * @return Platform信息 all app
     */
    @Operation(summary = "查询表Platform信息", description = "查询表Platform信息", responses = {
        @ApiResponse(responseCode = "200", description = "返回信息",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Platform.class))),
        @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "查询表Platform信息")
    @GetMapping("/platform/list")
    public Result<List<Platform>> getAllPlatform() {
        List<Platform> appList = platformService.queryAllPlatform();
        return Result.success(appList);
    }

    /**
     * 根据id查询表Platform信息
     *
     * @param id the id
     * @return Platform信息 app by id
     */
    @Operation(summary = "根据id查询表Platform信息", description = "根据id查询表Platform信息", parameters = {
        @Parameter(name = "id", description = "Platform主键id")
    }, responses = {
        @ApiResponse(responseCode = "200", description = "返回信息",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Platform.class))),
        @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "根据id查询表Platform信息")
    @GetMapping("/platform/{id}")
    public Result<Platform> getPlatformById(@PathVariable Integer id) {
        Platform platform = platformService.queryPlatformById(id);
        return Result.success(platform);
    }

    /**
     * 创建Platform
     *
     * @param platform the platform
     * @return Platform信息 result
     */
    @Operation(summary = "创建platform", description = "创建platform", parameters = {
        @Parameter(name = "platform", description = "Platform入参对象")
    }, responses = {
        @ApiResponse(responseCode = "200", description = "返回信息",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Platform.class))),
        @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "创建platform")
    @PostMapping("/platform/create")
    public Result<Platform> createPlatform(@Valid @RequestBody Platform platform) {
        return platformService.createPlatform(platform);
    }

    /**
     * 修改Platform信息
     *
     * @param id  the id
     * @param platform the platform
     * @return Platform信息 result
     */
    @Operation(summary = "修改单个Platform信息", description = "修改单个Platform信息", parameters = {
        @Parameter(name = "id", description = "appId"),
        @Parameter(name = "Platform", description = "入参对象")
    }, responses = {
        @ApiResponse(responseCode = "200", description = "返回信息",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Platform.class))),
        @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "修改单个Platform信息")
    @PostMapping("/platform/update/{id}")
    public Result<Platform> updatePlatform(@PathVariable Integer id, @RequestBody Platform platform) {
        platform.setId(id);
        return platformService.updatePlatformById(platform);
    }

    /**
     * 删除Platform信息
     *
     * @param id the id
     * @return platform信息 result
     */
    @Operation(summary = "删除platform信息，与js同路由", description = "删除platform信息，与js同路由", parameters = {
        @Parameter(name = "id", description = "Platform主键id")
    }, responses = {
        @ApiResponse(responseCode = "200", description = "返回信息",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Platform.class))),
        @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "删除platform信息，与js同路由")
    @GetMapping("/platform/delete/{id}")
    public Result<Platform> delete(@PathVariable Integer id) {
        return platformService.deletePlatformById(id);
    }

    /**
     * 删除Platform信息
     *
     * @param id the id
     * @return platform信息 result
     */
    @Operation(summary = "删除platform信息", description = "删除platform信息", parameters = {
        @Parameter(name = "id", description = "Platform主键id")
    }, responses = {
        @ApiResponse(responseCode = "200", description = "返回信息",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Platform.class))),
        @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "删除platform信息")
    @DeleteMapping("/platform/delete/{id}")
    public Result<Platform> deletePlatform(@PathVariable Integer id) {
        return platformService.deletePlatformById(id);
    }
}

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
import com.tinyengine.it.model.entity.ComponentLibrary;
import com.tinyengine.it.service.material.ComponentLibraryService;
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
 * 组件库API
 *
 * @since 2025-4-02
 */
@Validated
@RestController
@RequestMapping("/material-center/api")
@Tag(name = "组件库")
public class ComponentLibraryController {
    /**
     * The ComponentLibrary service.
     */
    @Autowired
    private ComponentLibraryService componentLibraryService;

    /**
     * 查询表ComponentLibrary信息列表
     *
     * @return ComponentLibrary信息 all componentLibrary
     */
    @Operation(summary = "查询表ComponentLibrary信息列表", description = "查询表ComponentLibrary信息列表", responses = {
        @ApiResponse(responseCode = "200", description = "返回信息",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ComponentLibrary.class))),
        @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "查询表ComponentLibrary信息列表")
    @GetMapping("/component-library/list")
    public Result<List<ComponentLibrary>> getAllComponentLibrary() {
        List<ComponentLibrary> componentLibraryHistoryList = componentLibraryService.queryAllComponentLibrary();
        return Result.success(componentLibraryHistoryList);
    }

    /**
     * 创建ComponentLibrary
     *
     * @param componentLibrary the componentLibrary
     * @return ComponentLibrary信息 result
     */
    @Operation(summary = "创建ComponentLibrary", description = "创建ComponentLibrary", parameters = {
        @Parameter(name = "ComponentLibrary", description = "ComponentLibrary入参对象")
    }, responses = {
        @ApiResponse(responseCode = "200", description = "返回信息",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ComponentLibrary.class))),
        @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "创建ComponentLibrary")
    @PostMapping("/component-library/create")
    public Result<ComponentLibrary> createComponentLibrary(@Valid @RequestBody ComponentLibrary componentLibrary) {
        return componentLibraryService.createComponentLibrary(componentLibrary);
    }

    /**
     * 修改ComponentLibrary信息
     *
     * @param id  the id
     * @param componentLibrary the componentLibrary
     * @return ComponentLibrary信息 result
     */
    @Operation(summary = "修改单个ComponentLibrary信息", description = "修改单个ComponentLibrary信息", parameters = {
        @Parameter(name = "id", description = "appId"),
        @Parameter(name = "ComponentLibrary", description = "入参对象")
    }, responses = {
        @ApiResponse(responseCode = "200", description = "返回信息",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ComponentLibrary.class))),
        @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "修改单个ComponentLibrary信息")
    @PostMapping("/component-library/update/{id}")
    public Result<ComponentLibrary> updateComponentLibrary(@PathVariable Integer id,
        @RequestBody ComponentLibrary componentLibrary) {
        componentLibrary.setId(id);
        return componentLibraryService.updateComponentLibraryById(componentLibrary);
    }

    /**
     * 删除ComponentLibrary信息
     *
     * @param id the id
     * @return ComponentLibrary信息 result
     */
    @Operation(summary = "删除ComponentLibrary信息", description = "删除ComponentLibrary信息", parameters = {
        @Parameter(name = "id", description = "ComponentLibrary主键id")
    }, responses = {
        @ApiResponse(responseCode = "200", description = "返回信息",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ComponentLibrary.class))),
        @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "删除ComponentLibrary信息")
    @DeleteMapping("/component-library/delete/{id}")
    public Result<ComponentLibrary> deleteComponentLibrary(@PathVariable Integer id) {
        return componentLibraryService.deleteComponentLibraryById(id);
    }

    /**
     * 获取ComponentLibrary信息详情
     *
     * @param id the id
     * @return the result
     */
    @Operation(summary = "获取ComponentLibrary信息详情", description = "获取ComponentLibrary信息详情", parameters = {
        @Parameter(name = "id", description = "appId")
    }, responses = {
        @ApiResponse(responseCode = "200", description = "返回信息",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ComponentLibrary.class))),
        @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "获取ComponentLibrary信息详情")
    @GetMapping("/component-library/detail/{id}")
    public Result<ComponentLibrary> detail(@PathVariable Integer id) {
        return componentLibraryService.queryComponentLibraryById(id);
    }
}

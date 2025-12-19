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
import com.tinyengine.it.model.dto.AppDto;
import com.tinyengine.it.model.entity.App;
import com.tinyengine.it.service.app.AppTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 应用模版api
 *
 * @since 2025-10-20
 */
@Validated
@RestController
@RequestMapping("/app-center/api")
@Tag(name = "应用模版")
public class AppTemplateController {
    /**
     * The App template service.
     */
    @Autowired
    private AppTemplateService appTemplateService;

    /**
     * 分页查询应用模版信息
     *
     * @return App template 信息 all app template
     */
    @Operation(summary = "分页查询应用模版信息", description = "分页查询应用模版信息",
        parameters = {
            @Parameter(name = "currentPage", description = "当前页"),
            @Parameter(name = "pageSize", description = "返回条数"),
            @Parameter(name = "name", description = "名称"),
            @Parameter(name = "industry", description = "行业"),
            @Parameter(name = "scene", description = "场景"),
            @Parameter(name = "framework", description = "技术栈"),
            @Parameter(name = "orderBy", description = "排序方式"),
            @Parameter(name = "createBy", description = "创建人"),
        }, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = App.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "分页查询应用模版信息")
    @GetMapping("/app-template/list")
    public Result<AppDto> getAllAppTemplateByPage(@RequestParam Integer currentPage,
        @RequestParam Integer pageSize, @RequestParam(required = false) String name,
        @RequestParam(required = false) Integer industryId, @RequestParam(required = false) Integer sceneId,
        @RequestParam(required = false) String framework, @RequestParam(required = false) String orderBy,
        @RequestParam(required = false) String createBy) {
        App app = new App();
        app.setName(name);
        app.setSceneId(sceneId);
        app.setIndustryId(industryId);
        app.setFramework(framework);
        app.setCreatedBy(createBy);
        AppDto appDto = appTemplateService.queryAllAppTemplate(currentPage, pageSize, orderBy, app);
        return Result.success(appDto);
    }

    /**
     * 根据id查询应用模版信息
     *
     * @param id the id
     * @return App信息 app by id
     */
    @Operation(summary = "根据id查询应用模版信息", description = "根据id查询应用模版信息",
        parameters = {
            @Parameter(name = "id", description = "App主键id")
        }, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = App.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "根据id查询应用模版信息")
    @GetMapping("/app-template/{id}")
    public Result<App> getAppTemplateById(@PathVariable Integer id) {
        return appTemplateService.queryAppTemplateById(id);
    }

    /**
     * 通过模版创建应用
     *
     * @param app the app
     * @return App信息 result
     */
    @Operation(summary = "通过模版创建应用", description = "通过模版创建应用",
        parameters = {
            @Parameter(name = "app", description = "App入参对象")
        }, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = App.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "通过模版创建应用")
    @PostMapping("/app-template/create")
    public Result<App> createAppTemplate(@Valid @RequestBody App app) {
        App result = appTemplateService.createAppByTemplate(app);
        return Result.success(result);
    }

}

/**
 * Copyright (c) 2023 - present TinyEngine Authors.
 * Copyright (c) 2023 - present Huawei Cloud Computing Technologies Co., Ltd.
 *
 * Use of this source code is governed by an MIT-style license.
 *
 * THE OPEN SOURCE SOFTWARE IN THIS PRODUCT IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL,
 * BUT WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS FOR
 * A PARTICULAR PURPOSE. SEE THE APPLICABLE LICENSES FOR MORE DETAILS.
 */

package com.tinyengine.it.controller;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.log.SystemControllerLog;
import com.tinyengine.it.model.entity.PageTemplate;
import com.tinyengine.it.service.app.PageTemplateService;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.validation.Valid;

/**
 * <p>
 * 页面模版
 * </p>
 *
 * @author lu-yg
 * @since 2024-10-25
 */
@Validated
@RestController
@RequestMapping("/material-center/api")
public class PageTemplateController {
    @Autowired
    private PageTemplateService pageTemplateService;

    /**
     * 创建pageTemplate
     *
     * @param pageTemplate pageTemplate
     * @return pageTemplate信息
     */
    @Operation(summary = "创建页面模版", description = "创建页面模版", parameters = {
            @Parameter(name = "pageTemplate", description = "页面模版参数对象")}, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PageTemplate.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "创建页面模版")
    @PostMapping("/page-template/create")
    public Result<PageTemplate> createPageTemplate(@Valid @RequestBody PageTemplate pageTemplate) {
        return pageTemplateService.createPageTemplate(pageTemplate);
    }

    /**
     * 批量删除pageTemplate
     *
     * @param id id
     * @return pageTemplate信息
     */
    @Operation(summary = "批量删除页面模版", description = "批量删除页面模版", parameters = {
            @Parameter(name = "id", description = "页面模版多个主键ids")}, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PageTemplate.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "批量删除页面模版")
    @PostMapping("/template-basic/bulk/delete")
    public Result<Integer> deletePageTemplate(@RequestBody List<Integer> id) {
        return pageTemplateService.deletePageTemplateByIds(id);
    }

    /**
     * 获取页面模版信息详情
     *
     * @param id id
     * @return PageTemplate
     */
    @Operation(summary = "获取页面模版信息详情", description = "获取页面模版信息详情", parameters = {
            @Parameter(name = "id", description = "页面模板主键id")}, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PageTemplate.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "获取页面模版信息详情")
    @GetMapping("/template-basic/detail/{id}")
    public Result<PageTemplate> detail(@PathVariable Integer id) {
        return pageTemplateService.queryPageTemplateById(id);
    }

    /**
     * 获取页面模版信息列表
     *
     * @param name name
     * @param type name
     * @return PageTemplate
     */
    @Operation(summary = "获取页面模版信息列表", description = "获取页面模版信息列表",
            parameters = {
                    @Parameter(name = "name", description = "模版名称"),
                    @Parameter(name = "type", description = "模版类型")
            }, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PageTemplate.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "获取页面模版信息列表")
    @GetMapping("/template-basic/list")
    public Result<List<PageTemplate>> findAllPageTemplate(@RequestParam(required = false) String name,
                                                          @RequestParam String type) {
        return pageTemplateService.queryAllPageTemplate(name, type);
    }
}

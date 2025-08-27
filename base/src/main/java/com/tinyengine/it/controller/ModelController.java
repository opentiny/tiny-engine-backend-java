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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.log.SystemControllerLog;
import com.tinyengine.it.model.entity.Model;
import com.tinyengine.it.service.material.ModelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 模型
 *
 * @since 2025-07-17
 */
@Validated
@RestController
@RequestMapping("/material-center/api")
@Tag(name = "模型")
public class ModelController {
    /**
     * The Model service.
     */
    @Autowired
    private ModelService modelService;

    /**
     * 查询表Model信息
     *
     * @return all Model信息
     */
    @Operation(summary = "查询表Model信息列表", description = "查询表Model信息列表", parameters = {
        @Parameter(name = "currentPage", description = "当前页"),
        @Parameter(name = "pageSize", description = "页数"),
        @Parameter(name = "nameCn", description = "模型中文名称"),
        @Parameter(name = "nameEn", description = "模型英文名称")
    }, responses = {
        @ApiResponse(responseCode = "200", description = "返回信息",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Model.class))),
        @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "查询表Model信息列表")
    @GetMapping("/model/list")
    public Result<Page<Model>> getAllModel(@RequestParam(value = "currentPage", required = false) Integer currentPage,
        @RequestParam(value = "pageSize", required = false) Integer pageSize,
        @RequestParam(value = "nameCn", required = false) String nameCn,
        @RequestParam(value = "nameEn", required = false) String nameEn) {
        Page<Model> modelPage = modelService.pageQuery(currentPage, pageSize, nameCn, nameEn);
        return Result.success(modelPage);
    }

    /**
     * 根据name查询表Model信息
     *
     * @return Model信息
     */
    @Operation(summary = "根据nameCn查询表Model信息", description = "根据nameCn查询表Model信息", parameters = {
        @Parameter(name = "nameCn", description = "名称"),
    }, responses = {
        @ApiResponse(responseCode = "200", description = "返回信息",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Model.class))),
        @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "根据nameCn查询表Model信息")
    @GetMapping("/model/find")
    public Result<List<Model>> getModelByName(@RequestParam(value = "nameCn", required = false) String nameCn) {
        List<Model> modelPage = modelService.getModelByName(nameCn);
        return Result.success(modelPage);
    }

    /**
     * 创建Model
     *
     * @param model the model
     * @return Model信息 result
     */
    @Operation(summary = "创建Model", description = "创建Model", parameters = {
        @Parameter(name = "Model", description = "Model入参对象")
    }, responses = {
        @ApiResponse(responseCode = "200", description = "返回信息", content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = Model.class))),
        @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "创建Model")
    @PostMapping("/model/create")
    public Result<Model> createModel(@Valid @RequestBody Model model) {
        Model result = modelService.createModel(model);
        return Result.success(result);
    }

    /**
     * 修改Model信息
     *
     * @param id  the id
     * @param model the model
     * @return Model信息 result
     */
    @Operation(summary = "修改单个Model信息", description = "修改单个Model信息", parameters = {
        @Parameter(name = "id", description = "模型id"),
        @Parameter(name = "Model", description = "入参对象")
    }, responses = {
        @ApiResponse(responseCode = "200", description = "返回信息",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Model.class))),
        @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "修改单个Model信息")
    @PutMapping("/model/update/{id}")
    public Result<Model> updateModel(@PathVariable Integer id, @RequestBody Model model) {
        model.setId(id);
        Model result = modelService.updateModelById(model);
        return Result.success(result);
    }

    /**
     * 删除Model信息
     *
     * @param id the id
     * @return app信息 result
     */
    @Operation(summary = "删除Model信息", description = "删除Model信息", parameters = {
        @Parameter(name = "id", description = "Model主键id")
    }, responses = {
        @ApiResponse(responseCode = "200", description = "返回信息",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Model.class))),
        @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "删除Model信息")
    @DeleteMapping("/model/delete/{id}")
    public Result<Model> deleteModel(@PathVariable Integer id) {
        Model result = modelService.deleteModelById(id);
        return Result.success(result);
    }

    /**
     * 获取Model信息详情
     *
     * @param id the id
     * @return the result
     */
    @Operation(summary = "获取Model信息详情", description = "获取Model信息详情", parameters = {
        @Parameter(name = "id", description = "模型id")
    }, responses = {
        @ApiResponse(responseCode = "200", description = "返回信息",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Model.class))),
        @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "获取Model信息详情")
    @GetMapping("/model/detail/{id}")
    public Result<Model> detail(@PathVariable Integer id) {
        Model result = modelService.queryModelById(id);
        return Result.success(result);
    }

    /**
     * 获取Model建表sql
     *
     * @param id the id
     * @return the result
     */
    @Operation(summary = "获取Model建表sql", description = "获取Model建表sql", parameters = {
        @Parameter(name = "id", description = "模型id")
    }, responses = {
        @ApiResponse(responseCode = "200", description = "返回信息",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "获取Model建表sql")
    @GetMapping("/model/table/{id}")
    public Result<String> getTable(@PathVariable Integer id) {
        String result = modelService.getTableById(id);
        return Result.success(result);
    }

    /**
     * 获取所有Model建表sql
     *
     * @return the result
     */
    @Operation(summary = "获取所有Model建表sql", description = "获取所有Model建表sql", parameters = {
    }, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "获取所有Model建表sql")
    @GetMapping("/model/table/list")
    public Result<String> getAllTable() {
        String result = modelService.getAllTable();
        return Result.success(result);
    }
}

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
import com.tinyengine.it.model.entity.Material;
import com.tinyengine.it.service.material.MaterialService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 物料历史api
 *
 * @since 2025-4-1
 */
@Validated
@RestController
@RequestMapping("/material-center/api")
@Tag(name = "物料历史")
public class MaterialController {
    /**
     * The Material service.
     */
    @Autowired
    private MaterialService materialService;

    /**
     * 查询表Material信息
     *
     * @return Material信息 all material
     */
    @Operation(summary = "查询表Material信息列表", description = "查询表Material信息列表", responses = {
        @ApiResponse(responseCode = "200", description = "返回信息",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Material.class))),
        @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "查询表Material信息列表")
    @GetMapping("/material/list")
    public Result<List<Material>> getAllMaterial() {
        List<Material> materialHistoryList = materialService.queryAllMaterial();
        return Result.success(materialHistoryList);
    }

    /**
     * 创建Material
     *
     * @param material the material
     * @return Material信息 result
     */
    @Operation(summary = "创建Material", description = "创建Material", parameters = {
        @Parameter(name = "Material", description = "Material入参对象")
    }, responses = {
        @ApiResponse(responseCode = "200", description = "返回信息",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Material.class))),
        @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "创建Material")
    @PostMapping("/material/create")
    public Result<Material> createMaterial(@Valid @RequestBody Material material) {
        return materialService.createMaterial(material);
    }

    /**
     * 修改Material信息
     *
     * @param id  the id
     * @param material the material
     * @return Material信息 result
     */
    @Operation(summary = "修改单个Material信息", description = "修改单个Material信息", parameters = {
        @Parameter(name = "id", description = "appId"),
        @Parameter(name = "Material", description = "入参对象")
    }, responses = {
        @ApiResponse(responseCode = "200", description = "返回信息",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Material.class))),
        @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "修改单个Material信息")
    @PostMapping("/material/update/{id}")
    public Result<Material> updateMaterial(@PathVariable Integer id, @RequestBody Material material) {
        material.setId(id);
        return materialService.updateMaterialById(material);
    }

    /**
     * 删除Material信息
     *
     * @param id the id
     * @return app信息 result
     */
    @Operation(summary = "删除Material信息", description = "删除Material信息", parameters = {
        @Parameter(name = "id", description = "Material主键id")
    }, responses = {
        @ApiResponse(responseCode = "200", description = "返回信息",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Material.class))),
        @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "删除Material信息")
    @DeleteMapping("/material/delete/{id}")
    public Result<Material> deleteMaterial(@PathVariable Integer id) {
        return materialService.deleteMaterialById(id);
    }

    /**
     * 获取Material信息详情
     *
     * @param id the id
     * @return the result
     */
    @Operation(summary = "获取Material信息详情", description = "获取Material信息详情", parameters = {
        @Parameter(name = "id", description = "appId")
    }, responses = {
        @ApiResponse(responseCode = "200", description = "返回信息",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Material.class))),
        @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "获取Material信息详情")
    @GetMapping("/material/detail/{id}")
    public Result<Material> detail(@PathVariable Integer id) {
        return materialService.queryMaterialById(id);
    }
}

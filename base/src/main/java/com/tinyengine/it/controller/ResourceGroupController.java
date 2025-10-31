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
import com.tinyengine.it.model.entity.ResourceGroup;
import com.tinyengine.it.service.material.ResourceGroupService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 资源分组
 *
 * @since 2025-09-03
 */
@Validated
@RestController
@RequestMapping("/material-center/api")
@Tag(name = "资源分组")
public class ResourceGroupController {
    /**
     * The ResourceGroup service.
     */
    @Autowired
    private ResourceGroupService resourceGroupService;

    /**
     * 查询表ResourceGroup信息
     *
     * @return ResourceGroup信息 all resource
     */
    @Operation(summary = "查询表ResourceGroup信息", description = "查询表ResourceGroup信息",
        responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResourceGroup.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")
       })
    @SystemControllerLog(description = "查询表ResourceGroup信息")
    @GetMapping("/resource-group/list")
    public Result<List<ResourceGroup>> queryAllResourceGroupAndResource() {
        List<ResourceGroup> resourceGroupList = resourceGroupService.queryAllResourceGroupAndResource();
        return Result.success(resourceGroupList);
    }

    /**
     * 根据id查询表ResourceGroup信息
     *
     * @param appId the appId
     * @return ResourceGroup信息 app by id
     */
    @Operation(summary = "根据appId查询表ResourceGroup信息", description = "根据appId查询表ResourceGroup信息",
        parameters = {
            @Parameter(name = "appId", description = "ResourceGroup主键id")
        }, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResourceGroup.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "根据appId查询表ResourceGroup信息")
    @GetMapping("/resource-group/{appId}")
    public Result<List<ResourceGroup>> queryResourceGroupByAppId(@PathVariable Integer appId) {
        return resourceGroupService.queryResourceGroupByAppId(appId);
    }

    /**
     * 创建ResourceGroup
     *
     * @param resourceGroup the resourceGroup
     * @return ResourceGroup信息 result
     */
    @Operation(summary = "创建ResourceGroup", description = "创建ResourceGroup",
        parameters = {
            @Parameter(name = "resourceGroup", description = "ResourceGroup入参对象")
        }, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResourceGroup.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "创建ResourceGroup")
    @PostMapping("/resource-group/create")
    public Result<ResourceGroup> createResourceGroup(@Valid @RequestBody ResourceGroup resourceGroup) {
        return resourceGroupService.createResourceGroup(resourceGroup);
    }

    /**
     * 修改ResourceGroup信息
     *
     * @param id  the id
     * @param resource the resource
     * @return ResourceGroup信息 result
     */
    @Operation(summary = "修改单个ResourceGroup信息", description = "修改单个ResourceGroup信息",
        parameters = {
            @Parameter(name = "id", description = "appId"),
            @Parameter(name = "ResourceGroup", description = "入参对象")
        }, responses = {
               @ApiResponse(responseCode = "200", description = "返回信息",
               content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResourceGroup.class))),
               @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "修改单个ResourceGroup信息")
    @PutMapping("/resource-group/update/{id}")
    public Result<ResourceGroup> updateResourceGroup(@PathVariable Integer id, @RequestBody ResourceGroup resource) {
        resource.setId(id);
        return resourceGroupService.updateResourceGroupById(resource);
    }

    /**
     * 删除ResourceGroup信息
     *
     * @param id the id
     * @return resource信息 result
     */
    @Operation(summary = "删除ResourceGroup信息",
            description = "删除ResourceGroup信息",
            parameters = {
                    @Parameter(name = "id", description = "ResourceGroup主键id")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResourceGroup.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "删除ResourceGroup信息")
    @DeleteMapping("/resource-group/delete/{id}")
    public Result<ResourceGroup> deleteResourceGroup(@PathVariable Integer id) {
        return resourceGroupService.deleteResourceGroupById(id);
    }

    /**
     * 获取ResourceGroup信息详情
     *
     * @param id the id
     * @return the result
     */
    @Operation(summary = "获取ResourceGroup信息详情", description = "获取ResourceGroup信息详情", parameters = {
            @Parameter(name = "id", description = "appId")}, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResourceGroup.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "获取ResourceGroup信息详情")
    @GetMapping("/resource-group/detail/{id}")
    public Result<ResourceGroup> detail(@PathVariable Integer id) {
        return resourceGroupService.queryResourceGroupById(id);
    }
}

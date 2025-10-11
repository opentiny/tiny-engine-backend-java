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
import com.tinyengine.it.common.context.LoginUserContext;
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.common.log.SystemControllerLog;
import com.tinyengine.it.common.utils.ImageThumbnailGenerator;
import com.tinyengine.it.common.utils.Utils;
import com.tinyengine.it.model.entity.Resource;
import com.tinyengine.it.service.material.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

/**
 * 资源
 *
 * @since 2025-09-03
 */
@Validated
@RestController
@RequestMapping("/material-center/api")
@Tag(name = "资源")
public class ResourceController {
    /**
     * The Resource service.
     */
    @Autowired
    private ResourceService resourceService;

    @Autowired
    private LoginUserContext loginUserContext;

    /**
     * 查询表Resource信息
     *
     * @return Resource信息 all resource
     */
    @Operation(summary = "查询表Resource信息", description = "查询表Resource信息",
        responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Resource.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "查询表Resource信息")
    @GetMapping("/resource/list")
    public Result<List<Resource>> getAllResource() {
        List<Resource> appList = resourceService.queryAllResource();
        return Result.success(appList);
    }

    /**
     * 根据id查询表Resource信息
     *
     * @param id the id
     * @return Resource信息 app by id
     */
    @Operation(summary = "根据id查询表Resource信息", description = "根据id查询表Resource信息",
        parameters = {
            @Parameter(name = "id", description = "Resource主键id")
        }, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Resource.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "根据id查询表Resource信息")
    @GetMapping("/resource/{id}")
    public Result<Resource> getResourceById(@PathVariable Integer id) {
        return resourceService.queryResourceById(id);
    }

    /**
     * 根据分组id和创建人查询表t_resource信息
     *
     * @param resourceGroupId the resourceGroupId
     * @return the list
     */
    @Operation(summary = "根据分组id和创建人查询表t_resource信息", description = "根据分组id和创建人查询表t_resource信息",
        parameters = {
            @Parameter(name = "resourceGroupId", description = "ResourceGroup主键id")
        }, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Resource.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "根据分组id和创建人查询表t_resource信息")
    @GetMapping("/resource/find/{resourceGroupId}")
    public Result<List<Resource>> getResourceByResourceGroupId(@PathVariable Integer resourceGroupId) {
        List<Resource> resourceList = resourceService.queryResourceByResourceGroupId(resourceGroupId);
        return Result.success(resourceList);
    }

    /**
     * 模糊查询表Resource信息
     *
     * @param name the name
     * @param des the des
     * @return Resource信息列表
     */
    @Operation(summary = "模糊查询表Resource信息列表", description = "模糊查询表Resource信息列表",
        parameters = {
            @Parameter(name = "name", description = "名称"),
            @Parameter(name = "des", description = "描述")
        }, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Resource.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "模糊查询表Resource信息列表")
    @GetMapping("/resource/like")
    public Result<List<Resource>> getResourceById(@RequestParam String name, @RequestParam String des) {
        List<Resource> resourceList = resourceService.queryResourcesByNameAndDes(name, des);
        return Result.success(resourceList);
    }


    /**
     * 创建Resource
     *
     * @param resource the resource
     * @return Resource信息 result
     */
    @Operation(summary = "创建resource", description = "创建resource",
        parameters = {
            @Parameter(name = "resource", description = "Resource入参对象")
        }, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Resource.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "创建resource")
    @PostMapping("/resource/create")
    public Result<Resource> createResource(@Valid @RequestBody Resource resource) throws Exception {
        Resource result = resourceService.createResource(resource);
        return Result.success(result);
    }

    /**
     * 上传图片
     *
     * @param file the file
     * @return Resource信息 result
     */
    @Operation(summary = "上传图片", description = "上传图片",
        parameters = {
            @Parameter(name = "file", description = "图片")
        }, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Resource.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "上传图片")
    @PostMapping("/resource/upload")
    public Result<Resource> resourceUpload(@RequestParam MultipartFile file) throws Exception {
        // 获取文件的原始名称
        String fileName = StringUtils.cleanPath(java.util.Optional.ofNullable(file.getOriginalFilename()).orElse("image"));

        if(!ImageThumbnailGenerator.validateByImageIO(file)){
            return Result.failed(ExceptionEnum.CM325);
        }
        if(fileName.contains("..")) {
            return Result.failed(ExceptionEnum.CM325);
        }
        // 将文件转为 Base64
        String base64 = ImageThumbnailGenerator.convertToBase64(file);
        Resource resource = new Resource();
        resource.setName(fileName);
        resource.setResourceData(base64);
        resource.setAppId(loginUserContext.getAppId());
        Resource result = resourceService.resourceUpload(resource);
        return Result.success(result);
    }

    /**
     * 批量创建Resource
     *
     * @param resources the resources
     * @return Resource信息 result
     */
    @Operation(summary = "批量创建Resource", description = "批量创建Resource",
        parameters = {
            @Parameter(name = "resources", description = "Resource入参对象")
        }, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Resource.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "批量创建Resource")
    @PostMapping("/resource/create/batch")
    public Result<List<Resource>> createResource(@Valid @RequestBody List<Resource> resources) throws Exception {
        List<Resource> resourceList = resourceService.createBatchResource(resources);
        return Result.success(resourceList);
    }

    /**
     * 修改Resource信息
     *
     * @param id  the id
     * @param resource the resource
     * @return Resource信息 result
     */
    @Operation(summary = "修改单个Resource信息", description = "修改单个Resource信息",
        parameters = {
            @Parameter(name = "id", description = "id"),
            @Parameter(name = "Resource", description = "入参对象")}, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Resource.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "修改单个Resource信息")
    @PutMapping("/resource/update/{id}")
    public Result<Resource> updateResource(@PathVariable Integer id, @Valid @RequestBody Resource resource) {
        resource.setId(id);
        return resourceService.updateResourceById(resource);
    }

    /**
     * 删除Resource信息
     *
     * @param id the id
     * @return resource信息 result
     */
    @Operation(summary = "删除resource信息", description = "删除resource信息",
        parameters = {
            @Parameter(name = "id", description = "Resource主键id")
        }, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Resource.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "删除resource信息")
    @DeleteMapping("/resource/delete/{id}")
    public Result<Resource> deleteResource(@PathVariable Integer id) {
        return resourceService.deleteResourceById(id);
    }

    /**
     * 获取resource信息详情
     *
     * @param id the id
     * @return the result
     */
    @Operation(summary = "获取resource信息详情", description = "获取resource信息详情",
        parameters = {
            @Parameter(name = "id", description = "id")}, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Resource.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "获取resource信息详情")
    @GetMapping("/resource/detail/{id}")
    public Result<Resource> detail(@PathVariable Integer id) {
        return resourceService.queryResourceById(id);
    }

    /**
     * 获取资源
     *
     * @param name the name
     * @return the result
     */
    @Operation(summary = "获取资源", description = "获取资源",
        parameters = {
            @Parameter(name = "name", description = "名称"),
            @Parameter(name = "isResource", description = "isResource"),
        }, responses = {
            @ApiResponse(responseCode = "200", description = "图片流数据",
                content = @Content(mediaType = "image/*", schema = @Schema(implementation = Resource.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "获取资源")
    @GetMapping("/resource/download/{name}")
    public void getResource(@PathVariable String name, HttpServletResponse response) throws Exception {
        // 参数校验
        if (name == null || name.trim().isEmpty()) {
            throw new ServiceException(ExceptionEnum.CM009.getResultCode(), ExceptionEnum.CM009.getResultMsg());
        }

        Resource resource = resourceService.queryResourceByName(name);
        if (resource == null) {
            throw new ServiceException(ExceptionEnum.CM009.getResultCode(), ExceptionEnum.CM009.getResultMsg());
        }
        // 获取图片数据
        String base64Data = Utils.isResource(name) ? resource.getResourceData() : resource.getThumbnailData();
        if (base64Data == null || base64Data.trim().isEmpty()) {
            throw new ServiceException(ExceptionEnum.CM009.getResultCode(), "资源数据为空");
        }

        String cleanBase64 = ImageThumbnailGenerator.extractCleanBase64(base64Data);
        byte[] imageBytes = Base64.getDecoder().decode(cleanBase64);
        // 设置响应头
        String detectedType = ImageThumbnailGenerator.extractContentType(base64Data);
        String encodedFileName = URLEncoder.encode(name, StandardCharsets.UTF_8).replace("+", "%20");

        // 设置必要的HTTP头
        response.setContentType(detectedType);
        response.setContentLength(imageBytes.length);

        // 设置Content-Disposition
        String contentDisposition = Utils.isDownload(name)
                ? "attachment; filename*=UTF-8''" + encodedFileName
                : "inline; filename*=UTF-8''" + encodedFileName;
        response.setHeader("Content-Disposition", contentDisposition);
        // 写入响应体
        try (OutputStream out = response.getOutputStream()) {
            out.write(imageBytes);
            out.flush();
        }
    }
}

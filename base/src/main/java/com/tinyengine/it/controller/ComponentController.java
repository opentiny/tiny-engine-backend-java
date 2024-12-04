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
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.common.log.SystemControllerLog;
import com.tinyengine.it.model.dto.FileResult;
import com.tinyengine.it.service.material.ComponentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 组件api
 *
 * @since 2024-11-13
 */
@Validated
@RestController
@RequestMapping("/material-center/api")
public class ComponentController {
    @Autowired
    private ComponentService componentService;

    /**
     * 上传bunled.json文件处理自定义组件
     *
     * @param file the file
     * @return result
     */
    @Operation(summary = "上传bunled.json文件处理自定义组件", description = "上传bunled.json文件处理自定义组件", parameters = {
            @Parameter(name = "file", description = "文件参数对象")}, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json", schema = @Schema())),
            @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "上传bunled.json文件处理自定义组件")
    @PostMapping("/component/custom/create")
    public Result<FileResult> createCustComponent(@RequestParam MultipartFile file) {
        if (file.isEmpty()) {
            return Result.failed(ExceptionEnum.CM307);
        }
        // 返回插入和更新的条数
        return componentService.readFileAndBulkCreate(file);
    }
}

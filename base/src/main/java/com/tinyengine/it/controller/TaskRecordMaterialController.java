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
import com.tinyengine.it.common.enums.Enums;
import com.tinyengine.it.common.log.SystemControllerLog;
import com.tinyengine.it.model.entity.TaskRecord;
import com.tinyengine.it.service.material.TaskRecordService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 任务记录
 * </p>
 *
 * @author zhangjuncao
 * @since 2024-10-30
 */
@Validated
@RestController
@RequestMapping("/material-center/api")
@Tag(name = "任务")
public class TaskRecordMaterialController {
    @Autowired
    private TaskRecordService taskRecordService;

    /**
     * 根据id查询task信息
     *
     * @param id id
     * @return task信息
     */
    @Operation(summary = "根据id查询task信息",
            description = "根据id查询task信息",
            parameters = {
                    @Parameter(name = "id", description = "任务id")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TaskRecord.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "根据id查询task信息api")
    @GetMapping("/tasks/{id}")
    public Result<TaskRecord> getTaskRecordById(@PathVariable Integer id) {
        TaskRecord taskRecord = taskRecordService.queryTaskRecordById(id);
        return Result.success(taskRecord);
    }

    /**
     * 获取任务状态
     *
     * @param taskTypeId the task type id
     * @param uniqueIds  the unique ids
     * @return the result
     */
    @Operation(summary = "获取任务状态",
            description = "获取任务状态",
            parameters = {
                    @Parameter(name = "taskTypeId", description = "任务类型id"),
                    @Parameter(name = "uniqueIds", description = "uniqueIds")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TaskRecord.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "获取任务状态api")
    @GetMapping("/tasks/status")
    public Result<List<TaskRecord>> getTasksStatus(@RequestParam String taskTypeId, @RequestParam String uniqueIds) {
        // 使用 queries 上下文支持批量查询。若未指定 taskTypeId，则默认查询物料打包任务状态
        int taskTpyeIdTemp = Integer.parseInt(taskTypeId);
        if (taskTpyeIdTemp == 0) {
            // 若未指定 taskTypeId，则默认查询物料打包任务状态
            taskTpyeIdTemp = Enums.TaskType.ASSETS_BUILD.getValue();
        }
        List<List<TaskRecord>> taskRecords = taskRecordService.status(taskTpyeIdTemp, uniqueIds);
        List<TaskRecord> taskRecordList = taskRecords.stream()
                .map(items -> items != null && !items.isEmpty() ? items.get(0) : null)
                .filter(task -> task != null)
                .collect(Collectors.toList());
        return Result.success(taskRecordList);
    }
}

package com.tinyengine.it.controller;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.log.SystemControllerLog;
import com.tinyengine.it.model.entity.TaskRecord;
import com.tinyengine.it.service.app.TaskRecordService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@CrossOrigin
@RequestMapping("/app-center/api")
public class TaskRecordController {
    @Autowired
    TaskRecordService taskRecordService;

    /**
     * 根据id查询task信息
     *
     * @param id
     * @return task信息
     */
    @Operation(summary = "根据id查询task信息",
            description = "根据id查询task信息",
            parameters = {
                    @Parameter(name = "id", description = "task任务主键id")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TaskRecord.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "根据id查询task信息")
    @GetMapping("/tasks/status/{id}")
    public Result<TaskRecord> getTaskRecordById(@PathVariable Integer id) {
        TaskRecord taskRecord = taskRecordService.queryTaskRecordById(id);
        return Result.success(taskRecord);
    }

}

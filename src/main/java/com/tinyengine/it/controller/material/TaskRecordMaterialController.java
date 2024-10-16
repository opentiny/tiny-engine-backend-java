package com.tinyengine.it.controller.material;


import com.tinyengine.it.config.Enums;
import com.tinyengine.it.config.SystemControllerLog;
import com.tinyengine.it.model.dto.Result;
import com.tinyengine.it.model.entity.TaskRecord;
import com.tinyengine.it.service.material.TaskRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 任务记录
 * </p>
 *
 * @author zhangjuncao
 * @since 2024-06-11
 */
@Validated
@RestController
@RequestMapping("/material-center/api")
public class TaskRecordMaterialController {
    @Autowired
    TaskRecordService taskRecordService;

    /**
     * 根据id查询task信息
     *
     * @param id
     * @return task信息
     */
    @GetMapping("/tasks/{id}")
    public Result<TaskRecord> getTaskRecordById(@PathVariable Integer id) {
        TaskRecord taskRecord = taskRecordService.findTaskRecordById(id);
        return Result.success(taskRecord);
    }

    /**
     * 获取任务状态
     *
     * @param taskTypeId
     * @param uniqueIds
     * @return
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
            taskTpyeIdTemp = Enums.E_TaskType.ASSETS_BUILD.getValue();

        }
        List<List<TaskRecord>> taskRecords = taskRecordService.status(taskTpyeIdTemp, uniqueIds);
        List<TaskRecord> taskRecordList = taskRecords.stream()
                .map(items -> items != null && !items.isEmpty() ? items.get(0) : null)
                .filter(task -> task != null)
                .collect(Collectors.toList());
        return Result.success(taskRecordList);
    }

}

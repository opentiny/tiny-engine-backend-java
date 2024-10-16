package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author lyg
 * @since 2024-04-15
 */
@Getter
@Setter
@TableName("task_record")
@Schema(name = "task_record", description = "返回对象")
public class TaskRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "id", description = "taskId")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(name = "teamId", description = "团队id, 默认0")
    private Integer teamId;

    @Schema(name = "taskTypeId", description = "任务类型: 1 ASSETS_BUILD / 2 APP_BUILD / 3 PLATFORM_BUILD / 4 VSCODE_PLUGIN_BUILD/5 BLOCK_BUILD")
    private Integer taskTypeId;

    @Schema(name = "uniqueId", description = "构建资源id")
    private Integer uniqueId;

    @Schema(name = "taskName", description = "构建任务名称")
    private String taskName;

    @Schema(name = "", description = "任务状态：0 init / 1 running / 2 stopped / 3 finished")
    private Integer taskStatus;

    @Schema(name = "taskResult", description = "当前执行进度结果信息")
    private String taskResult;

    @Schema(name = "progress", description = "当前进行的子任务名")
    private String progress;

    @TableField(fill = FieldFill.INSERT)
    @JsonProperty("created_at")
    @Schema(name = "created_at", type = "LocalDateTime", description = "创建时间")
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonProperty("updated_at")
    @Schema(name = "updated_at", type = "LocalDateTime", description = "更新时间")
    private LocalDateTime updatedAt;

    @Schema(name = "createdBy", description = "任务创建人id")
    @TableField(value = "createdBy", fill = FieldFill.INSERT)
    private Integer createdBy;

    @Schema(name = "updatedBy", description = "任务更新人id")
    @TableField(value = "updatedBy", fill = FieldFill.INSERT)
    private Integer updatedBy;

    @Schema(name = "ratio", description = "无用字段")
    private Integer ratio;

    @Schema(name = "progressPercent", description = "构建进度百分比数")
    @JsonProperty("progress_percent")
    private Integer progressPercent;

    @Schema(name = "indicator", description = "构建指标")
    private String indicator;


}

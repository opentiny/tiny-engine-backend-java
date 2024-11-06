package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tinyengine.it.common.base.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 构建任务表
 * </p>
 *
 * @author zhangjuncao
 * @since 2024-10-17
 */
@Getter
@Setter
@TableName("t_task_record")
@Schema(name = "TaskRecord", description = "构建任务表")
public class TaskRecord extends BaseEntity {
    @Schema(name = "teamId", description = "团队id, 默认0")
    private Integer teamId;

    @Schema(name = "taskTypeId", description = "任务类型: 1 ASSETS_BUILD / "
            + "2 APP_BUILD / 3 PLATFORM_BUILD / 4 VSCODPLUGIN_BUILD/5 BLOCK_BUILD")
    private Integer taskTypeId;

    @Schema(name = "uniqueId", description = "构建资源id")
    private Integer uniqueId;

    @Schema(name = "taskName", description = "构建任务名称")
    private String taskName;

    @Schema(name = "taskStatus", description = "任务状态：0 init / 1 running / 2 stopped / 3 finished")
    private Integer taskStatus;

    @Schema(name = "taskResult", description = "当前执行进度结果信息")
    private String taskResult;

    @Schema(name = "progress", description = "当前进行的子任务名")
    private String progress;

    @Schema(name = "ratio", description = "无用字段")
    private Integer ratio;

    @Schema(name = "progressPercent", description = "构建进度百分比数")
    private Integer progressPercent;

    @Schema(name = "indicator", description = "构建指标")
    private String indicator;
}

package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
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
public class TaskRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(name= "id", description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(name= "teamId", description = "团队id, 默认0")
    private Integer teamId;

    @Schema(name= "taskTypeId", description = "任务类型: 1 ASSETS_BUILD / 2 APP_BUILD / 3 PLATFORM_BUILD / 4 VSCODE_PLUGIN_BUILD/5 BLOCK_BUILD")
    private Integer taskTypeId;

    @Schema(name= "uniqueId", description = "构建资源id")
    private Integer uniqueId;

    @Schema(name= "taskName", description = "构建任务名称")
    private String taskName;

    @Schema(name= "taskStatus", description = "任务状态：0 init / 1 running / 2 stopped / 3 finished")
    private Integer taskStatus;

    @Schema(name= "taskResult", description = "当前执行进度结果信息")
    private String taskResult;

    @Schema(name= "progress", description = "当前进行的子任务名")
    private String progress;

    @Schema(name= "ratio", description = "无用字段")
    private Integer ratio;

    @Schema(name= "progressPercent", description = "构建进度百分比数")
    private Integer progressPercent;

    @Schema(name= "indicator", description = "构建指标")
    private String indicator;

    @Schema(name= "createdBy", description = "创建人")
    private String createdBy;

    @Schema(name= "lastUpdatedBy", description = "最后修改人")
    private String lastUpdatedBy;

    @Schema(name= "createdTime", description = "创建时间")
    private LocalDateTime createdTime;

    @Schema(name= "lastUpdatedTime", description = "更新时间")
    private LocalDateTime lastUpdatedTime;

    @Schema(name= "tenantId", description = "租户ID")
    private String tenantId;

    @Schema(name= "siteId", description = "站点ID")
    private String siteId;

}

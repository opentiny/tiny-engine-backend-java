package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinyengine.it.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * <p>
 * 数据源表
 * </p>
 *
 * @author lu-yg
 * @since 2024-10-17
 */
@Getter
@Setter
@TableName("t_datasource")
@Schema(name = "Datasource", description = "数据源表")
public class Datasource extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @Schema(name = "id", description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(name = "name", description = "数据源名称")
    private String name;

    @Schema(name = "data", description = "数据源内容")
    private String data;

    @Schema(name = "tpl", description = "*暂不清楚*")
    private Long tpl;

    @Schema(name = "appId", description = "关联应用id")
    private Long appId;

    @Schema(name = "description", description = "描述")
    private String description;

    @Schema(name = "createdBy", description = "创建人")
    @JsonProperty("created_by")
    private String createdBy;

    @Schema(name = "lastUpdatedBy", description = "最后修改人")
    @JsonProperty("last_updated_by")
    private String lastUpdatedBy;

    @Schema(name = "createdTime", description = "创建时间")
    @TableField(fill = FieldFill.INSERT)

    private LocalDateTime createdTime;

    @Schema(name = "lastUpdatedTime", description = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime lastUpdatedTime;

    @Schema(name = "tenantId", description = "租户id")
    private String tenantId;

    @Schema(name = "siteId", description = "站点id")
    private String siteId;

}

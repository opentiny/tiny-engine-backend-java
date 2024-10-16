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
 * @author zhangjuncao
 * @since 2024-07-23
 */
@Getter
@Setter
@TableName("source_tpls")
@Schema(name = "SourceTpls对象", description = "主键id")
public class SourceTpls implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "id", description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(name = "name", description = "模板名称")
    private String name;

    @Schema(name = "type", description = "模板类型")
    private String type;

    @Schema(name = "desc", description = "模板描述")
    private String desc;

    @Schema(name = "data", description = "内容")
    private String data;

    @Schema(name = "platform", description = "关联设计器id")
    private Integer platform;

    @TableField(fill = FieldFill.INSERT)
    @JsonProperty("created_at")
    @Schema(name = "created_at", type = "LocalDateTime", description = "创建时间")
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonProperty("updated_at")
    @Schema(name = "updated_at", type = "LocalDateTime", description = "更新时间")
    private LocalDateTime updatedAt;

    @Schema(name = "createdBy", description = "创建人")
    @TableField(value = "createdBy", fill = FieldFill.INSERT)
    private Integer createdBy;

    @Schema(name = "updatedBy", description = "更新人")
    @TableField(value = "updatedBy", fill = FieldFill.INSERT)
    private Integer updatedBy;


}

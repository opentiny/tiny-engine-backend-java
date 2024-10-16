package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

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
@Schema(name = "Sources对象", description = "返回对象")
@TableName(value = "sources")
public class Sources implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "id", description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(name = "name", description = "数据源名称")
    private String name;

    @Schema(name = "data", description = "数据源内容")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> data;

    @Schema(name = "tpl", description = "*暂不清楚*")
    private Long tpl;

    @Schema(name = "app", description = "关联应用id")
    private Long app;

    @Schema(name = "desc", description = "描述")
    private String desc;

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

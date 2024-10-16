package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author zhangjuncao
 * @since 2024-04-21
 */
@Getter
@Setter
@TableName("material_histories")
@Schema(name = "MaterialHistories对象", description = "返回对象")
public class MaterialHistories implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(name = "id", description = "物料历史id")
    private Integer id;

    @Schema(name = "name", description = "物料历史名称")
    private String name;

    @JsonProperty("npm_name")
    @Schema(name = "nameName", description = "物料历史npm名称")
    private String npmName;

    @Schema(name = "version", description = "物料历史版本")
    private String version;

    @Schema(name = "framework", description = "物料历史技术栈")
    private String framework;

    @Schema(name = "description", description = "物料历史描述")
    private String description;

    @Schema(name = "material", description = "物料id")
    private Integer material;

    @Schema(name = "content", description = "物料内容")
    private String content;

    @Schema(name = "createdBy", description = "创建人")
    @TableField(value = "createdBy", fill = FieldFill.INSERT)
    private Integer createdBy;

    @Schema(name = "updatedBy", description = "更新人")
    @TableField(value = "updatedBy", fill = FieldFill.INSERT)
    private Integer updatedBy;

    @JsonProperty("assets_url")
    @TableField(typeHandler = JacksonTypeHandler.class)
    @Schema(name = "assets_url", description = "资源地址")
    private Map<String, Object> assetsUrl;

    @Schema(name = "components", description = "组件")
    private List<UserComponents> components;

    @TableField(fill = FieldFill.INSERT)
    @JsonProperty("created_at")
    @Schema(name = "created_at", type = "LocalDateTime", description = "创建时间")
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonProperty("updated_at")
    @Schema(name = "updated_at", type = "LocalDateTime", description = "更新时间")
    private LocalDateTime updatedAt;


}

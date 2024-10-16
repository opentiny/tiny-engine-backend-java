package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinyengine.it.utils.ListTypeHandler;
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
 * @author lyg
 * @since 2024-08-19
 */
@Getter
@Setter
@TableName("user_components")
@Schema(name = "user_components对象", description = "返回对象")
public class UserComponents implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(name = "id", description = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(name = "version", description = "版本")
    private String version;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @Schema(name = "name", description = "名称")
    private Map<String, String> name;

    @Schema(name = "component", description = "组件")
    private String component;

    @Schema(name = "icon", description = "图标")
    private String icon;

    @Schema(name = "description", description = "描述")
    private String description;

    @JsonProperty("doc_url")
    @Schema(name = "doc_url", description = "文档链接")
    private String docUrl;

    @Schema(name = "screenshot", description = "缩略图")
    private String screenshot;

    @Schema(name = "tags", description = "标签")
    private String tags;

    @Schema(name = "keywords", description = "关键字")
    private String keywords;

    @JsonProperty("dev_mode")
    @Schema(name = "dev_mode", description = "研发模式")
    private String devMode;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @Schema(name = "npm", description = "npm信息")
    private Map<String, Object> npm;

    @Schema(name = "group", description = "分组")
    private String group;

    @Schema(name = "category", description = "分类")
    private String category;

    @Schema(name = "priority", description = "排序")
    private Integer priority;

    @TableField(typeHandler = ListTypeHandler.class)
    @Schema(name = "snippets", description = "schema片段")
    private List<Map<String, Object>> snippets;

    @Schema(name = "schema_fragment", description = "schema片段")
    @JsonProperty("schema_fragment")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> schemaFragment;

    @Schema(name = "configure", description = "配置信息")
    @JsonProperty("configure")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> configure;


    @Schema(name = "createdBy", type = "Integer", description = "创建人")
    @TableField(value = "createdBy", fill = FieldFill.INSERT)
    private Integer createdBy;

    @Schema(name = "updatedBy", type = "Integer", description = "更新人")
    @TableField(value = "updatedBy", fill = FieldFill.INSERT)
    private Integer updatedBy;

    @TableField(fill = FieldFill.INSERT)
    @JsonProperty("created_at")
    @Schema(name = "created_at", type = "LocalDateTime", description = "创建时间")
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonProperty("updated_at")
    @Schema(name = "updated_at", type = "LocalDateTime", description = "更新时间")
    private LocalDateTime updatedAt;

    @JsonProperty("public")
    @Schema(name = "public", description = "公开类型")
    private Integer isPublic;

    @Schema(name = "framework", description = "技术栈")
    private String framework;

    @Schema(name = "isOfficial", description = "标识官方组件")
    private Boolean isOfficial;

    @Schema(name = "isDefault", description = "标识默认组件")
    private Boolean isDefault;

    @JsonProperty("tiny_reserved")
    @Schema(name = "tiny_reserved", description = "是否tiny自有")
    private Boolean tinyReserved;

    @JsonProperty("component_metadata")
    @TableField(typeHandler = JacksonTypeHandler.class)
    @Schema(name = "component_metadata", description = "属性信息")
    private Map<String, Object> componentMetadata;

    @Schema(name = "library", description = "组件库")
    private Integer library;
}

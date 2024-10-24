package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinyengine.it.common.base.BaseEntity;
import com.tinyengine.it.common.utils.ListTypeHandler;
import com.tinyengine.it.common.utils.MapTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 组件表
 * </p>
 *
 * @author lu-yg
 * @since 2024-10-17
 */
@Getter
@Setter
@TableName("t_component")
@Schema(name = "Component", description = "组件表")
public class Component extends BaseEntity {
    @Schema(name = "version", description = "版本")
    private String version;

    @TableField(typeHandler = MapTypeHandler.class)
    @Schema(name = "name", description = "名称")
    private Map<String, String> name;

    @Schema(name = "component", description = "组件")
    private String component;

    @Schema(name = "icon", description = "组件图标")
    private String icon;

    @Schema(name = "description", description = "描述")
    private String description;

    @Schema(name = "docUrl", description = "文档链接")
    private String docUrl;

    @Schema(name = "screenshot", description = "缩略图")
    private String screenshot;

    @Schema(name = "tags", description = "标签")
    private String tags;

    @Schema(name = "keywords", description = "关键字")
    private String keywords;

    @Schema(name = "devMode", description = "研发模式")
    private String devMode;

    @TableField(typeHandler = MapTypeHandler.class)
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

    @Schema(name = "schemaFragment", description = "schema片段")
    private String schemaFragment;

    @Schema(name = "configure", description = "配置信息")
    @JsonProperty("configure")
    @TableField(typeHandler = MapTypeHandler.class)
    private Map<String, Object> configure;

    @JsonProperty("public")
    @Schema(name = "public", description = "公开状态：0，1，2")
    private Integer publicStatus;

    @Schema(name = "framework", description = "技术栈")
    private String framework;

    @Schema(name = "isOfficial", description = "标识官方组件")
    private Boolean isOfficial;

    @Schema(name = "isDefault", description = "标识默认组件")
    private Boolean isDefault;

    @Schema(name = "tinyReserved", description = "是否tiny自有")
    private Boolean tinyReserved;

    @JsonProperty("component_metadata")
    @TableField(typeHandler = MapTypeHandler.class)
    @Schema(name = "component_metadata", description = "属性信息")
    private Map<String, Object> componentMetadata;

    @Schema(name = "libraryId", description = "关联组件库id")
    private Integer libraryId;
}

package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

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
public class Component implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(name= "version", description = "版本")
    private String version;

    @Schema(name= "name", description = "名称")
    private String name;

    @Schema(name= "nameEn", description = "英文名称")
    private String nameEn;

    @Schema(name= "icon", description = "组件图标")
    private String icon;

    @Schema(name= "description", description = "描述")
    private String description;

    @Schema(name= "docUrl", description = "文档链接")
    private String docUrl;

    @Schema(name= "screenshot", description = "缩略图")
    private String screenshot;

    @Schema(name= "tags", description = "标签")
    private String tags;

    @Schema(name= "keywords", description = "关键字")
    private String keywords;

    @Schema(name= "devMode", description = "研发模式")
    private String devMode;

    @Schema(name= "npm", description = "npm属性对象")
    private String npm;

    @Schema(name= "group", description = "分组")
    private String group;

    @Schema(name= "category", description = "分类")
    private String category;

    @Schema(name= "priority", description = "排序")
    private Integer priority;

    @Schema(name= "snippets", description = "schema片段")
    private String snippets;

    @Schema(name= "schemaFragment", description = "schema片段")
    private String schemaFragment;

    @Schema(name= "configure", description = "配置信息")
    private String configure;

    @JsonProperty("public")
    @Schema(name= "public", description = "公开状态：0，1，2")
    private Integer isPublic;

    @Schema(name= "framework", description = "技术栈")
    private String framework;

    @Schema(name= "isOfficial", description = "标识官方组件")
    private Boolean isOfficial;

    @Schema(name= "isDefault", description = "标识默认组件")
    private Boolean isDefault;

    @Schema(name= "tinyReserved", description = "是否tiny自有")
    private Boolean tinyReserved;

    @Schema(name= "componentMetadata", description = "属性信息")
    private String componentMetadata;

    @Schema(name= "createdBy", description = "创建人")
    private String createdBy;

    @Schema(name= "lastUpdatedBy", description = "最后修改人")
    private String lastUpdatedBy;

    @Schema(name= "createdTime", description = "创建时间")
    private LocalDateTime createdTime;

    @Schema(name= "lastUpdatedTime", description = "更新时间")
    private LocalDateTime lastUpdatedTime;

    @Schema(name= "tenantId", description = "租户id")
    private String tenantId;

    @Schema(name= "siteId", description = "站点id")
    private String siteId;

}

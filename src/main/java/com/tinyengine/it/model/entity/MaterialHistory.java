package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinyengine.it.utils.MapTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 物料历史表
 * </p>
 *
 * @author lu-yg
 * @since 2024-10-17
 */
@Getter
@Setter
@TableName("t_material_history")
@Schema(name = "MaterialHistory", description = "物料历史表")
public class MaterialHistory implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(name= "refId", description = "关联主表id")
    private Integer refId;

    @Schema(name= "version", description = "版本")
    private String version;

    @Schema(name= "content", description = "物料内容")
    @TableField(typeHandler = MapTypeHandler.class)
    private Map<String, Object> content;

    @Schema(name= "name", description = "物料名称")
    private String name;

    @Schema(name= "npmName", description = "npm包名")
    private String npmName;

    @Schema(name= "framework", description = "技术栈")
    private String framework;

    @JsonProperty("assets_url")
    @TableField(typeHandler = MapTypeHandler.class)
    @Schema(name = "assets_url", description = "资源地址")
    private Map<String, Object> assetsUrl;

    @Schema(name= "imageUrl", description = "封面图片地址")
    private String imageUrl;

    @Schema(name= "description", description = "描述")
    private String description;

    @Schema(name= "materialSize", description = "物料包大小")
    private String materialSize;

    @Schema(name= "tgzUrl", description = "物料压缩包地址")
    private String tgzUrl;

    @Schema(name= "unzipTgzRootPathUrl", description = "物料压缩包解压后根地址")
    private String unzipTgzRootPathUrl;

    @Schema(name= "unzipTgzFiles", description = "物料压缩包解压后文件地址")
    private String unzipTgzFiles;

    @Schema(name= "createdBy", description = "创建人")
    @TableField(value = "createdBy", fill = FieldFill.INSERT)
    private String createdBy;

    @Schema(name= "lastUpdatedBy", description = "最后修改人")
    @TableField(value = "lastUpdatedBy", fill = FieldFill.INSERT_UPDATE)
    private String lastUpdatedBy;

    @Schema(name= "createdTime", description = "创建时间")
    @TableField(value = "createdTime", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    @Schema(name= "lastUpdatedTime", description = "更新时间")
    @TableField(value = "lastUpdatedTime", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime lastUpdatedTime;

    @Schema(name= "tenantId", description = "租户id")
    private String tenantId;

    @Schema(name= "siteId", description = "站点id")
    private String siteId;

    @Schema(name = "components", description = "组件")
    private List<Component> components;

}

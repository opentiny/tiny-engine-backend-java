package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinyengine.it.utils.ListTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author lyg
 * @since 2024-05-05
 */
@Getter
@Setter
@Schema(name = "Blocks", description = "通用返回对象")
public class Blocks implements Serializable {

    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.AUTO)
    @Schema(name = "id", type = "Integer", description = "主键id")
    private Integer id;

    @Schema(name = "label", type = "String", description = "区块编码")
    private String label;

    @Schema(name = "framework", type = "String", description = "技术栈")
    private String framework;

    @TableField(typeHandler = JacksonTypeHandler.class)

    @Schema(name = "content", type = "Map<String, Object>", description = "区块内容")
    private Map<String, Object> content;

    @TableField(fill = FieldFill.INSERT)
    @JsonProperty("created_at")
    @Schema(name = "created_at", type = "LocalDateTime", description = "创建时间")
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonProperty("updated_at")
    @Schema(name = "updated_at", type = "LocalDateTime", description = "更新时间")
    private LocalDateTime updatedAt;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @Schema(name = "assets", type = "Map<String, Object>", description = "构建资源")
    private Map<String, Object> assets;

    @Schema(name = "createdBy", type = "Integer", description = "创建人")
    @TableField(value = "createdBy", fill = FieldFill.INSERT)
    private Integer createdBy;

    @Schema(name = "updatedBy", type = "Integer", description = "更新人")
    @TableField(value = "updatedBy", fill = FieldFill.INSERT)
    private Integer updatedBy;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @JsonProperty("last_build_info")
    @Schema(name = "lastBuildInfo", type = "Map<String, Object>", description = "最新一次构建信息")
    private Map<String, Object> lastBuildInfo;

    @Schema(name = "description", type = "String", description = "描述")
    private String description;

    @TableField(typeHandler = ListTypeHandler.class)
    @Schema(name = "tags", type = "List<String>", description = "标签")
    private List<String> tags;

    @JsonProperty("current_history")
    @Schema(name = "current_history", type = "Integer", description = "当前历史记录")
    private Integer currentHistory;

    @Schema(name = "screenshot", type = "String", description = "截屏")
    private String screenshot;

    @Schema(name = "path", type = "String", description = "区块路径")
    private String path;

    @Schema(name = "occupier", type = "Integer", description = "当前锁定人")
    private Integer occupier;

    @TableField(value = "isOfficial")
    @Schema(name = "isOfficial", type = "Boolean", description = "是否是官方")
    private Boolean isOfficial;

    @TableField(value = "public")
    @JsonProperty("public")
    @Schema(name = "public", type = "Integer", description = "公开状态：0，1，")
    private Integer isPublic;

    @TableField(value = "isDefault")
    @Schema(name = "isDefault", type = "Boolean", description = "是否是默认")
    private Boolean isDefault;

    @JsonProperty("tiny_reserved")
    @Schema(name = "tiny_reserved", type = "Boolean", description = "是否是tiny专有")
    private Boolean tinyReserved;

    @Schema(name = "author", type = "Integer", description = "扩展字段暂时无用")
    private Integer author;

    @JsonProperty("name_cn")
    @Schema(name = "name_cn", type = "String", description = "区块名称")
    private String nameCn;

    @JsonProperty("npm_name")
    @Schema(name = "npm_name", type = "String", description = "npm包名")
    private String npmName;

    @JsonProperty("created_app")
    @Schema(name = "created_app", type = "Integer", description = "创建区块时所在appId")
    private Integer createdApp;

    @JsonProperty("content_blocks")
    @Schema(name = "content_blocks", type = "String", description = "扩展字段暂时无用")
    private String contentBlocks;

    @TableField(exist = false)
    private List<Object> public_scope_tenants = new ArrayList<>();

    @TableField(exist = false)
    private Integer histories_length = 0;

    @TableField(exist = false)
    private Boolean is_published;

    public Blocks() {
    }


    public Blocks(Map<String, Object> content, String label) {
    }
}

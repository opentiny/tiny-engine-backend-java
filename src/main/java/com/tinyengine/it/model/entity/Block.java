package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 区块表
 * </p>
 *
 * @author zhangjuncao
 * @since 2024-10-17
 */
@Getter
@Setter
@TableName("t_block")
@Schema(name = "Block", description = "区块表")
public class Block implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(name = "id", description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(name = "label", description = "区块显示名称，严格大小写格式")
    private String label;

    @Schema(name = "name", description = "区块名称")
    private String name;

    @Schema(name = "framework", description = "技术栈")
    private String framework;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @Schema(name = "content", description = "区块内容")
    private Map<String, Object> content;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @Schema(name = "assets", description = "构建资源")
    private Map<String, Object> assets;

    @JsonProperty("last_build_info")
    @TableField(typeHandler = JacksonTypeHandler.class)
    @Schema(name = "lastBuildInfo", description = "最新一次构建信息")
    private Map<String, Object> lastBuildInfo;

    @Schema(name = "description", description = "描述")
    private String description;

    @Schema(name = "tags", description = "标签")
    private String tags;

    @Schema(name = "latestVersion", description = "当前历史记录表最新版本")
    @JsonProperty("latest_version")
    private String latestVersion;

    @Schema(name = "latestHistoryId", description = "当前历史记录表ID")
    @JsonProperty("latest_history_id")
    private Integer latestHistoryId;

    @Schema(name = "screenshot", description = "截屏")
    private String screenshot;

    @Schema(name = "path", description = "区块路径")
    private String path;

    @Schema(name = "occupierBy", description = "当前锁定人id")
    private String occupierBy;

    @Schema(name = "isOfficial", description = "是否是官方")
    @JsonProperty("is_official")
    private Boolean isOfficial;

    @Schema(name = "public", description = "公开状态：0,1,2")
    @JsonProperty("public")
    private Integer publicStatus;

    @Schema(name = "isDefault", description = "是否是默认")
    @JsonProperty("is_default")
    private Boolean isDefault;

    @Schema(name = "tinyReserved", description = "是否是tiny专有")
    @JsonProperty("tiny_reserved")
    private Boolean tinyReserved;

    @Schema(name = "npmName", description = "npm包名")
    @JsonProperty("npm_name")
    private String npmName;

    @Schema(name = "i18n", description = "国际化内容")
    private String i18n;

    @Schema(name = "platformId", description = "设计器ID")
    @JsonProperty("platform_id")
    private Integer platformId;

    @Schema(name = "appId", description = "创建区块时所在appId")
    @JsonProperty("app_id")
    private Integer appId;

    @Schema(name = "contentBlocks", description = "*设计预留字段用途*")
    @JsonProperty("content_blocks")
    private String contentBlocks;

    @Schema(name = "blockGroupId", description = "区块分组id,关联t_block_group表id")
    @JsonProperty("block_group_id")
    private Integer blockGroupId;

    @Schema(name = "createdBy", description = "创建人")
    @JsonProperty("created_by")
    private String createdBy;

    @Schema(name = "lastUpdatedBy", description = "最后修改人")
    @JsonProperty("last_updated_by")
    private String lastUpdatedBy;

    @Schema(name = "createdTime", description = "创建时间")
    @JsonProperty("created_time")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    @Schema(name = "lastUpdatedTime", description = "更新时间")
    @JsonProperty("last_updated_time")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime lastUpdatedTime;

    @Schema(name = "tenantId", description = "租户ID")
    @JsonProperty("tenant_id")
    private String tenantId;

    @Schema(name = "siteId", description = "站点ID")
    @JsonProperty("site_id")
    private String siteId;

}

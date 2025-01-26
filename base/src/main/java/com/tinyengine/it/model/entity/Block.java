/**
 * Copyright (c) 2023 - present TinyEngine Authors.
 * Copyright (c) 2023 - present Huawei Cloud Computing Technologies Co., Ltd.
 *
 * Use of this source code is governed by an MIT-style license.
 *
 * THE OPEN SOURCE SOFTWARE IN THIS PRODUCT IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL,
 * BUT WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS FOR
 * A PARTICULAR PURPOSE. SEE THE APPLICABLE LICENSES FOR MORE DETAILS.
 *
 */

package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinyengine.it.common.base.BaseEntity;
import com.tinyengine.it.common.handler.ListTypeHandler;
import com.tinyengine.it.common.handler.MapTypeHandler;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
public class Block extends BaseEntity {
    @Schema(name = "label", description = "区块显示名称，严格大小写格式")
    private String label;

    @Schema(name = "name", description = "区块名称")
    @JsonProperty("name_cn")
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
    @TableField(typeHandler = ListTypeHandler.class)
    private List<String> tags;

    @Schema(name = "latestVersion", description = "当前历史记录表最新版本")
    @JsonProperty("version")
    private String latestVersion;

    @Schema(name = "latestHistoryId", description = "当前历史记录表ID")
    @JsonProperty("current_history")
    private Integer latestHistoryId;

    @Schema(name = "screenshot", description = "截屏")
    private String screenshot;

    @Schema(name = "path", description = "区块路径")
    private String path;

    @Schema(name = "occupierBy", description = "当前锁定人id")
    @JsonProperty("occupier")
    private String occupierBy;

    @Schema(name = "isOfficial", description = "是否是官方")
    @JsonProperty("is_official")
    private Boolean isOfficial;

    @Schema(name = "public", description = "公开状态：0,1,2")
    @JsonProperty("public")
    @TableField("public")
    private Integer publicStatus;

    @Schema(name = "isDefault", description = "是否是默认")
    @JsonProperty("is_default")
    private Boolean isDefault;

    @Schema(name = "tinyReserved", description = "是否是tiny专有")
    @JsonProperty("tiny_reserved")
    @TableField("tiny_reserved")
    private Boolean isTinyReserved;

    @Schema(name = "npmName", description = "npm包名")
    @JsonProperty("npm_name")
    private String npmName;

    @Schema(name = "i18n", description = "国际化内容")
    @TableField(typeHandler = MapTypeHandler.class)
    private Map<String, Map<String, String>> i18n;

    @Schema(name = "platformId", description = "设计器ID")
    @JsonProperty("platform_id")
    private Integer platformId;

    @Schema(name = "appId", description = "创建区块时所在appId")
    @JsonProperty("created_app")
    private Integer appId;

    @Schema(name = "contentBlocks", description = "*设计预留字段用途*")
    @JsonProperty("content_blocks")
    private String contentBlocks;

    @TableField(exist = false)
    @JsonProperty("public_scope_tenants")
    private List<Object> publicScopeTenants = new ArrayList<>();

    @TableField(exist = false)
    @JsonProperty("histories_length")
    private Integer historiesLength = 0;

    @TableField(exist = false)
    @JsonProperty("is_published")
    private Boolean isPublished;

    @TableField(exist = false)
    @JsonProperty("current_version")
    private String currentVersion;

    public Block(Map<String, Object> content, String label) {
        super();
    }

    public Block() {
        super();
    }
}

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

package com.tinyengine.it.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinyengine.it.common.base.BaseEntity;
import com.tinyengine.it.common.handler.ListTypeHandler;
import com.tinyengine.it.common.handler.MapTypeHandler;
import com.tinyengine.it.model.entity.BlockHistory;
import com.tinyengine.it.model.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 区块param
 * </p>
 * This class serves as a parameter object for block creation and updates,
 * working in conjunction with {@link BlockDto}. While BlockDto represents
 * the full block data model, BlockParam is specifically designed for
 * handling input parameters during block operations.
 * @author lu-yg
 * @since 2024-01-27
 */
@Data
public class BlockParam extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Schema(name = "label", description = "区块显示名称，严格大小写格式")
    private String label;

    @Schema(name = "name", description = "区块名称")
    @JsonProperty("name_cn")
    private String name;

    @Schema(name = "framework", description = "技术栈")
    private String framework;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @Schema(name = "assets", description = "构建资源")
    private Map<String, Object> assets;

    @JsonProperty("last_build_info")
    @TableField(typeHandler = JacksonTypeHandler.class)
    @Schema(name = "lastBuildInfo", description = "最新一次构建信息")
    private Map<String, Object> lastBuildInfo;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @Schema(name = "content", description = "区块内容")
    private Map<String, Object> content;

    @Schema(name = "description", description = "描述")
    private String description;

    @Schema(name = "tags", description = "标签")
    @TableField(typeHandler = ListTypeHandler.class)
    private List<String> tags;

    @Schema(name = "latestHistoryId", description = "当前历史记录表ID")
    @JsonProperty("current_history")
    private BlockHistory latestHistoryId;

    @Schema(name = "screenshot", description = "截屏")
    private String screenshot;

    @Schema(name = "path", description = "区块路径")
    private String path;

    @Schema(name = "latestVersion", description = "当前历史记录表最新版本")
    @JsonProperty("version")
    private String latestVersion;

    @Schema(name = "occupierId", description = "当前锁定人id")
    private String occupierId;

    @Schema(name = "isOfficial", description = "是否是官方")
    @JsonProperty("is_official")
    private Boolean isOfficial;

    @Schema(name = "isDefault", description = "是否是默认")
    @JsonProperty("is_default")
    private Boolean isDefault;

    @Schema(name = "tinyReserved", description = "是否是tiny专有")
    @JsonProperty("tiny_reserved")
    private Boolean isTinyReserved;

    @Schema(name = "npmName", description = "npm包名")
    @JsonProperty("npm_name")
    private String npmName;

    @Schema(name = "public", description = "公开状态：0,1,2")
    @JsonProperty("public")
    private Integer publicStatus;

    @Schema(name = "i18n", description = "国际化")
    @TableField(typeHandler = MapTypeHandler.class)
    private Map<String, Map<String, String>> i18n;

    @Schema(name = "appId", description = "创建区块时所在appId")
    @JsonProperty("created_app")
    private Integer appId;

    @Schema(name = "contentBlocks", description = "*设计预留字段用途*")
    @JsonProperty("content_blocks")
    private String contentBlocks;

    @Schema(name = "platformId", description = "设计器ID")
    @JsonProperty("platform_id")
    private Integer platformId;

    @JsonProperty("occupier")
    @Schema(name = "occupierBy", description = "当前锁定人")
    private User occupier;

    @TableField(exist = false)
    @JsonProperty("public_scope_tenants")
    private List<Object> publicScopeTenants = new ArrayList<>();

    @TableField(exist = false)
    @Schema(name = "groups", type = " List<BlockGroup>", description = "区块分组")
    private List<Integer> groups = new ArrayList<>();

    @TableField(exist = false)
    @Schema(name = "histories", type = " List<BlockHistory>", description = "区块历史")
    private List<BlockHistory> histories = new ArrayList<>();

    @TableField(exist = false)
    @JsonProperty("histories_length")
    private Integer historiesLength = 0;

    @TableField(exist = false)
    @JsonProperty("is_published")
    private Boolean isPublished;

    @TableField(exist = false)
    @JsonProperty("current_version")
    private String currentVersion;
    @JsonProperty("public")
    public Integer getPublic() {
        if (this.publicStatus != null && !Arrays.asList(0, 1, 2).contains(this.publicStatus)) {
            throw new IllegalStateException("Invalid public status value: " + this.publicStatus);
        }
        return this.publicStatus;
    }
}


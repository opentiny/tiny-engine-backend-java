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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinyengine.it.common.base.BaseEntity;
import com.tinyengine.it.common.handler.ListTypeHandler;
import com.tinyengine.it.common.handler.MapTypeHandler;
import com.tinyengine.it.model.dto.BlockVersionDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 页面表
 * </p>
 *
 * @author zhangjuncao
 * @since 2024-10-17
 */
@Getter
@Setter
@TableName("t_page")
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(name = "Page", description = "页面表")
public class Page extends BaseEntity {
    @Schema(name = "name", description = "名称")
    private String name;

    @Schema(name = "app", description = "关联appId")
    private Integer app;

    @Schema(name = "route", description = "访问路由")
    private String route;

    @Schema(name = "pageContent", description = "页面内容")
    @JsonProperty("page_content")
    @TableField(typeHandler = MapTypeHandler.class)
    private Map<String, Object> pageContent;

    @Schema(name = "isBody", description = "根元素是否是body")
    private Boolean isBody;

    @Schema(name = "parentId", description = "父文件夹id")
    private String parentId;

    @Schema(name = "group", description = "分组")
    private String group;

    @Schema(name = "depth", description = "页面/文件夹深度，更改层级时服务端校验用（校验可有可无）")
    private Integer depth;

    @Schema(name = "isPage", description = "是否为页面：分为页面和文件夹")
    private Boolean isPage;

    @Schema(name = "occupierBy", description = "当前检出者id")
    private String occupierBy;

    @Schema(name = "isDefault", description = "是否是默认页面")
    private Boolean isDefault;

    @Schema(name = "contentBlocks", description = "*设计预留字段*")
    @TableField(typeHandler = ListTypeHandler.class)
    private List<BlockVersionDto> contentBlocks;

    @Schema(name = "latestVersion", description = "当前历史记录表最新版本")
    private String latestVersion;

    @Schema(name = "latestHistoryId", description = "当前历史记录表ID")
    private Integer latestHistoryId;

    @Schema(name = "occupier", description = "当前检出者")
    private User occupier;

    @TableField(exist = false)
    private Boolean isHome;

    @TableField(exist = false)
    private Map<String, List<String>> assets;

    @TableField(exist = false)
    private String message;
}

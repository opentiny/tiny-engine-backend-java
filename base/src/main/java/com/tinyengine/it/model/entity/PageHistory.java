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

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinyengine.it.common.handler.ListTypeHandler;
import com.tinyengine.it.common.handler.MapTypeHandler;
import com.tinyengine.it.model.dto.BlockVersionDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 页面历史
 * </p>
 *
 * @author lu -yg
 * @since 2024-10-17
 */
@Getter
@Setter
@TableName("t_page_history")
@Schema(name = "PageHistory", description = "页面历史")
public class PageHistory {
    @Schema(name = "id", description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(name = "name", description = "名称")
    private String name;

    @Schema(name = "page", description = "关联page表Id")
    private Integer page;

    @Schema(name = "version", description = "版本")
    private String version;

    @Schema(name = "app", description = "关联app表Id")
    private Integer app;

    @Schema(name = "route", description = "页面路由")
    private String route;

    @Schema(name = "page_content", description = "页面内容")
    @JsonProperty("page_content")
    @TableField(typeHandler = MapTypeHandler.class)
    private Map<String, Object> pageContent;

    @Schema(name = "isBody", description = "根元素是否是body")
    private Boolean isBody;

    @Schema(name = "parentId", description = "父文件夹id")
    private String parentId;

    private String group;

    @Schema(name = "depth", description = "*页面/文件夹深度，更改层级时服务端校验用（校验可有可无）*")
    private Integer depth;

    @Schema(name = "isPage", description = "是否为页面：分为页面和文件夹")
    private Boolean isPage;

    @Schema(name = "isDefault", description = "是否是默认页面")
    private Boolean isDefault;

    @Schema(name = "message", description = "信息")
    private String message;

    @Schema(name = "isHome", description = "是否为主页")
    private Boolean isHome;

    @Schema(name = "isPublished", description = "是否发布：0：草稿状态 1：已发布")
    private Boolean isPublished;

    @Schema(name = "contentBlocks", description = "*设计预留字段*")
    @TableField(typeHandler = ListTypeHandler.class)
    private List<BlockVersionDto> contentBlocks;


    @TableField(fill = FieldFill.INSERT)
    @Schema(name = "createdBy", description = "创建人")
    private String createdBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(name = "lastUpdatedBy", description = "最后修改人")
    private String lastUpdatedBy;

    @TableField(fill = FieldFill.INSERT)
    @Schema(name = "created_at", description = "创建时间")
    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("updated_at")
    @Schema(name = "updated_at", description = "更新时间")
    private LocalDateTime lastUpdatedTime;

    @TableField(fill = FieldFill.INSERT)
    @Schema(name = "tenantId", description = "租户ID")
    private String tenantId;

    @TableField(fill = FieldFill.INSERT)
    @Schema(name = "renterId", description = "业务租户ID")
    private String renterId;

    @Schema(name = "siteId", description = "站点ID")
    private String siteId;
}

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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinyengine.it.common.base.BaseEntity;
import com.tinyengine.it.common.handler.MapTypeHandler;

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
 * @author lu -yg
 * @since 2025-4-2
 */
@Getter
@Setter
@TableName("t_component_library")
@Schema(name = "ComponentLibrary", description = "组件库表")
public class ComponentLibrary extends BaseEntity {
    @Schema(name = "version", description = "版本")
    private String version;

    @Schema(name = "name", description = "名称")
    private String name;

    @Schema(name = "appId", description = "关联应用id")
    private Integer appId;

    @JsonProperty("package")
    @Schema(name = "package", description = "包名")
    private String packageName;

    @Schema(name = "registry", description = "注册")
    private String registry;

    @Schema(name = "description", description = "描述")
    private String description;

    @Schema(name = "framework", description = "技术栈")
    private String framework;

    @Schema(name = "script", description = "脚本地址")
    private String script;

    @Schema(name = "css", description = "样式地址")
    private String css;

    @Schema(name = "bundle", description = "bundle.json地址")
    private String bundle;

    @TableField(typeHandler = MapTypeHandler.class)
    @Schema(name = "dependencies", description = "依赖")
    private Map<String, Object> dependencies;

    @TableField(typeHandler = MapTypeHandler.class)
    @Schema(name = "others", description = "其他")
    private Map<String, Object> others;

    @Schema(name = "thumbnail", description = "略图")
    private String thumbnail;

    @JsonProperty("public")
    @Schema(name = "public", description = "公开状态：0，1，2")
    private Integer publicStatus;

    @Schema(name = "isStarted", description = "标识启用")
    private Boolean isStarted;

    @Schema(name = "isOfficial", description = "标识官方组件")
    private Boolean isOfficial;

    @Schema(name = "isDefault", description = "标识默认组件")
    private Boolean isDefault;

    @Schema(name = "components", description = "组件库组件")
    private List<Component> components;
}

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
import com.tinyengine.it.common.base.HistoryEntity;
import com.tinyengine.it.common.handler.MapTypeHandler;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 物料历史表
 * </p>
 *
 * @author lu -yg
 * @since 2024-10-17
 */
@Getter
@Setter
@TableName("t_material_history")
@Schema(name = "MaterialHistory", description = "物料历史表")
public class MaterialHistory extends HistoryEntity {
    @Schema(name = "content", description = "物料内容")
    @TableField(typeHandler = MapTypeHandler.class)
    private Map<String, Object> content;

    @Schema(name = "name", description = "物料名称")
    private String name;

    @Schema(name = "npmName", description = "npm包名")
    private String npmName;

    @Schema(name = "framework", description = "技术栈")
    private String framework;

    @JsonProperty("assets_url")
    @TableField(typeHandler = MapTypeHandler.class)
    @Schema(name = "assets_url", description = "资源地址")
    private Map<String, Object> assetsUrl;

    @Schema(name = "imageUrl", description = "封面图片地址")
    private String imageUrl;

    @JsonProperty("build_info")
    @TableField(typeHandler = MapTypeHandler.class)
    @Schema(name = "build_info", description = "资源地址")
    private Map<String, Object> buildInfo;

    @Schema(name = "description", description = "描述")
    private String description;

    @Schema(name = "materialSize", description = "物料包大小")
    private String materialSize;

    @Schema(name = "tgzUrl", description = "物料压缩包地址")
    private String tgzUrl;

    @Schema(name = "unzipTgzRootPathUrl", description = "物料压缩包解压后根地址")
    private String unzipTgzRootPathUrl;

    @Schema(name = "unzipTgzFiles", description = "物料压缩包解压后文件地址")
    private String unzipTgzFiles;

    @Schema(name = "components", description = "组件")
    private List<Component> components;
}

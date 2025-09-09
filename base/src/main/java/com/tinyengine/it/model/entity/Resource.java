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
import com.tinyengine.it.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * 资源表
 *
 * @since 2025-09-03
 */
@Getter
@Setter
@TableName("t_resource")
@Schema(name = "Resource", description = "资源表")
public class Resource extends BaseEntity {
    @Schema(name = "appId", description = "关联appId")
    private Integer appId;

    @Schema(name = "platformId", description = "关联设计器id")
    private Integer platformId;
    
    @TableField(exist = false)
    @Schema(name = "resourceGroupId", description = "分组Id")
    private Integer resourceGroupId;

    @Schema(name = "name", description = "名称")
    private String name;

    @Schema(name = "resourceUrl", description = "资源url")
    private String resourceUrl;

    @Schema(name = "thumbnailUrl", description = "缩略图url")
    private String thumbnailUrl;

    @Schema(name = "category", description = "分类")
    private String category;

    @Schema(name = "description", description = "描述")
    private String description;

    @Schema(name = "thumbnailData", description = "缩略图数据")
    private String thumbnailData;

    @Schema(name = "resourceData", description = "资源数据")
    private String resourceData;

    @Schema(name = "publicStatus", description = "公开状态：0，1，2")
    private Integer publicStatus;

    @Schema(name = "isDefault", description = "是否是默认")
    private Boolean isDefault;
}

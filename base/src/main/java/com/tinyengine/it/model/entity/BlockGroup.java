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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 区块分组
 * </p>
 *
 * @author lu -yg
 * @since 2024-10-17
 */
@Getter
@Setter
@TableName("t_block_group")
@Schema(name = "BlockGroup", description = "区块分组")
public class BlockGroup extends BaseEntity {
    @Schema(name = "name", description = "分组名称")
    private String name;

    @Schema(name = "appId", description = "创建分组所在app")
    @JsonProperty("app")
    private Integer appId;

    @Schema(name = "platformId", description = "设计器id")
    private Integer platformId;

    @Schema(name = "description", description = "分组描述")
    private String description;

    @TableField(exist = false)
    @Schema(name = "blocks", description = "区块")
    private List<Block> blocks = new ArrayList<>();
}

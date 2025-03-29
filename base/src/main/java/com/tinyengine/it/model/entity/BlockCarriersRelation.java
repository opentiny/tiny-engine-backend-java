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

import com.baomidou.mybatisplus.annotation.TableName;
import com.tinyengine.it.common.base.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 区块与分组、物料历史关系表
 * </p>
 *
 * @author lu-yg
 * @since 2024-12-16
 */
@Getter
@Setter
@TableName("t_block_carriers_relation")
@Schema(name = "BlockCarriersRelation", description = "区块与分组、物料历史关系表")
public class BlockCarriersRelation extends BaseEntity {

    @Schema(name = "blockId", description = "区块id")
    private Integer blockId;

    @Schema(name = "hostId", description = "类型id")
    private Integer hostId;

    @Schema(name = "hostType", description = "类型")
    private String hostType;

    @Schema(name = "version", description = "当前分组使用区块历史版本")
    private String version;
}

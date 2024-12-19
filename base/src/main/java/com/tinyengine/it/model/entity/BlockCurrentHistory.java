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
 * 区块当前使用版本历史表
 * </p>
 *
 * @author lu-yg
 * @since 2024-12-16
 */
@Getter
@Setter
@TableName("t_block_current_history")
@Schema(name = "BlockCurrentHistory", description = "区块表")
public class BlockCurrentHistory extends BaseEntity {
    @Schema(name = "blockHistoryId", description = "区块历史id")
    private Integer blockHistoryId;

    @Schema(name = "blockId", description = "区块id")
    private Integer blockId;

    @Schema(name = "appId", description = "应用id")
    private Integer appId;
}

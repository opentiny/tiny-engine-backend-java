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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 条件查询不在区块分组参数
 * </p>
 *
 * @author lu-yg
 * @since 2024-01-21
 */
@Data
public class NotGroupDto {
    @Schema(name = "groupId", description = "区块分组id")
    private Integer groupId;

    @Schema(name = "label", description = "区块编码")
    private String label;

    @Schema(name = "createdBY", description = "创建人id")
    private String createdBy;

    @Schema(name = "tags", description = "区块标签")
    private String [] tags;
}

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

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * <p>
 * 区块传参dto
 * </p>
 *
 * @author zhangjuncao
 * @since 2024-10-17
 */
@Data
public class BlockParamDto {
    private String appId;
    private String sort;

    @JsonProperty("name_cn")
    private String name;

    private String description;
    private String label;
    private Integer limit;
    private Integer start;
}

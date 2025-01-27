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
 * The type BlockBuildDto.
 *
 * @since 2024-12-19
 */
@Data
public class BlockBuildDto {
    private BlockParam block;
    @JsonProperty("deploy_info")
    private String deployInfo;
    @JsonProperty("is_compile")
    private Boolean isCompile;
    private Boolean needToSave;
    private String version;
}

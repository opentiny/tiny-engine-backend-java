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

import lombok.Data;

import java.util.List;

/**
 * ChatRequest dto
 *
 * @since 2025-08-06
 */
@Data
public class ChatRequest {
    private String model;
    private String apiKey;
    private String baseUrl;
    private List<AiMessages> messages;
    private Double temperature = 0.7;
    private boolean stream = false; // 流式开关
}

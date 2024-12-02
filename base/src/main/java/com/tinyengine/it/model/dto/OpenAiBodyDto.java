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
 * The type Open AI body dto.
 *
 * @since 2024-10-20
 */
@Data
public class OpenAiBodyDto {
    private String model;
    private List<AiMessages> messages;

    /**
     * Instantiates a new Open AI body dto.
     *
     * @param model    the model
     * @param messages the messages
     */
    public OpenAiBodyDto(String model, List<AiMessages> messages) {
        this.model = model;
        this.messages = messages;
    }
}

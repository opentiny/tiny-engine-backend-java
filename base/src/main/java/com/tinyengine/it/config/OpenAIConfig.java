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

package com.tinyengine.it.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

/**
 * The type Open AI config.
 *
 * @since 2025-08-06
 */
@Data
@Configuration
public class OpenAIConfig {
    private String apiKey = "your-api-key";
    private String baseUrl = "https://api.deepseek.com/chat/completions";
    private String defaultModel = "deepseek-chat";
    private int timeoutSeconds = 300;
}

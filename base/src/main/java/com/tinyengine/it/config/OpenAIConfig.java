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
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Open AI config.
 *
 * @since 2025-08-06
 */
@Data
@Component
@ConfigurationProperties(prefix = "openai")
public class OpenAIConfig {
    private String apiKey = "your-api-key";
    private String baseUrl = "https://api.deepseek.com/chat/completions";
    private String defaultModel = "deepseek-chat";
    private int timeoutSeconds = 300;

    // Streamable HTTP 工具映射（工具名 -> 服务URL）
    private Map<String, String> streamableTools = new HashMap<>();

    // Streamable HTTP 连接池配置
    private StreamableHttpConfig streamableHttp = new StreamableHttpConfig();

    @Data
    public static class StreamableHttpConfig  {
        private int maxConnections = 20;
        private int connectionTimeoutSeconds = 30;
        private int requestTimeoutSeconds = 60;
        private String protocolVersion = "2025-06-18"; // 协议版本
        private boolean keepAlive = true;
    }
}

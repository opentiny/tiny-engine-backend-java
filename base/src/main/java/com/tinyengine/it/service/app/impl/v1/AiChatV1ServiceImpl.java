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

package com.tinyengine.it.service.app.impl.v1;

import com.fasterxml.jackson.databind.JsonNode;
import com.tinyengine.it.common.log.SystemServiceLog;
import com.tinyengine.it.common.utils.JsonUtils;
import com.tinyengine.it.common.utils.SM4Utils;
import com.tinyengine.it.config.OpenAIConfig;
import com.tinyengine.it.model.dto.ChatRequest;
import com.tinyengine.it.service.app.v1.AiChatV1Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * The type AiChat v1 service.
 *
 * @since 2025-08-06
 */
@Slf4j
@Service
public class AiChatV1ServiceImpl implements AiChatV1Service {
    private final OpenAIConfig config = new OpenAIConfig();
    private HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(config.getTimeoutSeconds()))
            .build();

    /**
     * chatCompletion.
     *
     * @param request the request
     * @return Object the Object
     */
    @Override
    @SystemServiceLog(description = "chatCompletion")
    public Object chatCompletion(ChatRequest request) throws Exception {
        String requestBody = buildRequestBody(request);
        String encryptApiKey = request.getApiKey() != null ? request.getApiKey() : config.getApiKey();
        String apiKey = getApiKey(encryptApiKey);
        String baseUrl = request.getBaseUrl();

        // 规范化URL处理
        String normalizedUrl = normalizeApiUrl(baseUrl);

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
            .uri(URI.create(normalizedUrl))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + apiKey)
            .POST(HttpRequest.BodyPublishers.ofString(requestBody));
        if (request.isStream()) {
            requestBuilder.header("Accept", "text/event-stream");
            return processStreamResponse(requestBuilder);
        } else {
            return processStandardResponse(requestBuilder);
        }
    }

    /**
     * get token.
     *
     * @param apiKey the apiKey
     * @return token the token
     */
    @Override
    public String getToken(String apiKey) throws Exception {
        String sm4Key = System.getenv("SM4KEY");
        String encrypt = SM4Utils.encryptECB(apiKey, sm4Key);
        return "EKEY_"+ encrypt;
    }

    /**
     * 规范化API URL，兼容不同厂商
     */
    private String normalizeApiUrl(String baseUrl) {
        if (baseUrl == null || baseUrl.trim().isEmpty()) {
            baseUrl = config.getBaseUrl();
        }
        baseUrl = baseUrl.trim();

        if (baseUrl.contains("/chat/completions") || baseUrl.contains("/v1/chat/completions")) {
            return ensureUrlProtocol(baseUrl);
        }

        if (baseUrl.contains("v1")) {
            return ensureUrlProtocol(baseUrl) + "/chat/completions";
        }
        if (baseUrl.endsWith("#")) {
            return ensureUrlProtocol(baseUrl);
        } else {
            return ensureUrlProtocol(baseUrl) + "/v1/chat/completions";
        }
    }

    /**
     * 确保URL有正确的协议前缀
     */
    private String ensureUrlProtocol(String url) {
        if (url.startsWith("http://") || url.startsWith("https://")) {
            return url;
        }
        // 默认使用https
        return "https://" + url;
    }

    private String buildRequestBody(ChatRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("model", request.getModel() != null ? request.getModel() : config.getDefaultModel());
        body.put("messages", request.getMessages());
        body.put("stream", request.isStream());
        body.put("tools", request.getTools());
        if (request.getMaxTokens() != null) {
            body.put("max_tokens", request.getMaxTokens());
        }
        body.put("temperature", request.getTemperature());
        if (request.getTemperature() != null) {
            body.put("temperature", request.getTemperature());
        }
        if (request.getSearchOptions() != null) {
            body.put("stream_options", request.getSearchOptions());
        }
        if (request.getPresencePenalty() != null) {
            body.put("presence_penalty", request.getPresencePenalty());
        }
        if (request.getResponseFormat() != null) {
            body.put("response_format", request.getResponseFormat());
        }
        if (request.getMaxInputTokens() != null) {
            body.put("max_input_tokens", request.getMaxInputTokens());
        }
        if (request.getMaxInputTokens() != null) {
            body.put("vl_high_resolution_images", request.getVlHighResolutionImages());
        }
        if (request.getEnableThinking() != null) {
            body.put("enable_thinking", request.getEnableThinking());
        }
        if (request.getToolChoice() != null) {
            body.put("tool_choice", request.getToolChoice());
        }
        if (request.getStop() != null) {
            body.put("stop", request.getStop());
        }
        if (request.getParallelToolCalls() != null) {
            body.put("parallel_tool_calls", request.getParallelToolCalls());
        }
        if (request.getEnableSearch() != null) {
            body.put("enable_search", request.getEnableSearch());
        }
        if (request.getFrequencyPenalty() != null) {
            body.put("frequency_penalty", request.getFrequencyPenalty());
        }

        return JsonUtils.encode(body);
    }

    private JsonNode processStandardResponse(HttpRequest.Builder requestBuilder)
        throws Exception {
        HttpResponse<String> response = httpClient.send(
            requestBuilder.build(), HttpResponse.BodyHandlers.ofString());

        // 添加状态码检查
        if (response.statusCode() != 200) {
            String errorBody = response.body();
            try {
                // 尝试解析错误JSON
                JsonNode errorNode = JsonUtils.MAPPER.readTree(errorBody);
                throw new IOException("API请求失败: " + response.statusCode() + " - " +
                    errorNode.get("error").get("message").asText());
            } catch (Exception e) {
                // 如果无法解析JSON，返回原始错误信息
                throw new IOException("API请求失败: " + response.statusCode() + " - " + errorBody);
            }
        }

        return JsonUtils.MAPPER.readTree(response.body());
    }
    private StreamingResponseBody processStreamResponse(HttpRequest.Builder requestBuilder) {
        return outputStream -> {
            try {
                // 使用相同的httpClient实例，确保配置一致
                HttpResponse<InputStream> response = httpClient.send(
                    requestBuilder.build(),
                    HttpResponse.BodyHandlers.ofInputStream()
                );

                // 立即检查状态码
                if (response.statusCode() != 200) {
                    String errorBody = new String(response.body().readAllBytes(), StandardCharsets.UTF_8);
                    // 格式化为正确的SSE错误事件
                    String errorEvent = formatSSEError(response.statusCode(), errorBody);
                    outputStream.write(errorEvent.getBytes(StandardCharsets.UTF_8));
                    outputStream.flush();
                    return; // 重要：立即返回，不再处理流
                }

                // 正常流处理逻辑
                try (InputStream inputStream = response.body()) {
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                        outputStream.flush();
                    }
                }
            } catch (Exception e) {
                try {
                    // 格式化为标准SSE错误格式
                    String errorEvent = formatSSEError(500, e.getMessage());
                    outputStream.write(errorEvent.getBytes(StandardCharsets.UTF_8));
                    outputStream.flush();
                } catch (IOException ioException) {
                    log.error("无法发送错误信息: " + ioException.getMessage());
                }
            }
        };
    }
    /**
     * 格式化SSE错误事件
     */
    private String formatSSEError(int statusCode, String errorBody) {
        try {
            // 尝试解析API错误信息
            JsonNode errorNode = JsonUtils.MAPPER.readTree(errorBody);
            String errorMessage = errorNode.get("error").get("message").asText();
            return String.format("data: {\"error\": {\"code\": %d, \"message\": \"%s\"}}\n\n",
                statusCode, errorMessage);
        } catch (Exception e) {
            // 如果无法解析，返回原始错误
            return String.format("data: {\"error\": {\"code\": %d, \"message\": \"%s\"}}\n\n",
                statusCode, errorBody.replace("\"", "\\\""));
        }
    }

    private String getApiKey(String encryptApiKey) throws Exception {
        String sm4Key = System.getenv("SM4KEY");

        if (encryptApiKey.startsWith("EKEY_")) {
            String  encryptBase64ApiKey = encryptApiKey.substring(5);
            return SM4Utils.decryptECB(encryptBase64ApiKey, sm4Key);
        }
        return encryptApiKey;
    }
}

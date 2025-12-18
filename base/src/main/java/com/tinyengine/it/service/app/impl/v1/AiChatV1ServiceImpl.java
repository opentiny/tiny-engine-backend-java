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
import com.tinyengine.it.config.OpenAIConfig;
import com.tinyengine.it.model.dto.ChatRequest;
import com.tinyengine.it.service.app.adapter.GeminiApiAdapter;
import com.tinyengine.it.service.app.v1.AiChatV1Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type AiChat v1 service.
 *
 * @since 2025-08-06
 */
@Service
public class AiChatV1ServiceImpl implements AiChatV1Service {
    private static final Logger LOGGER = LoggerFactory.getLogger(AiChatV1ServiceImpl.class);
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
        String model = request.getModel() != null ? request.getModel() : config.getDefaultModel();
        boolean isGemini = GeminiApiAdapter.isGeminiModel(model);

        String requestBody = buildRequestBody(request, isGemini);
        String apiKey = request.getApiKey() != null ? request.getApiKey() : config.getApiKey();
        String baseUrl = request.getBaseUrl();

        // 规范化URL处理（Gemini 需要在 URL 中包含 API Key）
        String normalizedUrl = normalizeApiUrl(baseUrl, model, isGemini, apiKey);

        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(normalizedUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody));

        // Gemini uses API key in URL parameter, not in header
        // Other providers use Bearer token in Authorization header
        if (!isGemini) {
            requestBuilder.header("Authorization", "Bearer " + apiKey);
        }

        if (request.isStream()) {
            requestBuilder.header("Accept", "text/event-stream");
            return processStreamResponse(requestBuilder, isGemini, model);
        } else {
            return processStandardResponse(requestBuilder, isGemini, model);
        }
    }

    /**
     * 规范化API URL，兼容不同厂商
     */
    private String normalizeApiUrl(String baseUrl, String model, boolean isGemini, String apiKey) {
        if (baseUrl == null || baseUrl.trim().isEmpty()) {
            if (isGemini) {
                // Normalize model name: remove "models/" prefix if exists
                String normalizedModel = normalizeGeminiModelName(model);
                // Gemini default URL structure with API key as query parameter
                baseUrl = "https://generativelanguage.googleapis.com/v1beta/models/"
                        + normalizedModel + ":generateContent?key=" + apiKey;
            } else {
                baseUrl = config.getBaseUrl();
            }
        }
        baseUrl = baseUrl.trim();

        // Handle Gemini URLs
        if (isGemini) {
            // If already a complete Gemini URL with key parameter, use it
            if ((baseUrl.contains(":generateContent") || baseUrl.contains(":streamGenerateContent"))
                    && baseUrl.contains("key=")) {
                return ensureUrlProtocol(baseUrl);
            }

            // Build Gemini URL with API key
            String geminiBase = ensureUrlProtocol(baseUrl);

            // Remove existing key parameter if any
            if (geminiBase.contains("?key=")) {
                geminiBase = geminiBase.substring(0, geminiBase.indexOf("?key="));
            }

            if (!geminiBase.contains("/v1beta/models/")) {
                // Normalize model name: remove "models/" prefix if exists
                String normalizedModel = normalizeGeminiModelName(model);
                geminiBase = geminiBase + "/v1beta/models/" + normalizedModel + ":generateContent";
            }

            // Add API key as query parameter
            return geminiBase + "?key=" + apiKey;
        }

        // Handle non-Gemini URLs
        if (baseUrl.contains("/chat/completions") || baseUrl.contains("/v1/chat/completions")) {
            return ensureUrlProtocol(baseUrl);
        }

        if (baseUrl.contains("v1")) {
            return ensureUrlProtocol(baseUrl) + "/chat/completions";
        } else {
            return ensureUrlProtocol(baseUrl) + "/v1/chat/completions";
        }
    }

    /**
     * 规范化 Gemini 模型名称，移除 "models/" 前缀
     */
    private String normalizeGeminiModelName(String model) {
        if (model == null) {
            return "gemini-1.5-pro";
        }
        // Remove "models/" prefix if exists
        if (model.startsWith("models/")) {
            return model.substring(7); // Remove "models/"
        }
        return model;
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

    private String buildRequestBody(ChatRequest request, boolean isGemini) {
        Map<String, Object> body = new HashMap<>();
        body.put("model", request.getModel() != null ? request.getModel() : config.getDefaultModel());
        body.put("messages", normalizeMessages(request.getMessages()));
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

        // Convert to Gemini format if needed
        if (isGemini) {
            body = GeminiApiAdapter.convertRequestToGemini(body);
        }

        String requestBody = JsonUtils.encode(body);
        
        // 添加调试日志以便排查问题
        LOGGER.debug("AI Chat Request Body: {}", requestBody);
        
        return requestBody;
    }

    /**
     * Normalize messages to fix format issues
     * Fixes: role:tool messages with array content should have string content
     */
    private Object normalizeMessages(Object messages) {
        if (!(messages instanceof List)) {
            return messages;
        }
        
        List<?> messageList = (List<?>) messages;
        List<Map<String, Object>> normalizedMessages = new ArrayList<>();
        
        for (Object msg : messageList) {
            if (!(msg instanceof Map)) {
                normalizedMessages.add((Map<String, Object>) msg);
                continue;
            }
            
            Map<String, Object> messageMap = new HashMap<>((Map<String, Object>) msg);
            
            // Remove invalid "type" field at message level (should only be in content array)
            messageMap.remove("type");
            
            // Fix role:tool messages with array content
            Object role = messageMap.get("role");
            Object content = messageMap.get("content");
            
            if ("tool".equals(role) && content instanceof List) {
                // For tool messages, content must be a string
                List<?> contentArray = (List<?>) content;
                if (!contentArray.isEmpty() && contentArray.get(0) instanceof Map) {
                    Map<?, ?> firstItem = (Map<?, ?>) contentArray.get(0);
                    Object text = firstItem.get("text");
                    if (text != null) {
                        messageMap.put("content", text.toString());
                    }
                }
            }
            
            normalizedMessages.add(messageMap);
        }
        
        return normalizedMessages;
    }

    private JsonNode processStandardResponse(HttpRequest.Builder requestBuilder, boolean isGemini, String model)
            throws Exception {
        HttpResponse<String> response = httpClient.send(
                requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
        JsonNode jsonResponse = JsonUtils.MAPPER.readTree(response.body());

        // Convert Gemini response to OpenAI format if needed
        if (isGemini) {
            Map<String, Object> convertedResponse = GeminiApiAdapter.convertResponseFromGemini(jsonResponse, model);
            return JsonUtils.MAPPER.valueToTree(convertedResponse);
        }

        return jsonResponse;
    }

    private StreamingResponseBody processStreamResponse(HttpRequest.Builder requestBuilder, boolean isGemini, String model) {
        return outputStream -> {
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpResponse<InputStream> response = client.send(
                        requestBuilder.build(),
                        HttpResponse.BodyHandlers.ofInputStream()
                );
                if (response.statusCode() != 200) {
                    String errorBody = new String(response.body().readAllBytes(), StandardCharsets.UTF_8);
                    throw new IOException("API请求失败: " + response.statusCode() + " - " + errorBody);
                }
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
                    String errorEvent = "data: {\"error\": \"" + e.getMessage() + "\"}\n\n";
                    outputStream.write(errorEvent.getBytes(StandardCharsets.UTF_8));
                    outputStream.flush();
                } catch (IOException ioException) {
                    throw new IOException("API请求失败，且无法发送错误信息: " + e.getMessage() +
                            " (IO错误: " + ioException.getMessage() + ")", e);
                }
            }
        };
    }
}

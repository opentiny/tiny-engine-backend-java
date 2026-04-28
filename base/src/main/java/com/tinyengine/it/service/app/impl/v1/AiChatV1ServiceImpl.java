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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.common.log.SystemServiceLog;
import com.tinyengine.it.common.utils.JsonUtils;
import com.tinyengine.it.common.utils.SM4Utils;
import com.tinyengine.it.config.OpenAIConfig;
import com.tinyengine.it.mcp.core.MCPToolDefinition;
import com.tinyengine.it.mcp.core.MCPToolRegistry;
import com.tinyengine.it.mcp.core.RemoteToolDefinition;
import com.tinyengine.it.mcp.core.StreamableHttpClientManager;
import com.tinyengine.it.model.dto.ChatRequest;
import com.tinyengine.it.service.app.v1.AiChatV1Service;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Lazy;
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

    private static final double DEFAULT_TEMPERATURE = 0.7;
    private static final int DEFAULT_MAX_TOKENS = 1000;

    private final MCPToolRegistry toolRegistry;
    private final ObjectMapper objectMapper;
    private final OpenAIConfig openAIConfig;
    private final HttpClient httpClient;

    public AiChatV1ServiceImpl(MCPToolRegistry toolRegistry,
                               ObjectMapper objectMapper,
                               OpenAIConfig openAIConfig) {
        this.toolRegistry = toolRegistry;
        this.objectMapper = objectMapper;
        this.openAIConfig = openAIConfig;
        this.httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(openAIConfig.getTimeoutSeconds()))
            .build();
    }

    /**
     * chatCompletion.
     *
     * @param request the request
     * @return Object the Object
     */
    @Override
    @SystemServiceLog(description = "chatCompletion")
    public Object chatCompletion(ChatRequest request) throws Exception {
        log.info("base url: "+openAIConfig.getBaseUrl());
        log.info("Streamable tools config: {}", openAIConfig.getStreamableTools());
        log.info("Received chatCompletion request: model={}, stream={}, tools={}",
            request.getModel(), request.isStream(), request.getTools());
        // 1. 确定 API Key 和 Base URL
        String encryptApiKey = request.getApiKey() != null ? request.getApiKey() : openAIConfig.getApiKey();
        String apiKey = getApiKey(encryptApiKey);
        String baseUrl = request.getBaseUrl() != null ? request.getBaseUrl() : openAIConfig.getBaseUrl();
        // 规范化URL处理
        String normalizedUrl = normalizeApiUrl(baseUrl);
        // 2. 构建初始请求体
        String requestBody = buildRequestBody(request);



        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
            .uri(URI.create(normalizedUrl))
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + apiKey)
            .POST(HttpRequest.BodyPublishers.ofString(requestBody));
        if (request.isStream()) {
            if(request.getTools() != null ) {
                log.info("流式请求不支持工具调用，已自动切换为非流式模式");
                request.setStream(false);
                String finalAnswer  = (String) processNonStreamWithTools(requestBuilder, request);
                // 将最终回答包装成 SSE 格式的 StreamingResponseBody
                return (StreamingResponseBody) outputStream -> {
                    System.out.println("Final answer: " + finalAnswer);
                    outputStream.write(("data: " + finalAnswer + "\n\n").getBytes(StandardCharsets.UTF_8));
                    outputStream.write("data: [DONE]\n\n".getBytes(StandardCharsets.UTF_8));
                    outputStream.flush();
                };
            }else{
                log.info("处理流式请求，URL: {}", normalizedUrl);
                requestBuilder.header("Accept", "text/event-stream");
                return processStreamResponse(requestBuilder);
            }

        } else {
           // return processNonStreamWithTools(requestBuilder, request);

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
        //String sm4Key = System.getenv("SM4KEY");
        String sm4Key="rOGV7EsU7thOawaUrOI+LA==";
        String encrypt = SM4Utils.encryptECB(apiKey, sm4Key);
        return "EKEY_"+ encrypt;
    }

    /**
     * 规范化API URL，兼容不同厂商
     */
    private String normalizeApiUrl(String baseUrl) {
        if (baseUrl == null || baseUrl.trim().isEmpty()) {
            baseUrl = openAIConfig.getBaseUrl();
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

    // 构建请求体（仅处理本地工具）
    private String buildRequestBody(ChatRequest request) throws Exception {
        ObjectNode root = objectMapper.createObjectNode();
        root.put("model", request.getModel() != null ? request.getModel() : openAIConfig.getDefaultModel());
        root.put("stream", request.isStream());

        if (request.getTemperature() != null) {
            root.put("temperature", request.getTemperature());
        } else {
            root.put("temperature", DEFAULT_TEMPERATURE);
        }
        if (request.getMaxTokens() != null) {
            root.put("max_tokens", request.getMaxTokens());
        } else {
            root.put("max_tokens", DEFAULT_MAX_TOKENS);
        }
        if (request.getPresencePenalty() != null) {
            root.put("presence_penalty", request.getPresencePenalty());
        }
        if (request.getFrequencyPenalty() != null) {
            root.put("frequency_penalty", request.getFrequencyPenalty());
        }
        if (request.getStop() != null) {
            root.put("stop", request.getStop());
        }
        if (request.getResponseFormat() != null) {
            root.set("response_format", objectMapper.valueToTree(request.getResponseFormat()));
        }
        if (request.getToolChoice() != null) {
            root.put("tool_choice", request.getToolChoice().toString());
        }
        if (request.getParallelToolCalls() != null) {
            root.put("parallel_tool_calls", request.getParallelToolCalls());
        }
        if (request.getEnableSearch() != null) {
            root.put("enable_search", request.getEnableSearch());
        }
        if (request.getEnableThinking() != null) {
            root.put("enable_thinking", request.getEnableThinking());
        }

        // 处理 messages
        ArrayNode messagesArray = objectMapper.createArrayNode();
        if (request.getMessages() != null) {
            JsonNode messagesNode = objectMapper.valueToTree(request.getMessages());
            if (messagesNode.isArray()) {
                messagesArray = (ArrayNode) messagesNode;
            } else {
                throw new ServiceException("400", "messages must be an array");
            }
        }
        root.set("messages", messagesArray);

        // 处理 tools（仅本地工具）
        if (request.getTools() != null) {
            JsonNode toolsNode = objectMapper.valueToTree(request.getTools());
            if (toolsNode.isArray()) {
                ArrayNode toolsArray = objectMapper.createArrayNode();
                for (JsonNode toolItem : toolsNode) {
                    String toolName = null;
                    if (toolItem.isTextual()) {
                        toolName = toolItem.asText();
                    } else if (toolItem.isObject() && toolItem.has("name")) {
                        toolName = toolItem.get("name").asText();
                    } else {
                        log.warn("无法识别的工具项: {}", toolItem);
                        continue;
                    }

                    MCPToolDefinition localDef = toolRegistry.getTool(toolName);
                    if (localDef != null) {
                        toolsArray.add(convertToolToSchema(localDef));
                    } else {
                        log.warn("本地工具 {} 未注册", toolName);
                    }
                }
                if (toolsArray.size() > 0) {
                    root.set("tools", toolsArray);
                }
            } else {
                log.warn("tools 字段不是数组，忽略");
            }
        }

        return objectMapper.writeValueAsString(root);
    }


    private String buildRequestBody2(ChatRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("model", request.getModel() != null ? request.getModel() : openAIConfig.getDefaultModel());
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
    /**
     * 将本地工具定义转换为 OpenAI 函数格式
     * @param def 本地工具定义（来自 MCPToolRegistry）
     * @return OpenAI 函数格式的 JSON 节点
     */
    private ObjectNode convertLocalToolToSchema(MCPToolDefinition def) {
        ObjectNode toolNode = objectMapper.createObjectNode();
        toolNode.put("type", "function");

        ObjectNode function = objectMapper.createObjectNode();
        function.put("name", def.getName());
        function.put("description", def.getDescription());
        // 直接使用本地工具的 inputSchema（已经是 JSON Schema 对象）
        function.set("parameters", def.getInputSchema());

        toolNode.set("function", function);
        return toolNode;
    }

    /**
     * 将远程工具定义转换为 OpenAI 函数格式
     * @param def 远程工具定义（从 MCP 服务器缓存获取）
     * @return OpenAI 函数格式的 JSON 节点
     */
    private ObjectNode convertRemoteToolToSchema(RemoteToolDefinition def) {
        ObjectNode toolNode = objectMapper.createObjectNode();
        toolNode.put("type", "function");

        ObjectNode function = objectMapper.createObjectNode();
        function.put("name", def.getName());
        function.put("description", def.getDescription());
        // 远程工具的 parameters 字段就是 JSON Schema（可能包含 type: "object" 和 properties）
        function.set("parameters", def.getParameters());

        toolNode.set("function", function);
        return toolNode;
    }
    /**
     * 将本地工具定义转换为 OpenAI 函数格式
     */
    private ObjectNode convertToolToSchema(MCPToolDefinition def) {
        ObjectNode toolNode = objectMapper.createObjectNode();
        toolNode.put("type", "function");

        ObjectNode function = objectMapper.createObjectNode();
        function.put("name", def.getName());
        function.put("description", def.getDescription());
        function.set("parameters", def.getInputSchema());

        toolNode.set("function", function);
        return toolNode;
    }
    private JsonNode processStandardResponse(HttpRequest.Builder requestBuilder) {
        HttpResponse<String> response = null;
        String code = null;
        String message = null;
        try {
         response = httpClient.send(
             requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
         code = String.valueOf(response.statusCode());
            if (response.statusCode() != 200) {
                String errorBody = response.body();

                // 尝试解析错误JSON
                JsonNode errorNode = JsonUtils.MAPPER.readTree(errorBody);
                message = errorNode.get("error").get("message").asText();
                throw new ServiceException(code, message);
            }
            return JsonUtils.MAPPER.readTree(response.body());
        } catch (IOException | InterruptedException e) {
            throw new ServiceException(code, message);
        }


    }
    /**
     * 非流式处理，支持多轮工具调用
     * 动态维护 messages 数组（ArrayNode）
     */
    private Object processNonStreamWithTools(HttpRequest.Builder requestBuilder, ChatRequest originalRequest) throws Exception {
        ArrayNode messagesArray;
        if (originalRequest.getMessages() == null) {
            messagesArray = objectMapper.createArrayNode();
        } else {
            JsonNode messagesNode = objectMapper.valueToTree(originalRequest.getMessages());
            if (!messagesNode.isArray()) {
                throw new ServiceException("400", "messages must be an array");
            }
            messagesArray = (ArrayNode) messagesNode;
        }

        while (true) {
            originalRequest.setMessages(messagesArray);
            String requestBody = buildRequestBody(originalRequest);
            String apiKey = getApiKey(originalRequest.getApiKey() != null ? originalRequest.getApiKey() : openAIConfig.getApiKey());
            String baseUrl = originalRequest.getBaseUrl() != null ? originalRequest.getBaseUrl() : openAIConfig.getBaseUrl();
            requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(normalizeApiUrl(baseUrl)))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody));

            HttpRequest request = requestBuilder.build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                handleErrorResponse(response);
                return null;
            }

            JsonNode root = objectMapper.readTree(response.body());
            JsonNode choices = root.get("choices");
            if (choices == null || choices.isEmpty()) {
                throw new ServiceException("500", "Invalid LLM response: " + response.body());
            }
            JsonNode messageJson = choices.get(0).get("message");
            log.info("LLM response message: {}", messageJson);

            ObjectNode assistantMsg = objectMapper.createObjectNode();
            assistantMsg.put("role", messageJson.get("role").asText());
            if (messageJson.has("content") && !messageJson.get("content").isNull()) {
                assistantMsg.put("content", messageJson.get("content").asText());
            }

            JsonNode toolCallsJson = messageJson.get("tool_calls");
            if (toolCallsJson != null && toolCallsJson.isArray() && toolCallsJson.size() > 0) {
                assistantMsg.set("tool_calls", toolCallsJson);
            }

            messagesArray.add(assistantMsg);

            if (toolCallsJson == null || toolCallsJson.isEmpty()) {
                return assistantMsg.has("content") ? assistantMsg.get("content").asText() : "";
            }

            // 执行工具调用（仅本地）
            for (JsonNode tc : toolCallsJson) {
                String toolCallId = tc.get("id").asText();
                JsonNode function = tc.get("function");
                String toolName = function.get("name").asText();
                String arguments = function.get("arguments").asText();

                log.info("执行本地工具: {}，参数: {}", toolName, arguments);
                String toolResult = executeLocalTool(toolName, arguments);

                ObjectNode toolMsg = objectMapper.createObjectNode();
                toolMsg.put("role", "tool");
                toolMsg.put("tool_call_id", toolCallId);
                toolMsg.put("content", toolResult);
                messagesArray.add(toolMsg);
            }

            originalRequest.setMessages(messagesArray);
        }
    }


    private String executeLocalTool(String toolName, String argumentsJson) {
        try {
            MCPToolDefinition def = toolRegistry.getTool(toolName);
            if (def == null) {
                return "错误：未找到本地工具 " + toolName;
            }
            JsonNode args = objectMapper.readTree(argumentsJson);
            Object result = def.execute(args);
            return result instanceof String ? (String) result : objectMapper.writeValueAsString(result);
        } catch (Exception e) {
            log.error("本地工具执行失败", e);
            return "工具执行错误: " + e.getMessage();
        }
    }

    /**
     * 处理错误响应
     */
    private void handleErrorResponse(HttpResponse<String> response) {
        String code = String.valueOf(response.statusCode());
        String message;
        try {
            JsonNode errorNode = objectMapper.readTree(response.body());
            message = errorNode.path("error").path("message").asText("未知错误");
        } catch (Exception e) {
            message = response.body();
        }
        throw new ServiceException(code, message);
    }
    /**
     * 流式响应处理（无工具调用）
     */
    private StreamingResponseBody processStreamResponse(HttpRequest.Builder requestBuilder) {
        return outputStream -> {
            HttpResponse<InputStream> response = null;
            try {
                response = httpClient.send(
                    requestBuilder.build(), HttpResponse.BodyHandlers.ofInputStream()
                );
            } catch (InterruptedException e) {
                throw new ServiceException("500", e.getMessage());
            }

            log.info("Received AI API response, status code {}", response.statusCode());

            if (response.statusCode() != 200) {
                String errorBody = new String(response.body().readAllBytes(), StandardCharsets.UTF_8);

                log.info("errorBody: {}", errorBody);

                JsonNode errorNode = JsonUtils.MAPPER.readTree(errorBody);
                throw new ServiceException(String.valueOf(response.statusCode()), errorNode.get("error").get("message").asText());
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
        };
    }

    private String getApiKey(String encryptApiKey) throws Exception {
       // String sm4Key = System.getenv("SM4KEY");
        String sm4Key="rOGV7EsU7thOawaUrOI+LA==";

        if (encryptApiKey.startsWith("EKEY_")) {
            String  encryptBase64ApiKey = encryptApiKey.substring(5);
            return SM4Utils.decryptECB(encryptBase64ApiKey, sm4Key);
        }
        return encryptApiKey;
    }
}

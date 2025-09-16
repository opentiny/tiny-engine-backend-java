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

import com.aliyun.bailian20231229.Client;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teaopenapi.models.OpenApiRequest;
import com.aliyun.teaopenapi.models.Params;
import com.aliyun.teautil.models.RuntimeOptions;
import com.fasterxml.jackson.databind.JsonNode;
import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.log.SystemServiceLog;
import com.tinyengine.it.common.utils.JsonUtils;
import com.tinyengine.it.config.OpenAIConfig;
import com.tinyengine.it.model.dto.ChatRequest;
import com.tinyengine.it.model.dto.NodeDto;
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
    private static final String ACCESS_KEY_ID = System.getenv("ACCESS_KEY_ID");
    private static final String ACCESS_KEY_SECRET = System.getenv("ACCESS_KEY_SECRET");
    private static final String ENDPOINT = "bailian.cn-beijing.aliyuncs.com";
    private static final String INDEX_ID = System.getenv("INDEX_ID");
    private static final String WORK_SPACE_ID = System.getenv("WORK_SPACE_ID");
    private final OpenAIConfig config = new OpenAIConfig();
    private static final Logger log = LoggerFactory.getLogger(AiChatV1ServiceImpl.class);
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
        String apiKey = request.getApiKey() != null ? request.getApiKey() : config.getApiKey();
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
        } else  {
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

    /**
     * 创建客户端
     */
    private Client createClient() throws Exception {
        return new Client(new Config()
            .setAccessKeyId(ACCESS_KEY_ID)
            .setAccessKeySecret(ACCESS_KEY_SECRET)
            .setEndpoint(ENDPOINT)
            .setEndpointType("access_key"));
    }

    /**
     * 创建API信息
     */
    private Params createApiInfo(String WorkspaceId) throws Exception {
        return new Params()
            // 接口名称
            .setAction("Retrieve")
            // 接口版本
            .setVersion("2023-12-29")
            // 接口协议
            .setProtocol("HTTPS")
            // 接口 HTTP 方法
            .setMethod("POST")
            .setAuthType("AK")
            .setStyle("ROA")
            // 接口 PATH
            .setPathname("/" + com.aliyun.openapiutil.Client.getEncodeParam(WorkspaceId) + "/index/retrieve")
            // 接口请求体内容格式
            .setReqBodyType("json")
            // 接口响应体内容格式
            .setBodyType("json");
    }

    /**
     * 安全类型转换工具方法
     */
    private <T> T safeCast(Object obj, Class<T> clazz, T defaultValue) {
        if (obj == null) {
            return defaultValue;
        }
        try {
            return clazz.cast(obj);
        } catch (ClassCastException e) {
            log.warn("类型转换失败: {} 无法转换为 {}", obj.getClass().getName(), clazz.getName());
            return defaultValue;
        }
    }

    private String safeCastToString(Object obj) {
        return safeCast(obj, String.class, "");
    }

    private Double safeCastToDouble(Object obj) {
        return safeCast(obj, Double.class, 0.0);
    }

    private Long safeCastToLong(Object obj) {
        return safeCast(obj, Long.class, 0L);
    }

    /**
     * chatSearch.
     *
     * @param content the content
     * @return String the String
     */
    public Result chatSearch(String content) {
        try {
            Client client = createClient();
            Params params = createApiInfo(WORK_SPACE_ID);

            Map<String, Object> queries = new HashMap<>();
            queries.put("IndexId", INDEX_ID);
            queries.put("Query", content);
            queries.put("EnableRewrite", "true");

            RuntimeOptions runtime = new RuntimeOptions();
            OpenApiRequest request = new OpenApiRequest()
                    .setQuery(com.aliyun.openapiutil.Client.query(queries));

            Map<String, ?> response = client.callApi(params, request, runtime);
            Map<String, Object> body = (Map<String, Object>) response.get("body");

            if (body == null) {
                return Result.failed("响应体为空");
            }

            long status = safeCastToLong(body.get("Status"));
            if (status != 200L) {
                String message = safeCastToString(body.get("Message"));
                log.error("搜索失败: status={}, message={}", status, message);
                return Result.failed("搜索失败: " + message);
            }

            Map data = safeCast(body.get("Data"), Map.class, new HashMap<>());
            if (data == null || data.isEmpty()) {
                return Result.success(new ArrayList<>());
            }

            List nodes = safeCast(data.get("Nodes"), List.class, new ArrayList<>());
            if (nodes.isEmpty()) {
                return Result.success(new ArrayList<>());
            }

            List nodeDtos = convertToNodeDtos(nodes);
            return Result.success(nodeDtos);

        } catch (TeaException e) {
            log.error("阿里云Tea异常: {}", e.getMessage(), e);
            return Result.failed("阿里云服务异常: " + e.getMessage());
        } catch (Exception e) {
            log.error("搜索异常: {}", e.getMessage(), e);
            return Result.failed("系统异常: " + e.getMessage());
        }
    }

    /**
     * 转换节点数据
     */
    private List<NodeDto> convertToNodeDtos(List<Map<String, Object>> nodes) {
        List<NodeDto> nodeDtos = new ArrayList<>();

        for (Map<String, Object> node : nodes) {
            try {
                NodeDto nodeDto = new NodeDto();

                // 安全获取文本内容
                nodeDto.setContent(safeCastToString(node.get("Text")));

                // 安全获取分数
                Object scoreObj = node.get("Score");
                if (scoreObj instanceof Number) {
                    nodeDto.setScore(((Number) scoreObj).doubleValue());
                } else {
                    nodeDto.setScore(safeCastToDouble(scoreObj));
                }

                // 安全获取元数据
                Map metadata = safeCast(node.get("Metadata"), Map.class, new HashMap<>());
                if (metadata != null) {
                    nodeDto.setDocName(safeCastToString(metadata.get("doc_name")));
                }

                nodeDtos.add(nodeDto);

            } catch (Exception e) {
                log.warn("节点数据转换失败: {}", e.getMessage());
            }
        }

        return nodeDtos;
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
        if (request.getTemperature() != null) {
            body.put("temperature", request.getTemperature());
        }
        return JsonUtils.encode(body);
    }

    private JsonNode processStandardResponse(HttpRequest.Builder requestBuilder)
        throws Exception {
        HttpResponse<String> response = httpClient.send(
            requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
        return JsonUtils.MAPPER.readTree(response.body());
    }

    private StreamingResponseBody processStreamResponse(HttpRequest.Builder requestBuilder) {
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
                // 简单的错误处理：如果是客户端断开连接，忽略错误
                String errorMsg = e.getMessage();
                if (errorMsg != null &&
                    (errorMsg.contains("Broken pipe") || errorMsg.contains("Connection reset"))) {
                    return;
                }

                try {
                    String errorEvent = "data: {\"error\": \"" + e.getMessage() + "\"}\n\n";
                    outputStream.write(errorEvent.getBytes(StandardCharsets.UTF_8));
                    outputStream.flush();
                } catch (IOException ignored) {

                }
            }
        };
    }
}

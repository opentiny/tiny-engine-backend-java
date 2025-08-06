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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.common.utils.JsonUtils;
import com.tinyengine.it.config.OpenAIConfig;
import com.tinyengine.it.model.dto.ChatRequest;
import com.tinyengine.it.service.app.v1.AiChatV1Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * The type AiChat v1 service.
 *
 * @since 2025-08-06
 */
@Service
@Slf4j
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
    public Object chatCompletion(ChatRequest request) throws Exception {
        String requestBody = buildRequestBody(request);
        String apiKey = request.getApiKey() != null ? request.getApiKey() : config.getApiKey();
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
            .uri(URI.create(request.getBaseUrl() != null ? request.getBaseUrl() : config.getBaseUrl()))
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

    private String buildRequestBody(ChatRequest request) throws JsonProcessingException {
        Map<String, Object> body = new HashMap<>();
        body.put("model", request.getModel() != null ? request.getModel() : config.getDefaultModel());
        body.put("messages", request.getMessages());
        body.put("temperature", request.getTemperature());
        body.put("stream", request.isStream());

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
                HttpResponse<Stream<String>> response = httpClient.send(
                    requestBuilder.build(), HttpResponse.BodyHandlers.ofLines());
                try (Stream<String> lines = response.body()) {
                    lines.filter(line -> !line.isEmpty())
                        .forEach(line -> {
                            try {
                                 if (!line.startsWith("data:")) {
                                     line = "data: " + line;
                                 }
                                 if (!line.endsWith("\n\n")) {
                                     line = line + "\n\n";
                                 }
                                 outputStream.write(line.getBytes(StandardCharsets.UTF_8));
                                 outputStream.flush();
                                } catch (IOException e) {
                                    throw new ServiceException(ExceptionEnum.CM326.getResultCode(),
                                        ExceptionEnum.CM326.getResultMsg());
                            }
                        });
                }
            } catch (Exception e) {
                try {
                    String errorEvent = "data: " +
                        JsonUtils.encode(Map.of("error", e.getMessage())) + "\n\n";
                    outputStream.write(errorEvent.getBytes(StandardCharsets.UTF_8));
                    outputStream.flush();
                } catch (IOException ioException) {
                    throw new ServiceException(ExceptionEnum.CM326.getResultCode(), ExceptionEnum.CM326.getResultMsg());
                }
            } finally {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    // 忽略关闭异常
                }
            }
        };
    }
}

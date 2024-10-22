package com.tinyengine.it.gateway.ai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinyengine.it.config.AiChatConfig;
import com.tinyengine.it.model.dto.OpenAiBodyDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.Map;

@Slf4j
public class AiChatClient {
    private final Map<String, AiChatConfig.AiChatConfigData> config;
    private final WebClient webClient;

    public AiChatClient() {
        this.config = AiChatConfig.getAiChatConfig();
        this.webClient = WebClient.builder()
                .baseUrl("https://default.api.url") // Optional: Default base URL
                .build();
    }

    public Map<String, Object> executeChatRequest(OpenAiBodyDto openAiBodyDto) {
        AiChatConfig.AiChatConfigData configData = config.get(openAiBodyDto.getModel());
        if (configData == null) {
            log.error("No configuration found for model: " + openAiBodyDto.getModel());
            return Collections.emptyMap();
        }

        String httpRequestUrl = configData.httpRequestUrl;
        AiChatConfig.HttpRequestOption httpRequestOption = configData.httpRequestOption;

        log.info("URL: " + httpRequestUrl);
        log.info("Request Option: " + httpRequestOption.method);
        log.info("Headers: " + configData.headers);

        WebClient.RequestHeadersSpec<?> requestSpec = webClient
                .method(httpRequestOption.method.equalsIgnoreCase("POST") ? HttpMethod.POST : HttpMethod.GET)
                .uri(httpRequestUrl);

        for (Map.Entry<String, String> header : configData.headers.entrySet()) {
            requestSpec.header(header.getKey(), header.getValue());
        }


        if (httpRequestOption.method.equalsIgnoreCase("POST") && !openAiBodyDto.getMessages().isEmpty()) {
            requestSpec = ((WebClient.RequestBodySpec) requestSpec).bodyValue(openAiBodyDto); // Add request body
        }

        return requestSpec.retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    try {
                        return new ObjectMapper().readValue(response, new TypeReference<Map<String, Object>>() {
                        });
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .block(); // 等待结果
    }
}

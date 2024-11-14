package com.tinyengine.it.gateway.ai;

import static com.tinyengine.it.common.exception.ExceptionEnum.CM322;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.config.AiChatConfig;
import com.tinyengine.it.model.dto.OpenAiBodyDto;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.Map;

/**
 * The type Ai chat client.
 *
 * @since 2024-10-20
 */
@Slf4j
public class AiChatClient {
    private final Map<String, AiChatConfig.AiChatConfigData> config;
    private WebClient webClient;

    /**
     * Instantiates a new Ai chat client.
     */
    public AiChatClient() {
        this.config = AiChatConfig.getAiChatConfig();
        // Optional: Default base URL
        this.webClient = WebClient.builder().baseUrl("https://default.api.url").build();
    }

    /**
     * Execute chat request map.
     *
     * @param openAiBodyDto the open AI body dto
     * @return the map
     */
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

        HttpMethod method = "POST".equalsIgnoreCase(httpRequestOption.method) ? HttpMethod.POST : HttpMethod.GET;
        WebClient.RequestHeadersSpec<?> requestSpec = webClient.method(method).uri(httpRequestUrl);

        for (Map.Entry<String, String> header : configData.headers.entrySet()) {
            requestSpec.header(header.getKey(), header.getValue());
        }

        if ("POST".equalsIgnoreCase(httpRequestOption.method) && !openAiBodyDto.getMessages().isEmpty()) {
            if (requestSpec instanceof WebClient.RequestBodySpec) {
                requestSpec = ((WebClient.RequestBodySpec) requestSpec).bodyValue(openAiBodyDto);
                // Add request body
            }
        }

        Mono<String> stringMono = requestSpec.retrieve().bodyToMono(String.class);
        return stringMono.map(response -> {
            try {
                return new ObjectMapper().readValue(response, new TypeReference<Map<String, Object>>() {});
            } catch (JsonProcessingException e) {
                throw new ServiceException(CM322.getResultCode(), e.getMessage());
            }
            }).block(); // 等待结果
    }
}

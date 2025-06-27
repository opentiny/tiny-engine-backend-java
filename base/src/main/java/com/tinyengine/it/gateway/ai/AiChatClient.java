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

package com.tinyengine.it.gateway.ai;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tinyengine.it.common.utils.JsonUtils;
import com.tinyengine.it.config.AiChatConfig;
import com.tinyengine.it.model.dto.AiParam;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

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

    @Setter
    private RestTemplate restTemplate;

    /**
     * Instantiates a new Ai chat client.
     * @param model
     * @param token
     */
    public AiChatClient(String model, String token) {
        this.config = AiChatConfig.getAiChatConfig(model, token);
        // Use RestTemplate for WebMVC
        this.restTemplate = new RestTemplate();
    }

    /**
     * Execute chat request map.
     *
     * @param openAiBodyDto the open AI body dto
     * @return the map
     */
    public Map<String, Object> executeChatRequest(AiParam openAiBodyDto) {
        AiChatConfig.AiChatConfigData configData = config.get(openAiBodyDto.getFoundationModel().get("model"));
        if (configData == null) {
            log.error("No configuration found for model: " + openAiBodyDto.getFoundationModel().get("model"));
            return Collections.emptyMap();
        }

        String httpRequestUrl = configData.httpRequestUrl;
        AiChatConfig.HttpRequestOption httpRequestOption = configData.httpRequestOption;

        log.info("URL: " + httpRequestUrl);
        log.info("Request Option: " + httpRequestOption.method);
        log.info("Headers: " + configData.headers);

        HttpMethod method = "POST".equalsIgnoreCase(httpRequestOption.method) ? HttpMethod.POST : HttpMethod.GET;
        HttpHeaders headers = new HttpHeaders();
        configData.headers.forEach(headers::set);

        HttpEntity<AiParam> entity = new HttpEntity<>(openAiBodyDto, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(httpRequestUrl, method, entity, String.class);
        return JsonUtils.decode(responseEntity.getBody(), new TypeReference<Map<String, Object>>() {});
    }

}
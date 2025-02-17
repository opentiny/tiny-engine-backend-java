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

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

import com.tinyengine.it.config.AiChatConfig;
import com.tinyengine.it.model.dto.AiMessages;
import com.tinyengine.it.model.dto.AiParam;

import reactor.core.publisher.Mono;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * test case
 *
 * @since 2024-10-29
 */
class AiChatClientTest {
    @Mock
    private Map<String, AiChatConfig.AiChatConfigData> config;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private WebClient webClient;

    @InjectMocks
    private AiChatClient aiChatClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExecuteChatRequest() {
        HashMap<String, String> headers = new HashMap<String, String>() {
            {
                put("headers", "headers");
            }
        };
        String modelName = "ERNIE-4.0-8K";
        AiChatConfig.HttpRequestOption option =
                new AiChatConfig.HttpRequestOption("POST", "json", "json", 100);
        AiChatConfig.AiChatConfigData configData =
                new AiChatConfig.AiChatConfigData("httpRequestUrl", option, headers, null);

        when(config.get(modelName)).thenReturn(configData);

        WebClient.RequestBodyUriSpec bodyUriSpec =
                Mockito.mock(WebClient.RequestBodyUriSpec.class, RETURNS_DEEP_STUBS);
        Mono<String> mono = Mockito.mock(Mono.class, RETURNS_DEEP_STUBS);
        Map<String, Object> result = new HashMap<>();
        when(mono.map(any()).block()).thenReturn(result);
        when(bodyUriSpec.retrieve().bodyToMono(String.class)).thenReturn(mono);
        when(webClient.method(any(HttpMethod.class))).thenReturn(bodyUriSpec);
        WebClient.RequestHeadersSpec<?> requestSpec =
                Mockito.mock(WebClient.RequestHeadersSpec.class, RETURNS_DEEP_STUBS);

        when(bodyUriSpec.uri(anyString())).thenReturn(bodyUriSpec);
        HashMap<String, String> foundationModel = new HashMap<>();
        foundationModel.put("model", "ERNIE-4.0-8K");
        foundationModel.put("token","asdf");
        ArrayList<AiMessages> messages = new ArrayList<>();
        AiMessages aiMessages = new AiMessages();
        aiMessages.setContent("dddd编码时遵从以下几条要求aaa");
        aiMessages.setName("John");
        aiMessages.setRole("user");
        messages.add(aiMessages);
        AiParam param = new AiParam(foundationModel,Arrays.asList(aiMessages));
        Map<String, Object> returnData = aiChatClient.executeChatRequest(param);
        Assertions.assertNull(returnData.get("data"));
    }

    @Test
    void testInvalidTokenExecuteChatRequest() {
        HashMap<String, String> foundationModel = new HashMap<>();
        foundationModel.put("model", "gpt-3.5-turbo");
        foundationModel.put("token","你好");
        ArrayList<AiMessages> messages = new ArrayList<>();
        AiMessages aiMessages = new AiMessages();
        aiMessages.setContent("dddd编码时遵从以下几条要求aaa");
        messages.add(aiMessages);
        AiParam param = new AiParam(foundationModel,Arrays.asList(aiMessages));
        Map<String, Object> returnData = aiChatClient.executeChatRequest(param);
        Assertions.assertEquals("Invalid token format",returnData.get("error_message"));
    }
}


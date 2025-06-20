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

import static javax.management.Query.eq;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tinyengine.it.common.utils.TestUtil;
import com.tinyengine.it.config.AiChatConfig;
import com.tinyengine.it.model.dto.AiMessages;
import com.tinyengine.it.model.dto.AiParam;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * test case
 *
 * @since 2024-10-29
 */
class AiChatClientTest {
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExecuteChatRequest() {
        // 1. 构造测试数据
        String modelName = "ERNIE-4.0-8K";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        AiChatConfig.HttpRequestOption option = new AiChatConfig.HttpRequestOption("POST", "json", "json", 100);
        AiChatConfig.AiChatConfigData configData = new AiChatConfig.AiChatConfigData(
            "http://mock-api-url", option, headers, null);

        // 2. 构造 AiParam 请求参数
        Map<String, String> foundationModel = new HashMap<>();
        foundationModel.put("model", modelName);
        foundationModel.put("token", "mock-token");

        AiMessages message = new AiMessages();
        message.setRole("user");
        message.setContent("Hello, AI!");
        AiParam param = new AiParam(foundationModel, Collections.singletonList(message));

        // 3. Mock RestTemplate 的行为
        RestTemplate mockRestTemplate = Mockito.mock(RestTemplate.class);
        ResponseEntity<String> mockResponse = new ResponseEntity<>(
            "{\"data\":\"mock-response\"}", HttpStatus.OK);

        // 关键点：设置 Mock 行为，匹配任意参数
        when(mockRestTemplate.exchange(
            anyString(),        // 任意 URL
            any(HttpMethod.class), // 任意 HTTP 方法
            any(HttpEntity.class), // 任意请求体
            any(Class.class)    // 任意返回类型（如 String.class）
        )).thenReturn(mockResponse);

        // 4. 创建 AiChatClient 并注入 Mock 的 RestTemplate
        AiChatClient aiChatClient = new AiChatClient("ERNIE-4.0-8K", "mock-token");
        aiChatClient.setRestTemplate(mockRestTemplate);

        // 5. 调用方法并验证结果
        Map<String, Object> result = aiChatClient.executeChatRequest(param);
        assertEquals("mock-response", result.get("data"));  // 验证返回的数据
    }
}
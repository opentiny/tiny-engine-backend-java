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

package com.tinyengine.it.service.app.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.gateway.ai.AiChatClient;
import com.tinyengine.it.model.dto.AiMessages;
import com.tinyengine.it.model.dto.AiParam;
import com.tinyengine.it.model.dto.OpenAiBodyDto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * test case
 *
 * @since 2024-10-29
 */
class AiChatServiceImplTest {
    @Mock
    private AiChatClient aiChatClient;

    @InjectMocks
    private AiChatServiceImpl aiChatServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetStartAndEnd() {
        int[] result = AiChatServiceImpl.getStartAndEnd("<template>str</template>");
        Assertions.assertEquals(10, result[0]);
        Assertions.assertEquals(13, result[1]);
    }

    @Test
    void testGetAnswerFromAi() {
        AiParam aiParam = new AiParam();
        ArrayList<AiMessages> messages = new ArrayList<>();
        AiMessages aiMessage = new AiMessages();
        aiMessage.setContent("dddd编码时遵从以下几条要求aaa");
        aiMessage.setName("John");
        aiMessage.setRole("user");
        messages.add(aiMessage);
        aiParam.setMessages(messages);
        HashMap<String, String> foundationModel = new HashMap<>();
        foundationModel.put("model", "ERNIE-4.0-8K");
        aiParam.setFoundationModel(foundationModel);

        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("id", 1);
        List<Map<String, Object>> choices = new ArrayList<>();
        Map<String, Object> choice = new HashMap<>();
        Map<String, String> message = new HashMap<>();
        message.put("role", "assistant");
        message.put("content", (String) "<template>str</template>");
        message.put("name", "AI");
        choice.put("message", message);
        choices.add(choice);

        // 将 choices 添加到响应中
        dataMap.put("choices", choices);

        Map<String, Object> response = new HashMap<>();
        response.put("data", dataMap);
        response.put("result",(String) "<template>str</template>");
        when(aiChatClient.executeChatRequest(any(OpenAiBodyDto.class))).thenReturn(response);
        Result<Map<String, Object>> result = aiChatServiceImpl.getAnswerFromAi(aiParam);
        Map<String, Object> resultData = result.getData();

        Assertions.assertEquals("<template><代码在画布中展示></template>", resultData.get("replyWithoutCode"));
    }

    @Test
    void testExtractCode() {
        String result = aiChatServiceImpl.extractCode("<template>str</template>");
        Assertions.assertEquals("str", result);
    }

    @Test
    void testRemoveCode() {
        String result = aiChatServiceImpl.removeCode("<template>str</template>");
        Assertions.assertEquals("<template><代码在画布中展示></template>", result);
    }
}

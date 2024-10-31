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
        messages.add(aiMessage);
        aiParam.setMessages(messages);
        HashMap<String, String> foundationModel = new HashMap<>();
        foundationModel.put("model", "ERNIE-Bot-turbo");
        aiParam.setFoundationModel(foundationModel);

        Map<String, Object> response = new HashMap<>();
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("id",1);
        ArrayList<Object> choices = new ArrayList<>();
        Map<String, Object> originalChoice = new HashMap<>();
        originalChoice.put("text", "text");
        originalChoice.put("index", "index");
        HashMap<Object, Object> message = new HashMap<>();
        message.put("content","<template>str</template>");
        originalChoice.put("message", message);
        choices.add(originalChoice);
        dataMap.put("choices", choices);
        response.put("data", dataMap);
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

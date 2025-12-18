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

package com.tinyengine.it.service.app.adapter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for GeminiApiAdapter
 *
 * @since 2025-11-26
 */
class GeminiApiAdapterTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void testIsGeminiModel() {
        assertTrue(GeminiApiAdapter.isGeminiModel("gemini-pro"));
        assertTrue(GeminiApiAdapter.isGeminiModel("gemini-1.5-pro"));
        assertTrue(GeminiApiAdapter.isGeminiModel("gemini-1.5-flash"));
        assertTrue(GeminiApiAdapter.isGeminiModel("models/gemini-pro"));

        assertFalse(GeminiApiAdapter.isGeminiModel("gpt-3.5-turbo"));
        assertFalse(GeminiApiAdapter.isGeminiModel("deepseek-chat"));
        assertFalse(GeminiApiAdapter.isGeminiModel(null));
    }

    @Test
    void testConvertRequestToGemini_SimpleTextMessage() {
        // Prepare OpenAI format request
        Map<String, Object> openAiRequest = new HashMap<>();
        openAiRequest.put("model", "gemini-1.5-pro");

        List<Map<String, Object>> messages = new ArrayList<>();
        Map<String, Object> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", "Hello, how are you?");
        messages.add(userMessage);

        openAiRequest.put("messages", messages);
        openAiRequest.put("temperature", 0.7);
        openAiRequest.put("max_tokens", 1000);

        // Convert to Gemini format
        Map<String, Object> geminiRequest = GeminiApiAdapter.convertRequestToGemini(openAiRequest);

        // Verify conversion
        assertNotNull(geminiRequest);
        assertTrue(geminiRequest.containsKey("contents"));
        assertTrue(geminiRequest.containsKey("generationConfig"));

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> contents = (List<Map<String, Object>>) geminiRequest.get("contents");
        assertEquals(1, contents.size());
        assertEquals("user", contents.get(0).get("role"));

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> parts = (List<Map<String, Object>>) contents.get(0).get("parts");
        assertEquals(1, parts.size());
        assertEquals("Hello, how are you?", parts.get(0).get("text"));

        @SuppressWarnings("unchecked")
        Map<String, Object> genConfig = (Map<String, Object>) geminiRequest.get("generationConfig");
        assertEquals(0.7, genConfig.get("temperature"));
        assertEquals(1000, genConfig.get("maxOutputTokens"));
    }

    @Test
    void testConvertRequestToGemini_RoleMapping() {
        Map<String, Object> openAiRequest = new HashMap<>();

        List<Map<String, Object>> messages = new ArrayList<>();

        // System message
        Map<String, Object> systemMsg = new HashMap<>();
        systemMsg.put("role", "system");
        systemMsg.put("content", "You are a helpful assistant");
        messages.add(systemMsg);

        // User message
        Map<String, Object> userMsg = new HashMap<>();
        userMsg.put("role", "user");
        userMsg.put("content", "Hello");
        messages.add(userMsg);

        // Assistant message
        Map<String, Object> assistantMsg = new HashMap<>();
        assistantMsg.put("role", "assistant");
        assistantMsg.put("content", "Hi there!");
        messages.add(assistantMsg);

        openAiRequest.put("messages", messages);

        Map<String, Object> geminiRequest = GeminiApiAdapter.convertRequestToGemini(openAiRequest);

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> contents = (List<Map<String, Object>>) geminiRequest.get("contents");

        // System -> user
        assertEquals("user", contents.get(0).get("role"));

        // User -> user
        assertEquals("user", contents.get(1).get("role"));

        // Assistant -> model
        assertEquals("model", contents.get(2).get("role"));
    }

    @Test
    void testConvertResponseFromGemini() throws Exception {
        // Prepare Gemini response JSON
        String geminiResponseJson = """
            {
              "candidates": [
                {
                  "content": {
                    "parts": [
                      {
                        "text": "Hello! I'm doing well, thank you for asking."
                      }
                    ],
                    "role": "model"
                  },
                  "finishReason": "STOP"
                }
              ],
              "usageMetadata": {
                "promptTokenCount": 10,
                "candidatesTokenCount": 20,
                "totalTokenCount": 30
              }
            }
            """;

        JsonNode geminiResponse = mapper.readTree(geminiResponseJson);
        Map<String, Object> openAiResponse = GeminiApiAdapter.convertResponseFromGemini(geminiResponse, "gemini-1.5-pro");

        // Verify conversion
        assertNotNull(openAiResponse);
        assertEquals("chat.completion", openAiResponse.get("object"));
        assertEquals("gemini-1.5-pro", openAiResponse.get("model"));
        assertTrue(openAiResponse.containsKey("id"));
        assertTrue(openAiResponse.containsKey("created"));

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> choices = (List<Map<String, Object>>) openAiResponse.get("choices");
        assertEquals(1, choices.size());

        Map<String, Object> choice = choices.get(0);
        assertEquals(0, choice.get("index"));
        assertEquals("stop", choice.get("finish_reason"));

        @SuppressWarnings("unchecked")
        Map<String, Object> message = (Map<String, Object>) choice.get("message");
        assertEquals("assistant", message.get("role"));
        assertEquals("Hello! I'm doing well, thank you for asking.", message.get("content"));

        @SuppressWarnings("unchecked")
        Map<String, Object> usage = (Map<String, Object>) openAiResponse.get("usage");
        assertEquals(10, usage.get("prompt_tokens"));
        assertEquals(20, usage.get("completion_tokens"));
        assertEquals(30, usage.get("total_tokens"));
    }

    @Test
    void testConvertRequestToGemini_WithStopSequence() {
        Map<String, Object> openAiRequest = new HashMap<>();
        openAiRequest.put("messages", Collections.emptyList());
        openAiRequest.put("stop", Arrays.asList("END", "STOP"));

        Map<String, Object> geminiRequest = GeminiApiAdapter.convertRequestToGemini(openAiRequest);

        @SuppressWarnings("unchecked")
        Map<String, Object> genConfig = (Map<String, Object>) geminiRequest.get("generationConfig");

        @SuppressWarnings("unchecked")
        List<String> stopSequences = (List<String>) genConfig.get("stopSequences");
        assertEquals(2, stopSequences.size());
        assertTrue(stopSequences.contains("END"));
        assertTrue(stopSequences.contains("STOP"));
    }

    @Test
    void testConvertRequestToGemini_WithSingleStopString() {
        Map<String, Object> openAiRequest = new HashMap<>();
        openAiRequest.put("messages", Collections.emptyList());
        openAiRequest.put("stop", "END");

        Map<String, Object> geminiRequest = GeminiApiAdapter.convertRequestToGemini(openAiRequest);

        @SuppressWarnings("unchecked")
        Map<String, Object> genConfig = (Map<String, Object>) geminiRequest.get("generationConfig");

        @SuppressWarnings("unchecked")
        List<String> stopSequences = (List<String>) genConfig.get("stopSequences");
        assertEquals(1, stopSequences.size());
        assertEquals("END", stopSequences.get(0));
    }

    @Test
    void testConvertResponseFromGemini_EmptyCandidates() throws Exception {
        String geminiResponseJson = """
            {
              "candidates": []
            }
            """;

        JsonNode geminiResponse = mapper.readTree(geminiResponseJson);
        Map<String, Object> openAiResponse = GeminiApiAdapter.convertResponseFromGemini(geminiResponse, "gemini-1.5-pro");

        assertNotNull(openAiResponse);

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> choices = (List<Map<String, Object>>) openAiResponse.get("choices");
        assertEquals(1, choices.size());
    }
}


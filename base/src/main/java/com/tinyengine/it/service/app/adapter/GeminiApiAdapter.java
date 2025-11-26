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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Adapter for converting between OpenAI-style API and Gemini API formats
 *
 * @since 2025-11-26
 */
public class GeminiApiAdapter {
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Convert OpenAI-style request to Gemini API format
     *
     * @param openAiRequest the OpenAI-style request body
     * @return Gemini API request body
     */
    public static Map<String, Object> convertRequestToGemini(Map<String, Object> openAiRequest) {
        Map<String, Object> geminiRequest = new HashMap<>();

        // Convert messages to contents
        Object messages = openAiRequest.get("messages");
        if (messages instanceof List) {
            List<Map<String, Object>> contents = convertMessagesToContents((List<?>) messages);
            geminiRequest.put("contents", contents);
        }

        // Convert generation config
        Map<String, Object> generationConfig = new HashMap<>();

        if (openAiRequest.containsKey("temperature")) {
            generationConfig.put("temperature", openAiRequest.get("temperature"));
        }

        if (openAiRequest.containsKey("max_tokens")) {
            generationConfig.put("maxOutputTokens", openAiRequest.get("max_tokens"));
        }

        if (openAiRequest.containsKey("top_p")) {
            generationConfig.put("topP", openAiRequest.get("top_p"));
        }

        if (openAiRequest.containsKey("stop")) {
            Object stop = openAiRequest.get("stop");
            if (stop instanceof List) {
                generationConfig.put("stopSequences", stop);
            } else if (stop instanceof String) {
                generationConfig.put("stopSequences", List.of(stop));
            }
        }

        if (!generationConfig.isEmpty()) {
            geminiRequest.put("generationConfig", generationConfig);
        }

        return geminiRequest;
    }

    /**
     * Convert OpenAI-style messages to Gemini contents format
     */
    private static List<Map<String, Object>> convertMessagesToContents(List<?> messages) {
        List<Map<String, Object>> contents = new ArrayList<>();

        for (Object msg : messages) {
            if (!(msg instanceof Map)) {
                continue;
            }

            Map<?, ?> message = (Map<?, ?>) msg;
            String role = (String) message.get("role");
            Object content = message.get("content");

            Map<String, Object> geminiContent = new HashMap<>();

            // Map roles: user -> user, assistant -> model, system -> user
            if ("assistant".equals(role)) {
                geminiContent.put("role", "model");
            } else if ("system".equals(role)) {
                geminiContent.put("role", "user");
            } else {
                geminiContent.put("role", role);
            }

            // Convert content
            List<Map<String, Object>> parts = new ArrayList<>();

            if (content instanceof String) {
                Map<String, Object> part = new HashMap<>();
                part.put("text", content);
                parts.add(part);
            } else if (content instanceof List) {
                List<?> contentList = (List<?>) content;
                for (Object item : contentList) {
                    if (item instanceof Map) {
                        Map<?, ?> contentItem = (Map<?, ?>) item;
                        String type = (String) contentItem.get("type");

                        if ("text".equals(type)) {
                            Map<String, Object> part = new HashMap<>();
                            part.put("text", contentItem.get("text"));
                            parts.add(part);
                        } else if ("image_url".equals(type)) {
                            Map<?, ?> imageUrl = (Map<?, ?>) contentItem.get("image_url");
                            if (imageUrl != null) {
                                Map<String, Object> part = new HashMap<>();
                                Map<String, Object> inlineData = new HashMap<>();
                                String url = (String) imageUrl.get("url");

                                // Handle base64 images
                                if (url.startsWith("data:")) {
                                    String[] parts_url = url.split(",");
                                    if (parts_url.length == 2) {
                                        String mimeType = parts_url[0].split(";")[0].substring(5);
                                        inlineData.put("mimeType", mimeType);
                                        inlineData.put("data", parts_url[1]);
                                    }
                                }

                                part.put("inlineData", inlineData);
                                parts.add(part);
                            }
                        }
                    }
                }
            }

            geminiContent.put("parts", parts);
            contents.add(geminiContent);
        }

        return contents;
    }

    /**
     * Convert Gemini response to OpenAI-style response format
     *
     * @param geminiResponse the Gemini API response
     * @param model the model name
     * @return OpenAI-style response
     */
    public static Map<String, Object> convertResponseFromGemini(JsonNode geminiResponse, String model) {
        Map<String, Object> openAiResponse = new HashMap<>();

        openAiResponse.put("id", "gemini-" + System.currentTimeMillis());
        openAiResponse.put("object", "chat.completion");
        openAiResponse.put("created", System.currentTimeMillis() / 1000);
        openAiResponse.put("model", model);

        List<Map<String, Object>> choices = new ArrayList<>();
        Map<String, Object> choice = new HashMap<>();
        choice.put("index", 0);

        // Extract content from Gemini response
        JsonNode candidates = geminiResponse.get("candidates");
        if (candidates != null && candidates.isArray() && candidates.size() > 0) {
            JsonNode firstCandidate = candidates.get(0);
            JsonNode content = firstCandidate.get("content");

            if (content != null) {
                JsonNode parts = content.get("parts");
                StringBuilder textBuilder = new StringBuilder();

                if (parts != null && parts.isArray()) {
                    for (JsonNode part : parts) {
                        if (part.has("text")) {
                            textBuilder.append(part.get("text").asText());
                        }
                    }
                }

                Map<String, Object> message = new HashMap<>();
                message.put("role", "assistant");
                message.put("content", textBuilder.toString());
                choice.put("message", message);
            }

            // Add finish reason
            JsonNode finishReason = firstCandidate.get("finishReason");
            if (finishReason != null) {
                String reason = finishReason.asText().toLowerCase();
                // Map Gemini finish reasons to OpenAI format
                if ("STOP".equalsIgnoreCase(reason)) {
                    choice.put("finish_reason", "stop");
                } else if ("MAX_TOKENS".equalsIgnoreCase(reason)) {
                    choice.put("finish_reason", "length");
                } else {
                    choice.put("finish_reason", reason.toLowerCase());
                }
            } else {
                choice.put("finish_reason", "stop");
            }
        }

        choices.add(choice);
        openAiResponse.put("choices", choices);

        // Add usage information if available
        JsonNode usageMetadata = geminiResponse.get("usageMetadata");
        if (usageMetadata != null) {
            Map<String, Object> usage = new HashMap<>();
            if (usageMetadata.has("promptTokenCount")) {
                usage.put("prompt_tokens", usageMetadata.get("promptTokenCount").asInt());
            }
            if (usageMetadata.has("candidatesTokenCount")) {
                usage.put("completion_tokens", usageMetadata.get("candidatesTokenCount").asInt());
            }
            if (usageMetadata.has("totalTokenCount")) {
                usage.put("total_tokens", usageMetadata.get("totalTokenCount").asInt());
            }
            openAiResponse.put("usage", usage);
        }

        return openAiResponse;
    }

    /**
     * Check if a model is a Gemini model
     *
     * @param model the model name
     * @return true if it's a Gemini model
     */
    public static boolean isGeminiModel(String model) {
        return model != null && (model.startsWith("gemini-") || model.startsWith("models/gemini-"));
    }
}


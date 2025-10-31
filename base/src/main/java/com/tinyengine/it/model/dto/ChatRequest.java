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

package com.tinyengine.it.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * ChatRequest dto
 *
 * @since 2025-08-06
 */
@Data
public class ChatRequest {
    private String model;
    private String apiKey;
    private String baseUrl;
    private Object messages;
    private Object tools;
    private Float temperature;
    private boolean stream = false;
    @JsonProperty("max_tokens")
    private Integer maxTokens;
    @JsonProperty("stream_options")
    private Object streamOptions;
    @JsonProperty("presence_penalty")
    private Float presencePenalty;
    @JsonProperty("response_format")
    private Object responseFormat;
    @JsonProperty("max_input_tokens")
    private Integer maxInputTokens;
    @JsonProperty("vl_high_resolution_images")
    private Boolean vlHighResolutionImages = false;
    @JsonProperty("enable_thinking")
    private Boolean enableThinking;
    @JsonProperty("thinking_budget")
    private Integer thinkingBudget;
    private String stop;
    @JsonProperty("tool_choice")
    private Object toolChoice;
    @JsonProperty("parallel_tool_calls")
    private Boolean parallelToolCalls;
    @JsonProperty("enable_search")
    private Boolean enableSearch;
    @JsonProperty("search_options")
    private Object searchOptions;
    @JsonProperty("frequency_penalty")
    private Float frequencyPenalty;

}

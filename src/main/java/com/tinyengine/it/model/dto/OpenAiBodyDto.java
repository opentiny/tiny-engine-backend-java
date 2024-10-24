package com.tinyengine.it.model.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * The type Open ai body dto.
 */
@Data
public class OpenAiBodyDto {
    private String model;
    private List<Map<String, String>> messages;

    /**
     * Instantiates a new Open ai body dto.
     *
     * @param model    the model
     * @param messages the messages
     */
    public OpenAiBodyDto(String model, List<Map<String, String>> messages) {
        this.model = model;
        this.messages = messages;
    }
}

package com.tinyengine.it.model.dto;

import lombok.Data;

import java.util.List;

/**
 * The type Open AI body dto.
 *
 * @since 2024-10-20
 */
@Data
public class OpenAiBodyDto {
    private String model;
    private List<AiMessages> messages;

    /**
     * Instantiates a new Open AI body dto.
     *
     * @param model    the model
     * @param messages the messages
     */
    public OpenAiBodyDto(String model, List<AiMessages> messages) {
        this.model = model;
        this.messages = messages;
    }
}

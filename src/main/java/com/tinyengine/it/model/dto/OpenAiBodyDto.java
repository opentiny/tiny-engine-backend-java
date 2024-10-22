package com.tinyengine.it.model.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class OpenAiBodyDto {
    private String model;
    private List<Map<String, String>> messages;

    public OpenAiBodyDto(String model, List<Map<String, String>> messages) {
        this.model = model;
        this.messages = messages;
    }
}

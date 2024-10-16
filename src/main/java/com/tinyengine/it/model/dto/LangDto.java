package com.tinyengine.it.model.dto;

import lombok.Data;

@Data
public class LangDto {
    private String lang;

    public LangDto(String lang) {
        this.lang = lang;
    }
}

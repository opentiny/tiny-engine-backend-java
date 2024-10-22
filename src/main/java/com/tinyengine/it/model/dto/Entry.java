package com.tinyengine.it.model.dto;

import lombok.Data;

import java.util.Map;

@Data
public class Entry {
    private String key;
    private Map<String, String> contents;
}

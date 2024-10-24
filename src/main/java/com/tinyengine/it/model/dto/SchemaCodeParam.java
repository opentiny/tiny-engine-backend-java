package com.tinyengine.it.model.dto;

import lombok.Data;

import java.util.Map;

@Data
public class SchemaCodeParam {
    private Integer app;
    private Map<String, Object> pageInfo;
    private Integer platform;
}

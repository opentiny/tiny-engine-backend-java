package com.tinyengine.it.model.dto;

import lombok.Data;

import java.util.Map;

/**
 * The type Schema code param.
 */
@Data
public class SchemaCodeParam {
    private Integer app;
    private Map<String, Object> pageInfo;
    private Integer platform;
}

package com.tinyengine.it.model.dto;

import lombok.Data;

import java.util.Map;

/**
 * The type Schema code param.
 * @since 2024-10-20
 */
@Data
public class SchemaCodeParam {
    private Integer app;
    private Map<String, Object> pageInfo;
    private Integer platform;
}

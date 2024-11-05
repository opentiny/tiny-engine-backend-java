package com.tinyengine.it.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * SchemaUtils
 *
 * @since 2024-10-20
 */
@Getter
@Setter
public class SchemaUtils {
    private String name;
    private String type;
    private Map<String, Object> content;
}

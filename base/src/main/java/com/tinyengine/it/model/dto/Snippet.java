package com.tinyengine.it.model.dto;

import lombok.Data;

import java.util.Map;

/**
 * Snippet
 *
 * @since 2024-11-13
 */
@Data
public class Snippet {
    private Map<String, String> name;
    private String icon;
    private String screenshot;
    private String snippetName;
    private Map<String, Object> schema;

}

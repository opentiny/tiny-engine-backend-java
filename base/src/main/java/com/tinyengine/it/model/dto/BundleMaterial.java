package com.tinyengine.it.model.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * BundleMaterial
 *
 * @since 2024-11-13
 */
@Data
public class BundleMaterial {
    private List<Map<String, Object>> components;
    private List<Child> snippets;
    private List<Map<String, Object>> blocks;
}

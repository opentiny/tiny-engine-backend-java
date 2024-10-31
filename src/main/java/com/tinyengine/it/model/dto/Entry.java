package com.tinyengine.it.model.dto;

import lombok.Data;

import java.util.Map;

/**
 * The type Entry.
 *
 * @since 2024-10-20
 */
@Data
public class Entry {
    private String key;
    private Map<String, String> contents;
}

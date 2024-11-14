package com.tinyengine.it.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * The type EntriesItem.
 *
 * @since 2024-10-20
 */
@Setter
@Getter
public class EntriesItem {
    private Integer lang;
    private Map<String, Object> entries;
}

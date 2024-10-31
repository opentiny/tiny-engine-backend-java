package com.tinyengine.it.model.dto;

import lombok.Data;

import java.util.List;

/**
 * The type Delete i 18 n entry.
 *
 * @since 2024-10-20
 */
@Data
public class DeleteI18nEntry {
    private String host;
    private String hostType;
    private List<String> keyIn;
}

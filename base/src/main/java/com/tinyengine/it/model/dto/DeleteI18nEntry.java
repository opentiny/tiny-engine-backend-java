package com.tinyengine.it.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

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
    @JsonProperty("host_type")
    private String hostType;
    @JsonProperty("key_in")
    private List<String> keyIn;
}

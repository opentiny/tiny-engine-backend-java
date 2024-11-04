package com.tinyengine.it.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * The type Operate i 18 n batch entries.
 *
 * @since 2024-10-20
 */
@Data
public class OperateI18nBatchEntries {
    private List<Entry> entries;
    private String host;
    @JsonProperty("host_type")
    private String hostType;
}

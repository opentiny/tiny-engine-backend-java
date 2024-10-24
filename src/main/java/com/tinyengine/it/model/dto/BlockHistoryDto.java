package com.tinyengine.it.model.dto;

import lombok.Data;

/**
 * The type Block history dto.
 */
@Data
public class BlockHistoryDto {
    private Integer blockId;
    private Integer historyId;
    private String version;
}

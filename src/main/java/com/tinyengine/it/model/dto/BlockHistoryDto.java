package com.tinyengine.it.model.dto;

import lombok.Data;

@Data
public class BlockHistoryDto {
    private Integer blockId;
    private Integer historyId;
    private String version;
}

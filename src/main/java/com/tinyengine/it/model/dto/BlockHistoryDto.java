
package com.tinyengine.it.model.dto;

import lombok.Data;

/**
 * The type Block history dto.
 *
 * @since 2024-10-20
 */
@Data
public class BlockHistoryDto {
    private Integer blockId;
    private Integer historyId;
    private String version;
}

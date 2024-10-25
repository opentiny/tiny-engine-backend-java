
package com.tinyengine.it.model.dto;

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
    private String host_type;
}

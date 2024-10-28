
package com.tinyengine.it.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * The type Operate i 18 n entries.
 *
 * @since 2024-10-20
 */
@Data
@NoArgsConstructor
public class OperateI18nEntries {
    private String key;
    private String host;
    private String hostType;
    private Map<String, String> contents;
}


package com.tinyengine.it.model.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * The type Preview dto.
 *
 * @since 2024-10-20
 */
@Data
public class PreviewDto {
    private Map<String, Object> dataSource;
    private List<Map<String, Object>> globalState;
    private SchemaI18n i18n;
    private List<SchemaUtils> utils;

}

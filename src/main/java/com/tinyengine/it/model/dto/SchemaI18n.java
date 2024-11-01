package com.tinyengine.it.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * SchemaI18n
 *
 * @since 2024-10-20
 */
@Getter
@Setter
public class SchemaI18n {
    private Map<String, String> en_US;

    private Map<String, String> zh_CN;
}

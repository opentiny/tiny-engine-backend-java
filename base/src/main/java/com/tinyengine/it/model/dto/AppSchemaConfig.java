package com.tinyengine.it.model.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * AppSchemaConfig
 *
 * @since 2024-10-20
 */
@Getter
@Setter
public class AppSchemaConfig {
    private String sdkVersion;
    private String historyMode;
    private String targetRootID;
}

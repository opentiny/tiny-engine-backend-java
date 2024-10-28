package com.tinyengine.it.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppSchemaConfig {
    private String sdkVersion;
    private String historyMode;
    private String targetRootID;
}

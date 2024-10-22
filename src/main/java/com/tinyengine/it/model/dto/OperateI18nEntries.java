package com.tinyengine.it.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class OperateI18nEntries {
    private String key;
    private String host;
    private String host_type;
    private Map<String, String> contents;
}

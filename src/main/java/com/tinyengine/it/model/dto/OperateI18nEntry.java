package com.tinyengine.it.model.dto;

import lombok.Data;

@Data
public class OperateI18nEntry {
    private String entriesKey;
    private String content;
    private String host;
    private String host_type;
    private String lang;

}

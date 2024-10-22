package com.tinyengine.it.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class OperateI18nBatchEntries {
    private List<Entry> entries;
    private String host;
    private String host_type;
}

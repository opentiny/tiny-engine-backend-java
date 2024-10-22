package com.tinyengine.it.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class DeleteI18nEntry {
    private String host;
    private String host_type;
    private List<String> key_in;
}

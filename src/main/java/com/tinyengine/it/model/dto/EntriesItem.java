package com.tinyengine.it.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
@Setter
@Getter
public class EntriesItem {
    private Integer lang;
    private Map<String,Object> entries;
}

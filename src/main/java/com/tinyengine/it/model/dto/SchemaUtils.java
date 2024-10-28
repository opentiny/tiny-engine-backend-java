package com.tinyengine.it.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
@Getter
@Setter
public class SchemaUtils {
    private String name;
    private String type;
    private Map<String,Object> content;
}

package com.tinyengine.it.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
@Getter
@Setter
public class SchemaI18n {
    private Map<String,String> en_US;
    private Map<String,String> zh_CN;
}

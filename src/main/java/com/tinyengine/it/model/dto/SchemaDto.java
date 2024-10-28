package com.tinyengine.it.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
@Getter
@Setter
public class SchemaDto {
    private List<SchemaUtils> bridge;
    private  List<Map<String, Object>> componentsMap;
    private List<ComponentTree> componentTree;
    private Map<String,Object> config;
    private String constants;
    private String css;
    private SchemaDataSource dataSource;
    private SchemaI18n i18n;
    private SchemaMeta meta;
    private List<SchemaUtils> utils;
    private String version;
}

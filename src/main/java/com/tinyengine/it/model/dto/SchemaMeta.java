package com.tinyengine.it.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * SchemaMeta
 *
 * @since 2024-10-20
 */
@Getter
@Setter
public class SchemaMeta {
    private String appId;
    private String branch;
    private String creator;
    private String description;
    private String name;
    private String gitGroup;
    private List<Map<String, Object>> globalState;
    private String projectName;
    private Integer tenant;
    private String gmtCreate;
    private String gmtModified;
    private String isDemo;
}

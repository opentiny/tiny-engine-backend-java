package com.tinyengine.it.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
@Getter
@Setter
public class SchemaMeta {
    private String appId;
    private String branch;
    private String creator;
    private String description;
    private String name;
    private String git_group;
    private List<Map<String,Object>> global_state;
    private String project_name;
    private Integer tenant;
    private String gmt_create;
    private String gmt_modified;
    private String isDemo;


}

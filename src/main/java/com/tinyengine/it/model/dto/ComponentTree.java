package com.tinyengine.it.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ComponentTree {
    private List<Object> children;
    private String css;
    private String componentName;
    private String fileName;
    private Map<String,Object> lifeCycles;
    private Map<String,Object> meta;
    private Map<String,Object> methods;
    private Map<String,Object> props;
    private Map<String,Object> state;

}

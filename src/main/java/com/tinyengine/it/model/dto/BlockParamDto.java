package com.tinyengine.it.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BlockParamDto {
    private String appId;
    private String sort;

    @JsonProperty("name_cn")
    private String name;

    private String description;
    private String label;
    private String limit;
    private String start;


}

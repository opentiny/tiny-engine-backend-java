package com.tinyengine.it.model.dto;

import lombok.Data;

@Data
public class BlockBuildDto {
    private Blocks blocks;
    private String deploy_info;
    private Boolean is_compile;
    private Boolean needToSave;
    private String version;
}

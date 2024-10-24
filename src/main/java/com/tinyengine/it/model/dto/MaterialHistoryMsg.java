package com.tinyengine.it.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MaterialHistoryMsg {
    @Schema(name = "isUnpkg", description = "是否已发布")
    private Boolean isUnpkg;
    @Schema(name = "materialHistoryId", description = "物料历史id")
    private Integer materialHistoryId;
}

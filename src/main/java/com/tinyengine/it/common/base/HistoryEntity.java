package com.tinyengine.it.common.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 历史数据对象
 */
@Getter
@Setter
public class HistoryEntity extends BaseEntity {
    @Schema(name = "refId", description = "关联主表id")
    private Integer refId;

    @Schema(name = "version", description = "版本")
    private String version;
}

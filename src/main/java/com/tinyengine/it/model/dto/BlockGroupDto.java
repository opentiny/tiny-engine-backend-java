package com.tinyengine.it.model.dto;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinyengine.it.common.base.BaseEntity;
import com.tinyengine.it.model.entity.App;
import com.tinyengine.it.model.entity.Block;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@TableName("t_block_group")
@Schema(name = "BlockGroup对象", description = "返回对象")
public class BlockGroupDto extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(name = "name", description = "分组名称")
    private String name;

    @Schema(name = "appId", description = "创建分组所在app")
    @JsonProperty("app_id")
    private Integer appId;

    @Schema(name = "platformId", description = "设计器id")
    @JsonProperty("platform_id")
    private Integer platformId;

    @Schema(name = "description", description = "分组描述")
    private String description;

    @JsonProperty("app")
    private App app;

    @TableField(exist = false)
    @Schema(name = "blocks", description = "区块")
    private List<Block> blocks = new ArrayList<>();

}
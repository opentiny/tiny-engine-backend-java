package com.tinyengine.it.model.dto;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class BlockGroupDto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(name = "id", description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

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

    @Schema(name = "createdBy", description = "创建人")
    @JsonProperty("created_by")
    private String createdBy;

    @Schema(name = "lastUpdatedBy", description = "最后修改人")
    @JsonProperty("last_updated_by")
    private String lastUpdatedBy;

    @Schema(name = "createdTime", description = "创建时间")
    @JsonProperty("created_time")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    @Schema(name = "lastUpdatedTime", description = "更新时间")
    @JsonProperty("last_updated_time")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime lastUpdatedTime;

    @Schema(name = "tenantId", description = "租户id")
    @JsonProperty("tenant_id")
    private String tenantId;

    @Schema(name = "siteId", description = "站点id")
    @JsonProperty("site_id")
    private String siteId;

    @JsonProperty("app")
    private App app;

    @TableField(exist = false)
    @Schema(name = "blocks", description = "区块")
    private List<Block> blocks = new ArrayList<>();

}
package com.tinyengine.it.model.dto;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@TableName("block_groups")
@Schema(name = "BlockGroups对象", description = "返回对象")
public class BlockGroupsDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "id", description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(name = "name", description = "分组名称")
    private String name;
    @JsonProperty("appId")
    @Schema(name = "appId", description = "关联appId")
    private Integer appId;

    @Schema(name = "desc", description = "分组描述")
    private String desc;

    @JsonProperty("created_at")
    @Schema(name = "createdAt", description = "创建时间")
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    @Schema(name = "updatedAt", description = "更新时间")
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @Schema(name = "createdBy", description = "创建人")
    @TableField(value = "createdBy", fill = FieldFill.INSERT)
    private Integer createdBy;

    @Schema(name = "updatedBy", description = "更新人")
    @TableField(value = "updatedBy", fill = FieldFill.INSERT)
    private Integer updatedBy;

    @JsonProperty("app")
    private Apps app;

    @TableField(exist = false)
    @Schema(name = "blocks", description = "区块")
    private List<Blocks> blocks = new ArrayList<>();

}
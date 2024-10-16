package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author zhangjuncao
 * @since 2024-04-19
 */
@Getter
@Setter
@TableName("blocks_carriers_relations")
@Schema(name = "BlocksCarriersRelations对象", description = "返回对象")
public class BlocksCarriersRelations implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "id", description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(name = "block", description = "区块id")
    private Integer block;

    @Schema(name = "host", description = "hostId: 区块分组id， 物料历史记录id")
    private Integer host;

    @JsonProperty("host_type")
    @Schema(name = "hostType", description = "host类型：blockGroup, materialHistory")
    private String hostType;

    @Schema(name = "version", description = "版本")
    private String version;

    @TableField(fill = FieldFill.INSERT)
    @JsonProperty("created_at")
    @Schema(name = "created_at", type = "LocalDateTime", description = "创建时间")
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonProperty("updated_at")
    @Schema(name = "updated_at", type = "LocalDateTime", description = "更新时间")
    private LocalDateTime updatedAt;

    @Schema(name = "createdBy", description = "创建人")
    @TableField(value = "createdBy", fill = FieldFill.INSERT)
    private Integer createdBy;

    @Schema(name = "updatedBy", description = "更新人")
    @TableField(value = "updatedBy", fill = FieldFill.INSERT)
    private Integer updatedBy;


}

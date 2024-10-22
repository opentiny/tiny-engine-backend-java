package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 区块分组
 * </p>
 *
 * @author lu-yg
 * @since 2024-10-17
 */
@Getter
@Setter
@TableName("t_block_group")
@Schema(name = "BlockGroup", description = "区块分组")
public class BlockGroup implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(name= "id", description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(name= "name", description = "分组名称")
    private String name;

    @Schema(name= "appId", description = "创建分组所在app")
    private Integer appId;

    @Schema(name= "platformId", description = "设计器id")
    private Integer platformId;

    @Schema(name= "description", description = "分组描述")
    private String description;

    @Schema(name= "createdBy", description = "创建人")
    private String createdBy;

    @Schema(name= "lastUpdatedBy", description = "最后修改人")
    private String lastUpdatedBy;

    @Schema(name= "createdTime", description = "创建时间")
    private LocalDateTime createdTime;

    @Schema(name= "lastUpdatedTime", description = "更新时间")
    private LocalDateTime lastUpdatedTime;

    @Schema(name= "tenantId", description = "租户id")
    private String tenantId;

    @Schema(name= "siteId", description = "站点id")
    private String siteId;

}

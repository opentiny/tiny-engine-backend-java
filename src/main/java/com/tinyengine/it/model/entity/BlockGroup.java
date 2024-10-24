package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tinyengine.it.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 区块分组
 * </p>
 *
 * @author lu -yg
 * @since 2024 -10-17
 */
@Getter
@Setter
@TableName("t_block_group")
@Schema(name = "BlockGroup", description = "区块分组")
public class BlockGroup extends BaseEntity {
    @Schema(name = "name", description = "分组名称")
    private String name;

    @Schema(name = "appId", description = "创建分组所在app")
    private Integer appId;

    @Schema(name = "platformId", description = "设计器id")
    private Integer platformId;

    @Schema(name = "description", description = "分组描述")
    private String description;
}

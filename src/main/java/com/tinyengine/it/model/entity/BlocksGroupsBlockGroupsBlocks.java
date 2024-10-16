package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author zhangjuncao
 * @since 2024-06-06
 */
@Getter
@Setter
@TableName("blocks_groups__block_groups_blocks")
@Schema(name = "BlocksGroupsBlockGroupsBlocks对象", description = "返回对象")
public class BlocksGroupsBlockGroupsBlocks implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "id", description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @JsonProperty("block_group_id")
    @Schema(name = "blockGroupId", description = "区块分组id")
    private Integer blockGroupId;

    @JsonProperty("block_id")
    @Schema(name = "blockId", description = "区块id")
    private Integer blockId;


}

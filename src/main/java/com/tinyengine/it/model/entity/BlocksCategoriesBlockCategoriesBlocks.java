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
 * @since 2024-05-27
 */
@Getter
@Setter
@TableName("blocks_categories__block_categories_blocks")
@Schema(name = "blocks_categories__block_categories_blocks", description = "返回对象")
public class BlocksCategoriesBlockCategoriesBlocks implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "id", description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @JsonProperty("block-category_id")
    @Schema(name = "blockCategoryId", description = "区块分类id")
    private Integer blockCategoryId;

    @JsonProperty("block_id")
    @Schema(name = "blockId", description = "区块id")
    private Integer blockId;


}

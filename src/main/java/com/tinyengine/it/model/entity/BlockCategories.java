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
 * 区块分类
 * </p>
 *
 * @author zhangjuncao
 * @since 2024-05-23
 */
@Getter
@Setter
@TableName("block_categories")
@Schema(name = "BlockCategories对象", description = "返回对象")
public class BlockCategories implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(name = "name", description = "区块分类名称")
    private String name;

    @Schema(name = "app", description = "关联appId")
    @JsonProperty("app")
    private Integer app;

    @Schema(name = "desc", description = "区块分类描述")
    private String desc;

    @Schema(name = "createdBy", description = "创建人Id")
    @TableField(value = "createdBy", fill = FieldFill.INSERT)
    private Integer createdBy;

    @Schema(name = "updatedBy", description = "更新人Id")
    @TableField(value = "updatedBy", fill = FieldFill.INSERT)
    private Integer updatedBy;

    @JsonProperty("created_at")
    @Schema(name = "createdAt", description = "创建时间")
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    @Schema(name = "updatedAt", description = "更新时间")
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @Schema(name = "category_id", description = "区块分类唯一id,业务用")
    @JsonProperty("category_id")
    @TableField("category_id")
    private String categoryId;


}

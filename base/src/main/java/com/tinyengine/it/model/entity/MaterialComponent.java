package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 物料组件关系表
 * </p>
 *
 * @since 2024-1-14
 */
@Data
@TableName("r_material_component")
@Schema(name = "r_material_component", description = "物料组件关系表")
public class MaterialComponent {
    @Schema(name = "id", description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(name = "materialId", description = "物料id")
    private Integer materialId;

    @Schema(name = "componentId", description = "组件id")
    private Integer componentId;
}

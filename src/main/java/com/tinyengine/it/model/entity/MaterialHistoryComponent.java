package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 物料包历史组件关系表
 * </p>
 *
 * @author zhangjuncao
 * @since 2024-10-17
 */
@Getter
@Setter
@TableName("r_material_history_component")
@Schema(name = "MaterialHistoryComponent", description = "物料包历史组件关系表")
public class MaterialHistoryComponent implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(name= "id", description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(name= "materialHistoryId", description = "物料包历史ID")
    private Integer materialHistoryId;

    @Schema(name= "componentId", description = "组件ID")
    private Integer componentId;

}

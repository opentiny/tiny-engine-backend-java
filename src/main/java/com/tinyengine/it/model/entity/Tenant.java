package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tinyengine.it.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 组织表
 * </p>
 *
 * @author lu -yg
 * @since 2024-10-17
 */
@Getter
@Setter
@TableName("t_tenant")
@Schema(name = "Tenant", description = "组织表")
public class Tenant extends BaseEntity {
    @Schema(name = "nameCn", description = "组织唯一代码")
    private String orgCode;

    @Schema(name = "nameCn", description = "组织中文名")
    private String nameCn;

    @Schema(name = "nameEn", description = "组织英文名")
    private String nameEn;

    @Schema(name = "description", description = "组织描述")
    private String description;
}

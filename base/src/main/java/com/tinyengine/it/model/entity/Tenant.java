package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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
public class Tenant {
    @Schema(name = "id", description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(name = "nameCn", description = "组织唯一代码")
    private String orgCode;

    @Schema(name = "nameCn", description = "组织中文名")
    private String nameCn;

    @Schema(name = "nameEn", description = "组织英文名")
    private String nameEn;

    @Schema(name = "description", description = "组织描述")
    private String description;

    @TableField(fill = FieldFill.INSERT)
    @Schema(name = "renterId", description = "业务租户ID")
    private String renterId;

    @TableField(fill = FieldFill.INSERT)
    @Schema(name = "createdTime", description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    @TableField(fill = FieldFill.INSERT)
    @Schema(name = "createdBy", description = "创建人")
    private String createdBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(name = "lastUpdatedBy", description = "最后修改人")
    private String lastUpdatedBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(name = "lastUpdatedTime", description = "更新时间")
    private LocalDateTime lastUpdatedTime;
}

package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 *
 *  权限管理
 *
 *
 * @since 2025-11-19
 */
@Getter
@Setter
@TableName("r_auth_users_units_roles")
public class AuthUsersUnitsRoles {
    @Schema(name = "id", description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(name = "userId", description = "用户id")
    private Integer userId;

    @Schema(name = "unitId", description = "业务单元id")
    private Integer unitId;

    @Schema(name = "unitType", description = "业务单元类型")
    private String unitType;

    @Schema(name = "tenantId", description = "租户id")
    private Integer tenantId;

    @Schema(name = "roleId", description = "角色id")
    private Integer roleId;

    @Schema(name = "expiredTime", description = "过期时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expiredTime;

    @TableField(fill = FieldFill.INSERT)
    @Schema(name = "createdBy", description = "创建人")
    private String createdBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(name = "lastUpdatedBy", description = "最后修改人")
    private String lastUpdatedBy;

    @TableField(fill = FieldFill.INSERT)
    @Schema(name = "createdTime", description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("created_at")
    private LocalDateTime createdTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(name = "lastUpdatedTime", description = "更新时间")
    @JsonProperty("updated_at")
    private LocalDateTime lastUpdatedTime;
}

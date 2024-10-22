package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户权限表
 * </p>
 *
 * @author zhangjuncao
 * @since 2024-10-17
 */
@Getter
@Setter
@TableName("t_user")
@Schema(name = "User", description = "用户权限表")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(name= "id", description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(name= "username", description = "用户名")
    private String username;

    @Schema(name= "email", description = "邮箱")
    private String email;

    @Schema(name= "enable", description = "账号是否可用")
    private Boolean enable;

    @Schema(name= "createdBy", description = "创建人")
    @TableField(value = "createdBy", fill = FieldFill.INSERT)
    private String createdBy;

    @Schema(name= "lastUpdatedBy", description = "最后修改人")
    @TableField(value = "lastUpdatedBy", fill = FieldFill.INSERT_UPDATE)
    private String lastUpdatedBy;

    @Schema(name= "createdTime", description = "创建时间")
    @TableField(value = "createdTime", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    @Schema(name= "lastUpdatedTime", description = "更新时间")
    @TableField(value = "lastUpdatedTime", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime lastUpdatedTime;

    @Schema(name= "tenantId", description = "租户ID")
    private String tenantId;

    @Schema(name= "siteId", description = "站点ID")
    private String siteId;

    private Boolean isAdmin;

    private Boolean isPublic;

}

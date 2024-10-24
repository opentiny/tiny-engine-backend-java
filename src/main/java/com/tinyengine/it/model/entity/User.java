package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tinyengine.it.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户权限表
 * </p>
 *
 * @author zhangjuncao
 * @since 2024 -10-17
 */
@Getter
@Setter
@TableName("t_user")
@Schema(name = "User", description = "用户权限表")
public class User extends BaseEntity {
    @Schema(name = "username", description = "用户名")
    private String username;

    @Schema(name = "email", description = "邮箱")
    private String email;

    @Schema(name = "enable", description = "账号是否可用")
    private Boolean enable;

    private Boolean isAdmin;

    private Boolean isPublic;
}

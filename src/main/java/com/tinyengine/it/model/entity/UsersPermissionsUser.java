package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author zhangjuncoa
 * @since 2024-06-24
 */
@Getter
@Setter
@TableName("users-permissions_user")
public class UsersPermissionsUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String username;

    private String email;

    private String provider;

    private String password;

    private String resetPasswordToken;

    private String confirmationToken;

    private Boolean confirmed;

    private Boolean blocked;

    private Integer role;

    private Boolean isAdmin;

    private Boolean isPublic;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Integer createdBy;

    private Integer updatedBy;


}

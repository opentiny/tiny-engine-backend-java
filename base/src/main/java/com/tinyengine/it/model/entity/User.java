/**
 * Copyright (c) 2023 - present TinyEngine Authors.
 * Copyright (c) 2023 - present Huawei Cloud Computing Technologies Co., Ltd.
 *
 * Use of this source code is governed by an MIT-style license.
 *
 * THE OPEN SOURCE SOFTWARE IN THIS PRODUCT IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL,
 * BUT WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS FOR
 * A PARTICULAR PURPOSE. SEE THE APPLICABLE LICENSES FOR MORE DETAILS.
 *
 */

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
import java.util.ArrayList;
import java.util.List;

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
public class User {
    @Schema(name = "id", description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @Schema(name = "username", description = "用户名")
    private String username;

    @Schema(name = "email", description = "邮箱")
    private String email;

    @Schema(name = "password", description = "密码")
    private String password;

    @Schema(name = "salt", description = "盐")
    private String salt;

    @Schema(name = "public_key", description = "公钥")
    private String publicKey;

    @Schema(name = "private_key", description = "私钥")
    private String privateKey;

    @TableField("enable")
    @Schema(name = "enable", description = "账号是否可用")
    private Boolean isEnable;

    @Schema(name = "isAdmin", description = "是否管理员")
    private Boolean isAdmin;

    @Schema(name = "isPublic", description = "是否公共账号")
    private Boolean isPublic;

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

    @TableField(exist = false)
    @Schema(name = "tenant", description = "组织")
    private List<Tenant> tenant = new ArrayList<>();
}

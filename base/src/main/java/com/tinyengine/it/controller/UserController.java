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

package com.tinyengine.it.controller;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.context.LoginUserContext;
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.common.log.SystemControllerLog;
import com.tinyengine.it.mapper.AuthUsersUnitsRolesMapper;
import com.tinyengine.it.model.entity.Tenant;
import com.tinyengine.it.model.entity.User;
import com.tinyengine.it.service.app.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 查询用户信息
 *
 * @since 2024-10-20
 */
@Validated
@RestController
@CrossOrigin
@RequestMapping("/platform-center/api")
@Tag(name = "用户")
public class UserController {
    /**
     * The User service.
     */
    @Autowired
    private UserService userService;

    /**
     * The loginUserContext service.
     */
    @Autowired
    private LoginUserContext loginUserContext;

    @Autowired
    AuthUsersUnitsRolesMapper authUsersUnitsRolesMapper;

    /**
     * Me result.
     *
     * @return the result
     */
    @Operation(summary = "获取用户信息", description = "获取用户信息", responses = {
        @ApiResponse(responseCode = "200", description = "返回信息",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "获取用户信息")
    @GetMapping("/user/me")
    public Result<User> me() {
        String loginUserId = loginUserContext.getLoginUserId();
        if (loginUserId == null) {
            return Result.failed(ExceptionEnum.CM009);
        }
        Integer userId = Integer.valueOf(loginUserId);
        List<Tenant> tenants = authUsersUnitsRolesMapper.queryAllTenantByUserId(userId);
        User user = userService.queryUserById(loginUserId);
        if (user == null) {
            user = new User();
            user.setId(loginUserContext.getLoginUserId());
            user.setUsername(loginUserContext.getLoginUserId());
        }
        user.setTenant(tenants);

        return Result.success(user);
    }

}

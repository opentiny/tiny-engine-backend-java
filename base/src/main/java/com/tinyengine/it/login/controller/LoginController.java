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

package com.tinyengine.it.login.controller;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.context.LoginUserContext;
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.common.log.SystemControllerLog;
import com.tinyengine.it.login.utils.JwtUtil;
import com.tinyengine.it.login.utils.SM3PasswordUtil;
import com.tinyengine.it.login.config.context.DefaultLoginUserContext;
import com.tinyengine.it.login.model.PasswordResult;
import com.tinyengine.it.login.model.PasswordValidationResult;
import com.tinyengine.it.login.model.SSOTicket;
import com.tinyengine.it.login.model.ValidationResult;
import com.tinyengine.it.login.service.ConfigurablePasswordValidator;
import com.tinyengine.it.login.service.LoginService;
import com.tinyengine.it.login.service.TokenBlacklistService;
import com.tinyengine.it.mapper.AuthUsersUnitsRolesMapper;
import com.tinyengine.it.model.entity.App;
import com.tinyengine.it.model.entity.Tenant;
import com.tinyengine.it.model.entity.User;
import com.tinyengine.it.service.app.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

import static com.tinyengine.it.login.utils.SM2EncryptionUtil.decrypt;
import static com.tinyengine.it.login.utils.SM2EncryptionUtil.getPrivateKeyFromBase64;

/**
 * Login Controller
 */
@Validated
@RestController
@CrossOrigin
@RequestMapping("/platform-center/api")
public class LoginController {
    /**
     * The User service.
     */
    @Autowired
    private UserService userService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @Autowired
    ConfigurablePasswordValidator configurablePasswordValidator;

    @Autowired
    AuthUsersUnitsRolesMapper authUsersUnitsRolesMapper;

    @Autowired
    LoginUserContext loginUserContext;

    /**
     * 注册
     *
     * @param user the user
     * @return user信息 result
     */
    @Operation(summary = "注册", description = "注册",
        parameters = {
            @Parameter(name = "user", description = "User入参对象")
        }, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = App.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "注册")
    @PostMapping("/user/register")
    public Result createUser(@Valid @RequestBody User user) throws Exception {
        PasswordValidationResult passwordValidationResult = configurablePasswordValidator
            .validateWithPolicy(user.getPassword());
        if (!passwordValidationResult.isValid()) {
            return Result.failed("密码格式检验失败", passwordValidationResult.getErrorMessage());
        }
        PasswordResult password = SM3PasswordUtil.createPassword(user.getPassword());
        user.setPassword(password.getPasswordHash());
        user.setSalt(password.getSalt());
        User userResult = loginService.createUser(user);
        return Result.success(userResult);
    }

    /**
     * 登录
     *
     * @param user the user
     * @return SSOTicket result
     */
    @Operation(summary = "登录", description = "登录",
        parameters = {
            @Parameter(name = "user", description = "User入参对象")
        }, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = App.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "登录")
    @PostMapping("/user/login")
    public Result<SSOTicket> login(@RequestBody User user) throws Exception {
        // 验证用户名密码
        User userParam = new User();
        userParam.setUsername(user.getUsername());
        List<User> users = userService.queryUserByCondition(userParam);
        if (users == null || users.isEmpty()) {
            return Result.failed(ExceptionEnum.CM338);
        }
        User userResult = users.get(0);

        PrivateKey privateKey = getPrivateKeyFromBase64(userResult.getPrivateKey());
        String salt = decrypt(userResult.getSalt(), privateKey);
        if (authenticate(salt, user.getPassword(), userResult.getPassword())) {
            List<Tenant> tenants = authUsersUnitsRolesMapper.queryAllTenantByUserId(Integer.valueOf(userResult.getId()));
            String token = jwtUtil.generateToken(user.getUsername(), "USER", userResult.getId(),
                    tenants, 1);

            // 创建SSO票据
            SSOTicket ticket = new SSOTicket();
            ticket.setToken(token);
            ticket.setUsername(user.getUsername());
            ticket.setExpireTime(System.currentTimeMillis() + 3600000);

            return Result.success(ticket);
        }
        return Result.failed(ExceptionEnum.CM004);
    }

    /**
     * 忘记密码
     *
     * @param user the user
     * @return SSOTicket result
     */
    @Operation(summary = "忘记密码", description = "忘记密码",
        parameters = {
            @Parameter(name = "user", description = "User入参对象")
        }, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = App.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "忘记密码")
    @PostMapping("/user/forgot-password")
    public Result forgotPassword(@RequestBody User user) throws Exception {
        PasswordValidationResult passwordValidationResult = configurablePasswordValidator
            .validateWithPolicy(user.getPassword());
        if (!passwordValidationResult.isValid()) {
            return Result.success(passwordValidationResult);
        }
        PasswordResult password = SM3PasswordUtil.createPassword(user.getPassword());
        user.setPassword(password.getPasswordHash());
        user.setSalt(password.getSalt());
        return loginService.forgotPassword(user);
    }

    /**
     * 验证令牌
     *
     * @param token the token
     * @return ValidationResult result
     */
    @Operation(summary = "验证令牌", description = "验证令牌",
        parameters = {
            @Parameter(name = "user", description = "User入参对象")
        }, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = App.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "验证令牌")
    @GetMapping("/user/validate")
    public Result<ValidationResult> validateToken(@RequestParam String token) {
        if (jwtUtil.validateToken(token)) {
            String username = jwtUtil.getUsernameFromToken(token);
            return Result.success(new ValidationResult(true, username));
        }
        return Result.success(new ValidationResult(false, null));
    }

    /**
     * 设置当前组织
     *
     * @param tenantId the tenantId
     * @return result
     */
    @Operation(summary = "设置当前组织", description = "设置当前组织",
        parameters = {
            @Parameter(name = "tenantId", description = "组织id")
        }, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = App.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "设置当前组织")
    @GetMapping("/user/tenant")
    public Result<SSOTicket> setTenant(@RequestParam Integer tenantId) {
        List<Tenant> tenants = loginUserContext.getTenants();
        if (tenants == null || tenants.isEmpty()) {
            return Result.failed(ExceptionEnum.CM337);
        }
        List<Tenant> currentTenant = new ArrayList<>();
        for (Tenant tenant : tenants) {
            if (tenant.getId().equals(tenantId.toString())) {
                currentTenant.add(tenant);
            }
        }
        if (currentTenant.isEmpty()) {
            return Result.failed(ExceptionEnum.CM337);
        }
        // 通过 RequestContextHolder 获取请求
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
            .getRequest();
        String authHeader = request.getHeader("Authorization");
        String headerToken = jwtUtil.getTokenFromRequest(authHeader);
        if (headerToken == null || headerToken.isEmpty()) {
            return Result.failed(ExceptionEnum.CM336);
        }
        String token = jwtUtil.generateTokenWithSelectedTenant(headerToken, currentTenant);
        // 将原 token 加入黑名单
        Claims claims = Jwts.parser()
            .verifyWith(JwtUtil.getSecretKey())
            .build()
            .parseSignedClaims(headerToken)
            .getPayload();

        long expiryTime = claims.getExpiration().getTime();
        tokenBlacklistService.blacklistToken(headerToken, expiryTime);
        // 创建SSO票据
        SSOTicket ticket = new SSOTicket();
        ticket.setToken(token);
        ticket.setUsername(DefaultLoginUserContext.getCurrentUser().getUsername());
        ticket.setExpireTime(System.currentTimeMillis() + 3600000);

        return Result.success(ticket);
    }
    private boolean authenticate(String salt, String password, String userPassword) throws Exception {
        return SM3PasswordUtil.verifyPassword(password, userPassword, salt);
    }
}

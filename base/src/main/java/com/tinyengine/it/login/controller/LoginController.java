package com.tinyengine.it.login.controller;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.context.LoginUserContext;
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.common.log.SystemControllerLog;
import com.tinyengine.it.login.Utils.JwtUtil;
import com.tinyengine.it.login.Utils.SM3PasswordUtil;
import com.tinyengine.it.login.model.PasswordResult;
import com.tinyengine.it.login.model.SSOTicket;
import com.tinyengine.it.login.model.ValidationResult;
import com.tinyengine.it.model.entity.App;
import com.tinyengine.it.model.entity.User;
import com.tinyengine.it.service.app.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

import java.util.List;

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
    private JwtUtil jwtUtil;

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
    public Result<User> createUser(@Valid @RequestBody User user) throws Exception {
        PasswordResult password = SM3PasswordUtil.createPassword(user.getPassword());
        user.setPassword(password.getPasswordHash());
        user.setSalt(password.getSalt());
        User userResult = userService.createUser(user);
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
        if(users.isEmpty()){
            Result.failed(ExceptionEnum.CM004);
        }
        User userResult = users.get(0);
        if (authenticate(userResult.getSalt(), user.getPassword(),userResult.getPassword())) {

            String token = jwtUtil.generateToken(user.getUsername(), "USER", userResult.getId(),
            "1", "1", 1, "1");

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

    private boolean authenticate(String salt, String password, String userPassword) throws Exception {
        return SM3PasswordUtil.verifyPassword(password, userPassword, salt);
    }
}

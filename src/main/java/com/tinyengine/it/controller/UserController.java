
package com.tinyengine.it.controller;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.model.entity.User;
import com.tinyengine.it.service.app.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 查询用户信息
 *
 * @since 2024-10-20
 */
@Validated
@RestController
@RequestMapping("/platform-center/api")
public class UserController {
    /**
     * The User service.
     */
    @Autowired
    private UserService userService;

    /**
     * Me result.
     *
     * @return the result
     */
    @GetMapping("/user/me")
    public Result<User> me() {
        User user = userService.queryUserById(1);
        return Result.success(user);
    }
}

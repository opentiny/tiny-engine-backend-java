package com.tinyengine.it.controller.platform;

import com.tinyengine.it.model.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 查询用户信息
 */
@Validated
@RestController
@RequestMapping("/platform-center/api")
public class AuthController {
    @Autowired
    UsersPermissionsUserMapper usersPermissionsUserMapper;

    /**
     * 查询user信息
     *
     * @param
     * @return user信息
     */
    @GetMapping("/user/me")
    public Result<UsersPermissionsUser> me() {
        UsersPermissionsUser usersPermissionsUser = usersPermissionsUserMapper.findUsersPermissionsUserById(86);
        return Result.success(usersPermissionsUser);
    }
}

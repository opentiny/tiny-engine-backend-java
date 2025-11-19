package com.tinyengine.it.login.service;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.model.entity.User;

public interface LoginService {
    /**
     * 新增表t_user数据
     *
     * @param user the user
     * @return the user
     */
    User createUser(User user) throws Exception;

    /**
     * 忘记密码
     *
     * @param user the user
     * @return the Result
     */
    Result forgotPassword(User user) throws Exception;
}

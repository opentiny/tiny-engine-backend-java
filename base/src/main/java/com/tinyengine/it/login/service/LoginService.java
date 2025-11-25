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

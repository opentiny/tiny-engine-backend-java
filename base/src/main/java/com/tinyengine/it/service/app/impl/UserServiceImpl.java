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

package com.tinyengine.it.service.app.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tinyengine.it.mapper.UserMapper;
import com.tinyengine.it.model.entity.User;
import com.tinyengine.it.service.app.UserService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type User service.
 *
 * @since 2024-10-20
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    /**
     * 查询表t_user所有数据
     *
     * @return User
     */
    @Override
    public List<User> queryAllUser() {
        return baseMapper.queryAllUser();
    }

    /**
     * 根据主键id查询表t_user信息
     *
     * @param id id
     * @return query result
     */
    @Override
    public User queryUserById(String id) {
        return baseMapper.queryUserById(id);
    }

    /**
     * 根据条件查询表t_user数据
     *
     * @param user user
     * @return query result
     */
    @Override
    public List<User> queryUserByCondition(User user) {
        return baseMapper.queryUserByCondition(user);
    }

    /**
     * 根据主键id删除表t_user数据
     *
     * @param id id
     * @return execute success data number
     */
    @Override
    public Integer deleteUserById(String id) {
        return baseMapper.deleteUserById(id);
    }

    /**
     * 根据主键id更新表t_user数据
     *
     * @param user user
     * @return execute success data number
     */
    @Override
    public Integer updateUserById(User user) {
        return baseMapper.updateUserById(user);
    }

    /**
     * 新增表t_user数据
     *
     * @param user user
     * @return execute success data number
     */
    @Override
    public Integer createUser(User user) {
        return baseMapper.createUser(user);
    }
}

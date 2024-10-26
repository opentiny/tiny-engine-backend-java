
package com.tinyengine.it.service.app.impl;

import com.tinyengine.it.mapper.UserMapper;
import com.tinyengine.it.model.entity.User;
import com.tinyengine.it.service.app.UserService;

import lombok.extern.slf4j.Slf4j;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type User service.
 *
 * @since 2024-10-20
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 查询表t_user所有数据
     *
     * @return User
     */
    @Override
    public List<User> queryAllUser() {
        return userMapper.queryAllUser();
    }

    /**
     * 根据主键id查询表t_user信息
     *
     * @param id id
     * @return query result
     */
    @Override
    public User queryUserById(@Param("id") Integer id) {
        return userMapper.queryUserById(id);
    }

    /**
     * 根据条件查询表t_user数据
     *
     * @param user user
     * @return query result
     */
    @Override
    public List<User> queryUserByCondition(User user) {
        return userMapper.queryUserByCondition(user);
    }

    /**
     * 根据主键id删除表t_user数据
     *
     * @param id id
     * @return execute success data number
     */
    @Override
    public Integer deleteUserById(@Param("id") Integer id) {
        return userMapper.deleteUserById(id);
    }

    /**
     * 根据主键id更新表t_user数据
     *
     * @param user user
     * @return execute success data number
     */
    @Override
    public Integer updateUserById(User user) {
        return userMapper.updateUserById(user);
    }

    /**
     * 新增表t_user数据
     *
     * @param user user
     * @return execute success data number
     */
    @Override
    public Integer createUser(User user) {
        return userMapper.createUser(user);
    }
}

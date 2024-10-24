package com.tinyengine.it.service.app.impl;

import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.mapper.UserMapper;
import com.tinyengine.it.model.entity.User;
import com.tinyengine.it.service.app.UserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type User service.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 查询表t_user所有数据
     */
    @Override
    public List<User> queryAllUser() throws ServiceException {
        return userMapper.queryAllUser();
    }

    /**
     * 根据主键id查询表t_user信息
     *
     * @param id
     */
    @Override
    public User queryUserById(@Param("id") Integer id) throws ServiceException {
        return userMapper.queryUserById(id);
    }

    /**
     * 根据条件查询表t_user数据
     *
     * @param user
     */
    @Override
    public List<User> queryUserByCondition(User user) throws ServiceException {
        return userMapper.queryUserByCondition(user);
    }

    /**
     * 根据主键id删除表t_user数据
     *
     * @param id
     */
    @Override
    public Integer deleteUserById(@Param("id") Integer id) throws ServiceException {
        return userMapper.deleteUserById(id);
    }

    /**
     * 根据主键id更新表t_user数据
     *
     * @param user
     */
    @Override
    public Integer updateUserById(User user) throws ServiceException {
        return userMapper.updateUserById(user);
    }

    /**
     * 新增表t_user数据
     *
     * @param user
     */
    @Override
    public Integer createUser(User user) throws ServiceException {
        return userMapper.createUser(user);
    }
}

package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {

    /**
     * 查询表t_user所有信息
     */
    List<User> queryAllUser();

    /**
     * 根据主键id查询表t_user数据
     *
     * @param id
     */
    User queryUserById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_user数据
     *
     * @param user
     */
    List<User> queryUserByCondition(User user);

    /**
     * 根据主键id删除表t_user数据
     *
     * @param id
     */
    Integer deleteUserById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_user数据
     *
     * @param user
     */
    Integer updateUserById(User user);

    /**
     * 新增表t_user数据
     *
     * @param user
     */
    Integer createUser(User user);
}
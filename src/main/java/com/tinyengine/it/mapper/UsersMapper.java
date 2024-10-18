package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.Users;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface UsersMapper extends BaseMapper<Users> {

    /**
     * 查询表t_users所有信息
     */
    List<Users> findAllUsers();

    /**
     * 根据主键id查询表t_users数据
     *
     * @param id
     */
    Users findUsersById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_users数据
     *
     * @param users
     */
    List<Users> findUsersByCondition(Users users);

    /**
     * 根据主键id删除表t_users数据
     *
     * @param id
     */
    Integer deleteUsersById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_users数据
     *
     * @param users
     */
    Integer updateUsersById(Users users);

    /**
     * 新增表t_users数据
     *
     * @param users
     */
    Integer createUsers(Users users);
}
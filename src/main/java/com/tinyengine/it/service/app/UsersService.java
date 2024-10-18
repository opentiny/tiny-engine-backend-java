package com.tinyengine.it.service.app;

import com.tinyengine.it.model.entity.Users;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UsersService{

    /**
    *  查询表t_users所有信息
    */
    List<Users> findAllUsers();

    /**
    *  根据主键id查询表t_users信息
    *  @param id
    */
    Users findUsersById(@Param("id") Integer id);

    /**
    *  根据条件查询表t_users信息
    *  @param users
    */
    List<Users> findUsersByCondition(Users users);

    /**
    *  根据主键id删除t_users数据
    *  @param id
    */
    Integer deleteUsersById(@Param("id") Integer id);

    /**
    *  根据主键id更新表t_users信息
    *  @param users
    */
    Integer updateUsersById(Users users);

    /**
    *  新增表t_users数据
    *  @param users
    */
    Integer createUsers(Users users);
}

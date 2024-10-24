package com.tinyengine.it.service.app;

import com.tinyengine.it.model.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface User service.
 */
public interface UserService {

    /**
     * 查询表t_user所有信息
     *
     * @return the list
     */
    List<User> queryAllUser();

    /**
     * 根据主键id查询表t_user信息
     *
     * @param id the id
     * @return the user
     */
    User queryUserById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_user信息
     *
     * @param user the user
     * @return the list
     */
    List<User> queryUserByCondition(User user);

    /**
     * 根据主键id删除t_user数据
     *
     * @param id the id
     * @return the integer
     */
    Integer deleteUserById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_user信息
     *
     * @param user the user
     * @return the integer
     */
    Integer updateUserById(User user);

    /**
     * 新增表t_user数据
     *
     * @param user the user
     * @return the integer
     */
    Integer createUser(User user);
}

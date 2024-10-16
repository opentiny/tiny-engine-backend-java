package com.tinyengine.it.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.UsersPermissionsUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UsersPermissionsUserMapper extends BaseMapper<UsersPermissionsUser> {

    /**
    *  查询表users-permissions_user所有信息
    */
    List<UsersPermissionsUser> findAllUsersPermissionsUser();

    /**
    * 根据主键id查询表users-permissions_user数据
    * @param id
    */
    UsersPermissionsUser findUsersPermissionsUserById(@Param("id") Integer id);
    /**
    *  根据条件查询表users-permissions_user数据
    *  @param usersPermissionsUser
    */
    List<UsersPermissionsUser> findUsersPermissionsUserByCondition(UsersPermissionsUser usersPermissionsUser);

    /**
    *  根据主键id删除表users-permissions_user数据
    *  @param id
    */
    Integer deleteUsersPermissionsUserById(@Param("id") Integer id);

    /**
    *  根据主键id更新表users-permissions_user数据
    *  @param usersPermissionsUser
    */
    Integer updateUsersPermissionsUserById(UsersPermissionsUser usersPermissionsUser);

    /**
    *  新增表users-permissions_user数据
    *  @param usersPermissionsUser
    */
    Integer createUsersPermissionsUser(UsersPermissionsUser usersPermissionsUser);

    List<UsersPermissionsUser> selectUsersByIds(List<String> ids);
}
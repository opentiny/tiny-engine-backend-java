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

package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.User;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface User mapper.
 *
 * @since 2024-10-20
 */
public interface UserMapper extends BaseMapper<User> {
    /**
     * 查询表t_user所有信息
     *
     * @return the list
     */
    List<User> queryAllUser();

    /**
     * 根据主键id查询表t_user数据
     *
     * @param id the id
     * @return the user
     */
    User queryUserById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_user数据
     *
     * @param user the user
     * @return the list
     */
    List<User> queryUserByCondition(User user);

    /**
     * 根据主键id删除表t_user数据
     *
     * @param id the id
     * @return the integer
     */
    Integer deleteUserById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_user数据
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

    /**
     * 通过ids获取用户列表信息
     *
     * @param ids the ids
     * @return the list
     */
    List<User> selectUsersByIds(List<String> ids);
}
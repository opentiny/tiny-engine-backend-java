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
import com.tinyengine.it.model.entity.AppExtension;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface App extension mapper.
 *
 * @since 2024-10-20
 */
public interface AppExtensionMapper extends BaseMapper<AppExtension> {
    /**
     * 查询表t_app_extension所有信息
     *
     * @return the list
     */
    List<AppExtension> queryAllAppExtension();

    /**
     * 根据主键id查询表t_app_extension数据
     *
     * @param id the id
     * @return the app extension
     */
    AppExtension queryAppExtensionById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_app_extension数据
     *
     * @param appExtension the app extension
     * @return the list
     */
    List<AppExtension> queryAppExtensionByCondition(AppExtension appExtension);

    /**
     * 根据主键id删除表t_app_extension数据
     *
     * @param id the id
     * @return the integer
     */
    Integer deleteAppExtensionById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_app_extension数据
     *
     * @param appExtension the app extension
     * @return the integer
     */
    Integer updateAppExtensionById(AppExtension appExtension);

    /**
     * 新增表t_app_extension数据
     *
     * @param appExtension the app extension
     * @return the integer
     */
    Integer createAppExtension(AppExtension appExtension);
}
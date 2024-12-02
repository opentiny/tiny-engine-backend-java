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

package com.tinyengine.it.service.platform;

import com.tinyengine.it.model.entity.Platform;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface Platform service.
 *
 * @since 2024-10-20
 */
public interface PlatformService {
    /**
     * 查询表t_platform所有信息
     *
     * @return the list
     */
    List<Platform> queryAllPlatform();

    /**
     * 根据主键id查询表t_platform信息
     *
     * @param id the id
     * @return the platform
     */
    Platform queryPlatformById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_platform信息
     *
     * @param platform the platform
     * @return the list
     */
    List<Platform> queryPlatformByCondition(Platform platform);

    /**
     * 根据主键id删除t_platform数据
     *
     * @param id the id
     * @return the integer
     */
    Integer deletePlatformById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_platform信息
     *
     * @param platform the platform
     * @return the integer
     */
    Integer updatePlatformById(Platform platform);

    /**
     * 新增表t_platform数据
     *
     * @param platform the platform
     * @return the integer
     */
    Integer createPlatform(Platform platform);
}

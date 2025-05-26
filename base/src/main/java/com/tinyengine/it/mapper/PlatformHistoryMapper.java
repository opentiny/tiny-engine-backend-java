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

import com.tinyengine.it.model.entity.PlatformHistory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface Platform history mapper.
 *
 * @since 2025-05-09
 */
public interface PlatformHistoryMapper extends BaseMapper<PlatformHistory> {
    /**
     * 查询表t_platform_history所有信息
     *
     * @return the list
     */
    List<PlatformHistory> queryAllPlatformHistory();

    /**
     * 根据主键id查询表t_platform_history数据
     *
     * @param id the id
     * @return the platform
     */
    PlatformHistory queryPlatformHistoryById(Integer id);

    /**
     * 根据条件查询表t_platform_history数据
     *
     * @param platformHistory the platformHistory
     * @return the list
     */
    List<PlatformHistory> queryPlatformHistoryByCondition(PlatformHistory platformHistory);

    /**
     * 根据主键id删除表t_platform_history数据
     *
     * @param id the id
     * @return the integer
     */
    Integer deletePlatformHistoryById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_platform_history数据
     *
     * @param platformHistory the platformHistory
     * @return the integer
     */
    Integer updatePlatformHistoryById(PlatformHistory platformHistory);

    /**
     * 新增表t_platform_history数据
     *
     * @param platformHistory the platformHistory
     * @return the integer
     */
    Integer createPlatformHistory(PlatformHistory platformHistory);
}

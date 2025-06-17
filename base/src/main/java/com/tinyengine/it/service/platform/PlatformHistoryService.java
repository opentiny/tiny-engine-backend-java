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

import com.baomidou.mybatisplus.extension.service.IService;
import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.model.entity.PlatformHistory;

import java.util.List;

/**
 * The interface platform history service.
 *
 * @since 2025-05-09
 */
public interface PlatformHistoryService extends IService<PlatformHistory> {
    /**
     * 查询表t_platform_history所有信息
     *
     * @return the list
     */
    List<PlatformHistory> queryAllPlatformHistory();

    /**
     * 根据主键id查询表t_platform_history信息
     *
     * @param id the id
     * @return the platformHistory
     */
    PlatformHistory queryPlatformHistoryById(Integer id);

    /**
     * 根据条件查询表t_platform_history信息
     *
     * @param platformHistory the platformHistory
     * @return the list
     */
    List<PlatformHistory> queryPlatformHistoryByCondition(PlatformHistory platformHistory);

    /**
     * 根据主键id删除t_platform_history数据
     *
     * @param id the id
     * @return the Result
     */
    Result<PlatformHistory> deletePlatformHistoryById(Integer id);

    /**
     * 根据主键id更新表t_platform_history信息
     *
     * @param platformHistory the platformHistory
     * @return the Result
     */
    Result<PlatformHistory> updatePlatformHistoryById(PlatformHistory platformHistory);

    /**
     * 新增表t_platform_history数据
     *
     * @param platformHistory the platformHistory
     * @return the Result
     */
    Result<PlatformHistory> createPlatformHistory(PlatformHistory platformHistory);
}

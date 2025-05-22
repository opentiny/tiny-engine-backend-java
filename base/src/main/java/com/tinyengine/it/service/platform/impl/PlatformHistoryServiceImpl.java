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

package com.tinyengine.it.service.platform.impl;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.mapper.PlatformHistoryMapper;
import com.tinyengine.it.model.entity.PlatformHistory;
import com.tinyengine.it.service.platform.PlatformHistoryService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The interface platform history service.
 *
 * @since 2025-05-09
 */
@Service
@Slf4j
public class PlatformHistoryServiceImpl implements PlatformHistoryService {
    @Autowired
    private PlatformHistoryMapper platformHistoryMapper;

    /**
     * 查询表t_platform_history所有信息
     *
     * @return the list
     */
    @Override
    public List<PlatformHistory> queryAllPlatformHistory() {
        return platformHistoryMapper.queryAllPlatformHistory();
    }

    /**
     * 根据主键id查询表t_platform_history信息
     *
     * @param id the id
     * @return the platformHistory
     */
    @Override
    public PlatformHistory queryPlatformHistoryById(Integer id) {
        return platformHistoryMapper.queryPlatformHistoryById(id);
    }

    /**
     * 根据条件查询表t_platform_history信息
     *
     * @param platformHistory the platformHistory
     * @return the list
     */
    @Override
    public List<PlatformHistory> queryPlatformHistoryByCondition(PlatformHistory platformHistory) {
        return platformHistoryMapper.queryPlatformHistoryByCondition(platformHistory);
    }

    /**
     * 根据主键id删除t_platform_history数据
     *
     * @param id the id
     * @return the Result
     */
    @Override
    public Result<PlatformHistory> deletePlatformHistoryById(Integer id) {
        PlatformHistory platformHistory = this.queryPlatformHistoryById(id);
        if (platformHistory == null || platformHistory.getId() == null) {
            return Result.success();
        }
        int deleteResult = platformHistoryMapper.deletePlatformHistoryById(id);
        if (deleteResult != 1) {
            return Result.failed(ExceptionEnum.CM008);
        }
        return Result.success(platformHistory);
    }

    /**
     * 根据主键id更新表t_platform_history信息
     *
     * @param platformHistory the platformHistory
     * @return the Result
     */
    @Override
    public Result<PlatformHistory> updatePlatformHistoryById(PlatformHistory platformHistory) {
        if (platformHistory == null || platformHistory.getId() == null) {
            return Result.failed(ExceptionEnum.CM002);
        }
        int updateResult = platformHistoryMapper.updatePlatformHistoryById(platformHistory);
        if (updateResult != 1) {
            return Result.failed(ExceptionEnum.CM008);
        }
        PlatformHistory platformHistoryResult = this.queryPlatformHistoryById(platformHistory.getId());
        return Result.success(platformHistoryResult);
    }

    /**
     * 新增表t_platform_history数据
     *
     * @param platformHistory the platformHistory
     * @return the Result
     */
    @Override
    public Result<PlatformHistory> createPlatformHistory(PlatformHistory platformHistory) {
        if (platformHistory == null) {
            return Result.failed(ExceptionEnum.CM002);
        }
        if (platformHistory.getRefId() == null) {
            return Result.failed(ExceptionEnum.CM002);
        }
        if (platformHistory.getName() == null || platformHistory.getName().isEmpty()) {
            return Result.failed(ExceptionEnum.CM002);
        }
        if (platformHistory.getVersion() == null || platformHistory.getVersion().isEmpty()) {
            return Result.failed(ExceptionEnum.CM002);
        }
        if (platformHistory.getMaterialHistoryId() == null) {
            return Result.failed(ExceptionEnum.CM002);
        }
        int createResult = platformHistoryMapper.createPlatformHistory(platformHistory);
        if (createResult != 1) {
            return Result.failed(ExceptionEnum.CM008);
        }
        return Result.success(platformHistory);
    }
}

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

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.mapper.PlatformMapper;
import com.tinyengine.it.model.entity.Platform;
import com.tinyengine.it.service.platform.PlatformService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Platform service.
 *
 * @since 2024-10-20
 */
@Service
@Slf4j
public class PlatformServiceImpl extends ServiceImpl<PlatformMapper, Platform> implements PlatformService {
    /**
     * 查询表t_platform所有数据
     *
     * @return Platform
     */
    @Override
    public List<Platform> queryAllPlatform() {
        return baseMapper.queryAllPlatform();
    }

    /**
     * 根据主键id查询表t_platform信息
     *
     * @param id id
     * @return query result
     */
    @Override
    public Platform queryPlatformById(Integer id) {
        return baseMapper.queryPlatformById(id);
    }

    /**
     * 根据条件查询表t_platform数据
     *
     * @param platform platform
     * @return query result
     */
    @Override
    public List<Platform> queryPlatformByCondition(Platform platform) {
        return baseMapper.queryPlatformByCondition(platform);
    }

    /**
     * 根据主键id删除表t_platform数据
     *
     * @param id id
     * @return execute success data number
     */
    @Override
    public Result<Platform> deletePlatformById(Integer id) {
        Platform platform = this.queryPlatformById(id);
        if (platform == null || platform.getId() == null) {
            return Result.success();
        }
        int deleteResult = baseMapper.deletePlatformById(id);
        if (deleteResult != 1) {
            return Result.failed(ExceptionEnum.CM008);
        }
        return Result.success(platform);
    }

    /**
     * 根据主键id更新表t_platform数据
     *
     * @param platform platform
     * @return execute success data number
     */
    @Override
    public Result<Platform> updatePlatformById(Platform platform) {
        if (platform == null || platform.getId() == null) {
            return Result.failed(ExceptionEnum.CM002);
        }
        int updateResult = baseMapper.updatePlatformById(platform);
        if (updateResult != 1) {
            return Result.failed(ExceptionEnum.CM008);
        }
        Platform platformResult = baseMapper.queryPlatformById(platform.getId());
        return Result.success(platformResult);
    }

    /**
     * 新增表t_platform数据
     *
     * @param platform platform
     * @return execute success data number
     */
    @Override
    public Result<Platform> createPlatform(Platform platform) {
        if (platform == null) {
            return Result.failed(ExceptionEnum.CM002);
        }
        if (platform.getName() == null || platform.getName().isEmpty()) {
            return Result.failed(ExceptionEnum.CM002);
        }
        int createResult = baseMapper.createPlatform(platform);
        if (createResult != 1) {
            return Result.failed(ExceptionEnum.CM008);
        }
        return Result.success(platform);
    }
}

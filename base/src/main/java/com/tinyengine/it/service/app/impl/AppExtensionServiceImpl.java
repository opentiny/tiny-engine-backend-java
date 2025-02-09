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

package com.tinyengine.it.service.app.impl;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.common.log.SystemServiceLog;
import com.tinyengine.it.mapper.AppExtensionMapper;
import com.tinyengine.it.model.entity.AppExtension;
import com.tinyengine.it.service.app.AppExtensionService;

import lombok.extern.slf4j.Slf4j;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type App extension service.
 *
 * @since 2024-10-20
 */
@Service
@Slf4j
public class AppExtensionServiceImpl implements AppExtensionService {
    @Autowired
    private AppExtensionMapper appExtensionMapper;

    /**
     * 查询表t_app_extension所有数据
     *
     * @return appExtension
     */
    @Override
    @SystemServiceLog(description = "应用扩展列表查询实现方法")
    public Result<List<AppExtension>> findAllAppExtension() {
        List<AppExtension> appExtensionList = appExtensionMapper.queryAllAppExtension();
        return Result.success(appExtensionList);
    }

    /**
     * 根据主键id查询表t_app_extension信息
     *
     * @param id ID
     * @return appExtension
     */
    @Override
    @SystemServiceLog(description = "应用扩展id查询实现方法")
    public AppExtension findAppExtensionById(@Param("id") Integer id) {
        return appExtensionMapper.queryAppExtensionById(id);
    }

    /**
     * 根据条件查询表t_app_extension数据
     *
     * @param appExtension appExtension
     * @return appExtension
     */
    @Override
    @SystemServiceLog(description = "应用扩展条件查询实现方法")
    public List<AppExtension> findAppExtensionByCondition(AppExtension appExtension) {
        return appExtensionMapper.queryAppExtensionByCondition(appExtension);
    }

    /**
     * 根据主键id删除表t_app_extension数据
     *
     * @param id id
     * @return appExtension
     */
    @Override
    @SystemServiceLog(description = "应用扩展删除实现方法")
    public Result<AppExtension> deleteAppExtensionById(@Param("id") Integer id) {
        AppExtension appExtension = appExtensionMapper.queryAppExtensionById(id);
        if (appExtension != null) {
            appExtensionMapper.deleteAppExtensionById(id);
            return Result.success(appExtension);
        }
        return Result.failed(ExceptionEnum.CM009);
    }

    /**
     * 根据主键id更新表t_app_extension数据
     *
     * @param appExtension the app extension
     * @return appExtension
     */
    @Override
    @SystemServiceLog(description = "应用扩展修改实现方法")
    public Result<AppExtension> updateAppExtensionById(AppExtension appExtension) {
        int result = appExtensionMapper.updateAppExtensionById(appExtension);
        if (result == 1) {
            AppExtension data = appExtensionMapper.queryAppExtensionById(appExtension.getId());
            return Result.success(data);
        }
        return Result.failed(ExceptionEnum.CM001);
    }

    /**
     * 新增表t_app_extension数据
     *
     * @param appExtension appExtension
     * @return appExtension
     */
    @Override
    @SystemServiceLog(description = "应用扩展创建实现方法")
    public Result<AppExtension> createAppExtension(AppExtension appExtension) {
        List<AppExtension> appExtensionResult = appExtensionMapper.queryAppExtensionByCondition(appExtension);
        if (!appExtensionResult.isEmpty()) {
            return Result.failed(ExceptionEnum.CM003);
        }
        int result = appExtensionMapper.createAppExtension(appExtension);
        if (result == 1) {
            return Result.success(appExtension);
        }
        return Result.failed(ExceptionEnum.CM001);
    }
}

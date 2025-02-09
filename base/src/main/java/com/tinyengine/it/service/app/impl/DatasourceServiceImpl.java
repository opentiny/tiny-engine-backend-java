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
import com.tinyengine.it.mapper.DatasourceMapper;
import com.tinyengine.it.model.entity.Datasource;
import com.tinyengine.it.service.app.DatasourceService;

import lombok.extern.slf4j.Slf4j;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Datasource service.
 *
 * @since 2024-10-20
 */
@Service
@Slf4j
public class DatasourceServiceImpl implements DatasourceService {
    @Autowired
    private DatasourceMapper datasourceMapper;

    /**
     * 根据主键id查询表t_datasource信息
     *
     * @param id id
     * @return query result
     */
    @Override
    public Datasource queryDatasourceById(@Param("id") Integer id) {
        return datasourceMapper.queryDatasourceById(id);
    }

    /**
     * 根据条件查询表t_datasource数据
     *
     * @param datasource datasource
     * @return query result
     */
    @Override
    public List<Datasource> queryDatasourceByCondition(Datasource datasource) {
        return datasourceMapper.queryDatasourceByCondition(datasource);
    }

    /**
     * 根据主键id删除表t_datasource数据
     *
     * @param id id
     * @return Datasource
     */
    @Override
    public Result<Datasource> deleteDatasourceById(@Param("id") Integer id) {
        Datasource sources = queryDatasourceById(id);
        if (sources != null) {
            datasourceMapper.deleteDatasourceById(id);
            return Result.success(sources);
        }
        return Result.failed(ExceptionEnum.CM009);
    }

    /**
     * 根据主键id更新表t_datasource数据
     *
     * @param datasource datasource
     * @return Datasource
     */
    @Override
    public Result<Datasource> updateDatasourceById(Datasource datasource) {
        int res = datasourceMapper.updateDatasourceById(datasource);
        if (res == 1) {
            return Result.success(queryDatasourceById(datasource.getId()));
        }
        return Result.failed(ExceptionEnum.CM001);
    }

    /**
     * 新增表t_datasource数据
     *
     * @param datasource datasource
     * @return Datasource
     */
    @Override
    public Result<Datasource> createDatasource(Datasource datasource) {
        Integer appId = datasource.getApp();
        String name = datasource.getName();
        if (appId != 0 && String.valueOf(appId).matches("^[0-9]+$") && !name.isEmpty()) {
            int res = datasourceMapper.createDatasource(datasource);
            if (res == 1) {
                int id = datasource.getId();
                return Result.success(queryDatasourceById(id));
            } else {
                Result.failed(ExceptionEnum.CM001);
            }
        }
        return Result.failed(ExceptionEnum.CM002);
    }
}

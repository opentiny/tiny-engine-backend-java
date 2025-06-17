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

package com.tinyengine.it.service.material.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.mapper.MaterialHistoryMapper;
import com.tinyengine.it.model.entity.MaterialHistory;
import com.tinyengine.it.service.material.MaterialHistoryService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Material history service.
 *
 * @since 2024-10-20
 */
@Service
@Slf4j
public class MaterialHistoryServiceImpl extends ServiceImpl<MaterialHistoryMapper, MaterialHistory> implements MaterialHistoryService {
    /**
     * 查询表t_material_history所有数据
     *
     * @return MaterialHistory
     */
    @Override
    public List<MaterialHistory> findAllMaterialHistory() {
        return baseMapper.queryAllMaterialHistory();
    }

    /**
     * 根据主键id查询表t_material_history信息
     *
     * @param id id
     * @return query result
     */
    @Override
    public Result<MaterialHistory> findMaterialHistoryById(Integer id) {
        MaterialHistory materialHistory = baseMapper.queryMaterialHistoryById(id);
        return Result.success(materialHistory);
    }

    /**
     * 根据条件查询表t_material_history数据
     *
     * @param materialHistory materialHistory
     * @return query result
     * @throws ServiceException ServiceException
     */
    @Override
    public List<MaterialHistory> findMaterialHistoryByCondition(MaterialHistory materialHistory)
            throws ServiceException {
        return baseMapper.queryMaterialHistoryByCondition(materialHistory);
    }

    /**
     * 根据主键id删除表t_material_history数据
     *
     * @param id id
     * @return execute success data number
     */
    @Override
    public Result<MaterialHistory> deleteMaterialHistoryById(Integer id) {
        Result<MaterialHistory> result = this.findMaterialHistoryById(id);
        int deleteResult = baseMapper.deleteMaterialHistoryById(id);
        if (deleteResult != 1) {
            return Result.failed(ExceptionEnum.CM008);
        }
        return result;
    }

    /**
     * 根据主键id更新表t_material_history数据
     *
     * @param materialHistory materialHistory
     * @return execute success data number
     */
    @Override
    public Result<MaterialHistory> updateMaterialHistoryById(MaterialHistory materialHistory) {
        int updateResult = baseMapper.updateMaterialHistoryById(materialHistory);
        if (updateResult != 1) {
            return Result.failed(ExceptionEnum.CM008);
        }
        return this.findMaterialHistoryById(materialHistory.getId());
    }

    /**
     * 新增表t_material_history数据
     *
     * @param materialHistory materialHistory
     * @return execute success data number
     */
    @Override
    public Result<MaterialHistory> createMaterialHistory(MaterialHistory materialHistory) {
        int createResult = baseMapper.createMaterialHistory(materialHistory);
        if (createResult != 1) {
            return Result.failed(ExceptionEnum.CM008);
        }
        return this.findMaterialHistoryById(materialHistory.getId());
    }
}

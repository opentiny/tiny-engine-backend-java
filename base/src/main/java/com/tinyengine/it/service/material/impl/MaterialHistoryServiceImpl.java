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

import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.mapper.MaterialHistoryMapper;
import com.tinyengine.it.model.entity.MaterialHistory;
import com.tinyengine.it.service.material.MaterialHistoryService;

import lombok.extern.slf4j.Slf4j;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Material history service.
 *
 * @since 2024-10-20
 */
@Service
@Slf4j
public class MaterialHistoryServiceImpl implements MaterialHistoryService {
    @Autowired
    private MaterialHistoryMapper materialHistoryMapper;

    /**
     * 查询表t_material_history所有数据
     *
     * @return MaterialHistory
     */
    @Override
    public List<MaterialHistory> findAllMaterialHistory() {
        return materialHistoryMapper.queryAllMaterialHistory();
    }

    /**
     * 根据主键id查询表t_material_history信息
     *
     * @param id id
     * @return query result
     */
    @Override
    public MaterialHistory findMaterialHistoryById(@Param("id") Integer id) {
        return materialHistoryMapper.queryMaterialHistoryById(id);
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
        return materialHistoryMapper.queryMaterialHistoryByCondition(materialHistory);
    }

    /**
     * 根据主键id删除表t_material_history数据
     *
     * @param id id
     * @return execute success data number
     */
    @Override
    public Integer deleteMaterialHistoryById(@Param("id") Integer id) {
        return materialHistoryMapper.deleteMaterialHistoryById(id);
    }

    /**
     * 根据主键id更新表t_material_history数据
     *
     * @param materialHistory materialHistory
     * @return execute success data number
     */
    @Override
    public Integer updateMaterialHistoryById(MaterialHistory materialHistory) {
        return materialHistoryMapper.updateMaterialHistoryById(materialHistory);
    }

    /**
     * 新增表t_material_history数据
     *
     * @param materialHistory materialHistory
     * @return execute success data number
     */
    @Override
    public Integer createMaterialHistory(MaterialHistory materialHistory) {
        return materialHistoryMapper.createMaterialHistory(materialHistory);
    }
}

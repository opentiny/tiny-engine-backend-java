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

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.mapper.MaterialMapper;
import com.tinyengine.it.model.entity.Material;
import com.tinyengine.it.service.material.MaterialService;

import lombok.extern.slf4j.Slf4j;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Material service.
 *
 * @since 2024-10-20
 */
@Service
@Slf4j
public class MaterialServiceImpl implements MaterialService {
    @Autowired
    private MaterialMapper materialMapper;

    /**
     * 查询表t_material所有数据
     *
     * @return Material
     */
    @Override
    public List<Material> queryAllMaterial() {
        return materialMapper.queryAllMaterial();
    }

    /**
     * 根据主键id查询表t_material信息
     *
     * @param id id
     * @return query result
     */
    @Override
    public Result<Material> queryMaterialById(@Param("id") Integer id) {
        Material material = materialMapper.queryMaterialById(id);
        return Result.success(material);
    }

    /**
     * 根据条件查询表t_material数据
     *
     * @param material material
     * @return query result
     */
    @Override
    public List<Material> queryMaterialByCondition(Material material) {
        return materialMapper.queryMaterialByCondition(material);
    }

    /**
     * 根据主键id删除表t_material数据
     *
     * @param id id
     * @return execute success data number
     */
    @Override
    public Result<Material> deleteMaterialById(@Param("id") Integer id) {
        int deleteResult = materialMapper.deleteMaterialById(id);
        if (deleteResult != 1) {
            return Result.failed(ExceptionEnum.CM008);
        }
        Result<Material> result = this.queryMaterialById(id);
        return result;

    }

    /**
     * 根据主键id更新表t_material数据
     *
     * @param material material
     * @return execute success data number
     */
    @Override
    public Result<Material> updateMaterialById(Material material) {
        int updateResult = materialMapper.updateMaterialById(material);
        if (updateResult != 1) {
            return Result.failed(ExceptionEnum.CM008);
        }
        Result<Material> result = this.queryMaterialById(material.getId());
        return result;
    }

    /**
     * 新增表t_material数据
     *
     * @param material material
     * @return execute success data number
     */
    @Override
    public Result<Material> createMaterial(Material material) {
        int createResult = materialMapper.createMaterial(material);
        if (createResult != 1) {
            return Result.failed(ExceptionEnum.CM008);
        }
        Result<Material> result = this.queryMaterialById(material.getId());
        return result;
    }
}

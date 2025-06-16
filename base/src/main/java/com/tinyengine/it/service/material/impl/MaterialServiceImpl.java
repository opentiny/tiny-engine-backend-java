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
import com.tinyengine.it.mapper.MaterialMapper;
import com.tinyengine.it.model.entity.Material;
import com.tinyengine.it.service.material.MaterialService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Material service.
 *
 * @since 2024-10-20
 */
@Service
@Slf4j
public class MaterialServiceImpl extends ServiceImpl<MaterialMapper, Material> implements MaterialService {
    /**
     * 查询表t_material所有数据
     *
     * @return Material
     */
    @Override
    public List<Material> queryAllMaterial() {
        return baseMapper.queryAllMaterial();
    }

    /**
     * 根据主键id查询表t_material信息
     *
     * @param id id
     * @return query result
     */
    @Override
    public Result<Material> queryMaterialById(Integer id) {
        Material material = baseMapper.queryMaterialById(id);
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
        return baseMapper.queryMaterialByCondition(material);
    }

    /**
     * 根据主键id删除表t_material数据
     *
     * @param id id
     * @return execute success data number
     */
    @Override
    public Result<Material> deleteMaterialById(Integer id) {
        int deleteResult = baseMapper.deleteMaterialById(id);
        if (deleteResult != 1) {
            return Result.failed(ExceptionEnum.CM008);
        }
        return this.queryMaterialById(id);

    }

    /**
     * 根据主键id更新表t_material数据
     *
     * @param material material
     * @return execute success data number
     */
    @Override
    public Result<Material> updateMaterialById(Material material) {
        int updateResult = baseMapper.updateMaterialById(material);
        if (updateResult != 1) {
            return Result.failed(ExceptionEnum.CM008);
        }
        return this.queryMaterialById(material.getId());
    }

    /**
     * 新增表t_material数据
     *
     * @param material material
     * @return execute success data number
     */
    @Override
    public Result<Material> createMaterial(Material material) {
        int createResult = baseMapper.createMaterial(material);
        if (createResult != 1) {
            return Result.failed(ExceptionEnum.CM008);
        }
        return this.queryMaterialById(material.getId());
    }
}

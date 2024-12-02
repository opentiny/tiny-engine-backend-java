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

package com.tinyengine.it.service.material;

import com.tinyengine.it.model.entity.Material;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface Material service.
 *
 * @since 2024-10-20
 */
public interface MaterialService {
    /**
     * 查询表t_material所有信息
     *
     * @return the list
     */
    List<Material> queryAllMaterial();

    /**
     * 根据主键id查询表t_material信息
     *
     * @param id the id
     * @return the material
     */
    Material queryMaterialById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_material信息
     *
     * @param material the material
     * @return the list
     */
    List<Material> queryMaterialByCondition(Material material);

    /**
     * 根据主键id删除t_material数据
     *
     * @param id the id
     * @return the integer
     */
    Integer deleteMaterialById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_material信息
     *
     * @param material the material
     * @return the integer
     */
    Integer updateMaterialById(Material material);

    /**
     * 新增表t_material数据
     *
     * @param material the material
     * @return the integer
     */
    Integer createMaterial(Material material);
}

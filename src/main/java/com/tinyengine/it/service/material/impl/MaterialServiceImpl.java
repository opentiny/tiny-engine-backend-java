
package com.tinyengine.it.service.material.impl;

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
    public Material queryMaterialById(@Param("id") Integer id) {
        return materialMapper.queryMaterialById(id);
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
    public Integer deleteMaterialById(@Param("id") Integer id) {
        return materialMapper.deleteMaterialById(id);
    }

    /**
     * 根据主键id更新表t_material数据
     *
     * @param material material
     * @return execute success data number
     */
    @Override
    public Integer updateMaterialById(Material material) {
        return materialMapper.updateMaterialById(material);
    }

    /**
     * 新增表t_material数据
     *
     * @param material material
     * @return execute success data number
     */
    @Override
    public Integer createMaterial(Material material) {
        return materialMapper.createMaterial(material);
    }
}

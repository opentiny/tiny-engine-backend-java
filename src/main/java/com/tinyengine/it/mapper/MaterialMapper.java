package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.Material;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface MaterialMapper extends BaseMapper<Material> {

    /**
     * 查询表t_material所有信息
     */
    List<Material> findAllMaterial();

    /**
     * 根据主键id查询表t_material数据
     *
     * @param id
     */
    Material findMaterialById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_material数据
     *
     * @param material
     */
    List<Material> findMaterialByCondition(Material material);

    /**
     * 根据主键id删除表t_material数据
     *
     * @param id
     */
    Integer deleteMaterialById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_material数据
     *
     * @param material
     */
    Integer updateMaterialById(Material material);

    /**
     * 新增表t_material数据
     *
     * @param material
     */
    Integer createMaterial(Material material);
}
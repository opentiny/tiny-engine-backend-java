package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.Material;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface Material mapper.
 * @since 2024-10-20
 */
public interface MaterialMapper extends BaseMapper<Material> {

    /**
     * 查询表t_material所有信息
     *
     * @return the list
     */
    List<Material> queryAllMaterial();

    /**
     * 根据主键id查询表t_material数据
     *
     * @param id the id
     * @return the material
     */
    Material queryMaterialById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_material数据
     *
     * @param material the material
     * @return the list
     */
    List<Material> queryMaterialByCondition(Material material);

    /**
     * 根据主键id删除表t_material数据
     *
     * @param id the id
     * @return the integer
     */
    Integer deleteMaterialById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_material数据
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
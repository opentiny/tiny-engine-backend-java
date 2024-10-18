package com.tinyengine.it.service.material;

import com.tinyengine.it.model.entity.Material;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MaterialService {

    /**
     * 查询表t_material所有信息
     */
    List<Material> queryAllMaterial();

    /**
     * 根据主键id查询表t_material信息
     *
     * @param id
     */
    Material queryMaterialById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_material信息
     *
     * @param material
     */
    List<Material> queryMaterialByCondition(Material material);

    /**
     * 根据主键id删除t_material数据
     *
     * @param id
     */
    Integer deleteMaterialById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_material信息
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

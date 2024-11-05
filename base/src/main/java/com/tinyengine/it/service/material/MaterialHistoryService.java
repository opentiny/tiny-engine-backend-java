package com.tinyengine.it.service.material;

import com.tinyengine.it.model.entity.MaterialHistory;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface Material history service.
 *
 * @since 2024-10-20
 */
public interface MaterialHistoryService {
    /**
     * 查询表t_material_history所有信息
     *
     * @return the list
     */
    List<MaterialHistory> findAllMaterialHistory();

    /**
     * 根据主键id查询表t_material_history信息
     *
     * @param id the id
     * @return the material history
     */
    MaterialHistory findMaterialHistoryById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_material_history信息
     *
     * @param materialHistory the material history
     * @return the list
     */
    List<MaterialHistory> findMaterialHistoryByCondition(MaterialHistory materialHistory);

    /**
     * 根据主键id删除t_material_history数据
     *
     * @param id the id
     * @return the integer
     */
    Integer deleteMaterialHistoryById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_material_history信息
     *
     * @param materialHistory the material history
     * @return the integer
     */
    Integer updateMaterialHistoryById(MaterialHistory materialHistory);

    /**
     * 新增表t_material_history数据
     *
     * @param materialHistory the material history
     * @return the integer
     */
    Integer createMaterialHistory(MaterialHistory materialHistory);
}

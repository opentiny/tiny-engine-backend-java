package com.tinyengine.it.service.material;

import com.tinyengine.it.model.entity.MaterialHistory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MaterialHistoryService{

    /**
    *  查询表t_material_history所有信息
    */
    List<MaterialHistory> findAllMaterialHistory();

    /**
    *  根据主键id查询表t_material_history信息
    *  @param id
    */
    MaterialHistory findMaterialHistoryById(@Param("id") Integer id);

    /**
    *  根据条件查询表t_material_history信息
    *  @param materialHistory
    */
    List<MaterialHistory> findMaterialHistoryByCondition(MaterialHistory materialHistory);

    /**
    *  根据主键id删除t_material_history数据
    *  @param id
    */
    Integer deleteMaterialHistoryById(@Param("id") Integer id);

    /**
    *  根据主键id更新表t_material_history信息
    *  @param materialHistory
    */
    Integer updateMaterialHistoryById(MaterialHistory materialHistory);

    /**
    *  新增表t_material_history数据
    *  @param materialHistory
    */
    Integer createMaterialHistory(MaterialHistory materialHistory);
}

package com.tinyengine.it.service.material;

import com.tinyengine.it.model.entity.MaterialHistoryComponent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MaterialHistoryComponentService{

    /**
    *  查询表r_material_history_component所有信息
    */
    List<MaterialHistoryComponent> findAllMaterialHistoryComponent();

    /**
    *  根据主键id查询表r_material_history_component信息
    *  @param id
    */
    MaterialHistoryComponent findMaterialHistoryComponentById(@Param("id") Integer id);

    /**
    *  根据条件查询表r_material_history_component信息
    *  @param materialHistoryComponent
    */
    List<MaterialHistoryComponent> findMaterialHistoryComponentByCondition(MaterialHistoryComponent materialHistoryComponent);

    /**
    *  根据主键id删除r_material_history_component数据
    *  @param id
    */
    Integer deleteMaterialHistoryComponentById(@Param("id") Integer id);

    /**
    *  根据主键id更新表r_material_history_component信息
    *  @param materialHistoryComponent
    */
    Integer updateMaterialHistoryComponentById(MaterialHistoryComponent materialHistoryComponent);

    /**
    *  新增表r_material_history_component数据
    *  @param materialHistoryComponent
    */
    Integer createMaterialHistoryComponent(MaterialHistoryComponent materialHistoryComponent);
}

package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.MaterialHistoryComponent;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface MaterialHistoryComponentMapper extends BaseMapper<MaterialHistoryComponent> {

    /**
     * 查询表r_material_history_component所有信息
     */
    List<MaterialHistoryComponent> findAllMaterialHistoryComponent();

    /**
     * 根据主键id查询表r_material_history_component数据
     *
     * @param id
     */
    MaterialHistoryComponent findMaterialHistoryComponentById(@Param("id") Integer id);

    /**
     * 根据条件查询表r_material_history_component数据
     *
     * @param materialHistoryComponent
     */
    List<MaterialHistoryComponent> findMaterialHistoryComponentByCondition(MaterialHistoryComponent materialHistoryComponent);

    /**
     * 根据主键id删除表r_material_history_component数据
     *
     * @param id
     */
    Integer deleteMaterialHistoryComponentById(@Param("id") Integer id);

    /**
     * 根据主键id更新表r_material_history_component数据
     *
     * @param materialHistoryComponent
     */
    Integer updateMaterialHistoryComponentById(MaterialHistoryComponent materialHistoryComponent);

    /**
     * 新增表r_material_history_component数据
     *
     * @param materialHistoryComponent
     */
    Integer createMaterialHistoryComponent(MaterialHistoryComponent materialHistoryComponent);
}
package com.tinyengine.it.service.app;

import com.tinyengine.it.model.entity.MaterialHistories;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MaterialHistoriesService {

    /**
     * 查询表material_histories所有信息
     */
    List<MaterialHistories> findAllMaterialHistories();

    /**
     * 根据主键id查询表material_histories信息
     *
     * @param id
     */
    MaterialHistories findMaterialHistoriesById(@Param("id") Integer id);


    /**
     * 根据主键id删除material_histories数据
     *
     * @param id
     */
    Integer deleteMaterialHistoriesById(@Param("id") Integer id);

    /**
     * 根据主键id更新表material_histories信息
     *
     * @param materialHistories
     */
    Integer updateMaterialHistoriesById(MaterialHistories materialHistories);

    /**
     * 新增表material_histories数据
     *
     * @param materialHistories
     */
    Integer createMaterialHistories(MaterialHistories materialHistories);
}

package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.MaterialCategory;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface MaterialCategoryMapper extends BaseMapper<MaterialCategory> {

    /**
     * 查询表r_material_category所有信息
     */
    List<MaterialCategory> findAllMaterialCategory();

    /**
     * 根据主键id查询表r_material_category数据
     *
     * @param id
     */
    MaterialCategory findMaterialCategoryById(@Param("id") Integer id);

    /**
     * 根据条件查询表r_material_category数据
     *
     * @param materialCategory
     */
    List<MaterialCategory> findMaterialCategoryByCondition(MaterialCategory materialCategory);

    /**
     * 根据主键id删除表r_material_category数据
     *
     * @param id
     */
    Integer deleteMaterialCategoryById(@Param("id") Integer id);

    /**
     * 根据主键id更新表r_material_category数据
     *
     * @param materialCategory
     */
    Integer updateMaterialCategoryById(MaterialCategory materialCategory);

    /**
     * 新增表r_material_category数据
     *
     * @param materialCategory
     */
    Integer createMaterialCategory(MaterialCategory materialCategory);
}
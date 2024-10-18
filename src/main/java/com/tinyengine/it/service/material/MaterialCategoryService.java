package com.tinyengine.it.service.material;

import com.tinyengine.it.model.entity.MaterialCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MaterialCategoryService{

    /**
    *  查询表r_material_category所有信息
    */
    List<MaterialCategory> findAllMaterialCategory();

    /**
    *  根据主键id查询表r_material_category信息
    *  @param id
    */
    MaterialCategory findMaterialCategoryById(@Param("id") Integer id);

    /**
    *  根据条件查询表r_material_category信息
    *  @param materialCategory
    */
    List<MaterialCategory> findMaterialCategoryByCondition(MaterialCategory materialCategory);

    /**
    *  根据主键id删除r_material_category数据
    *  @param id
    */
    Integer deleteMaterialCategoryById(@Param("id") Integer id);

    /**
    *  根据主键id更新表r_material_category信息
    *  @param materialCategory
    */
    Integer updateMaterialCategoryById(MaterialCategory materialCategory);

    /**
    *  新增表r_material_category数据
    *  @param materialCategory
    */
    Integer createMaterialCategory(MaterialCategory materialCategory);
}

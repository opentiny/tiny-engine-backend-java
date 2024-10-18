package com.tinyengine.it.service.material;

import com.tinyengine.it.model.entity.MaterialBlock;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MaterialBlockService{

    /**
    *  查询表r_material_block所有信息
    */
    List<MaterialBlock> findAllMaterialBlock();

    /**
    *  根据主键id查询表r_material_block信息
    *  @param id
    */
    MaterialBlock findMaterialBlockById(@Param("id") Integer id);

    /**
    *  根据条件查询表r_material_block信息
    *  @param materialBlock
    */
    List<MaterialBlock> findMaterialBlockByCondition(MaterialBlock materialBlock);

    /**
    *  根据主键id删除r_material_block数据
    *  @param id
    */
    Integer deleteMaterialBlockById(@Param("id") Integer id);

    /**
    *  根据主键id更新表r_material_block信息
    *  @param materialBlock
    */
    Integer updateMaterialBlockById(MaterialBlock materialBlock);

    /**
    *  新增表r_material_block数据
    *  @param materialBlock
    */
    Integer createMaterialBlock(MaterialBlock materialBlock);
}

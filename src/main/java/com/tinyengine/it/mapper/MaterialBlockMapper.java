package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.MaterialBlock;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface MaterialBlockMapper extends BaseMapper<MaterialBlock> {

    /**
     * 查询表r_material_block所有信息
     */
    List<MaterialBlock> findAllMaterialBlock();

    /**
     * 根据主键id查询表r_material_block数据
     *
     * @param id
     */
    MaterialBlock findMaterialBlockById(@Param("id") Integer id);

    /**
     * 根据条件查询表r_material_block数据
     *
     * @param materialBlock
     */
    List<MaterialBlock> findMaterialBlockByCondition(MaterialBlock materialBlock);

    /**
     * 根据主键id删除表r_material_block数据
     *
     * @param id
     */
    Integer deleteMaterialBlockById(@Param("id") Integer id);

    /**
     * 根据主键id更新表r_material_block数据
     *
     * @param materialBlock
     */
    Integer updateMaterialBlockById(MaterialBlock materialBlock);

    /**
     * 新增表r_material_block数据
     *
     * @param materialBlock
     */
    Integer createMaterialBlock(MaterialBlock materialBlock);
}
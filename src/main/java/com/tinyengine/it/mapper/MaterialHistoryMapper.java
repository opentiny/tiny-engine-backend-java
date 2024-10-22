package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.MaterialHistory;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MaterialHistoryMapper extends BaseMapper<MaterialHistory> {

    /**
    *  查询表t_material_history所有信息
    */
    List<MaterialHistory> queryAllMaterialHistory();

    /**
    * 根据主键id查询表t_material_history数据
    * @param id
    */
    MaterialHistory queryMaterialHistoryById(@Param("id") Integer id);
    /**
    *  根据条件查询表t_material_history数据
    *  @param materialHistory
    */
    List<MaterialHistory> queryMaterialHistoryByCondition(MaterialHistory materialHistory);

    /**
    *  根据主键id删除表t_material_history数据
    *  @param id
    */
    Integer deleteMaterialHistoryById(@Param("id") Integer id);

    /**
    *  根据主键id更新表t_material_history数据
    *  @param materialHistory
    */
    Integer updateMaterialHistoryById(MaterialHistory materialHistory);

    /**
    *  新增表t_material_history数据
    *  @param materialHistory
    */
    Integer createMaterialHistory(MaterialHistory materialHistory);
}
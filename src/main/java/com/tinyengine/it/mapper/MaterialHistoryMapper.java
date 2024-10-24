package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.Component;
import com.tinyengine.it.model.entity.MaterialHistory;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface MaterialHistoryMapper extends BaseMapper<MaterialHistory> {

    /**
     * 查询表t_material_history所有信息
     */
    List<MaterialHistory> queryAllMaterialHistory();

    /**
     * 根据主键id查询表t_material_history数据
     *
     * @param id
     */

    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "components", column = "id",
                    many = @Many(select = "findUserComponentsByMaterialHistoryId"))
    })
    @Select("SELECT * FROM t_material_history WHERE id = #{id}")
    MaterialHistory queryMaterialHistoryById(@Param("id") Integer id);

    @Select("SELECT C.* FROM t_component C " +
            "JOIN r_material_history_component MHC ON C.id = MHC.`component_id` " +
            "WHERE MHC.`material_history_id` = #{id}")
    List<Component> findUserComponentsByMaterialHistoryId(@Param("id") Integer id);
    /**
     * 根据条件查询表t_material_history数据
     *
     * @param materialHistory
     */
    List<MaterialHistory> queryMaterialHistoryByCondition(MaterialHistory materialHistory);

    /**
     * 根据主键id删除表t_material_history数据
     *
     * @param id
     */
    Integer deleteMaterialHistoryById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_material_history数据
     *
     * @param materialHistory
     */
    Integer updateMaterialHistoryById(MaterialHistory materialHistory);

    /**
     * 新增表t_material_history数据
     *
     * @param materialHistory
     */
    Integer createMaterialHistory(MaterialHistory materialHistory);

    @Select(" SELECT block_history_id FROM r_material_history_block WHERE material_history_id=#{materialHistoryId}")
    List<Integer> queryBlockHistoryBymaterialHistoryId(@Param("materialHistoryId") Integer materialHistoryId);
}
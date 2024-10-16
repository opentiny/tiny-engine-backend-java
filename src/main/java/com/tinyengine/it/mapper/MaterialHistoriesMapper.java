package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.MaterialHistories;
import com.tinyengine.it.model.entity.UserComponents;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface MaterialHistoriesMapper extends BaseMapper<MaterialHistories> {

    /**
     * 查询表material_histories所有信息
     */
    List<MaterialHistories> findAllMaterialHistories();

    /**
     * 根据主键id查询表material_histories数据
     *
     * @param id
     */
    @Select("SELECT * FROM material_histories WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "components", column = "id",
                    many = @Many(select = "findUserComponentsByMaterialHistoryId"))
    })
    MaterialHistories findMaterialHistoriesById(@Param("id") Integer id);

    @Select("SELECT uc.* FROM user_components uc " +
            "JOIN material_histories_components__user_components_mhs mhucm ON uc.id = mhucm.`user-component_id` " +
            "WHERE mhucm.`material-history_id` = #{id}")
    List<UserComponents> findUserComponentsByMaterialHistoryId(@Param("id") Integer id);

    /**
     * 根据条件查询表material_histories数据
     *
     * @param materialHistories
     */
    List<MaterialHistories> findMaterialHistoriesByCondition(MaterialHistories materialHistories);

    /**
     * 根据主键id删除表material_histories数据
     *
     * @param id
     */
    Integer deleteMaterialHistoriesById(@Param("id") Integer id);

    /**
     * 根据主键id更新表material_histories数据
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
/**
 * Copyright (c) 2023 - present TinyEngine Authors.
 * Copyright (c) 2023 - present Huawei Cloud Computing Technologies Co., Ltd.
 *
 * Use of this source code is governed by an MIT-style license.
 *
 * THE OPEN SOURCE SOFTWARE IN THIS PRODUCT IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL,
 * BUT WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS FOR
 * A PARTICULAR PURPOSE. SEE THE APPLICABLE LICENSES FOR MORE DETAILS.
 *
 */

package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.MaterialHistory;

import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * The interface Material history mapper.
 *
 * @since 2024-10-20
 */
public interface MaterialHistoryMapper extends BaseMapper<MaterialHistory> {
    /**
     * 查询表t_material_history所有信息
     *
     * @return the list
     */
    List<MaterialHistory> queryAllMaterialHistory();

    /**
     * 根据主键id查询表t_material_history数据
     *
     * @param id the id
     * @return the material history
     */
    @Results({@Result(property = "id", column = "id"),
            @Result(property = "components", column = "id",
            many = @Many(select = "com.tinyengine.it.mapper.ComponentMapper.findUserComponentsByMaterialHistoryId"))})
    @Select("SELECT * FROM t_material_history WHERE id = #{id}")
    MaterialHistory queryMaterialHistoryById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_material_history数据
     *
     * @param materialHistory the material history
     * @return the list
     */
    List<MaterialHistory> queryMaterialHistoryByCondition(MaterialHistory materialHistory);

    /**
     * 根据主键id删除表t_material_history数据
     *
     * @param id the id
     * @return the integer
     */
    Integer deleteMaterialHistoryById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_material_history数据
     *
     * @param materialHistory the material history
     * @return the integer
     */
    Integer updateMaterialHistoryById(MaterialHistory materialHistory);

    /**
     * 新增表t_material_history数据
     *
     * @param materialHistory the material history
     * @return the integer
     */
    Integer createMaterialHistory(MaterialHistory materialHistory);

    /**
     * Query block history bymaterial history id list.
     *
     * @param materialHistoryId the material history id
     * @return the list
     */
    @Select(" SELECT block_history_id FROM r_material_history_block WHERE material_history_id=#{materialHistoryId}")
    List<Integer> queryBlockHistoryBymaterialHistoryId(@Param("materialHistoryId") Integer materialHistoryId);
}
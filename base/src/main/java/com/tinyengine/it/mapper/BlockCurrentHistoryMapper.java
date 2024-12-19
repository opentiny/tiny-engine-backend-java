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
import com.tinyengine.it.model.entity.BlockCurrentHistory;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * The interface Block Current History mapper.
 *
 * @since 2024-12-16
 */
public interface BlockCurrentHistoryMapper extends BaseMapper<BlockCurrentHistory> {
    /**
     * 查询表t_block_current_history所有信息
     *
     * @return the list
     */
    List<BlockCurrentHistory> queryAllBlockCurrentHistory();

    /**
     * 根据主键id查询表t_block_current_history数据
     *
     * @param id the id
     * @return the block current history
     */
    BlockCurrentHistory queryBlockCurrentHistoryById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_block_current_history数据
     *
     * @param BlockCurrentHistory the block current history
     * @return the list
     */
    List<BlockCurrentHistory> queryBlockCurrentHistoryByCondition(BlockCurrentHistory BlockCurrentHistory);

    /**
     * 根据主键id删除表t_block_current_history数据
     *
     * @param id the id
     * @return the integer
     */
    Integer deleteBlockCurrentHistoryById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_block_current_history数据
     *
     * @param BlockCurrentHistory the block current history
     * @return the integer
     */
    Integer updateBlockCurrentHistoryById(BlockCurrentHistory BlockCurrentHistory);

    /**
     * 新增表t_block_current_history数据
     *
     * @param BlockCurrentHistory the block current history
     * @return the integer
     */
    Integer createBlockCurrentHistory(BlockCurrentHistory BlockCurrentHistory);

    /**
     * 根据区块id查询t_block_current_history数据
     *
     * @param blockId the block id
     * @return the BlockCurrentHistory
     */
    @Select("select id, block_id, block_history_id, app_id from t_block_current_history where block_id = #{blockId}")
    BlockCurrentHistory findBlockCurrentHistoriesByBlockId(int blockId);
}

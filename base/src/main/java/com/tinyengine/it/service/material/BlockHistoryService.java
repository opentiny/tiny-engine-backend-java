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

package com.tinyengine.it.service.material;

import com.tinyengine.it.model.entity.BlockHistory;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface Block history service.
 *
 * @since 2024-10-20
 */
public interface BlockHistoryService {
    /**
     * 查询表t_block_history所有信息
     *
     * @return the list
     */
    List<BlockHistory> findAllBlockHistory();

    /**
     * 根据主键id查询表t_block_history信息
     *
     * @param id the id
     * @return the block history
     */
    BlockHistory findBlockHistoryById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_block_history信息
     *
     * @param blockHistory the block history
     * @return the list
     */
    List<BlockHistory> findBlockHistoryByCondition(BlockHistory blockHistory);

    /**
     * 根据主键id删除t_block_history数据
     *
     * @param id the id
     * @return the integer
     */
    Integer deleteBlockHistoryById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_block_history信息
     *
     * @param blockHistory the block history
     * @return the integer
     */
    Integer updateBlockHistoryById(BlockHistory blockHistory);

    /**
     * 新增表t_block_history数据
     *
     * @param blockHistory the block history
     * @return the integer
     */
    Integer createBlockHistory(BlockHistory blockHistory);
}

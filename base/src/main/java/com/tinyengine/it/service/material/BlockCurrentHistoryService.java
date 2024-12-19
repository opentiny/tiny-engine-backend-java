/**
 * Copyright (c) 2023 - present TinyEngine Authors.
 * Copyright (c) 2023 - present Huawei Cloud Computing Technologies Co., Ltd.
 * <p>
 * Use of this source code is governed by an MIT-style license.
 * <p>
 * THE OPEN SOURCE SOFTWARE IN THIS PRODUCT IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL,
 * BUT WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS FOR
 * A PARTICULAR PURPOSE. SEE THE APPLICABLE LICENSES FOR MORE DETAILS.
 */

package com.tinyengine.it.service.material;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.model.entity.BlockCurrentHistory;
import com.tinyengine.it.model.entity.BlockHistory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface BlockCurrentHistory service.
 *
 * @since 2024-12-17
 */
public interface BlockCurrentHistoryService {
    /**
     * 查询表t_block_current_history所有信息
     *
     * @return the list
     */
    List<BlockCurrentHistory> queryAllBlockCurrentHistory();

    /**
     * 根据主键id查询表t_block_history信息
     *
     * @param id the id
     * @return the BlockCurrentHistory
     */
    BlockHistory queryBlockCurrentHistoryById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_block_current_history信息
     *
     * @param blockCurrentHistory the BlockCurrentHistory
     * @return the list
     */
    List<BlockCurrentHistory> queryBlockCurrentHistoryByCondition(BlockCurrentHistory blockCurrentHistory);

    /**
     * 根据主键id删除t_block_current_history数据
     *
     * @param id the id
     * @return the integer
     */
    Result<BlockCurrentHistory> deleteBlockCurrentHistoryById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_block_current_history信息
     *
     * @param blockCurrentHistory the BlockCurrentHistory
     * @return the integer
     */
    Result<BlockCurrentHistory> updateBlockCurrentHistoryById(BlockCurrentHistory blockCurrentHistory);

    /**
     * 新增表t_block_current_history数据
     *
     * @param blockCurrentHistory the BlockCurrentHistory
     * @return the integer
     */
    Result<BlockCurrentHistory> createBlockCurrentHistory(BlockCurrentHistory blockCurrentHistory);
}

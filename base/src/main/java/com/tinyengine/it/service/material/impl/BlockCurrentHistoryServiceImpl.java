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

package com.tinyengine.it.service.material.impl;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.common.log.SystemServiceLog;
import com.tinyengine.it.mapper.BlockCurrentHistoryMapper;
import com.tinyengine.it.mapper.BlockHistoryMapper;
import com.tinyengine.it.model.entity.BlockCurrentHistory;
import com.tinyengine.it.model.entity.BlockHistory;
import com.tinyengine.it.service.material.BlockCurrentHistoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The interface BlockCurrentHistory service.
 *
 * @since 2024-12-17
 */
@Service
public class BlockCurrentHistoryServiceImpl implements BlockCurrentHistoryService {
    @Autowired
    private BlockCurrentHistoryMapper blockCurrentHistoryMapper;

    @Autowired
    private BlockHistoryMapper blockHistoryMapper;

    /**
     * 查询表t_block_current_history所有信息
     *
     * @return the list
     */
    @Override
    public List<BlockCurrentHistory> queryAllBlockCurrentHistory() {
        return blockCurrentHistoryMapper.queryAllBlockCurrentHistory();
    }

    /**
     * 根据主键id查询表t_block_history信息
     *
     * @param id the id
     * @return the BlockCurrentHistory
     */
    @Override
    public BlockHistory queryBlockCurrentHistoryById(Integer id) {
        BlockCurrentHistory blockCurrentHistory = blockCurrentHistoryMapper
                .queryBlockCurrentHistoryById(id);
        int blockHistoryId = blockCurrentHistory.getBlockHistoryId();
        return blockHistoryMapper.queryBlockHistoryById(blockHistoryId);
    }

    /**
     * 根据条件查询表t_block_current_history信息
     *
     * @param blockCurrentHistory the BlockCurrentHistory
     * @return the list
     */
    @Override
    public List<BlockCurrentHistory> queryBlockCurrentHistoryByCondition(BlockCurrentHistory blockCurrentHistory) {
        return queryBlockCurrentHistoryByCondition(blockCurrentHistory);
    }

    /**
     * 根据主键id删除t_block_current_history数据
     *
     * @param id the id
     * @return the integer
     */
    @Override
    @SystemServiceLog(description = "删除BlockCurrentHistory信息")
    public Result<BlockCurrentHistory> deleteBlockCurrentHistoryById(Integer id) {
        BlockCurrentHistory blockCurrentHistory = blockCurrentHistoryMapper
                .queryBlockCurrentHistoryById(id);
        if (blockCurrentHistory == null) {
            return Result.failed(ExceptionEnum.CM009);
        }
        Integer result = blockCurrentHistoryMapper.deleteBlockCurrentHistoryById(id);
        if (result < 1) {
            return Result.failed(ExceptionEnum.CM008);
        }
        return Result.success(blockCurrentHistory);
    }

    /**
     * 根据主键id更新表t_block_current_history信息
     *
     * @param blockCurrentHistory the BlockCurrentHistory
     * @return the integer
     */
    @Override
    @SystemServiceLog(description = "修改单个BlockCurrentHistory信息")
    public Result<BlockCurrentHistory> updateBlockCurrentHistoryById(BlockCurrentHistory blockCurrentHistory) {
        Integer result = blockCurrentHistoryMapper.updateBlockCurrentHistoryById(blockCurrentHistory);
        if (result < 1) {
            return Result.failed(ExceptionEnum.CM008);
        }
        int id = blockCurrentHistory.getId();
        BlockCurrentHistory queryResult = blockCurrentHistoryMapper.queryBlockCurrentHistoryById(id);
        return Result.success(queryResult);
    }

    /**
     * 新增表t_block_current_history数据
     *
     * @param blockCurrentHistory the BlockCurrentHistory
     * @return the integer
     */
    @Override
    @SystemServiceLog(description = "创建BlockCurrentHistory")
    public Result<BlockCurrentHistory> createBlockCurrentHistory(BlockCurrentHistory blockCurrentHistory) {
        List<BlockCurrentHistory> historyList = blockCurrentHistoryMapper
                .queryBlockCurrentHistoryByCondition(blockCurrentHistory);
        if (!historyList.isEmpty() && historyList.get(0) != null) {
            int id = historyList.get(0).getId();
            blockCurrentHistory.setId(id);
            return updateBlockCurrentHistoryById(blockCurrentHistory);
        }
        int result = blockCurrentHistoryMapper.createBlockCurrentHistory(blockCurrentHistory);
        if (result < 1) {
            return Result.failed(ExceptionEnum.CM008);
        }
        BlockCurrentHistory queryResult = blockCurrentHistoryMapper
                .queryBlockCurrentHistoryById(blockCurrentHistory.getId());
        return Result.success(queryResult);
    }
}

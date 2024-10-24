package com.tinyengine.it.service.material.impl;

import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.mapper.BlockHistoryMapper;
import com.tinyengine.it.model.entity.BlockHistory;
import com.tinyengine.it.service.material.BlockHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Block history service.
 */
@Service
@Slf4j
public class BlockHistoryServiceImpl implements BlockHistoryService {

    @Autowired
    private BlockHistoryMapper blockHistoryMapper;

    /**
     * 查询表t_block_history所有数据
     */
    @Override
    public List<BlockHistory> findAllBlockHistory() throws ServiceException {
        return blockHistoryMapper.queryAllBlockHistory();
    }

    /**
     * 根据主键id查询表t_block_history信息
     *
     * @param id
     */
    @Override
    public BlockHistory findBlockHistoryById(@Param("id") Integer id) throws ServiceException {
        return blockHistoryMapper.queryBlockHistoryById(id);
    }

    /**
     * 根据条件查询表t_block_history数据
     *
     * @param blockHistory
     */
    @Override
    public List<BlockHistory> findBlockHistoryByCondition(BlockHistory blockHistory) throws ServiceException {
        return blockHistoryMapper.queryBlockHistoryByCondition(blockHistory);
    }

    /**
     * 根据主键id删除表t_block_history数据
     *
     * @param id
     */
    @Override
    public Integer deleteBlockHistoryById(@Param("id") Integer id) throws ServiceException {
        return blockHistoryMapper.deleteBlockHistoryById(id);
    }

    /**
     * 根据主键id更新表t_block_history数据
     *
     * @param blockHistory
     */
    @Override
    public Integer updateBlockHistoryById(BlockHistory blockHistory) throws ServiceException {
        return blockHistoryMapper.updateBlockHistoryById(blockHistory);
    }

    /**
     * 新增表t_block_history数据
     *
     * @param blockHistory
     */
    @Override
    public Integer createBlockHistory(BlockHistory blockHistory) throws ServiceException {
        return blockHistoryMapper.createBlockHistory(blockHistory);
    }
}

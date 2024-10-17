package com.tinyengine.it.service.impl.material;

import com.tinyengine.it.model.entity.BlockHistory;
import com.tinyengine.it.mapper.BlockHistoryMapper;
import com.tinyengine.it.service.material.BlockHistoryService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.ibatis.annotations.Param;
import com.tinyengine.it.exception.ServiceException;

import java.util.List;

@Service
public class BlockHistoryServiceImpl implements BlockHistoryService {

    @Autowired
    private BlockHistoryMapper blockHistoryMapper;

    /**
    *  查询表t_block_history所有数据
    */
    @Override
    public List<BlockHistory> findAllBlockHistory() throws ServiceException {
        return blockHistoryMapper.findAllBlockHistory();
    }

    /**
    *  根据主键id查询表t_block_history信息
    *  @param id
    */
    @Override
    public BlockHistory findBlockHistoryById(@Param("id") Integer id) throws ServiceException {
        return blockHistoryMapper.findBlockHistoryById(id);
    }

    /**
    *  根据条件查询表t_block_history数据
    *  @param blockHistory
    */
    @Override
    public List<BlockHistory> findBlockHistoryByCondition(BlockHistory blockHistory) throws ServiceException {
        return blockHistoryMapper.findBlockHistoryByCondition(blockHistory);
    }

    /**
    *  根据主键id删除表t_block_history数据
    *  @param id
    */
    @Override
    public Integer deleteBlockHistoryById(@Param("id") Integer id) throws ServiceException {
        return blockHistoryMapper.deleteBlockHistoryById(id);
    }

    /**
    *  根据主键id更新表t_block_history数据
    *  @param blockHistory
    */
    @Override
    public Integer updateBlockHistoryById(BlockHistory blockHistory) throws ServiceException {
        return blockHistoryMapper.updateBlockHistoryById(blockHistory);
    }

    /**
    *  新增表t_block_history数据
    *  @param blockHistory
    */
    @Override
    public Integer createBlockHistory(BlockHistory blockHistory) throws ServiceException {
        return blockHistoryMapper.createBlockHistory(blockHistory);
    }
}

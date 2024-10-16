package com.tinyengine.it.service.impl.material;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tinyengine.it.config.SystemServiceLog;
import com.tinyengine.it.exception.ServiceException;
import com.tinyengine.it.mapper.BlockHistoriesMapper;
import com.tinyengine.it.model.entity.BlockHistories;
import com.tinyengine.it.service.material.BlockHistoriesService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlockHistoriesServiceImpl implements BlockHistoriesService {

    @Autowired
    BlockHistoriesMapper blockHistoriesMapper;


    /**
     * 根据主键id查询表block_histories信息
     *
     * @param id
     */
    @Override
    public BlockHistories findBlockHistoriesById(@Param("id") Integer id) throws ServiceException {
        return blockHistoriesMapper.findBlockHistoriesById(id);
    }

    /**
     * 根据条件查询表block_histories数据
     *
     * @param blockHistories
     */
    @SystemServiceLog(description = "findBlockHistoriesByCondition 根据条件查询表block_histories数据 实现类")
    @Override
    public List<BlockHistories> findBlockHistoriesByCondition(BlockHistories blockHistories) throws ServiceException {
        if (blockHistories.getBlockId() == null) {
            return blockHistoriesMapper.findBlockHistoriesByCondition(blockHistories);
        }
        QueryWrapper<BlockHistories> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("block_id", blockHistories.getBlockId());
        return blockHistoriesMapper.selectList(queryWrapper);
    }

    /**
     * 根据主键id删除表block_histories数据
     *
     * @param id
     */
    @Override
    public Integer deleteBlockHistoriesById(@Param("id") Integer id) throws ServiceException {
        return blockHistoriesMapper.deleteBlockHistoriesById(id);
    }


    /**
     * 新增表block_histories数据
     *
     * @param blockHistories
     */
    @Override
    public Integer createBlockHistories(BlockHistories blockHistories) throws ServiceException {
        return blockHistoriesMapper.createBlockHistories(blockHistories);
    }
}

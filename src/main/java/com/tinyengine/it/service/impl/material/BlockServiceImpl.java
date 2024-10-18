package com.tinyengine.it.service.impl.material;

import com.tinyengine.it.model.entity.Block;
import com.tinyengine.it.mapper.BlockMapper;
import com.tinyengine.it.service.material.BlockService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.ibatis.annotations.Param;
import com.tinyengine.it.exception.ServiceException;

import java.util.List;

@Service
public class BlockServiceImpl implements BlockService {

    @Autowired
    private BlockMapper blockMapper;

    /**
    *  查询表t_block所有数据
    */
    @Override
    public List<Block> findAllBlock() throws ServiceException {
        return blockMapper.findAllBlock();
    }

    /**
    *  根据主键id查询表t_block信息
    *  @param id
    */
    @Override
    public Block findBlockById(@Param("id") Integer id) throws ServiceException {
        return blockMapper.findBlockById(id);
    }

    /**
    *  根据条件查询表t_block数据
    *  @param block
    */
    @Override
    public List<Block> findBlockByCondition(Block block) throws ServiceException {
        return blockMapper.findBlockByCondition(block);
    }

    /**
    *  根据主键id删除表t_block数据
    *  @param id
    */
    @Override
    public Integer deleteBlockById(@Param("id") Integer id) throws ServiceException {
        return blockMapper.deleteBlockById(id);
    }

    /**
    *  根据主键id更新表t_block数据
    *  @param block
    */
    @Override
    public Integer updateBlockById(Block block) throws ServiceException {
        return blockMapper.updateBlockById(block);
    }

    /**
    *  新增表t_block数据
    *  @param block
    */
    @Override
    public Integer createBlock(Block block) throws ServiceException {
        return blockMapper.createBlock(block);
    }
}

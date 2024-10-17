package com.tinyengine.it.service.impl.material;

import com.tinyengine.it.model.entity.BlockGroup;
import com.tinyengine.it.mapper.BlockGroupMapper;
import com.tinyengine.it.service.material.BlockGroupService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.ibatis.annotations.Param;
import com.tinyengine.it.exception.ServiceException;

import java.util.List;

@Service
public class BlockGroupServiceImpl implements BlockGroupService {

    @Autowired
    private BlockGroupMapper blockGroupMapper;

    /**
    *  查询表t_block_group所有数据
    */
    @Override
    public List<BlockGroup> findAllBlockGroup() throws ServiceException {
        return blockGroupMapper.findAllBlockGroup();
    }

    /**
    *  根据主键id查询表t_block_group信息
    *  @param id
    */
    @Override
    public BlockGroup findBlockGroupById(@Param("id") Integer id) throws ServiceException {
        return blockGroupMapper.findBlockGroupById(id);
    }

    /**
    *  根据条件查询表t_block_group数据
    *  @param blockGroup
    */
    @Override
    public List<BlockGroup> findBlockGroupByCondition(BlockGroup blockGroup) throws ServiceException {
        return blockGroupMapper.findBlockGroupByCondition(blockGroup);
    }

    /**
    *  根据主键id删除表t_block_group数据
    *  @param id
    */
    @Override
    public Integer deleteBlockGroupById(@Param("id") Integer id) throws ServiceException {
        return blockGroupMapper.deleteBlockGroupById(id);
    }

    /**
    *  根据主键id更新表t_block_group数据
    *  @param blockGroup
    */
    @Override
    public Integer updateBlockGroupById(BlockGroup blockGroup) throws ServiceException {
        return blockGroupMapper.updateBlockGroupById(blockGroup);
    }

    /**
    *  新增表t_block_group数据
    *  @param blockGroup
    */
    @Override
    public Integer createBlockGroup(BlockGroup blockGroup) throws ServiceException {
        return blockGroupMapper.createBlockGroup(blockGroup);
    }
}

package com.tinyengine.it.service.material.impl;

import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.mapper.BlockGroupMapper;
import com.tinyengine.it.model.entity.BlockGroup;
import com.tinyengine.it.service.material.BlockGroupService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Block group service.
 *
 * @since 2024-10-20
 */
@Service
@Slf4j
public class BlockGroupServiceImpl implements BlockGroupService {
    @Autowired
    private BlockGroupMapper blockGroupMapper;

    /**
     * 查询表t_block_group所有数据
     */
    @Override
    public List<BlockGroup> findAllBlockGroup() throws ServiceException {
        return blockGroupMapper.queryAllBlockGroup();
    }

    /**
     * 根据主键id查询表t_block_group信息
     *
     * @param id
     */
    @Override
    public BlockGroup findBlockGroupById(@Param("id") Integer id) throws ServiceException {
        return blockGroupMapper.queryBlockGroupById(id);
    }

    /**
     * 根据条件查询表t_block_group数据
     *
     * @param blockGroup
     */
    @Override
    public List<BlockGroup> findBlockGroupByCondition(BlockGroup blockGroup) throws ServiceException {
        return blockGroupMapper.queryBlockGroupByCondition(blockGroup);
    }

    /**
     * 根据主键id删除表t_block_group数据
     *
     * @param id
     */
    @Override
    public Integer deleteBlockGroupById(@Param("id") Integer id) throws ServiceException {
        return blockGroupMapper.deleteBlockGroupById(id);
    }

    /**
     * 根据主键id更新表t_block_group数据
     *
     * @param blockGroup
     */
    @Override
    public Integer updateBlockGroupById(BlockGroup blockGroup) throws ServiceException {
        return blockGroupMapper.updateBlockGroupById(blockGroup);
    }

    /**
     * 新增表t_block_group数据
     *
     * @param blockGroup
     */
    @Override
    public Integer createBlockGroup(BlockGroup blockGroup) throws ServiceException {
        return blockGroupMapper.createBlockGroup(blockGroup);
    }
}

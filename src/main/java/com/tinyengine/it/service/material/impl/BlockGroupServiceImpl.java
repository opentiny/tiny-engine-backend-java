package com.tinyengine.it.service.material.impl;

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
    public List<BlockGroup> findAllBlockGroup() {
        return blockGroupMapper.queryAllBlockGroup();
    }

    /**
     * 根据主键id查询表t_block_group信息
     *
     * @param id id
     * @return block group
     */
    @Override
    public BlockGroup findBlockGroupById(@Param("id") Integer id) {
        return blockGroupMapper.queryBlockGroupById(id);
    }

    /**
     * 根据条件查询表t_block_group数据
     *
     * @param blockGroup blockGroup
     * @return block group
     */
    @Override
    public List<BlockGroup> findBlockGroupByCondition(BlockGroup blockGroup) {
        return blockGroupMapper.queryBlockGroupByCondition(blockGroup);
    }

    /**
     * 根据主键id删除表t_block_group数据
     *
     * @param id id
     * @return delete number
     */
    @Override
    public Integer deleteBlockGroupById(@Param("id") Integer id) {
        return blockGroupMapper.deleteBlockGroupById(id);
    }

    /**
     * 根据主键id更新表t_block_group数据
     *
     * @param blockGroup blockGroup
     * @return update number
     */
    @Override
    public Integer updateBlockGroupById(BlockGroup blockGroup) {
        return blockGroupMapper.updateBlockGroupById(blockGroup);
    }

    /**
     * 新增表t_block_group数据
     *
     * @param blockGroup blockGroup
     * @return insert number
     */
    @Override
    public Integer createBlockGroup(BlockGroup blockGroup) {
        return blockGroupMapper.createBlockGroup(blockGroup);
    }
}

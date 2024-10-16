package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.BlocksGroupsBlockGroupsBlocks;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BlocksGroupsBlockGroupsBlocksMapper extends BaseMapper<BlocksGroupsBlockGroupsBlocks> {

    /**
     * 查询表blocks_groups__block_groups_blocks所有信息
     */
    List<BlocksGroupsBlockGroupsBlocks> findAllBlocksGroupsBlockGroupsBlocks();

    /**
     * 根据主键id查询表blocks_groups__block_groups_blocks数据
     *
     * @param id
     */
    BlocksGroupsBlockGroupsBlocks findBlocksGroupsBlockGroupsBlocksById(@Param("id") Integer id);

    /**
     * 根据条件查询表blocks_groups__block_groups_blocks数据
     *
     * @param blocksGroupsBlockGroupsBlocks
     */
    List<BlocksGroupsBlockGroupsBlocks> findBlocksGroupsBlockGroupsBlocksByCondition(BlocksGroupsBlockGroupsBlocks blocksGroupsBlockGroupsBlocks);

    /**
     * 根据主键id删除表blocks_groups__block_groups_blocks数据
     *
     * @param id
     */
    Integer deleteBlocksGroupsBlockGroupsBlocksById(@Param("id") Integer id);

    /**
     * 根据主键id更新表blocks_groups__block_groups_blocks数据
     *
     * @param blocksGroupsBlockGroupsBlocks
     */
    Integer updateBlocksGroupsBlockGroupsBlocksById(BlocksGroupsBlockGroupsBlocks blocksGroupsBlockGroupsBlocks);

    /**
     * 新增表blocks_groups__block_groups_blocks数据
     *
     * @param blocksGroupsBlockGroupsBlocks
     */
    Integer createBlocksGroupsBlockGroupsBlocks(BlocksGroupsBlockGroupsBlocks blocksGroupsBlockGroupsBlocks);
}
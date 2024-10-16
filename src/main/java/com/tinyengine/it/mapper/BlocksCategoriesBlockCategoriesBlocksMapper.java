package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.BlocksCategoriesBlockCategoriesBlocks;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BlocksCategoriesBlockCategoriesBlocksMapper extends BaseMapper<BlocksCategoriesBlockCategoriesBlocks> {

    /**
     * 查询表blocks_categories__block_categories_blocks所有信息
     */
    List<BlocksCategoriesBlockCategoriesBlocks> findAllBlocksCategoriesBlockCategoriesBlocks();

    /**
     * 根据主键id查询表blocks_categories__block_categories_blocks数据
     *
     * @param id
     */
    BlocksCategoriesBlockCategoriesBlocks findBlocksCategoriesBlockCategoriesBlocksById(@Param("id") Integer id);

    /**
     * 根据条件查询表blocks_categories__block_categories_blocks数据
     *
     * @param blocksCategoriesBlockCategoriesBlocks
     */
    List<BlocksCategoriesBlockCategoriesBlocks> findBlocksCategoriesBlockCategoriesBlocksByCondition(BlocksCategoriesBlockCategoriesBlocks blocksCategoriesBlockCategoriesBlocks);

    /**
     * 根据主键id删除表blocks_categories__block_categories_blocks数据
     *
     * @param id
     */
    Integer deleteBlocksCategoriesBlockCategoriesBlocksById(@Param("id") Integer id);

    /**
     * 根据主键id更新表blocks_categories__block_categories_blocks数据
     *
     * @param blocksCategoriesBlockCategoriesBlocks
     */
    Integer updateBlocksCategoriesBlockCategoriesBlocksById(BlocksCategoriesBlockCategoriesBlocks blocksCategoriesBlockCategoriesBlocks);

    /**
     * 新增表blocks_categories__block_categories_blocks数据
     *
     * @param blocksCategoriesBlockCategoriesBlocks
     */
    Integer createBlocksCategoriesBlockCategoriesBlocks(BlocksCategoriesBlockCategoriesBlocks blocksCategoriesBlockCategoriesBlocks);
}
package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.Block;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface Block mapper.
 *
 * @since 2024-10-20
 */
public interface BlockMapper extends BaseMapper<Block> {
    /**
     * 查询表t_block所有信息
     *
     * @return the list
     */
    List<Block> queryAllBlock();

    /**
     * 根据主键id查询表t_block数据
     *
     * @param id the id
     * @return the block
     */
    Block queryBlockById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_block数据
     *
     * @param block the block
     * @return the list
     */
    List<Block> queryBlockByCondition(Block block);

    /**
     * 根据主键id删除表t_block数据
     *
     * @param id the id
     * @return the integer
     */
    Integer deleteBlockById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_block数据
     *
     * @param block the block
     * @return the integer
     */
    Integer updateBlockById(Block block);

    /**
     * 新增表t_block数据
     *
     * @param block the block
     * @return the integer
     */
    Integer createBlock(Block block);
}
package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.Block;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface BlockMapper extends BaseMapper<Block> {

    /**
     * 查询表t_block所有信息
     */
    List<Block> queryAllBlock();

    /**
     * 根据主键id查询表t_block数据
     *
     * @param id
     */
    Block queryBlockById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_block数据
     *
     * @param block
     */
    List<Block> queryBlockByCondition(Block block);

    /**
     * 根据主键id删除表t_block数据
     *
     * @param id
     */
    Integer deleteBlockById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_block数据
     *
     * @param block
     */
    Integer updateBlockById(Block block);

    /**
     * 新增表t_block数据
     *
     * @param block
     */
    Integer createBlock(Block block);
}
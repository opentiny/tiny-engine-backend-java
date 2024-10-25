package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.BlockGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface Block group mapper.
 *
 * @since 2024-10-20
 */
public interface BlockGroupMapper extends BaseMapper<BlockGroup> {
    /**
     * 查询表t_block_group所有信息
     *
     * @return the list
     */
    List<BlockGroup> queryAllBlockGroup();

    /**
     * 根据主键id查询表t_block_group数据
     *
     * @param id the id
     * @return the block group
     */
    BlockGroup queryBlockGroupById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_block_group数据
     *
     * @param blockGroup the block group
     * @return the list
     */
    List<BlockGroup> queryBlockGroupByCondition(BlockGroup blockGroup);

    /**
     * 根据主键id删除表t_block_group数据
     *
     * @param id the id
     * @return the integer
     */
    Integer deleteBlockGroupById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_block_group数据
     *
     * @param blockGroup the block group
     * @return the integer
     */
    Integer updateBlockGroupById(BlockGroup blockGroup);

    /**
     * 新增表t_block_group数据
     *
     * @param blockGroup the block group
     * @return the integer
     */
    Integer createBlockGroup(BlockGroup blockGroup);

    /**
     * 通过appId查区块分组
     *
     * @param appId the app id
     * @return the list
     */
    List<BlockGroup> queryBlockGroupByApp(Integer appId);
}
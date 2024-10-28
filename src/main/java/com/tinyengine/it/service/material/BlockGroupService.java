
package com.tinyengine.it.service.material;

import com.tinyengine.it.model.entity.BlockGroup;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface Block group service.
 *
 * @since 2024-10-20
 */
public interface BlockGroupService {
    /**
     * 查询表t_block_group所有信息
     *
     * @return the list
     */
    List<BlockGroup> findAllBlockGroup();

    /**
     * 根据主键id查询表t_block_group信息
     *
     * @param id the id
     * @return the block group
     */
    BlockGroup findBlockGroupById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_block_group信息
     *
     * @param blockGroup the block group
     * @return the list
     */
    List<BlockGroup> findBlockGroupByCondition(BlockGroup blockGroup);

    /**
     * 根据主键id删除t_block_group数据
     *
     * @param id the id
     * @return the integer
     */
    Integer deleteBlockGroupById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_block_group信息
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
}

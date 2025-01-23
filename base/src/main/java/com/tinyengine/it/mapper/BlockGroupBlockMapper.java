/**
 * Copyright (c) 2023 - present TinyEngine Authors.
 * Copyright (c) 2023 - present Huawei Cloud Computing Technologies Co., Ltd.
 * <p>
 * Use of this source code is governed by an MIT-style license.
 * <p>
 * THE OPEN SOURCE SOFTWARE IN THIS PRODUCT IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL,
 * BUT WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS FOR
 * A PARTICULAR PURPOSE. SEE THE APPLICABLE LICENSES FOR MORE DETAILS.
 */

package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.BlockGroupBlock;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * The block group block mapper.
 *
 * @since 2024-10-20
 */
public interface BlockGroupBlockMapper extends BaseMapper<BlockGroupBlock> {
    /**
     * 查询表r_bock_group_block所有信息
     *
     * @return the list
     */
    List<BlockGroupBlock> queryAllBlockGroupBlock();

    /**
     * 根据主键id查询表r_bock_group_block数据
     *
     * @param id the id
     * @return the bock group block
     */
    BlockGroupBlock queryBlockGroupBlockById(@Param("id") Integer id);

    /**
     * 根据条件查询表r_bock_group_block数据
     *
     * @param blockGroupBlock the bock group block
     * @return the list
     */
    List<BlockGroupBlock> queryBlockGroupBlockByCondition(BlockGroupBlock blockGroupBlock);

    /**
     * 根据主键id删除表r_bock_group_block数据
     *
     * @param id the id
     * @return the integer
     */
    Integer deleteBlockGroupBlockById(@Param("id") Integer id);

    /**
     * 根据主键id更新表r_bock_group_block数据
     *
     * @param blockGroupBlock the bock group block
     * @return the integer
     */
    Integer updateBlockGroupBlockById(BlockGroupBlock blockGroupBlock);

    /**
     * 新增表r_bock_group_block数据
     *
     * @param blockGroupBlock the bock group block
     * @return the integer
     */
    Integer createBlockGroupBlock(BlockGroupBlock blockGroupBlock);

    /**
     * 通过区块分组id删除分组下区块
     *
     * @param blockGroupId the block group id
     * @return the int
     */
    int deleteBlockGroupBlockByGroupId(Integer blockGroupId);

    /**
     * 通过区块分组id查询分组下区块
     *
     * @param blockGroupId the block group id
     * @return the list
     */
    @Select("select * from r_block_group_block where block_group_id = #{blockGroupId}")
    List<BlockGroupBlock> findBlockGroupBlockByBlockGroupId(Integer blockGroupId);

    /**
     * 通过区块分组id查询分组下区块
     * @param blockId the block id
     * @param groupId the block group id
     * @return the list
     */
    @Delete("delete from r_block_group_block where block_group_id = #{groupId} and block_id = #{blockId}")
    Integer deleteByGroupIdAndBlockId(Integer groupId, Integer blockId);
}

/**
 * Copyright (c) 2023 - present TinyEngine Authors.
 * Copyright (c) 2023 - present Huawei Cloud Computing Technologies Co., Ltd.
 *
 * Use of this source code is governed by an MIT-style license.
 *
 * THE OPEN SOURCE SOFTWARE IN THIS PRODUCT IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL,
 * BUT WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS FOR
 * A PARTICULAR PURPOSE. SEE THE APPLICABLE LICENSES FOR MORE DETAILS.
 *
 */

package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.dto.BlockDto;
import com.tinyengine.it.model.entity.Block;

import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

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

    /**
     * 通过区块分组id获取区块信息
     *
     * @param blockGroupId the block group id
     * @return the list
     */
    @Select("select b.* from t_block b "
            + "where b.block_group_id = #{blockGroupId} ")
    List<Block> findBlocksByBlockGroupId(int blockGroupId);

    /**
     * 根据name或者description查询表t_block信息
     *
     * @param nameCn      name
     * @param description description
     * @return the list
     */
    List<Block> findBlocksByNameCnAndDes(String nameCn, String description);

    /**
     * 根据区块id查询区块信息、以及关联的分组信息、区块历史信息
     *
     * @param blockId the block id
     * @return block dto
     */
    @Results({
            @Result(column = "occupier_by", property = "occupierId"),
            @Result(column = "block_id", javaType = List.class, property = "groups",
                    many = @Many(select = "com.tinyengine.it.mapper.BlockGroupMapper.findBlockGroupByBlockId")),
            @Result(column = "block_id", javaType = List.class, property = "histories",
                    many = @Many(select = "com.tinyengine.it.mapper.BlockHistoryMapper.findBlockHistoriesByBlockId")),
            @Result(column = "occupier_by", property = "occupier",
                    one = @One(select = "com.tinyengine.it.mapper.UserMapper.queryUserById")),
            @Result(column = "block_id", property = "currentHistory",
                    one = @One(select = "com.tinyengine.it.mapper.BlockCurrentHistoryMapper.findBlockCurrentHistoriesByBlockId"))
    })
    @Select("select b.*, b.id as block_id "
            + "from t_block b "
            + "left join t_block_group bg on b.block_group_id = bg.id "
            + "left join t_block_history bh on b.latest_history_id = bh.id "
            + "where b.id = #{blockId} "
            + "group by b.id")
    BlockDto findBlockAndGroupAndHistoByBlockId(Integer blockId);


    /**
     * 根据区块id查询区块信息以及关联的历史信息
     *
     * @param blockId the block id
     * @return the list
     */
    @Results({
            @Result(column = "occupier_by", property = "occupierId"),
            @Result(column = "block_id", javaType = List.class, property = "histories",
                    many = @Many(select = "com.tinyengine.it.mapper.BlockHistoryMapper.findBlockHistoriesByBlockId")),
            @Result(column = "occupier_by", property = "occupier",
                    one = @One(select = "com.tinyengine.it.mapper.UserMapper.queryUserById"))
    })
    @Select("select b.*,bcbcb.block_id as block_categories_blocks_id,bsh.block_id as block_histories_block_id "
            + "from t_block b "
            + "left join t_block_history bh on b.latest_history_id = bh.id "
            + "where b.id = #{blockId} "
            + "group by b.id")
    List<BlockDto> findBlockAndHistorByBlockId(Integer blockId);


    /**
     * 通过区块分组id获取区块信息
     *
     * @param appId the app id
     * @return the list
     */

    List<Block> findBlocksByBlockGroupIdAppId(int appId);

    /**
     * 根据分组id返回对应的区块信息及关联分组信息
     *
     * @return the list
     */
    @Results({
            @Result(column = "occupier_by", property = "occupierId"),
            @Result(column = "public", property = "publicStatus"),
            @Result(column = "tiny_reserved", property = "isTinyReserved"),
            @Result(column = "block_group_id", property = "blockGroupId"),
            @Result(column = "block_group_id", javaType = List.class, property = "groups",
                    many = @Many(select = "com.tinyengine.it.mapper.BlockGroupMapper.queryBlockGroupById")),
            @Result(column = "occupier_by", property = "occupier",
                    one = @One(select = "com.tinyengine.it.mapper.UserMapper.queryUserById"))
    })
    @Select("select b.*, b.block_group_id as block_group_id "
            + "from t_block b "
    )
    List<BlockDto> findBlocksReturn();

    /**
     * 通过区块分组id获取自己创建的区块信息
     *
     * @param blockGroupId the block group id
     * @param createdBy    createdBy
     * @return the list
     */
    @Select("select b.* from t_block b "
            + "where b.block_group_id = #{blockGroupId} and b.created_by = #{createdBy}")
    List<Block> findBlockByBlockGroupId(int blockGroupId, String createdBy);
}
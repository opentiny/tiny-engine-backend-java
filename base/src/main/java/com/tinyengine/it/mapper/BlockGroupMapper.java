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
import com.tinyengine.it.model.dto.BlockGroupDto;
import com.tinyengine.it.model.entity.BlockGroup;

import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * The interface Block group mapper.
 *
 * @since 2024-10-20
 */
public interface BlockGroupMapper extends BaseMapper<BlockGroup> {
    /**
     * 查询表t_block_group所有信息及关联的区块信息
     * @param blockCreatedBy the blockCreatedBy
     * @param groupCreatedBy the groupCreatedBy
     * @return the list
     */
    List<BlockGroup> queryAllBlockGroupAndBlock(String blockCreatedBy, String groupCreatedBy);

    /**
     * 查询表t_block_group所有信息
     *
     * @return the list
     */
    @Select("select * from t_block_group ")
    List<BlockGroup> queryAllBlockGroup();

    /**
     * 根据主键id查询表t_block_group数据
     *
     * @param id        the id
     * @param blockCreatedBy the lockCreatedBy
     * @param groupCreatedBy the groupCreatedBy
     * @return the block group
     */
    BlockGroup queryBlockGroupAndBlockById(@Param("id") Integer id, String blockCreatedBy, String groupCreatedBy);

    /**
     * 通过ID查询分组信息
     *
     * @param id id
     * @return the list
     */
    @Select("select * from t_block_group bg where bg.id = #{id}")
    BlockGroup queryBlockGroupById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_block_group数据
     *
     * @param blockGroup the block group
     * @return the list
     */
    List<BlockGroupDto> queryBlockGroupByCondition(BlockGroup blockGroup);

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
     * 通过appId和createdBy查区块分组关联的区块信息
     *
     * @param appId     the app id
     * @param blockCreatedBy the blockCreatedBy
     * @param groupCreatedBy the groupCreatedBy
     * @return the list
     */
    List<BlockGroup> queryBlockGroupByAppId(Integer appId, String blockCreatedBy, String groupCreatedBy);

    /**
     * 通过appId获取分组信息
     *
     * @param appId appId
     * @return the list
     */
    @Select("select * from t_block_group bg where bg.app_id = #{appId}")
    List<BlockGroup> queryBlockGroupByApp(Integer appId);

    /**
     * 根据blockGroupId获取区块分组信息以及关联的app和block信息
     *
     * @param blockGroupId the block group id
     * @return the list
     */
    @Results({
            @Result(column = "app", property = "appId"),
            @Result(column = "app", property = "app",
                    one = @One(select = "com.tinyengine.it.mapper.AppMapper.queryAppById")),
            @Result(column = "id", javaType = List.class, property = "blocks",
                    many = @Many(select = "com.tinyengine.it.mapper.BlockMapper.findBlockByBlockGroupId"))
    })
    @Select("SELECT bg.*, a.id as app "
            + "FROM t_block_group bg "
            + "left join t_app a on bg.app_id = a.id "
            + "WHERE bg.id = #{blockGroupId} ")
    List<BlockGroupDto> getBlockGroupsById(int blockGroupId);


    /**
     * 根据区块id查询区块分组信息
     * @param createdBy the user id
     * @param blockId the block id
     * @return the list
     */
    @Select("select * from t_block_group bg "
            + "left join r_block_group_block bgb on bgb.block_group_id = bg.id "
            + "where bgb.block_id = #{blockId} and bg.created_by = #{createdBy}")
    List<BlockGroup> findBlockGroupByBlockId(Integer blockId, String createdBy);

}
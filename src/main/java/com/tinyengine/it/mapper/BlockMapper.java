package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.dto.BlockDto;
import com.tinyengine.it.model.entity.Block;

import org.apache.ibatis.annotations.*;

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
    @Select("select b.* from t_block b " +
            "where b.block_group_id = #{blockGroupId}")
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
                    one = @One(select = "com.tinyengine.it.mapper.UserMapper.queryUserById"))
    })
    @Select("select b.*, b.id as block_id " +
            "from t_block b " +
            "left join t_block_group bg on b.block_group_id = bg.id " +
            "left join t_block_history bh on b.latest_history_id = bh.id " +
            "where b.id = #{blockId} " +
            "group by b.id")
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
    @Select("select b.*,bcbcb.block_id as block_categories_blocks_id,bsh.block_id as block_histories_block_id " +
            "from t_block b " +
            "left join t_block_history bh on b.latest_history_id = bh.id " +
            "where b.id = #{blockId} " +
            "group by b.id")
    List<BlockDto> findBlockAndHistorByBlockId(Integer blockId);

    /**
     * 根据分组id返回对应的区块信息及关联分组信息
     *
     * @param blockGroupId the block group id
     * @return the list
     */
    @Results({
            @Result(column = "occupier_by", property = "occupierId"),
            @Result(column = "block_group_id", javaType = List.class, property = "groups",
                    many = @Many(select = "com.tinyengine.it.mapper.BlockGroupMapper.queryBlockGroupById")),
            @Result(column = "occupier_by", property = "occupier",
                    one = @One(select = "com.tinyengine.it.mapper.UserMapper.queryUserById"))
    })
    @Select("select b.*, bg.id as block_group_id " +
            "from t_block b " +
            "left join t_block_group bg on b.block_group_id = bg.id " +
            "where bg.id = #{blockGroupId}")
    List<BlockDto> findBlocksReturnByBlockGroupId(int blockGroupId);

    /**
     * 通过label列表查询区块数据
     *
     * @param labelList the label list
     * @return the list
     */
    List<Block> findBlocksByLabels(List<String> labelList);

    /**
     * 通过区块分组id获取区块信息
     *
     * @param appId the app id
     * @return the list
     */

    List<Block> findBlocksByBlockGroupIdAppId(int appId);

}
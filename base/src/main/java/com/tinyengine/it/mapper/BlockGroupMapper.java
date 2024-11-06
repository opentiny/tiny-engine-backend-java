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
    BlockGroupDto queryBlockGroupById(@Param("id") Integer id);

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
     * 通过appId查区块分组
     *
     * @param appId the app id
     * @return the list
     */
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
            @Result(column = "block_group_id", javaType = List.class, property = "blocks",
                    many = @Many(select = "com.tinyengine.it.mapper.BlockMapper.findBlocksByBlockGroupId"))
    })
    @Select("SELECT bg.*, bs.block_group_id as block_group_id, a.id as app  "
            + "FROM t_block_group bg "
            + "left join t_app a on bg.app_id = a.id "
            + "left join t_block bs on bg.id = bs.block_group_id "
            + "WHERE bg.id = #{blockGroupId} "
            + "GROUP BY bg.id")
    List<BlockGroupDto> getBlockGroupsById(int blockGroupId);


    /**
     * 根据blockGroupIds获取区块分组信息以及关联的app和block信息
     *
     * @param ids the ids
     * @return the list
     */
    @Results({
            @Result(column = "app", property = "appId"),
            @Result(column = "app", property = "app",
                    one = @One(select = "com.tinyengine.it.mapper.AppMapper.queryAppById")),
            @Result(column = "block_group_id", javaType = List.class, property = "blocks",
                    many = @Many(select = "com.tinyengine.it.mapper.BlockMapper.findBlocksByBlockGroupId"))
    })
    @Select("<script>"
            + "SELECT bg.*, bs.block_group_id as block_group_id, a.id as app  "
            + "FROM t_block_group bg "
            + "left join t_app a on bg.app_id = a.id "
            + "left join t_block bs on bg.id = bs.block_group_id "
            + "WHERE bg.id in "
            + "<foreach item='id' collection='ids' open='(' separator=',' close=')'>"
            + "#{id}"
            + "</foreach>"
            + "GROUP BY bg.id"
            + "</script>"
    )
    List<BlockGroupDto> getBlockGroupsByIds(List<Integer> ids);


    /**
     * 根据appId获取区块分组信息以及关联的app和block信息
     *
     * @param appId the app id
     * @return the list
     */
    @Results({
            @Result(column = "app", property = "appId"),
            @Result(column = "app", property = "app",
                    one = @One(select = "com.tinyengine.it.mapper.AppMapper.queryAppById")),
            @Result(column = "block_group_id", javaType = List.class, property = "blocks",
                    many = @Many(select = "com.tinyengine.it.mapper.BlockMapper.findBlocksByBlockGroupId"))
    })
    @Select("select bg.*, bs.block_group_id as block_group_id "
            + "from t_block_group bg "
            + "left join t_app a on bg.app_id = a.id "
            + "left join t_block bs on bg.id = bs.block_group_id "
            + "where bg.app_id = #{appId} "
            + "GROUP BY bg.id")
    List<BlockGroupDto> findGroupByAppId(int appId);


    /**
     * 根据区块id查询区块分组信息
     *
     * @param blockId the block id
     * @return the list
     */
    @Select("select * from t_block_group bg "
            + "left join t_block tb on bg.id = tb.block_group_id "
            + "where tb.id = #{blockId}")
    List<BlockGroup> findBlockGroupByBlockId(Integer blockId);
}
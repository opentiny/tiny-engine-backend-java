package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.dto.BlockGroupsDto;
import com.tinyengine.it.model.entity.BlockGroups;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface BlockGroupsMapper extends BaseMapper<BlockGroups> {

    /**
     * 查询表block_groups所有信息
     */
    List<BlockGroupsDto> findAllBlockGroups();

    /**
     * 根据主键id查询表block_groups数据
     *
     * @param id
     */
    BlockGroupsDto findBlockGroupsById(@Param("id") Integer id);

    /**
     * 根据条件查询表block_groups数据
     *
     * @param blockGroups
     */
    List<BlockGroupsDto> findBlockGroupsByCondition(BlockGroups blockGroups);

    /**
     * 根据主键id删除表block_groups数据
     *
     * @param id
     */
    Integer deleteBlockGroupsById(@Param("id") Integer id);

    /**
     * 根据主键id更新表block_groups数据
     *
     * @param blockGroups
     */
    Integer updateBlockGroupsById(BlockGroups blockGroups);

    /**
     * 新增表block_groups数据
     *
     * @param blockGroups
     */
    Integer createBlockGroups(BlockGroups blockGroups);

    List<BlockGroups> findBlockGroupsByApp(@Param("app") Integer app);


    /**
     * 根据blockGroupId获取区块分组信息以及关联的app和block信息
     *
     * @param blockGroupId
     * @return
     */
    @Results({
            @Result(column = "app", property = "appId"),
            @Result(column = "app", property = "app",
                    one = @One(select = "com.tinyengine.it.mapper.AppsMapper.findAppsById")),
            @Result(column = "block_group_id", javaType = List.class, property = "blocks",
                    many = @Many(select = "com.tinyengine.it.mapper.BlocksMapper.findBlocksByBlockGroupId"))
    })
    @Select("SELECT bg.*, bgbgb.`block-group_id` as block_group_id  " +
            "FROM block_groups bg " +
            "left join apps a on bg.app = a.id " +
            "left join blocks_groups__block_groups_blocks bgbgb on bg.id = bgbgb.`block-group_id` " +
            "left join blocks bs on bgbgb.block_id = bs.id " +
            "WHERE bg.id = #{blockGroupId} " +
            "GROUP BY bg.id")
    List<BlockGroupsDto> getBlockGroupsById(int blockGroupId);


    /**
     * 根据appId获取区块分组信息以及关联的app和block信息
     *
     * @param appId
     * @return
     */
    @Results({
            @Result(column = "app", property = "appId"),
            @Result(column = "app", property = "app",
                    one = @One(select = "com.tinyengine.it.mapper.AppsMapper.findAppsById")),
            @Result(column = "block_group_id", javaType = List.class, property = "blocks",
                    many = @Many(select = "com.tinyengine.it.mapper.BlocksMapper.findBlocksByBlockGroupId"))
    })
    @Select("select bg.*, bgbgb.`block-group_id` as block_group_id " +
            "from block_groups bg " +
            "left join apps a on bg.app = a.id " +
            "left join blocks_groups__block_groups_blocks bgbgb on bg.id = bgbgb.`block-group_id` " +
            "left join blocks bs on bgbgb.block_id = bs.id " +
            "where bg.app = #{appId} " +
            "GROUP BY bg.id")
    List<BlockGroupsDto> findGroupByAppId(int appId);


    /**
     * 根据appId查询区块分组信息
     *
     * @param appId
     * @return
     */
    @Select("select * from block_groups where app = #{appId}")
    List<BlockGroups> findBlockGroupByAppId(Integer appId);

    /**
     * 根据区块id查询区块分组信息
     *
     * @param blockId
     * @return
     */
    @Select("select * from block_groups bg " +
            "left join blocks_groups__block_groups_blocks bgbgb on bg.id = bgbgb.`block-group_id` " +
            "where bgbgb.block_id = #{blockId}")
    List<BlockGroups> findBlockGroupByBlockId(Integer blockId);

}
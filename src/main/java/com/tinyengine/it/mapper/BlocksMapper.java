package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.dto.BlockDto;
import com.tinyengine.it.model.entity.Blocks;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface BlocksMapper extends BaseMapper<Blocks> {

    /**
     * 查询表blocks所有信息
     */
    List<Blocks> findAllBlocks();

    /**
     * 根据主键id查询表blocks数据
     *
     * @param id
     */
    Blocks findBlocksById(@Param("id") Integer id);

    /**
     * 根据条件查询表blocks数据
     *
     * @param blocks
     */
    List<Blocks> findBlocksByCondition(Blocks blocks);

    /**
     * 根据主键id删除表blocks数据
     *
     * @param id
     */
    Integer deleteBlocksById(@Param("id") Integer id);

    /**
     * 根据主键id更新表blocks数据
     *
     * @param blocks
     */
    Integer updateBlocksById(Blocks blocks);

    /**
     * 新增表blocks数据
     *
     * @param blocks
     */
    Integer createBlocks(Blocks blocks);

    /**
     * 查找表中所有tags
     *
     * @return
     */
    List<List<String>> allTags();

    /**
     * 根据区块分组id查询表blocks信息
     *
     * @param id
     * @return
     */
    List<Map<String, Object>> findBlocksByBlockGroup(Integer id);

    /**
     * 根据区块分类appId查询表blocks信息
     *
     * @param appId
     * @return
     */
    List<Blocks> findBlocksByBlockCategoriesAppId(Integer appId);

    /**
     * 根据区块分类id查询表blocks信息
     *
     * @param blockCategoriesId
     * @return
     */
    List<Blocks> findBlocksByBlockCategoriesId(Integer blockCategoriesId);


    /**
     * 根据name_cn或者description查询表blocks信息
     *
     * @param nameCn
     * @param description
     * @return
     */
    List<Blocks> findBlocksByNameCnAndDes(String nameCn, String description);

    List<Blocks> translate(Blocks blocks);

    List<Blocks> findBlocksByLabels(List<String> labelList);

    @Select("select b.* from blocks b " +
            "left join blocks_groups__block_groups_blocks bgbgb on b.id = bgbgb.block_id " +
            "where bgbgb.`block-group_id` = #{blockGroupId}")
    List<Blocks> findBlocksByBlockGroupId(int blockGroupId);

    /**
     * 根据分组id返回对应的区块信息及关联分组信息
     *
     * @param blockGroupId
     * @return
     */
    @Results({
            @Result(column = "occupier", property = "occupierId"),
            @Result(column = "block_group_id", javaType = List.class, property = "groups",
                    many = @Many(select = "com.tinyengine.it.mapper.BlockGroupsMapper.findBlockGroupsById")),
            @Result(column = "occupier", property = "occupier",
                    one = @One(select = "com.tinyengine.it.mapper.UsersPermissionsUserMapper.findUsersPermissionsUserById"))
    })
    @Select("select b.*, bgbgb.`block-group_id` as block_group_id " +
            "from blocks b " +
            "left join blocks_groups__block_groups_blocks bgbgb on b.id = bgbgb.block_id " +
            "left join block_groups bg on bgbgb.`block-group_id` = bg.id " +
            "where bg.id = #{blockGroupId}")
    List<BlockDto> findBlocksReturnByBlockGroupId(int blockGroupId);

    /**
     * 根据label列表查询区块信息信息
     *
     * @param labels
     * @return
     */
    @Select("select * from blocks where label in #{labels}")
    List<Blocks> findBlocksByLables(List<String> labels);


    /**
     * 根据区块id查询区块信息、以及关联的分组信息、分类信息、区块历史信息、国际化语言信息
     *
     * @param blockId
     * @return
     */
    @Results({
            @Result(column = "occupier", property = "occupierId"),
            @Result(column = "block_groups_block_id", javaType = List.class, property = "groups",
                    many = @Many(select = "com.tinyengine.it.mapper.BlockGroupsMapper.findBlockGroupByBlockId")),
            @Result(column = "block_categories_blocks_id", javaType = List.class, property = "categories",
                    many = @Many(select = "com.tinyengine.it.mapper.BlockCategoriesMapper.findBlockCateByBlockId")),
            @Result(column = "block_histories_block_id", javaType = List.class, property = "histories",
                    many = @Many(select = "com.tinyengine.it.mapper.BlockHistoriesMapper.findBlockHistoriesByBlockId")),
            @Result(column = "blocks_i_18_n_langs_id", javaType = List.class, property = "i18n_langs",
                    many = @Many(select = "com.tinyengine.it.mapper.I18nLangsMapper.findI18nLangByBlockId")),
            @Result(column = "occupier", property = "occupier",
                    one = @One(select = "com.tinyengine.it.mapper.UsersPermissionsUserMapper.findUsersPermissionsUserById"))
    })
    @Select("select b.*, bgbgb.block_id as block_groups_block_id,  bcbcb.block_id as block_categories_blocks_id, " +
            "bsh.block_id as block_histories_block_id, bi18nli18nlb.block_id as blocks_i_18_n_langs_id  " +
            "from blocks b " +
            "left join blocks_groups__block_groups_blocks bgbgb on b.id = bgbgb.block_id " +
            "left join block_groups bg on bgbgb.`block-group_id` = bg.id " +
            "left join blocks_categories__block_categories_blocks bcbcb on b.id = bcbcb.block_id " +
            "left join block_categories bc on bcbcb.`block-category_id` = bc.id " +
            "left join blocks__histories bsh on b.id = bsh.block_id " +
            "left join block_histories bh on bsh.`block-history_id` = bh.id " +
            "left join blocks_i_18_n_langs__i_18_n_langs_blocks bi18nli18nlb on b.id = bi18nli18nlb.block_id " +
            "left join i18n_langs il on bi18nli18nlb.`i18n-lang_id` = il.id " +
            "where b.id = #{blockId} " +
            "group by b.id")
    BlockDto fingBlockAndLangAngGroupAndHistoByBlockId(Integer blockId);

    /**
     * 根据分类id查区块信息
     *
     * @param categoryId
     * @return
     */
    @Select("select * from blocks b " +
            "left join blocks_categories__block_categories_blocks bcbcb on b.id = bcbcb.block_id " +
            "where bcbcb.`block-category_id` = #{categoryId}")
    List<Blocks> findBlockByCategoryId(Integer categoryId);

    /**
     * 根据区块id查询区块信息以及关联的分类信息和历史信息
     *
     * @param blockId
     * @return
     */
    @Results({
            @Result(column = "occupier", property = "occupierId"),
            @Result(column = "block_categories_blocks_id", javaType = List.class, property = "categories",
                    many = @Many(select = "com.tinyengine.it.mapper.BlockCategoriesMapper.findBlockCateByBlockId")),
            @Result(column = "block_histories_block_id", javaType = List.class, property = "histories",
                    many = @Many(select = "com.tinyengine.it.mapper.BlockHistoriesMapper.findBlockHistoriesByBlockId")),
            @Result(column = "occupier", property = "occupier",
                    one = @One(select = "com.tinyengine.it.mapper.UsersPermissionsUserMapper.findUsersPermissionsUserById"))
    })
    @Select("select b.*,bcbcb.block_id as block_categories_blocks_id,bsh.block_id as block_histories_block_id " +
            "from blocks b " +
            "left join blocks_categories__block_categories_blocks bcbcb on b.id = bcbcb.block_id " +
            "left join block_categories bc on bcbcb.`block-category_id` = bc.id " +
            "left join blocks__histories bsh on b.id = bsh.block_id " +
            "left join block_histories bh on bsh.`block-history_id` = bh.id " +
            "where b.id = #{blockId} " +
            "group by b.id")
    List<BlockDto> fingBlockAngCateAndHistorByBlockId(Integer blockId);
}
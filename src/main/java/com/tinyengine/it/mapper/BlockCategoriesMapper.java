package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.dto.BlockCategoriesDto;
import com.tinyengine.it.model.entity.BlockCategories;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BlockCategoriesMapper extends BaseMapper<BlockCategories> {

    /**
     * 查询表block_categories所有信息
     */
    List<BlockCategories> findAllBlockCategories();

    /**
     * 根据主键id查询表block_categories数据
     *
     * @param id
     */
    BlockCategories findBlockCategoriesById(@Param("id") Integer id);

    /**
     * 根据条件查询表block_categories数据
     *
     * @param blockCategories
     */
    List<BlockCategories> findBlockCategoriesByCondition(BlockCategories blockCategories);

    /**
     * 根据主键id删除表block_categories数据
     *
     * @param id
     */
    Integer deleteBlockCategoriesById(@Param("id") Integer id);

    /**
     * 根据主键id更新表block_categories数据
     *
     * @param blockCategories
     */
    Integer updateBlockCategoriesById(BlockCategories blockCategories);

    /**
     * 新增表block_categories数据
     *
     * @param blockCategories
     */

    Integer createBlockCategories(BlockCategories blockCategories);

    /**
     * 根据分类id查询分类信息以及关联的app信息
     *
     * @param id
     * @return
     */
    @Results({
            @Result(column = "app", property = "appId"),
            @Result(column = "app", property = "app",
                    one = @One(select = "com.tinyengine.it.mapper.AppsMapper.findAppsById"))
    })
    @Select("SELECT * FROM block_categories WHERE id = #{id}")
    BlockCategoriesDto getBlockCategoriesById(int id);


    /**
     * 根据appId查询区块分类信息
     *
     * @param appId
     * @return
     */
    @Select("select * from block_categories where app = #{appId}")
    List<BlockCategories> findBlockCategoriesByAppId(Integer appId);


    /**
     * 根据appId查询区块分类及关联的App和区块信息
     *
     * @param appId
     * @return
     */
    @Results({
            @Result(column = "app", property = "appId"),
            @Result(column = "app", property = "app",
                    one = @One(select = "com.tinyengine.it.mapper.AppsMapper.findAppsById")),
            @Result(column = "categoryId", property = "blocks",
                    many = @Many(select = "com.tinyengine.it.mapper.BlocksMapper.findBlockByCategoryId"))
    })
    @Select("select bc.*, bcbcb.`block-category_id` as categoryId  from block_categories bc " +
            "left join apps a on bc.app = a.id " +
            "left join blocks_categories__block_categories_blocks bcbcb on bc.id = bcbcb.`block-category_id` " +
            "left join blocks b on bcbcb.block_id = b.id " +
            "where bc.app = #{appId} " +
            "group by bc.id")
    List<BlockCategoriesDto> findBlockCateRelationAppByAppId(Integer appId);

    /**
     * 根据区块id查询区块分类信息
     *
     * @param blockId
     * @return
     */
    @Select("select * from block_categories bc " +
            "left join blocks_categories__block_categories_blocks bcbcb on bc.id = bcbcb.`block-category_id` " +
            "where bcbcb.block_id = #{blockId}")
    List<BlockCategories> findBlockCateByBlockId(Integer blockId);

}
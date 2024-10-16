package com.tinyengine.it.service.material;

import com.tinyengine.it.model.entity.BlockCategories;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BlockCategoriesService {

    /**
     * 根据主键id查询表block_categories信息
     *
     * @param id
     */
    BlockCategories findBlockCategoriesById(@Param("id") Integer id);

    /**
     * 根据条件查询表block_categories信息
     *
     * @param blockCategories
     */
    List<BlockCategories> findBlockCategoriesByCondition(BlockCategories blockCategories);

    /**
     * 根据主键id删除block_categories数据
     *
     * @param id
     */
    Integer deleteBlockCategoriesById(@Param("id") Integer id);

    /**
     * 根据主键id更新表block_categories信息
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
}

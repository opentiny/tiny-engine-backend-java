package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.BlockHistoriesMaterialHistoriesMaterialHistoriesBlocks;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BlockHistoriesMaterialHistoriesMaterialHistoriesBlocksMapper extends BaseMapper<BlockHistoriesMaterialHistoriesMaterialHistoriesBlocks> {

    /**
     * 查询表block_histories_material_histories__material_histories_blocks所有信息
     */
    List<BlockHistoriesMaterialHistoriesMaterialHistoriesBlocks> findAllBlockHistoriesMaterialHistoriesMaterialHistoriesBlocks();

    /**
     * 根据主键id查询表block_histories_material_histories__material_histories_blocks数据
     *
     * @param id
     */
    BlockHistoriesMaterialHistoriesMaterialHistoriesBlocks findBlockHistoriesMaterialHistoriesMaterialHistoriesBlocksById(@Param("id") Integer id);

    /**
     * 根据条件查询表block_histories_material_histories__material_histories_blocks数据
     *
     * @param blockHistoriesMaterialHistoriesMaterialHistoriesBlocks
     */
    List<BlockHistoriesMaterialHistoriesMaterialHistoriesBlocks> findBlockHistoriesMaterialHistoriesMaterialHistoriesBlocksByCondition(BlockHistoriesMaterialHistoriesMaterialHistoriesBlocks blockHistoriesMaterialHistoriesMaterialHistoriesBlocks);

    /**
     * 根据material_history_id查询block_history_id数据
     *
     * @param materialHistoryId
     */
    List<BlockHistoriesMaterialHistoriesMaterialHistoriesBlocks> findBlockHistoriesMaterialHistoriesMaterialHistoriesBlocksByMaterialHisId(@Param("materialHistoryId") Integer materialHistoryId);


    /**
     * 根据主键id删除表block_histories_material_histories__material_histories_blocks数据
     *
     * @param id
     */
    Integer deleteBlockHistoriesMaterialHistoriesMaterialHistoriesBlocksById(@Param("id") Integer id);

    /**
     * 根据主键id更新表block_histories_material_histories__material_histories_blocks数据
     *
     * @param blockHistoriesMaterialHistoriesMaterialHistoriesBlocks
     */
    Integer updateBlockHistoriesMaterialHistoriesMaterialHistoriesBlocksById(BlockHistoriesMaterialHistoriesMaterialHistoriesBlocks blockHistoriesMaterialHistoriesMaterialHistoriesBlocks);

    /**
     * 新增表block_histories_material_histories__material_histories_blocks数据
     *
     * @param blockHistoriesMaterialHistoriesMaterialHistoriesBlocks
     */
    Integer createBlockHistoriesMaterialHistoriesMaterialHistoriesBlocks(BlockHistoriesMaterialHistoriesMaterialHistoriesBlocks blockHistoriesMaterialHistoriesMaterialHistoriesBlocks);
}
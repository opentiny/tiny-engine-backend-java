package com.tinyengine.it.service.material;

import com.tinyengine.it.model.entity.BlockHistoriesMaterialHistoriesMaterialHistoriesBlocks;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BlockHistoriesMaterialHistoriesMaterialHistoriesBlocksService {

    /**
     * 查询表block_histories_material_histories__material_histories_blocks所有信息
     */
    List<BlockHistoriesMaterialHistoriesMaterialHistoriesBlocks> findAllBlockHistoriesMaterialHistoriesMaterialHistoriesBlocks();

    /**
     * 根据主键id查询表block_histories_material_histories__material_histories_blocks信息
     *
     * @param id
     */
    BlockHistoriesMaterialHistoriesMaterialHistoriesBlocks findBlockHistoriesMaterialHistoriesMaterialHistoriesBlocksById(@Param("id") Integer id);


    /**
     * 根据主键id删除block_histories_material_histories__material_histories_blocks数据
     *
     * @param id
     */
    Integer deleteBlockHistoriesMaterialHistoriesMaterialHistoriesBlocksById(@Param("id") Integer id);

    /**
     * 根据主键id更新表block_histories_material_histories__material_histories_blocks信息
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

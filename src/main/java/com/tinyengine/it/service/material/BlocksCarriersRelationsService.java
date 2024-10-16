package com.tinyengine.it.service.material;

import com.tinyengine.it.model.entity.BlocksCarriersRelations;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BlocksCarriersRelationsService {

    /**
     * 查询表blocks_carriers_relations所有信息
     */
    List<BlocksCarriersRelations> findAllBlocksCarriersRelations();

    /**
     * 根据主键id查询表blocks_carriers_relations信息
     *
     * @param id
     */
    BlocksCarriersRelations findBlocksCarriersRelationsById(@Param("id") Integer id);


    /**
     * 根据主键id删除blocks_carriers_relations数据
     *
     * @param id
     */
    Integer deleteBlocksCarriersRelationsById(@Param("id") Integer id);

    /**
     * 根据主键id更新表blocks_carriers_relations信息
     *
     * @param blocksCarriersRelations
     */
    Integer updateBlocksCarriersRelationsById(BlocksCarriersRelations blocksCarriersRelations);

    /**
     * 新增表blocks_carriers_relations数据
     *
     * @param blocksCarriersRelations
     */
    Integer createBlocksCarriersRelations(BlocksCarriersRelations blocksCarriersRelations);
}

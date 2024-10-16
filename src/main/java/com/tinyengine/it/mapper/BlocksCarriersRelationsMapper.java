package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.dto.BlockVersionDto;
import com.tinyengine.it.model.entity.BlocksCarriersRelations;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BlocksCarriersRelationsMapper extends BaseMapper<BlocksCarriersRelations> {

    /**
     * 查询表blocks_carriers_relations所有信息
     */
    List<BlocksCarriersRelations> findAllBlocksCarriersRelations();

    /**
     * 根据主键id查询表blocks_carriers_relations数据
     *
     * @param id
     */
    BlocksCarriersRelations findBlocksCarriersRelationsById(@Param("id") Integer id);

    /**
     * 根据条件查询表blocks_carriers_relations数据
     *
     * @param blocksCarriersRelations
     */
    List<BlocksCarriersRelations> findBlocksCarriersRelationsByCondition(BlocksCarriersRelations blocksCarriersRelations);

    /**
     * 根据主键id删除表blocks_carriers_relations数据
     *
     * @param id
     */
    Integer deleteBlocksCarriersRelationsById(@Param("id") Integer id);

    /**
     * 根据主键id更新表blocks_carriers_relations数据
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

    /**
     * 根据分组id集合和物料历史id查询新增表blocks_carriers_relations数据
     *
     * @param ids,materialHistryId
     */
    @Select("<script>" +
            "SELECT A.block, A.version " +
            "FROM blocks_carriers_relations A " +
            "LEFT JOIN material_histories C ON A.host = C.id " +
            "WHERE A.host_type = 'materialHistory' AND C.id = #{materialHistoryId} " +
            "UNION ALL " +
            "SELECT A.block, A.version " +
            "FROM blocks_carriers_relations A " +
            "LEFT JOIN block_groups C ON A.host = C.id " +
            "WHERE A.host_type = 'blockGroup' AND C.id IN " +
            "<foreach item='id' collection='ids' open='(' separator=',' close=')'> " +
            "#{id} " +
            "</foreach>" +
            "</script>")
    List<BlockVersionDto> findBlockAndVersion(@Param("ids") List<Integer> ids, @Param("materialHistoryId") Integer materialHistoryId);
}
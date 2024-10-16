package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.dto.BlockHistoryDto;
import com.tinyengine.it.model.entity.BlockHistories;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BlockHistoriesMapper extends BaseMapper<BlockHistories> {

    /**
     * 查询表block_histories所有信息
     */
    List<BlockHistories> findAllBlockHistories();

    /**
     * 根据主键id查询表block_histories数据
     *
     * @param id
     */
    BlockHistories findBlockHistoriesById(@Param("id") Integer id);

    /**
     * 根据条件查询表block_histories数据
     *
     * @param blockHistories
     */
    List<BlockHistories> findBlockHistoriesByCondition(BlockHistories blockHistories);

    /**
     * 根据主键id删除表block_histories数据
     *
     * @param id
     */
    Integer deleteBlockHistoriesById(@Param("id") Integer id);

    /**
     * 根据主键id更新表block_histories数据
     *
     * @param blockHistories
     */
    Integer updateBlockHistoriesById(BlockHistories blockHistories);

    /**
     * 新增表block_histories数据
     *
     * @param blockHistories
     */
    Integer createBlockHistories(BlockHistories blockHistories);

    /**
     * 通过id集合查block_histories数据
     *
     * @param blockHistoriesIds
     */
    List<BlockHistories> findBlockHistoriesByIds(List<Integer> blockHistoriesIds);

    @Select("<script>" +
            "SELECT A.block_id AS blockId, A.id AS historyId, A.version " +
            "FROM block_histories A " +
            "WHERE A.version IS NOT NULL " +
            "AND A.version != 'N/A' " +
            "AND A.block_id IN " +
            "<foreach item='id' collection='ids' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    List<BlockHistoryDto> findMapByIds(@Param("ids") List<Integer> ids);


    /**
     * 根据blockId获取区块历史记录信息
     *
     * @param blockId
     * @return
     */
    @Select("select * from block_histories bh " +
            "left join blocks b on bh.block_id = b.id " +
            "where bh.block_id = #{blockId}")
    List<BlockHistories> findBlockHistoriesByBlockId(int blockId);
}
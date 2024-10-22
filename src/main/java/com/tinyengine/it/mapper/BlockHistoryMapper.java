package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.dto.BlockHistoryDto;
import com.tinyengine.it.model.dto.BlockVersionDto;
import com.tinyengine.it.model.entity.BlockHistory;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface BlockHistoryMapper extends BaseMapper<BlockHistory> {

    /**
     * 查询表t_block_history所有信息
     */
    List<BlockHistory> queryAllBlockHistory();

    /**
     * 根据主键id查询表t_block_history数据
     *
     * @param id
     */
    BlockHistory queryBlockHistoryById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_block_history数据
     *
     * @param blockHistory
     */
    List<BlockHistory> queryBlockHistoryByCondition(BlockHistory blockHistory);

    /**
     * 根据主键id删除表t_block_history数据
     *
     * @param id
     */
    Integer deleteBlockHistoryById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_block_history数据
     *
     * @param blockHistory
     */
    Integer updateBlockHistoryById(BlockHistory blockHistory);

    /**
     * 新增表t_block_history数据
     *
     * @param blockHistory
     */
    Integer createBlockHistory(BlockHistory blockHistory);

    /**
     * 通过id集合查block_histories数据
     *
     * @param blockHistoriesIds
     */
    List<BlockHistory> queryBlockHistoriesByIds(List<Integer> blockHistoriesIds);

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
    List<BlockHistoryDto> queryMapByIds(@Param("ids") List<Integer> ids);


    @Select("SELECT B.id,B.version FROM `t_block_history` B LEFT JOIN r_material_history_block M ON `material_history_id` = #{materialHistoryId} WHERE B.id = M.block_history_id and B.block_group_id in (#{ids})")
    List<BlockVersionDto> queryBlockAndVersion(@Param("ids") List<Integer> ids, @Param("materialHistoryId") Integer materialHistoryId);
}
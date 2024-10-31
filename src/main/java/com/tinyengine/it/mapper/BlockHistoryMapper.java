package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.dto.BlockHistoryDto;
import com.tinyengine.it.model.dto.BlockVersionDto;
import com.tinyengine.it.model.entity.BlockHistory;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * The interface Block history mapper.
 *
 * @since 2024-10-20
 */
public interface BlockHistoryMapper extends BaseMapper<BlockHistory> {
    /**
     * 查询表t_block_history所有信息
     *
     * @return the list
     */
    List<BlockHistory> queryAllBlockHistory();

    /**
     * 根据主键id查询表t_block_history数据
     *
     * @param id the id
     * @return the block history
     */
    BlockHistory queryBlockHistoryById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_block_history数据
     *
     * @param blockHistory the block history
     * @return the list
     */
    List<BlockHistory> queryBlockHistoryByCondition(BlockHistory blockHistory);

    /**
     * 根据主键id删除表t_block_history数据
     *
     * @param id the id
     * @return the integer
     */
    Integer deleteBlockHistoryById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_block_history数据
     *
     * @param blockHistory the block history
     * @return the integer
     */
    Integer updateBlockHistoryById(BlockHistory blockHistory);

    /**
     * 新增表t_block_history数据
     *
     * @param blockHistory the block history
     * @return the integer
     */
    Integer createBlockHistory(BlockHistory blockHistory);

    /**
     * 通过id集合查block_histories数据
     *
     * @param blockHistoryIds the block history ids
     * @return the list
     */
    List<BlockHistory> queryBlockHistoryByIds(List<Integer> blockHistoryIds);

    /**
     * Query map by ids list.
     *
     * @param ids the ids
     * @return the list
     */
    @Select("<script>" + "SELECT A.ref_id AS blockId, A.id AS historyId, A.version " + "FROM t_block_history A "
            + "WHERE A.version IS NOT NULL " + "AND A.version != 'N/A' " + "AND A.ref_id IN "
            + "<foreach item='id' collection='ids' open='(' separator=',' close=')'>" + "#{id}" + "</foreach>"
            + "</script>")
    List<BlockHistoryDto> queryMapByIds(@Param("ids") List<Integer> ids);

    /**
     * Query block and version list.
     *
     * @param ids               the ids
     * @param materialHistoryId the material history id
     * @return the list
     */
    @Select({"<script>", "SELECT B.ref_id AS block, B.version", "FROM `t_block_history` B",
            "LEFT JOIN r_material_history_block M ON M.material_history_id = #{materialHistoryId}",
            "WHERE B.id = M.block_history_id", "AND B.block_group_id IN",
            "<foreach item='id' collection='ids' open='(' separator=',' close=')'>", "#{id}", "</foreach>",
            "</script>"})
    List<BlockVersionDto> queryBlockAndVersion(@Param("ids") List<Integer> ids,
                                               @Param("materialHistoryId") Integer materialHistoryId);

    /**
     * 根据blockId获取区块历史记录信息
     *
     * @param blockId the block id
     * @return the list
     */
    @Select("select * from t_block_history bh " +
            "left join t_block b on bh.ref_id = b.id " +
            "where bh.ref_id = #{blockId}")
    List<BlockHistory> findBlockHistoriesByBlockId(int blockId);
}
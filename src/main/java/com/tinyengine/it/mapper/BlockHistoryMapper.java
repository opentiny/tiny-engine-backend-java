package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.BlockHistory;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BlockHistoryMapper extends BaseMapper<BlockHistory> {

    /**
    *  查询表t_block_history所有信息
    */
    List<BlockHistory> findAllBlockHistory();

    /**
    * 根据主键id查询表t_block_history数据
    * @param id
    */
    BlockHistory findBlockHistoryById(@Param("id") Integer id);
    /**
    *  根据条件查询表t_block_history数据
    *  @param blockHistory
    */
    List<BlockHistory> findBlockHistoryByCondition(BlockHistory blockHistory);

    /**
    *  根据主键id删除表t_block_history数据
    *  @param id
    */
    Integer deleteBlockHistoryById(@Param("id") Integer id);

    /**
    *  根据主键id更新表t_block_history数据
    *  @param blockHistory
    */
    Integer updateBlockHistoryById(BlockHistory blockHistory);

    /**
    *  新增表t_block_history数据
    *  @param blockHistory
    */
    Integer createBlockHistory(BlockHistory blockHistory);
}
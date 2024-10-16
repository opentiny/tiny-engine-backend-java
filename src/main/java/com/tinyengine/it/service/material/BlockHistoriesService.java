package com.tinyengine.it.service.material;

import com.tinyengine.it.model.entity.BlockHistories;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BlockHistoriesService {


    /**
     * 根据主键id查询表block_histories信息
     *
     * @param id
     */
    BlockHistories findBlockHistoriesById(@Param("id") Integer id);

    /**
     * 根据条件查询表block_histories信息
     *
     * @param blockHistories
     */
    List<BlockHistories> findBlockHistoriesByCondition(BlockHistories blockHistories);

    /**
     * 根据主键id删除block_histories数据
     *
     * @param id
     */
    Integer deleteBlockHistoriesById(@Param("id") Integer id);


    /**
     * 新增表block_histories数据
     *
     * @param blockHistories
     */
    Integer createBlockHistories(BlockHistories blockHistories);
}

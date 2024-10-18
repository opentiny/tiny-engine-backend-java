package com.tinyengine.it.service.material;

import com.tinyengine.it.model.entity.Block;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BlockService {

    /**
     * 查询表t_block所有信息
     */
    List<Block> queryAllBlock();

    /**
     * 根据主键id查询表t_block信息
     *
     * @param id
     */
    Block queryBlockById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_block信息
     *
     * @param block
     */
    List<Block> queryBlockByCondition(Block block);

    /**
     * 根据主键id删除t_block数据
     *
     * @param id
     */
    Integer deleteBlockById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_block信息
     *
     * @param block
     */
    Integer updateBlockById(Block block);

    /**
     * 新增表t_block数据
     *
     * @param block
     */
    Integer createBlock(Block block);
}

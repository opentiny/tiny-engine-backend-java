package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.BlockGroup;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BlockGroupMapper extends BaseMapper<BlockGroup> {

    /**
    *  查询表t_block_group所有信息
    */
    List<BlockGroup> queryAllBlockGroup();

    /**
    * 根据主键id查询表t_block_group数据
    * @param id
    */
    BlockGroup queryBlockGroupById(@Param("id") Integer id);
    /**
    *  根据条件查询表t_block_group数据
    *  @param blockGroup
    */
    List<BlockGroup> queryBlockGroupByCondition(BlockGroup blockGroup);

    /**
    *  根据主键id删除表t_block_group数据
    *  @param id
    */
    Integer deleteBlockGroupById(@Param("id") Integer id);

    /**
    *  根据主键id更新表t_block_group数据
    *  @param blockGroup
    */
    Integer updateBlockGroupById(BlockGroup blockGroup);

    /**
    *  新增表t_block_group数据
    *  @param blockGroup
    */
    Integer createBlockGroup(BlockGroup blockGroup);
}
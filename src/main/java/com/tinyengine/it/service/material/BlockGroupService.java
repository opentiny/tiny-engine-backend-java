package com.tinyengine.it.service.material;

import com.tinyengine.it.model.entity.BlockGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BlockGroupService{

    /**
    *  查询表t_block_group所有信息
    */
    List<BlockGroup> findAllBlockGroup();

    /**
    *  根据主键id查询表t_block_group信息
    *  @param id
    */
    BlockGroup findBlockGroupById(@Param("id") Integer id);

    /**
    *  根据条件查询表t_block_group信息
    *  @param blockGroup
    */
    List<BlockGroup> findBlockGroupByCondition(BlockGroup blockGroup);

    /**
    *  根据主键id删除t_block_group数据
    *  @param id
    */
    Integer deleteBlockGroupById(@Param("id") Integer id);

    /**
    *  根据主键id更新表t_block_group信息
    *  @param blockGroup
    */
    Integer updateBlockGroupById(BlockGroup blockGroup);

    /**
    *  新增表t_block_group数据
    *  @param blockGroup
    */
    Integer createBlockGroup(BlockGroup blockGroup);
}

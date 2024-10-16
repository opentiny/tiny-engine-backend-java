package com.tinyengine.it.service.material;

import com.tinyengine.it.model.dto.BlockGroupsDto;
import com.tinyengine.it.model.entity.BlockGroups;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BlockGroupsService {

    /**
     * 根据主键id查询表block_groups信息
     *
     * @param id
     */
    BlockGroupsDto findBlockGroupsById(@Param("id") Integer id);

    /**
     * 根据条件查询表block_groups信息
     *
     * @param blockGroups
     */
    List<BlockGroupsDto> findBlockGroupsByCondition(BlockGroups blockGroups);

    /**
     * 根据主键id删除block_groups数据
     *
     * @param id
     */
    Integer deleteBlockGroupsById(@Param("id") Integer id);

    /**
     * 根据主键id更新表block_groups信息
     *
     * @param blockGroups
     */
    Integer updateBlockGroupsById(BlockGroups blockGroups);

    /**
     * 新增表block_groups数据
     *
     * @param blockGroups
     */
    Integer createBlockGroups(BlockGroups blockGroups);
}

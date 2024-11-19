package com.tinyengine.it.service.material.impl;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.mapper.BlockGroupMapper;
import com.tinyengine.it.model.dto.BlockGroupDto;
import com.tinyengine.it.model.entity.BlockGroup;
import com.tinyengine.it.service.material.BlockGroupService;

import lombok.extern.slf4j.Slf4j;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Block group service.
 *
 * @since 2024-10-20
 */
@Service
@Slf4j
public class BlockGroupServiceImpl implements BlockGroupService {
    @Autowired
    private BlockGroupMapper blockGroupMapper;

    /**
     * 查询表t_block_group所有数据
     *
     * @return BlockGroup
     */
    @Override
    public List<BlockGroup> findAllBlockGroup() {
        return blockGroupMapper.queryAllBlockGroup();
    }

    /**
     * 根据主键id查询表t_block_group信息
     *
     * @param id id
     * @return block group
     */
    @Override
    public BlockGroup findBlockGroupById(@Param("id") Integer id) {
        return blockGroupMapper.queryBlockGroupById(id);
    }

    /**
     * 根据条件查询表t_block_group数据
     *
     * @param blockGroup blockGroup
     * @return block group
     */
    @Override
    public List<BlockGroupDto> findBlockGroupByCondition(BlockGroup blockGroup) {
        return blockGroupMapper.queryBlockGroupByCondition(blockGroup);
    }

    /**
     * 根据主键id删除表t_block_group数据
     *
     * @param id id
     * @return delete number
     */
    @Override
    public Integer deleteBlockGroupById(@Param("id") Integer id) {
        return blockGroupMapper.deleteBlockGroupById(id);
    }

    /**
     * 根据主键id更新表t_block_group数据
     *
     * @param blockGroup blockGroup
     * @return update number
     */
    @Override
    public Integer updateBlockGroupById(BlockGroup blockGroup) {
        return blockGroupMapper.updateBlockGroupById(blockGroup);
    }

    /**
     * 新增表t_block_group数据
     *
     * @param blockGroup blockGroup
     * @return insert number
     */
    @Override
    public Result<List<BlockGroupDto>> createBlockGroup(BlockGroup blockGroup) {
        List<BlockGroupDto> blockGroupsList = blockGroupMapper.queryBlockGroupByCondition(blockGroup);
        if (blockGroupsList.isEmpty()) {
            blockGroupMapper.createBlockGroup(blockGroup);
        } else {
            return Result.failed(ExceptionEnum.CM003);
        }
        // 页面返回数据显示
        List<BlockGroupDto> blockGroupsListResult = blockGroupMapper.getBlockGroupsById(blockGroup.getId());
        return Result.success(blockGroupsListResult);
    }

    /**
     * 根据ids或者appId获取区块信息
     *
     * @param ids   ids
     * @param appId the app id
     * @return the list
     */
    @Override
    public List<BlockGroup> getBlockGroupByIdsOrAppId(List<Integer> ids, Integer appId, String from) {
        String createdBy = "1"; // 获取登录用户信息
        // 此接收到的两个参数不一定同时存在
        List<BlockGroup> blockGroupsListResult = new ArrayList<>();
        // 判断是否是区块管理还是物料区块查询
        BlockGroup blockGroup = new BlockGroup();
        if (!from.equals("block")) {
            // 物料管理的分组查询
            if (ids != null) {
                for (int blockgroupId : ids) {
                    blockGroup = blockGroupMapper.queryBlockGroupAndBlockById(blockgroupId, null);
                    blockGroupsListResult.add(blockGroup);
                }
            }
            if (appId != null) {
                blockGroupsListResult = blockGroupMapper.queryBlockGroupByAppId(appId, null);
            }
            if (ids == null && appId == null) {
                blockGroupsListResult = blockGroupMapper.queryAllBlockGroupAndBlock(null);
            }
        } else {
            // 区块管理差分组，过滤个人创建的
            if (ids != null) {
                for (int blockgroupId : ids) {
                    blockGroup = blockGroupMapper.queryBlockGroupAndBlockById(blockgroupId, createdBy);
                    blockGroupsListResult.add(blockGroup);
                }
            }
            if (appId != null) {
                blockGroupsListResult = blockGroupMapper.queryBlockGroupByAppId(appId, createdBy);
            }
            if (ids == null && appId == null) {
                blockGroupsListResult = blockGroupMapper.queryAllBlockGroupAndBlock(createdBy);
            }
        }
        return blockGroupsListResult;
    }
}

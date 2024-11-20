package com.tinyengine.it.service.material.impl;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.mapper.BlockGroupMapper;
import com.tinyengine.it.mapper.BlockMapper;
import com.tinyengine.it.model.dto.BlockGroupDto;
import com.tinyengine.it.model.entity.Block;
import com.tinyengine.it.model.entity.BlockGroup;
import com.tinyengine.it.service.material.BlockGroupService;

import lombok.extern.slf4j.Slf4j;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private BlockMapper blockMapper;

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
        // 判断是对正常的分组修改，还是在分组下添加区块操作的修改
        List<Block> blockList = blockGroup.getBlocks();
        Block blockTemp = new Block();
        if (!blockList.isEmpty()) {
            // 对分组下添加别人区块的操作进行区块分组的更新
            for (Block block : blockList) {
                blockTemp = blockMapper.queryBlockById(block.getId());
                blockTemp.setBlockGroupId(blockGroup.getId());
                blockTemp.setCreatedTime(null);
                blockTemp.setLastUpdatedTime(null);
                blockMapper.updateBlockById(blockTemp);
            }
        }
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
        List<BlockGroup> blockGroupsListTemp = new ArrayList<>();
        from = from != null ? from : "";
        createdBy = from.equals("block") ? createdBy : null; // from值为block在区块管理处增加createdBy条件
        BlockGroup blockGroup = new BlockGroup();
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

        // 对查询结果做相关处理和判断
        if (blockGroupsListResult.isEmpty()) {
            return blockGroupsListResult;
        }
        // 对查询的结果过滤blocks中id为null的情况
        for (BlockGroup blockGroupTemp : blockGroupsListResult) {
            if (blockGroupTemp != null && blockGroupTemp.getBlocks() != null) {
                List<Block> blocks = blockGroupTemp.getBlocks().stream()
                        .filter(block -> block.getId() != null)
                        .collect(Collectors.toList());
                blockGroupTemp.setBlocks(blocks);
                blockGroupsListTemp.add(blockGroupTemp);
            }
        }
        return blockGroupsListTemp;
    }
}

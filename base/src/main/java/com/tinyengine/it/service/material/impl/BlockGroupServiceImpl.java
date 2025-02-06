/**
 * Copyright (c) 2023 - present TinyEngine Authors.
 * Copyright (c) 2023 - present Huawei Cloud Computing Technologies Co., Ltd.
 *
 * Use of this source code is governed by an MIT-style license.
 *
 * THE OPEN SOURCE SOFTWARE IN THIS PRODUCT IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL,
 * BUT WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS FOR
 * A PARTICULAR PURPOSE. SEE THE APPLICABLE LICENSES FOR MORE DETAILS.
 *
 */

package com.tinyengine.it.service.material.impl;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.enums.Enums;
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.mapper.BlockCarriersRelationMapper;
import com.tinyengine.it.mapper.BlockGroupBlockMapper;
import com.tinyengine.it.mapper.BlockGroupMapper;
import com.tinyengine.it.model.dto.BlockGroupDto;
import com.tinyengine.it.model.entity.Block;
import com.tinyengine.it.model.entity.BlockCarriersRelation;
import com.tinyengine.it.model.entity.BlockGroup;
import com.tinyengine.it.model.entity.BlockGroupBlock;
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
    private BlockCarriersRelationMapper blockCarriersRelationMapper;
    @Autowired
    private BlockGroupBlockMapper blockGroupBlockMapper;

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
        List<BlockGroupBlock> blockGroupBlocks = blockGroupBlockMapper.findBlockGroupBlockByBlockGroupId(blockGroup.getId());
        List<Integer> groupBlockIds = blockGroupBlocks.stream().map(BlockGroupBlock::getBlockId).collect(Collectors.toList());

        String hostType = Enums.BlockGroup.BLOCK_GROUP.getValue();
        // 区块列表为空，分组下无区块
        if (blockList.isEmpty()) {
            // 删除区块分组与区块历史版本关系
            blockCarriersRelationMapper.deleteBlockCarriersRelation(blockGroup.getId(), hostType, null);
            // 删除区块分组与区块关系
            blockGroupBlockMapper.deleteBlockGroupBlockByGroupId(blockGroup.getId());
            return blockGroupMapper.updateBlockGroupById(blockGroup);
        }
        // 处理参数分组区块
        List<Integer> blockIds = blockList.stream().map(Block::getId).collect(Collectors.toList());
        int result = getBlockGroupIds(groupBlockIds, blockIds, blockGroup.getId());
        // 更新区块分组和区块历史关系表
        List<BlockCarriersRelation> blockCarriersRelations = new ArrayList<>();
        for (Block block : blockList) {
            BlockCarriersRelation blockCarriersRelation = new BlockCarriersRelation();
            blockCarriersRelation.setHostId(blockGroup.getId());
            blockCarriersRelation.setHostType(hostType);
            blockCarriersRelation.setBlockId(block.getId());
            blockCarriersRelation.setVersion(block.getLatestVersion());
            blockCarriersRelations.add(blockCarriersRelation);
        }
        blockCarriersRelationMapper.createOrUpdateBatch(blockCarriersRelations);
        if (result > 0) {
            blockCarriersRelationMapper.deleteBlockCarriersRelation(blockGroup.getId(), hostType, result);
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
     * @param from  the from
     * @return the list
     */
    @Override
    public List<BlockGroup> getBlockGroupByIdsOrAppId(List<Integer> ids, Integer appId, String from) {
        // 此接收到的两个参数不一定同时存在
        List<BlockGroup> blockGroupsListResult = new ArrayList<>();
        String groupCreatedBy = "1"; // 获取登录用户id
        String blockCreatedBy = "1";
        blockCreatedBy = (Enums.BlockGroup.BLOCK.getValue()).equals(from) ? blockCreatedBy : null; // from值为block在区块管理处增加createdBy条件
        BlockGroup blockGroup = new BlockGroup();
        if (ids != null) {
            for (int blockgroupId : ids) {
                blockGroup = blockGroupMapper.queryBlockGroupAndBlockById(blockgroupId, blockCreatedBy, groupCreatedBy);
                blockGroupsListResult.add(blockGroup);
            }
        }
        if (appId != null) {
            blockGroupsListResult = blockGroupMapper.queryBlockGroupByAppId(appId, blockCreatedBy, groupCreatedBy);
        }
        if (ids == null && appId == null) {
            blockGroupsListResult = blockGroupMapper.queryAllBlockGroupAndBlock( blockCreatedBy, groupCreatedBy);
        }

        if (blockGroupsListResult.get(0).getId() == null) {
            return blockGroupsListResult;
        }
        // 对查询的结果的区块赋值current_version
        for (BlockGroup blockGroupTemp : blockGroupsListResult) {
            for (Block block : blockGroupTemp.getBlocks()) {
                BlockCarriersRelation queryParam = new BlockCarriersRelation();
                queryParam.setBlockId(block.getId());
                queryParam.setHostId(blockGroup.getId());
                queryParam.setHostType(Enums.BlockGroup.BLOCK_GROUP.getValue());
                List<BlockCarriersRelation> blockCarriersRelations = blockCarriersRelationMapper.queryBlockCarriersRelationByCondition(queryParam);
                if (blockCarriersRelations.isEmpty()) {
                    continue;
                }
                String version = blockCarriersRelations.get(0).getVersion();
                block.setCurrentVersion(version);
            }

        }
        return blockGroupsListResult;
    }

    /**
     * 根据参数处理区块分组与区块关系
     *
     * @param groupBlockIds the groupBlockIds
     * @param paramIds      the paramIds
     * @param groupId       the groupId
     * @return the result
     */
    private Integer getBlockGroupIds(List<Integer> groupBlockIds, List<Integer> paramIds, Integer groupId) {
        int result = 0;
        if (groupBlockIds.size() > paramIds.size()) {
            Block block = new Block();
            for (Integer blockId : groupBlockIds) {
                if (!paramIds.contains(blockId)) {
                    result = blockId;
                    block.setId(blockId);  // 找到多出的元素
                    break;
                }
            }
            BlockGroupBlock blockGroupBlock = new BlockGroupBlock();
            blockGroupBlock.setBlockId(block.getId());
            blockGroupBlock.setBlockGroupId(groupId);
            List<BlockGroupBlock> blockGroupBlocks = blockGroupBlockMapper.queryBlockGroupBlockByCondition(blockGroupBlock);
            if (blockGroupBlocks.isEmpty()) {
                return result;
            }
            int blockGroupBlockId = blockGroupBlocks.get(0).getId();
            blockGroupBlockMapper.deleteBlockGroupBlockById(blockGroupBlockId);
            return result;
        } else {
            for (int block : paramIds) {
                BlockGroupBlock blockGroupBlockParam = new BlockGroupBlock();
                blockGroupBlockParam.setBlockId(block);
                blockGroupBlockParam.setBlockGroupId(groupId);
                blockGroupBlockMapper.createBlockGroupBlock(blockGroupBlockParam);
            }
        }
        return result;
    }
}

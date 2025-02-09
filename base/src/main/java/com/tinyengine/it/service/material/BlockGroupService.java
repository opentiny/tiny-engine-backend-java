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

package com.tinyengine.it.service.material;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.model.dto.BlockGroupDto;
import com.tinyengine.it.model.entity.BlockGroup;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface Block group service.
 *
 * @since 2024-10-20
 */
public interface BlockGroupService {
    /**
     * 查询表t_block_group所有信息
     *
     * @return the list
     */
    List<BlockGroup> findAllBlockGroup();

    /**
     * 根据主键id查询表t_block_group信息
     *
     * @param id the id
     * @return the block group dto
     */
    BlockGroup findBlockGroupById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_block_group信息
     *
     * @param blockGroup the block group
     * @return the list
     */
    List<BlockGroupDto> findBlockGroupByCondition(BlockGroup blockGroup);

    /**
     * 根据主键id删除t_block_group数据
     *
     * @param id the id
     * @return the integer
     */
    Integer deleteBlockGroupById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_block_group信息
     *
     * @param blockGroup the block group
     * @return the integer
     */
    Integer updateBlockGroupById(BlockGroup blockGroup);

    /**
     * 新增表t_block_group数据
     *
     * @param blockGroup the block group
     * @return the result
     */
    Result<List<BlockGroupDto>> createBlockGroup(BlockGroup blockGroup);

    /**
     * 根据ids或者appId获取区块信息
     *
     * @param ids   ids
     * @param appId the app id
     * @param from  the from
     * @return the list
     */
    List<BlockGroup> getBlockGroupByIdsOrAppId(List<Integer> ids, Integer appId, String from);
}

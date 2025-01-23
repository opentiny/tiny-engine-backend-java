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

package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.BlockCarriersRelation;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface Block Carriers Relation mapper.
 *
 * @since 2024-12-16
 */
public interface BlockCarriersRelationMapper extends BaseMapper<BlockCarriersRelation> {
    /**
     * 查询表t_block_carriers_relation所有信息
     *
     * @return the list
     */
    List<BlockCarriersRelation> queryAllBlockCarriersRelation();

    /**
     * 根据主键id查询表t_block_carriers_relation数据
     *
     * @param id the id
     * @return the Block Carriers Relation
     */
    BlockCarriersRelation queryBlockCarriersRelationById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_block_carriers_relation数据
     *
     * @param blockCarriersRelation the Block Carriers Relation
     * @return the list
     */
    List<BlockCarriersRelation> queryBlockCarriersRelationByCondition(BlockCarriersRelation blockCarriersRelation);

    /**
     * 根据主键id删除表t_block_carriers_relation数据
     *
     * @param id the id
     * @return the integer
     */
    Integer deleteBlockCarriersRelationById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_block_carriers_relation数据
     *
     * @param blockCarriersRelation the Block Carriers Relation
     * @return the integer
     */
    Integer updateBlockCarriersRelationById(BlockCarriersRelation blockCarriersRelation);

    /**
     * 新增表t_block_carriers_relation数据
     *
     * @param blockCarriersRelation the Block Carriers Relation
     * @return the integer
     */
    Integer createBlockCarriersRelation(BlockCarriersRelation blockCarriersRelation);

    /**
     * 通过区块分组id和类型删除分组下区块
     *
     * @param hostId   the block group id
     * @param hostType the host type
     * @param blockId  the block id
     * @return the int
     */
    Integer deleteBlockCarriersRelation(Integer hostId, String hostType, Integer blockId);

    /**
     * 批量新增表t_block_carriers_relation数据
     *
     * @param blockCarriersRelations the Block Carriers Relation
     * @return the integer
     */
    Integer createOrUpdateBatch(List<BlockCarriersRelation> blockCarriersRelations);
}

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
import com.tinyengine.it.model.entity.ResourceGroupResource;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * The resource group resource mapper.
 *
 * @since 2024-10-20
 */
public interface ResourceGroupResourceMapper extends BaseMapper<ResourceGroupResource> {
    /**
     * 新增表r_resource_group_resource数据
     *
     * @param resourceGroupResource the resourceGroupResource
     * @return the integer
     */
    Integer createResourceGroupResource(ResourceGroupResource resourceGroupResource);

    /**
     * 通过资源分组id查询分组下区块
     *
     * @param resourceGroupId the resource group id
     * @return the list
     */
    @Select("select * from r_resource_group_resource where resource_group_id = #{resourceGroupId}")
    List<ResourceGroupResource> findResourceGroupResourceByResourceGroupId(Integer resourceGroupId);

    /**
     * 通过资源分组id删除分组下资源
     *
     * @param resourceGroupId the resource group id
     * @return the int
     */
    int deleteResourceGroupResourceByGroupId(Integer resourceGroupId);

    /**
     * 通过资源分组id查询分组下区块
     *
     * @param resourceGroupId the resource group id
     * @param resourceId the resource group id
     * @return ResourceGroupResource the ResourceGroupResource
     */
    @Select("select * from r_resource_group_resource where resource_group_id = #{resourceGroupId} and resource_id = #{resourceId}")
    ResourceGroupResource findResourceGroupResourceByResourceGroupIdAndResourceId(Integer resourceGroupId, Integer resourceId);
}

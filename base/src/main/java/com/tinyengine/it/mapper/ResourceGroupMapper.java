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
import com.tinyengine.it.model.entity.ResourceGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ResourceGroupMapper extends BaseMapper<ResourceGroup> {
    /**
     * 查询表t_resource_group所有信息
     *
     * @return the list
     */
    List<ResourceGroup> queryAllResourceGroupAndResource(String groupCreatedBy);

    /**
     * 根据appId查询表t_resource_group数据
     *
     * @param appId the appId
     * @param groupCreatedBy the groupCreatedBy
     * @return the resourceGroup
     */
    List<ResourceGroup> queryResourceGroupByAppId(Integer appId, String groupCreatedBy);

    /**
     * 根据主键id查询表t_resource_group数据
     *
     * @param id the id
     * @param groupCreatedBy the groupCreatedBy
     * @return the resourceGroup
     */
    ResourceGroup queryResourceGroupById(Integer id, String groupCreatedBy);

    /**
     * 根据条件查询表t_resource_group数据
     *
     * @param resourceGroup the resourceGroup
     * @return the list
     */
    List<ResourceGroup> queryResourceGroupByCondition(ResourceGroup resourceGroup);

    /**
     * 根据主键id删除表t_resource_group数据
     *
     * @param id the id
     * @return the integer
     */
    Integer deleteResourceGroupById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_resource_group数据
     *
     * @param resourceGroup the resourceGroup
     * @return the integer
     */
    Integer updateResourceGroupById(ResourceGroup resourceGroup);

    /**
     * 新增表t_resource_group数据
     *
     * @param resourceGroup the resourceGroup
     * @return the integer
     */
    Integer createResourceGroup(ResourceGroup resourceGroup);
}

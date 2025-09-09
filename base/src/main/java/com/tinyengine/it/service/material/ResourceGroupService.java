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

import com.baomidou.mybatisplus.extension.service.IService;
import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.model.entity.ResourceGroup;

import java.util.List;

public interface ResourceGroupService extends IService<ResourceGroup> {
    /**
     * 查询表t_resource_group所有信息
     *
     * @return the list
     */
    List<ResourceGroup> queryAllResourceGroupAndResource();

    /**
     * 根据appId查询表t_resource_group信息
     *
     * @param appId the appId
     * @return the resourceGroup
     */
    Result<List<ResourceGroup>> queryResourceGroupByAppId(Integer appId);

    /**
     * 根据Id查询表t_resource_group信息
     *
     * @param id the id
     * @return the resourceGroup
     */
    Result<ResourceGroup> queryResourceGroupById(Integer id);

    /**
     * 根据条件查询表t_resource_group信息
     *
     * @param resourceGroup the resourceGroup
     * @return the list
     */
    List<ResourceGroup> queryResourceGroupByCondition(ResourceGroup resourceGroup);

    /**
     * 根据主键id删除t_resource_group数据
     *
     * @param id the id
     * @return the integer
     */
    Result<ResourceGroup> deleteResourceGroupById(Integer id);

    /**
     * 根据主键id更新表t_resource_group信息
     *
     * @param resourceGroup the resourceGroup
     * @return the integer
     */
    Result<ResourceGroup> updateResourceGroupById(ResourceGroup resourceGroup);

    /**
     * 新增表t_resource_group数据
     *
     * @param resourceGroup the resourceGroup
     * @return the integer
     */
    Result<ResourceGroup> createResourceGroup(ResourceGroup resourceGroup);
}

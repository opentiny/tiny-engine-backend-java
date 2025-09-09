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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.context.LoginUserContext;
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.common.log.SystemServiceLog;
import com.tinyengine.it.mapper.ResourceGroupMapper;
import com.tinyengine.it.mapper.ResourceGroupResourceMapper;
import com.tinyengine.it.mapper.ResourceMapper;
import com.tinyengine.it.model.entity.Resource;
import com.tinyengine.it.model.entity.ResourceGroup;
import com.tinyengine.it.model.entity.ResourceGroupResource;
import com.tinyengine.it.service.material.ResourceGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ResourceGroupServiceImpl extends ServiceImpl<ResourceGroupMapper, ResourceGroup> implements ResourceGroupService {
    /**
     * The loginUserContext service.
     */
    @Autowired
    private LoginUserContext loginUserContext;

    @Autowired
    private ResourceGroupResourceMapper resourceGroupResourceMapper;

    @Autowired
    private ResourceMapper resourceMapper;

    /**
     * 查询表t_resource_group所有信息
     *
     * @return the list
     */
    @Override
    @SystemServiceLog(description = "查询表t_resource_group所有信息")
    public List<ResourceGroup> queryAllResourceGroupAndResource() {
        return baseMapper.queryAllResourceGroupAndResource(loginUserContext.getLoginUserId());
    }

    /**
     * 根据appId查询表t_resource_group信息
     *
     * @param appId the appId
     * @return the resourceGroup
     */
    @Override
    @SystemServiceLog(description = "根据主键appId查询表t_resource_group信息")
    public Result<List<ResourceGroup>> queryResourceGroupByAppId(Integer appId) {
        List<ResourceGroup> resourceGroups = baseMapper.queryResourceGroupByAppId(appId, loginUserContext.getLoginUserId());
        return Result.success(resourceGroups);
    }

    /**
     * 根据Id查询表t_resource_group信息
     *
     * @param id the id
     * @return the resourceGroup
     */
    @Override
    public Result<ResourceGroup> queryResourceGroupById(Integer id) {
        ResourceGroup resourceGroup = this.baseMapper.queryResourceGroupById(id, loginUserContext.getLoginUserId());
        return Result.success(resourceGroup);
    }

    /**
     * 根据条件查询表t_resource_group信息
     *
     * @param resourceGroup the resourceGroup
     * @return the list
     */
    @Override
    @SystemServiceLog(description = "根据条件查询表t_resource_group信息")
    public List<ResourceGroup> queryResourceGroupByCondition(ResourceGroup resourceGroup) {
        return null;
    }

    /**
     * 根据主键id删除t_resource_group数据
     *
     * @param id the id
     * @return the integer
     */
    @Override
    @SystemServiceLog(description = "根据主键id删除t_resource_group数据")
    public Result<ResourceGroup> deleteResourceGroupById(Integer id) {
        int deleteResult = baseMapper.deleteResourceGroupById(id);
        if (deleteResult != 1) {
            return Result.failed(ExceptionEnum.CM008);
        }
        resourceGroupResourceMapper.deleteResourceGroupResourceByGroupId(id);
        ResourceGroup resourceGroup = baseMapper.selectById(id);
        return Result.success(resourceGroup);
    }

    /**
     * 根据主键id更新表t_resource_group信息
     *
     * @param resourceGroup the resourceGroup
     * @return the integer
     */
    @Override
    @SystemServiceLog(description = "根据主键id更新表t_resource_group信息")
    public Result<ResourceGroup> updateResourceGroupById(ResourceGroup resourceGroup) {
        List<Resource> resourceList = resourceGroup.getResources();
        List<ResourceGroupResource> resourceGroupBlocks = resourceGroupResourceMapper.findResourceGroupResourceByResourceGroupId(
            resourceGroup.getId());
        List<Integer> groupResourceIds = resourceGroupBlocks.stream()
            .map(ResourceGroupResource::getResourceId)
            .collect(Collectors.toList());

        if (resourceList.isEmpty()) {
            resourceGroupResourceMapper.deleteResourceGroupResourceByGroupId(resourceGroup.getId());
            resourceMapper.deleteAllResource(loginUserContext.getLoginUserId());
            this.baseMapper.updateResourceGroupById(resourceGroup);
            ResourceGroup result = this.baseMapper.queryResourceGroupById(resourceGroup.getId(), loginUserContext.getLoginUserId());
            return Result.success(result);
        }

        List<Integer> resourceIds = resourceList.stream().map(Resource::getId).collect(Collectors.toList());
        getResourceGroupIds(groupResourceIds, resourceIds, resourceGroup.getId());
        this.baseMapper.updateResourceGroupById(resourceGroup);
        ResourceGroup result = this.baseMapper.queryResourceGroupById(resourceGroup.getId(), loginUserContext.getLoginUserId());
        return Result.success(result);
    }

    /**
     * 新增表t_resource_group数据
     *
     * @param resourceGroup the resourceGroup
     * @return the integer
     */
    @Override
    @SystemServiceLog(description = "新增表t_resource_group数据")
    public Result<ResourceGroup> createResourceGroup(ResourceGroup resourceGroup) {

        QueryWrapper<ResourceGroup> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", resourceGroup.getName());
        queryWrapper.eq("app_id", resourceGroup.getAppId());
        // 接入组合系统需添加租户id查询
        ResourceGroup groupResult = this.baseMapper.selectOne(queryWrapper);
        if (groupResult != null) {
            return Result.failed(ExceptionEnum.CM003);
        }

        int createResult = baseMapper.createResourceGroup(resourceGroup);

        if (createResult != 1) {
            return Result.failed(ExceptionEnum.CM003);
        }
        ResourceGroup result = baseMapper.selectById(resourceGroup.getId());
        return Result.success(result);
    }

    /**
     * 根据参数处理资源分组与资源关系
     *
     * @param groupResourceIds the groupResourceIds
     * @param resourceIds the resourceIds
     * @param groupId the groupId
     *
     */
    private void getResourceGroupIds(List<Integer> groupResourceIds, List<Integer> resourceIds, Integer groupId) {
        int result = 0;
        if (groupResourceIds.size() > resourceIds.size()) {
            for (Integer resourceId : groupResourceIds) {
                if (!resourceIds.contains(resourceId)) {
                    ResourceGroupResource queryResult = resourceGroupResourceMapper.findResourceGroupResourceByResourceGroupIdAndResourceId(groupId, resourceId);
                    resourceGroupResourceMapper.deleteById(queryResult.getId());
                    resourceMapper.deleteResourceById(resourceId);
                }
            }


        } else {
            for (int resourceId : resourceIds) {
                ResourceGroupResource resourceGroupResource = new ResourceGroupResource();
                resourceGroupResource.setResourceId(resourceId);
                resourceGroupResource.setResourceGroupId(groupId);
                resourceGroupResourceMapper.createResourceGroupResource(resourceGroupResource);
            }
        }
    }
}

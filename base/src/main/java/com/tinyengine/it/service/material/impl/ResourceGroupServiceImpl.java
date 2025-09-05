package com.tinyengine.it.service.material.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.context.LoginUserContext;
import com.tinyengine.it.common.enums.Enums;
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.common.log.SystemServiceLog;
import com.tinyengine.it.mapper.ResourceGroupMapper;
import com.tinyengine.it.mapper.ResourceGroupResourceMapper;
import com.tinyengine.it.model.entity.Block;
import com.tinyengine.it.model.entity.BlockCarriersRelation;
import com.tinyengine.it.model.entity.BlockGroupBlock;
import com.tinyengine.it.model.entity.Resource;
import com.tinyengine.it.model.entity.ResourceGroup;
import com.tinyengine.it.model.entity.ResourceGroupResource;
import com.tinyengine.it.service.material.ResourceGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ResourceGroupServiceImpl extends ServiceImpl<ResourceGroupMapper, ResourceGroup>  implements ResourceGroupService {
    /**
     * The loginUserContext service.
     */
    @Autowired
    private LoginUserContext loginUserContext;

    @Autowired
    private ResourceGroupResourceMapper resourceGroupResourceMapper;
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
        int createResult = baseMapper.createResourceGroup(resourceGroup);
        if (createResult != 1) {
            return Result.failed(ExceptionEnum.CM008);
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
     * @return the result
     */
    private Integer getResourceGroupIds(List<Integer> groupResourceIds, List<Integer> resourceIds, Integer groupId) {
        int result = 0;
        if (groupResourceIds.size() > resourceIds.size()) {
            Resource resource = new Resource();
            for (Integer resourceId : groupResourceIds) {
                if (!resourceIds.contains(resourceId)) {
                    result = resourceId;
                    resource.setId(resourceId);  // 找到多出的元素
                    break;
                }
            }
            ResourceGroupResource queryResult = resourceGroupResourceMapper.findResourceGroupResourceByResourceGroupIdAndResourceId(groupId, resource.getId());
            if (queryResult == null) {
                return result;
            }

            resourceGroupResourceMapper.deleteById(queryResult.getId());
            return result;
        } else {
            for (int resourceId : resourceIds) {
                ResourceGroupResource resourceGroupResource = new ResourceGroupResource();
                resourceGroupResource.setResourceId(resourceId);
                resourceGroupResource.setResourceGroupId(groupId);
                resourceGroupResourceMapper.createResourceGroupResource(resourceGroupResource);
            }
        }
        return result;
    }
}

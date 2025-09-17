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
import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.common.log.SystemServiceLog;
import com.tinyengine.it.common.utils.ImageThumbnailGenerator;
import com.tinyengine.it.mapper.ResourceGroupResourceMapper;
import com.tinyengine.it.mapper.ResourceMapper;
import com.tinyengine.it.model.entity.Resource;
import com.tinyengine.it.model.entity.ResourceGroupResource;
import com.tinyengine.it.service.material.ResourceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {
    /**
     * The loginUserContext service.
     */
    @Autowired
    private LoginUserContext loginUserContext;

    @Autowired
    private ResourceGroupResourceMapper resourceGroupResourceMapper;

    /**
     * 查询表t_resource所有信息
     *
     * @return the list
     */
    @Override
    @SystemServiceLog(description = "查询表t_resource所有信息")
    public List<Resource> queryAllResource() {
        return baseMapper.queryAllResource();
    }

    /**
     * 模糊查询表Resource信息
     *
     * @param name the name
     * @param des the des
     * @return Resource信息列表
     */
    @Override
    public List<Resource> queryResourcesByNameAndDes(String name, String des) {
        return this.baseMapper.findResourcesByNameAndDes(name, des);
    }

    /**
     * 根据主键id查询表t_resource信息
     *
     * @param id the id
     * @return the resource
     */
    @Override
    @SystemServiceLog(description = "根据主键id查询表t_resource信息")
    public Result<Resource> queryResourceById(Integer id) {
        Resource resource = baseMapper.queryResourceById(id);
        return Result.success(resource);
    }

    /**
     * 根据name查询表t_resource信息
     *
     * @param name the name
     * @return the resource
     */
    @Override
    @SystemServiceLog(description = "根据name查询表t_resource信息")
    public Resource queryResourceByName(String name) {

        QueryWrapper<Resource> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        queryWrapper.eq("app_id", loginUserContext.getAppId());

        return this.baseMapper.selectOne(queryWrapper);
    }

    /**
     * 根据分组id和创建人查询表t_resource信息
     *
     * @param resourceGroupId the resourceGroupId
     * @return the list
     */
    @Override
    @SystemServiceLog(description = "根据条件查询表t_resource信息")
    public List<Resource> queryResourceByResourceGroupId(Integer resourceGroupId) {

        return baseMapper.findResourceByResourceGroupId(resourceGroupId, loginUserContext.getLoginUserId());
    }

    /**
     * 根据主键id删除t_resource数据
     *
     * @param id the id
     * @return the integer
     */
    @Override
    @SystemServiceLog(description = "根据主键id删除t_resource数据")
    public Result<Resource> deleteResourceById(Integer id) {
        int deleteResult = baseMapper.deleteResourceById(id);
        if (deleteResult != 1) {
            return Result.failed(ExceptionEnum.CM008);
        }
        Resource resource = baseMapper.queryResourceById(id);
        return Result.success(resource);
    }

    /**
     * 根据主键id更新表t_resource信息
     *
     * @param resource the resource
     * @return the integer
     */
    @Override
    @SystemServiceLog(description = "根据主键id更新表t_resource信息")
    public Result<Resource> updateResourceById(Resource resource) {
        int updateResult = baseMapper.updateResourceById(resource);
        if (updateResult != 1) {
            return Result.failed(ExceptionEnum.CM008);
        }
        Resource result = baseMapper.queryResourceById(resource.getId());
        return Result.success(result);
    }

    /**
     * 新增表t_resource数据
     *
     * @param resource the resource
     * @return the integer
     */
    @Override
    @SystemServiceLog(description = "新增表t_resource数据")
    public Resource createResource(Resource resource) throws Exception {

        Resource res = this.resourceUpload(resource);
        ResourceGroupResource resourceGroupResource = new ResourceGroupResource();
        resourceGroupResource.setResourceId(res.getId());
        resourceGroupResource.setResourceGroupId(resource.getResourceGroupId());
        resourceGroupResourceMapper.createResourceGroupResource(resourceGroupResource);

        return res;
    }

    /**
     * 图片上传
     *
     * @param resource the resource
     * @return the integer
     */
    @Override
    @SystemServiceLog(description = "图片上传")
    public Resource resourceUpload(Resource resource) {
        String imageName = Instant.now().toEpochMilli()+resource.getName();
        resource.setName(imageName);
        String resourceData = resource.getResourceData();
        String tinyEngineUrl = System.getenv("TINY_ENGINE_URL");

        if (!StringUtils.isEmpty(resourceData)) {
            String encodedName = URLEncoder.encode(imageName, StandardCharsets.UTF_8);
            String resourceUrl = tinyEngineUrl + "?name=" + encodedName + "&isResource=" + true;
            String thumbnailUrl = tinyEngineUrl + "?name=" + encodedName + "&isResource=" + false;
            resource.setResourceUrl(resourceUrl);
            resource.setThumbnailUrl(thumbnailUrl);
            resource.setThumbnailData(ImageThumbnailGenerator.createThumbnail(resource.getResourceData(), 200, 200));
        }
        QueryWrapper<Resource> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", resource.getName());
        queryWrapper.eq("category", resource.getCategory());
        // 接入租户系统需添加租户id查询
        Resource resourceResult = this.baseMapper.selectOne(queryWrapper);
        if (resourceResult != null) {
            throw new ServiceException(ExceptionEnum.CM003.getResultCode(), ExceptionEnum.CM003.getResultMsg());
        }
        int createResult = this.baseMapper.createResource(resource);
        if (createResult != 1) {
            throw new ServiceException(ExceptionEnum.CM002.getResultCode(), ExceptionEnum.CM002.getResultMsg());
        }

        return this.baseMapper.queryResourceById(resource.getId());
    }

    /**
     * 批量新增表t_resource数据
     *
     * @param resources the resources
     * @return the integer
     */
    @Override
    public List<Resource> createBatchResource(List<Resource> resources) throws Exception {
        List<Resource> resourceList = new ArrayList<>();
        if (resources.isEmpty()) {
            return resourceList;
        }
        for (Resource resource : resources) {
            Resource result = this.createResource(resource);
            resourceList.add(result);
        }
        return resourceList;
    }

}

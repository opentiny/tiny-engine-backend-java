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
import com.tinyengine.it.common.utils.ImageThumbnailGenerator;
import com.tinyengine.it.common.utils.Utils;
import com.tinyengine.it.mapper.ResourceMapper;
import com.tinyengine.it.model.dto.ResourceRequestDto;
import com.tinyengine.it.model.entity.Resource;
import com.tinyengine.it.service.material.ResourceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {
    /**
     * The loginUserContext service.
     */
    @Autowired
    private LoginUserContext loginUserContext;

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
     * @param nameCn the nameCn
     * @param nameEn the nameEn
     * @param des the des
     * @return Resource信息列表
     */
    @Override
    public List<Resource> queryResourcesByNameAndDes(String nameCn, String nameEn, String des) {
        return this.baseMapper.findResourcesByNameAndDes(nameCn, nameEn, des);
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
     * 根据data查询表t_resource信息
     *
     * @param data the data
     * @return the resource
     */
    @Override
    @SystemServiceLog(description = "根据data查询表t_resource信息")
    public Resource queryResourceByData(ResourceRequestDto data) throws Exception {

        QueryWrapper<Resource> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name_cn", data.getNameCn());
        queryWrapper.eq("name_en", data.getNameEn());

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
    public Result<Resource> createResource(Resource resource) throws Exception {

        ResourceRequestDto resourceParam = new ResourceRequestDto();
        resourceParam.setNameCn(resource.getNameCn());
        resourceParam.setNameEn(resource.getNameEn());
        resourceParam.setResource(true);

        String encodedResourceParam = Utils.encodeObjectToBase64(resourceParam);

        ResourceRequestDto thumbnailParam = new ResourceRequestDto();
        thumbnailParam.setNameCn(resource.getNameCn());
        thumbnailParam.setNameEn(resource.getNameEn());
        thumbnailParam.setResource(false);
        String encodedThumbnailParam = Utils.encodeObjectToBase64(thumbnailParam);

        String resourceData = resource.getResourceData();
        String tinyEngineUrl = "http://127.0.0.1:9090/material-center/api/resource/download"; // System.getenv("TINY_ENGINE_URL");

        if(!StringUtils.isEmpty(resourceData)) {
            resource.setResourceUrl(String.format("%s?data=%s", tinyEngineUrl, encodedResourceParam));
            resource.setThumbnailUrl(String.format("%s?data=%s", tinyEngineUrl, encodedThumbnailParam));
            resource.setThumbnailData(ImageThumbnailGenerator.createThumbnail(resource.getResourceData(), 200, 200));
        }

        int createResult = baseMapper.createResource(resource);
        if (createResult != 1) {
            return Result.failed(ExceptionEnum.CM008);
        }

        Resource result = baseMapper.queryResourceById(resource.getId());
        return Result.success(result);
    }


    /**
     * 批量新增表t_resource数据
     * @param entityList
     * @param batchSize
     * @return
     */
    @Override
    @SystemServiceLog(description = "批量新增表t_resource数据")
    public boolean saveBatch(Collection<Resource> entityList, int batchSize) {
        return false;
    }

}

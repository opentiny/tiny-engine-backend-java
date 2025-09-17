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
import com.tinyengine.it.model.entity.Resource;

import java.util.List;

public interface ResourceService extends IService<Resource> {
    /**
     * 查询表t_resource所有信息
     *
     * @return the list
     */
    List<Resource> queryAllResource();

    /**
     * 模糊查询表Resource信息
     *
     * @param name the name
     * @param des the des
     * @return Resource信息列表
     */
    List<Resource> queryResourcesByNameAndDes(String name, String des);

    /**
     * 根据主键id查询表t_resource信息
     *
     * @param id the id
     * @return the resource
     */
    Result<Resource> queryResourceById(Integer id);

    /**
     * 根据name查询表t_resource信息
     *
     * @param name the name
     * @return the resource
     */
    Resource queryResourceByName(String name) throws Exception;

    /**
     * 根据分组id和创建人查询表t_resource信息
     *
     * @param resourceGroupId the resourceGroupId
     * @return the list
     */
    List<Resource> queryResourceByResourceGroupId(Integer resourceGroupId);

    /**
     * 根据主键id删除t_resource数据
     *
     * @param id the id
     * @return the integer
     */
    Result<Resource> deleteResourceById(Integer id);

    /**
     * 根据主键id更新表t_resource信息
     *
     * @param resource the resource
     * @return the integer
     */
    Result<Resource> updateResourceById(Resource resource);

    /**
     * 新增表t_resource数据
     *
     * @param resource the resource
     * @return the integer
     */
    Resource createResource(Resource resource) throws Exception;

    /**
     * 图片上传
     *
     * @param resource the resource
     * @return the integer
     */
    Resource resourceUpload(Resource resource) throws Exception;

    /**
     * 批量新增表t_resource数据
     *
     * @param resources the resources
     * @return the integer
     */
    List<Resource> createBatchResource(List<Resource> resources) throws Exception;
}

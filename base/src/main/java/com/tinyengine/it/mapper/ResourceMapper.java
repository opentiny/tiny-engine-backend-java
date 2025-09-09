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
import com.tinyengine.it.model.entity.Resource;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface Resource mapper.
 *
 * @since 2024-10-20
 */
public interface ResourceMapper extends BaseMapper<Resource> {
    /**
     * 查询表t_resource所有信息
     *
     * @return the list
     */
    List<Resource> queryAllResource();

    /**
     * 模糊查询表t_resource数据
     *
     * @param name the name
     * @param des the des
     * @return the resource
     */
    List<Resource> findResourcesByNameAndDes(String name, String des);

    /**
     * 根据主键id查询表t_resource数据
     *
     * @param id the id
     * @return the resource
     */
    Resource queryResourceById(@Param("id") Integer id);

    /**
     * 根据分组id和创建人查询表t_resource信息
     *
     * @param resourceGroupId the resourceGroupId
     * @param createdBy the createdBy
     * @return the list
     */
    List<Resource> findResourceByResourceGroupId(Integer resourceGroupId, String createdBy);

    /**
     * 根据条件查询表t_resource数据
     *
     * @param resource the resource
     * @return the list
     */
    List<Resource> queryResourceByCondition(Resource resource);

    /**
     * 根据主键id删除表t_resource数据
     *
     * @param id the id
     * @return the integer
     */
    Integer deleteResourceById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_resource数据
     *
     * @param resource the resource
     * @return the integer
     */
    Integer updateResourceById(Resource resource);

    /**
     * 新增表t_resource数据
     *
     * @param resource the resource
     * @return the integer
     */
    Integer createResource(Resource resource);

    /**
     * 删除表t_resource全部数据
     * @param createdBy the createdBy
     * @return the integer
     */
    @Delete("DELETE FROM t_resource WHERE created_by = #{createdBy}")
    Integer deleteAllResource(String createdBy);
}

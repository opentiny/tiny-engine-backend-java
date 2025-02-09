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
import com.tinyengine.it.model.entity.BusinessCategory;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface Business category mapper.
 *
 * @since 2024-10-20
 */
public interface BusinessCategoryMapper extends BaseMapper<BusinessCategory> {
    /**
     * 查询表t_business_category所有信息
     *
     * @return the list
     */
    List<BusinessCategory> queryAllBusinessCategory();

    /**
     * 根据主键id查询表t_business_category数据
     *
     * @param id the id
     * @return the business category
     */
    BusinessCategory queryBusinessCategoryById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_business_category数据
     *
     * @param businessCategory the business category
     * @return the list
     */
    List<BusinessCategory> queryBusinessCategoryByCondition(BusinessCategory businessCategory);

    /**
     * 根据主键id删除表t_business_category数据
     *
     * @param id the id
     * @return the integer
     */
    Integer deleteBusinessCategoryById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_business_category数据
     *
     * @param businessCategory the business category
     * @return the integer
     */
    Integer updateBusinessCategoryById(BusinessCategory businessCategory);

    /**
     * 新增表t_business_category数据
     *
     * @param businessCategory the business category
     * @return the integer
     */
    Integer createBusinessCategory(BusinessCategory businessCategory);
}
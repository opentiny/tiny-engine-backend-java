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

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.mapper.BusinessCategoryMapper;
import com.tinyengine.it.model.entity.BusinessCategory;
import com.tinyengine.it.service.material.BusinessCategoryService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Business category service.
 *
 * @since 2024-10-20
 */
@Service
@Slf4j
public class BusinessCategoryServiceImpl extends ServiceImpl<BusinessCategoryMapper, BusinessCategory> implements BusinessCategoryService {
    /**
     * 查询表t_business_category所有数据
     *
     * @return BusinessCategory
     */
    @Override
    public List<BusinessCategory> queryAllBusinessCategory() {
        return baseMapper.queryAllBusinessCategory();
    }

    /**
     * 根据主键id查询表t_business_category信息
     *
     * @param id id
     * @return BusinessCategory
     */
    @Override
    public BusinessCategory queryBusinessCategoryById(Integer id) {
        return baseMapper.queryBusinessCategoryById(id);
    }

    /**
     * 根据条件查询表t_business_category数据
     *
     * @param businessCategory businessCategory
     * @return BusinessCategory
     * @throws ServiceException ServiceException
     */
    @Override
    public List<BusinessCategory> queryBusinessCategoryByCondition(BusinessCategory businessCategory)
            throws ServiceException {
        return baseMapper.queryBusinessCategoryByCondition(businessCategory);
    }

    /**
     * 根据主键id删除表t_business_category数据
     *
     * @param id id
     * @return execute success data number
     */
    @Override
    public Integer deleteBusinessCategoryById(Integer id) {
        return baseMapper.deleteBusinessCategoryById(id);
    }

    /**
     * 根据主键id更新表t_business_category数据
     *
     * @param businessCategory category
     * @return execute success data number
     */
    @Override
    public Integer updateBusinessCategoryById(BusinessCategory businessCategory) {
        return baseMapper.updateBusinessCategoryById(businessCategory);
    }

    /**
     * 新增表t_business_category数据
     *
     * @param businessCategory category
     * @return execute success data number
     */
    @Override
    public Integer createBusinessCategory(BusinessCategory businessCategory) {
        return baseMapper.createBusinessCategory(businessCategory);
    }
}

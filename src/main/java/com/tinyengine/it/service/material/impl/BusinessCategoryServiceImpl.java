package com.tinyengine.it.service.material.impl;

import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.mapper.BusinessCategoryMapper;
import com.tinyengine.it.model.entity.BusinessCategory;
import com.tinyengine.it.service.material.BusinessCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Business category service.
 *
 * @since 2024-10-20
 */
@Service
@Slf4j
public class BusinessCategoryServiceImpl implements BusinessCategoryService {
    @Autowired
    private BusinessCategoryMapper businessCategoryMapper;

    /**
     * 查询表t_business_category所有数据
     */
    @Override
    public List<BusinessCategory> queryAllBusinessCategory() {
        return businessCategoryMapper.queryAllBusinessCategory();
    }

    /**
     * 根据主键id查询表t_business_category信息
     *
     * @param id id
     * @return BusinessCategory
     */
    @Override
    public BusinessCategory queryBusinessCategoryById(@Param("id") Integer id) {
        return businessCategoryMapper.queryBusinessCategoryById(id);
    }

    /**
     * 根据条件查询表t_business_category数据
     *
     * @param businessCategory businessCategory
     * @return BusinessCategory
     */
    @Override
    public List<BusinessCategory> queryBusinessCategoryByCondition(BusinessCategory businessCategory)
        throws ServiceException {
        return businessCategoryMapper.queryBusinessCategoryByCondition(businessCategory);
    }

    /**
     * 根据主键id删除表t_business_category数据
     *
     * @param id id
     * @return execute success data number
     */
    @Override
    public Integer deleteBusinessCategoryById(@Param("id") Integer id) {
        return businessCategoryMapper.deleteBusinessCategoryById(id);
    }

    /**
     * 根据主键id更新表t_business_category数据
     *
     * @param businessCategory category
     * @return execute success data number
     */
    @Override
    public Integer updateBusinessCategoryById(BusinessCategory businessCategory) {
        return businessCategoryMapper.updateBusinessCategoryById(businessCategory);
    }

    /**
     * 新增表t_business_category数据
     *
     * @param businessCategory category
     * @return execute success data number
     */
    @Override
    public Integer createBusinessCategory(BusinessCategory businessCategory) {
        return businessCategoryMapper.createBusinessCategory(businessCategory);
    }
}

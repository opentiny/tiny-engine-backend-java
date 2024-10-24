package com.tinyengine.it.service.material.impl;

import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.mapper.BusinessCategoryMapper;
import com.tinyengine.it.model.entity.BusinessCategory;
import com.tinyengine.it.service.material.BusinessCategoryService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Business category service.
 */
@Service
public class BusinessCategoryServiceImpl implements BusinessCategoryService {

    @Autowired
    private BusinessCategoryMapper businessCategoryMapper;

    /**
     * 查询表t_business_category所有数据
     */
    @Override
    public List<BusinessCategory> queryAllBusinessCategory() throws ServiceException {
        return businessCategoryMapper.queryAllBusinessCategory();
    }

    /**
     * 根据主键id查询表t_business_category信息
     *
     * @param id
     */
    @Override
    public BusinessCategory queryBusinessCategoryById(@Param("id") Integer id) throws ServiceException {
        return businessCategoryMapper.queryBusinessCategoryById(id);
    }

    /**
     * 根据条件查询表t_business_category数据
     *
     * @param businessCategory
     */
    @Override
    public List<BusinessCategory> queryBusinessCategoryByCondition(BusinessCategory businessCategory)
        throws ServiceException {
        return businessCategoryMapper.queryBusinessCategoryByCondition(businessCategory);
    }

    /**
     * 根据主键id删除表t_business_category数据
     *
     * @param id
     */
    @Override
    public Integer deleteBusinessCategoryById(@Param("id") Integer id) throws ServiceException {
        return businessCategoryMapper.deleteBusinessCategoryById(id);
    }

    /**
     * 根据主键id更新表t_business_category数据
     *
     * @param businessCategory
     */
    @Override
    public Integer updateBusinessCategoryById(BusinessCategory businessCategory) throws ServiceException {
        return businessCategoryMapper.updateBusinessCategoryById(businessCategory);
    }

    /**
     * 新增表t_business_category数据
     *
     * @param businessCategory
     */
    @Override
    public Integer createBusinessCategory(BusinessCategory businessCategory) throws ServiceException {
        return businessCategoryMapper.createBusinessCategory(businessCategory);
    }
}

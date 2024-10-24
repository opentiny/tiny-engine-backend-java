package com.tinyengine.it.service.material;

import com.tinyengine.it.model.entity.BusinessCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface Business category service.
 */
public interface BusinessCategoryService {

    /**
     * 查询表t_business_category所有信息
     *
     * @return the list
     */
    List<BusinessCategory> queryAllBusinessCategory();

    /**
     * 根据主键id查询表t_business_category信息
     *
     * @param id the id
     * @return the business category
     */
    BusinessCategory queryBusinessCategoryById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_business_category信息
     *
     * @param businessCategory the business category
     * @return the list
     */
    List<BusinessCategory> queryBusinessCategoryByCondition(BusinessCategory businessCategory);

    /**
     * 根据主键id删除t_business_category数据
     *
     * @param id the id
     * @return the integer
     */
    Integer deleteBusinessCategoryById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_business_category信息
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

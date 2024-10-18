package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.BusinessCategory;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface BusinessCategoryMapper extends BaseMapper<BusinessCategory> {

    /**
     * 查询表t_business_category所有信息
     */
    List<BusinessCategory> findAllBusinessCategory();

    /**
     * 根据主键id查询表t_business_category数据
     *
     * @param id
     */
    BusinessCategory findBusinessCategoryById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_business_category数据
     *
     * @param businessCategory
     */
    List<BusinessCategory> findBusinessCategoryByCondition(BusinessCategory businessCategory);

    /**
     * 根据主键id删除表t_business_category数据
     *
     * @param id
     */
    Integer deleteBusinessCategoryById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_business_category数据
     *
     * @param businessCategory
     */
    Integer updateBusinessCategoryById(BusinessCategory businessCategory);

    /**
     * 新增表t_business_category数据
     *
     * @param businessCategory
     */
    Integer createBusinessCategory(BusinessCategory businessCategory);
}
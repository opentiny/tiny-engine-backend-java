package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.BusinessCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BusinessCategoryMapper extends BaseMapper<BusinessCategory> {

    /**
     * 查询表t_business_category所有信息
     */
    List<BusinessCategory> queryAllBusinessCategory();

    /**
     * 根据主键id查询表t_business_category数据
     *
     * @param id
     */
    BusinessCategory queryBusinessCategoryById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_business_category数据
     *
     * @param businessCategory
     */
    List<BusinessCategory> queryBusinessCategoryByCondition(BusinessCategory businessCategory);

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
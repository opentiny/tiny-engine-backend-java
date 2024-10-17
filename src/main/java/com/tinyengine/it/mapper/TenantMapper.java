package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.Tenant;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TenantMapper extends BaseMapper<Tenant> {

    /**
    *  查询表t_tenant所有信息
    */
    List<Tenant> findAllTenant();

    /**
    * 根据主键id查询表t_tenant数据
    * @param id
    */
    Tenant findTenantById(@Param("id") Integer id);
    /**
    *  根据条件查询表t_tenant数据
    *  @param tenant
    */
    List<Tenant> findTenantByCondition(Tenant tenant);

    /**
    *  根据主键id删除表t_tenant数据
    *  @param id
    */
    Integer deleteTenantById(@Param("id") Integer id);

    /**
    *  根据主键id更新表t_tenant数据
    *  @param tenant
    */
    Integer updateTenantById(Tenant tenant);

    /**
    *  新增表t_tenant数据
    *  @param tenant
    */
    Integer createTenant(Tenant tenant);
}
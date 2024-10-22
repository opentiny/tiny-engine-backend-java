package com.tinyengine.it.service.platform;

import com.tinyengine.it.model.entity.Tenant;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TenantService {

    /**
     * 查询表t_tenant所有信息
     */
    List<Tenant> findAllTenant();

    /**
     * 根据主键id查询表t_tenant信息
     *
     * @param id
     */
    Tenant findTenantById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_tenant信息
     *
     * @param tenant
     */
    List<Tenant> findTenantByCondition(Tenant tenant);

    /**
     * 根据主键id删除t_tenant数据
     *
     * @param id
     */
    Integer deleteTenantById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_tenant信息
     *
     * @param tenant
     */
    Integer updateTenantById(Tenant tenant);

    /**
     * 新增表t_tenant数据
     *
     * @param tenant
     */
    Integer createTenant(Tenant tenant);
}

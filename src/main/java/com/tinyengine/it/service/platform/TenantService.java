package com.tinyengine.it.service.platform;

import com.tinyengine.it.model.entity.Tenant;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface Tenant service.
 * @since 2024-10-20
 */
public interface TenantService {

    /**
     * 查询表t_tenant所有信息
     *
     * @return the list
     */
    List<Tenant> findAllTenant();

    /**
     * 根据主键id查询表t_tenant信息
     *
     * @param id the id
     * @return the tenant
     */
    Tenant findTenantById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_tenant信息
     *
     * @param tenant the tenant
     * @return the list
     */
    List<Tenant> findTenantByCondition(Tenant tenant);

    /**
     * 根据主键id删除t_tenant数据
     *
     * @param id the id
     * @return the integer
     */
    Integer deleteTenantById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_tenant信息
     *
     * @param tenant the tenant
     * @return the integer
     */
    Integer updateTenantById(Tenant tenant);

    /**
     * 新增表t_tenant数据
     *
     * @param tenant the tenant
     * @return the integer
     */
    Integer createTenant(Tenant tenant);
}

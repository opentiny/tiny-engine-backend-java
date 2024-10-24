package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.Tenant;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TenantMapper extends BaseMapper<Tenant> {

    /**
     * 查询表t_tenant所有信息
     */
    List<Tenant> queryAllTenant();

    /**
     * 根据主键id查询表t_tenant数据
     *
     * @param id
     */
    Tenant queryTenantById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_tenant数据
     *
     * @param tenant
     */
    List<Tenant> queryTenantByCondition(Tenant tenant);

    /**
     * 根据主键id删除表t_tenant数据
     *
     * @param id
     */
    Integer deleteTenantById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_tenant数据
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
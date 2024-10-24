package com.tinyengine.it.service.platform.impl;

import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.mapper.TenantMapper;
import com.tinyengine.it.model.entity.Tenant;
import com.tinyengine.it.service.platform.TenantService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Tenant service.
 */

@Service
@Slf4j
public class TenantServiceImpl implements TenantService {

    @Autowired
    private TenantMapper tenantMapper;

    /**
     * 查询表t_tenant所有数据
     */
    @Override
    public List<Tenant> findAllTenant() throws ServiceException {
        return tenantMapper.queryAllTenant();
    }

    /**
     * 根据主键id查询表t_tenant信息
     *
     * @param id
     */
    @Override
    public Tenant findTenantById(@Param("id") Integer id) throws ServiceException {
        return tenantMapper.queryTenantById(id);
    }

    /**
     * 根据条件查询表t_tenant数据
     *
     * @param tenant
     */
    @Override
    public List<Tenant> findTenantByCondition(Tenant tenant) throws ServiceException {
        return tenantMapper.queryTenantByCondition(tenant);
    }

    /**
     * 根据主键id删除表t_tenant数据
     *
     * @param id
     */
    @Override
    public Integer deleteTenantById(@Param("id") Integer id) throws ServiceException {
        return tenantMapper.deleteTenantById(id);
    }

    /**
     * 根据主键id更新表t_tenant数据
     *
     * @param tenant
     */
    @Override
    public Integer updateTenantById(Tenant tenant) throws ServiceException {
        return tenantMapper.updateTenantById(tenant);
    }

    /**
     * 新增表t_tenant数据
     *
     * @param tenant
     */
    @Override
    public Integer createTenant(Tenant tenant) throws ServiceException {
        return tenantMapper.createTenant(tenant);
    }
}

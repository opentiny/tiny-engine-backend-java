/**
 * Copyright (c) 2023 - present TinyEngine Authors.
 * Copyright (c) 2023 - present Huawei Cloud Computing Technologies Co., Ltd.
 *
 * Use of this source code is governed by an MIT-style license.
 *
 * THE OPEN SOURCE SOFTWARE IN THIS PRODUCT IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL,
 * BUT WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS FOR
 * A PARTICULAR PURPOSE. SEE THE APPLICABLE LICENSES FOR MORE DETAILS.
 *
 */

package com.tinyengine.it.service.platform.impl;

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
 *
 * @since 2024-10-20
 */

@Service
@Slf4j
public class TenantServiceImpl implements TenantService {
    @Autowired
    private TenantMapper tenantMapper;

    /**
     * 查询表t_tenant所有数据
     *
     * @return query result
     */
    @Override
    public List<Tenant> findAllTenant() {
        return tenantMapper.queryAllTenant();
    }

    /**
     * 根据主键id查询表t_tenant信息
     *
     * @param id id
     * @return query result
     */
    @Override
    public Tenant findTenantById(@Param("id") Integer id) {
        return tenantMapper.queryTenantById(id);
    }

    /**
     * 根据条件查询表t_tenant数据
     *
     * @param tenant tenant
     * @return query result
     */
    @Override
    public List<Tenant> findTenantByCondition(Tenant tenant) {
        return tenantMapper.queryTenantByCondition(tenant);
    }

    /**
     * 根据主键id删除表t_tenant数据
     *
     * @param id id
     * @return execute success data number
     */
    @Override
    public Integer deleteTenantById(@Param("id") Integer id) {
        return tenantMapper.deleteTenantById(id);
    }

    /**
     * 根据主键id更新表t_tenant数据
     *
     * @param tenant tenant
     * @return execute success data number
     */
    @Override
    public Integer updateTenantById(Tenant tenant) {
        return tenantMapper.updateTenantById(tenant);
    }

    /**
     * 新增表t_tenant数据
     *
     * @param tenant tenant
     * @return execute success data number
     */
    @Override
    public Integer createTenant(Tenant tenant) {
        return tenantMapper.createTenant(tenant);
    }
}

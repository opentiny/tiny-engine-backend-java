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

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tinyengine.it.common.context.LoginUserContext;
import com.tinyengine.it.mapper.TenantMapper;
import com.tinyengine.it.mapper.AuthUsersUnitsRolesMapper;
import com.tinyengine.it.model.entity.AuthUsersUnitsRoles;
import com.tinyengine.it.model.entity.Tenant;
import com.tinyengine.it.service.platform.TenantService;

import lombok.extern.slf4j.Slf4j;

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
public class TenantServiceImpl extends ServiceImpl<TenantMapper, Tenant> implements TenantService {

    @Autowired
    LoginUserContext loginUserContext;

    @Autowired
    AuthUsersUnitsRolesMapper authUsersUnitsRolesMapper;

    /**
     * 查询表t_tenant所有数据
     *
     * @return query result
     */
    @Override
    public List<Tenant> findAllTenant() {

        return baseMapper.queryAllTenant();
    }

    /**
     * 根据主键id查询表t_tenant信息
     *
     * @param id id
     * @return query result
     */
    @Override
    public Tenant findTenantById(Integer id) {

        return baseMapper.queryTenantById(id);
    }

    /**
     * 根据条件查询表t_tenant数据
     *
     * @param tenant tenant
     * @return query result
     */
    @Override
    public List<Tenant> findTenantByCondition(Tenant tenant) {

        return baseMapper.queryTenantByCondition(tenant);
    }

    /**
     * 根据主键id删除表t_tenant数据
     *
     * @param id id
     * @return execute success data number
     */
    @Override
    public Integer deleteTenantById(Integer id) {

        return baseMapper.deleteTenantById(id);
    }

    /**
     * 根据主键id更新表t_tenant数据
     *
     * @param tenant tenant
     * @return execute success data number
     */
    @Override
    public Integer updateTenantById(Tenant tenant) {

        return baseMapper.updateTenantById(tenant);
    }

    /**
     * 新增表t_tenant数据
     *
     * @param tenant tenant
     * @return execute success data number
     */
    @Override
    public Integer createTenant(Tenant tenant) {
        int result = baseMapper.createTenant(tenant);
        if (result == 1) {
            AuthUsersUnitsRoles authUsersUnitsRoles = new AuthUsersUnitsRoles();
            authUsersUnitsRoles.setTenantId(Integer.valueOf(tenant.getId()));
            authUsersUnitsRoles.setRoleId(2);
            authUsersUnitsRoles.setUnitType("tenant");
            authUsersUnitsRoles.setUnitId(Integer.valueOf(tenant.getId()));
            authUsersUnitsRoles.setUserId(Integer.valueOf(loginUserContext.getLoginUserId()));
            authUsersUnitsRolesMapper.createAuthUsersUnitsRoles(authUsersUnitsRoles);
        }
        return Integer.valueOf(tenant.getId());
    }
}

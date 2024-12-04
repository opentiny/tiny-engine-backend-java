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

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.tinyengine.it.mapper.TenantMapper;
import com.tinyengine.it.model.entity.Tenant;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

/**
 * test case
 *
 * @since 2024-10-29
 */
class TenantServiceImplTest {
    @Mock
    private TenantMapper tenantMapper;
    @InjectMocks
    private TenantServiceImpl tenantServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllTenant() {
        List<Tenant> mockData = Arrays.asList(new Tenant());
        when(tenantMapper.queryAllTenant()).thenReturn(mockData);

        List<Tenant> result = tenantServiceImpl.findAllTenant();
        Assertions.assertEquals(mockData, result);
    }

    @Test
    void testFindTenantById() {
        Tenant mockData = new Tenant();
        when(tenantMapper.queryTenantById(1)).thenReturn(mockData);

        Tenant result = tenantServiceImpl.findTenantById(1);
        Assertions.assertEquals(mockData, result);
    }

    @Test
    void testFindTenantByCondition() {
        List<Tenant> mockData = Arrays.asList(new Tenant());
        when(tenantMapper.queryTenantByCondition(any(Tenant.class))).thenReturn(mockData);

        List<Tenant> result = tenantServiceImpl.findTenantByCondition(new Tenant());
        Assertions.assertEquals(mockData, result);
    }

    @Test
    void testDeleteTenantById() {
        when(tenantMapper.deleteTenantById(1)).thenReturn(2);

        Integer result = tenantServiceImpl.deleteTenantById(1);
        Assertions.assertEquals(2, result);
    }

    @Test
    void testUpdateTenantById() {
        Tenant param = new Tenant();
        when(tenantMapper.updateTenantById(param)).thenReturn(2);

        Integer result = tenantServiceImpl.updateTenantById(param);
        Assertions.assertEquals(2, result);
    }

    @Test
    void testCreateTenant() {
        Tenant param = new Tenant();
        when(tenantMapper.createTenant(param)).thenReturn(2);

        Integer result = tenantServiceImpl.createTenant(param);
        Assertions.assertEquals(2, result);
    }
}
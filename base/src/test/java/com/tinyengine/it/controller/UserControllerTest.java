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

package com.tinyengine.it.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.context.LoginUserContext;
import com.tinyengine.it.common.handler.MockUserContext;
import com.tinyengine.it.common.utils.TestUtil;
import com.tinyengine.it.mapper.AuthUsersUnitsRolesMapper;
import com.tinyengine.it.model.entity.Tenant;
import com.tinyengine.it.model.entity.User;
import com.tinyengine.it.service.app.UserService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

/**
 * test case
 *
 * @since 2024-10-29
 */
class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Mock
    private AuthUsersUnitsRolesMapper authUsersUnitsRolesMapper;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testMe() throws NoSuchFieldException, IllegalAccessException {
        TestUtil.setPrivateValue(userController, "loginUserContext", new MockUserContext());
        User mockData = new User();
        Tenant tenant = new Tenant();
        List<Tenant> tenants = new ArrayList<>();
        tenants.add(tenant);
        when(userService.queryUserById(anyString())).thenReturn(mockData);
        when(authUsersUnitsRolesMapper.queryAllTenantByUserId(anyInt())).thenReturn(tenants);
        Result<User> result = userController.me();
        Assertions.assertEquals(mockData, result.getData());
    }
}


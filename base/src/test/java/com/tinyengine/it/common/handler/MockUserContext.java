/**
 * Copyright (c) 2023 - present TinyEngine Authors.
 * Copyright (c) 2023 - present Huawei Cloud Computing Technologies Co., Ltd.
 * <p>
 * Use of this source code is governed by an MIT-style license.
 * <p>
 * THE OPEN SOURCE SOFTWARE IN THIS PRODUCT IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL,
 * BUT WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS FOR
 * A PARTICULAR PURPOSE. SEE THE APPLICABLE LICENSES FOR MORE DETAILS.
 */

package com.tinyengine.it.common.handler;

import com.tinyengine.it.common.context.LoginUserContext;
import com.tinyengine.it.model.entity.Tenant;

import java.util.ArrayList;
import java.util.List;

/**
 * Mock user context
 *
 * @since 2025-04-14
 */
public class MockUserContext implements LoginUserContext {
    /**
     * 返回当前用户所诉的业务租户信息
     *
     * @return 租户ID
     */
    @Override
    public List<Tenant> getTenants() {
        Tenant tenant = new Tenant();
        tenant.setId("1");
        List<Tenant> tenantList = new ArrayList<>();
        tenantList.add(tenant);
        return tenantList;
    }

    @Override
    public String getLoginUserId() {
        return "1";
    }


    @Override
    public int getPlatformId() {
        return 1;
    }

    /**
     * 设置当前组织信息
     *
     * @param tenants
     */
    @Override
    public void setTenants(List<Tenant> tenants) {

    }

}

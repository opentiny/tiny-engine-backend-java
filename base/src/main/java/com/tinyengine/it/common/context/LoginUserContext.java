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

package com.tinyengine.it.common.context;

import com.tinyengine.it.model.entity.Tenant;

import java.util.List;

/**
 * 保存用户信息的上下文
 * 由集成方自行实现接口
 */
public interface LoginUserContext {
    /**
     * 返回当前用户所在的业务租户id
     * @return 租户Id
     */
    String getTenantId();

    /**
     * 返回当前用户所在的业务租户信息
     * @return 租户
     */
    List<Tenant> getTenants();

    /**
     * 返回当前用户信息
     *
     * @return 用户ID
     */
    String getLoginUserId();

    /**
     * 返回当前设计器信息
     * @return 设计器ID
     */
    int getPlatformId();

    /**
     * 设置当前组织信息
     * @param tenants
     */
    void setTenants(List<Tenant> tenants);
}

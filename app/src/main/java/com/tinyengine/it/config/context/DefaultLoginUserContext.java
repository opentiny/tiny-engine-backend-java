/**
 * Copyright (c) 2023 - present TinyEngine Authors.
 * Copyright (c) 2023 - present Huawei Cloud Computing Technologies Co., Ltd.
 *
 * Use of this source code is governed by an MIT-style license.
 *
 * THE OPEN SOURCE SOFTWARE IN THIS PRODUCT IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL,
 * BUT WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS FOR
 * A PARTICULAR PURPOSE. SEE THE APPLICABLE LICENSES FOR MORE DETAILS.
 */

package com.tinyengine.it.config.context;

import com.tinyengine.it.common.context.LoginUserContext;
import org.springframework.stereotype.Service;

/**
 * 默认的登录用户Context实现
 */
@Service
public class DefaultLoginUserContext implements LoginUserContext {
    @Override
    public String getTenantId() {
        return "1";
    }

    @Override
    public String getLoginUserId() {
        return "1";
    }

    @Override
    public String getRenterId() {
        return "1";
    }

    @Override
    public int getAppId() {
        return 1;
    }

    @Override
    public int getPlatformId() {
        return 1;
    }

    @Override
    public String getSiteId() {
        return "1";
    }
}

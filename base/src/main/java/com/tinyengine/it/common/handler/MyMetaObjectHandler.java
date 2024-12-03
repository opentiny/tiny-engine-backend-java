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

package com.tinyengine.it.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

import lombok.extern.slf4j.Slf4j;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * The type My meta object handler.
 *
 * @since 2024-10-20
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createdTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("lastUpdatedTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("createdBy", "1", metaObject);
        this.setFieldValByName("lastUpdatedBy", "1", metaObject);
        this.setFieldValByName("tenantId", "1", metaObject);
        this.setFieldValByName("renterId", "1", metaObject);
        this.setFieldValByName("siteId", "1", metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("lastUpdatedTime", LocalDateTime.now(), metaObject);
    }
}

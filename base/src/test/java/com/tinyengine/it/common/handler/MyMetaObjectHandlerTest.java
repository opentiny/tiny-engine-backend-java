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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.ibatis.reflection.MetaObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * test case
 *
 * @since 2024-10-29
 */
class MyMetaObjectHandlerTest {
    MyMetaObjectHandler myMetaObjectHandler = new MyMetaObjectHandler();

    @Test
    void testInsertFill() {
        MetaObject param = Mockito.mock(MetaObject.class);
        when(param.hasSetter("tenantId")).thenReturn(true);
        myMetaObjectHandler.insertFill(param);
        verify(param, times(7)).hasSetter(anyString());
    }

    @Test
    void testUpdateFill() {
        MetaObject param = Mockito.mock(MetaObject.class);
        when(param.hasSetter("lastUpdatedTime")).thenReturn(true);
        myMetaObjectHandler.updateFill(param);
        verify(param, times(1)).hasSetter(anyString());
    }
}
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tinyengine.it.common.utils.TestUtil;

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
    void testInsertFill() throws NoSuchFieldException, IllegalAccessException {
        MetaObject param = Mockito.mock(MetaObject.class);
        when(param.hasSetter("tenantId")).thenReturn(true);
        TestUtil.setPrivateValue(myMetaObjectHandler, "loginUserContext", new MockUserContext());
        myMetaObjectHandler.insertFill(param);
        verify(param, times(5)).hasSetter(anyString());
    }

    @Test
    void testUpdateFill() throws NoSuchFieldException, IllegalAccessException {
        MetaObject param = Mockito.mock(MetaObject.class);
        when(param.hasSetter("lastUpdatedTime")).thenReturn(true);
        TestUtil.setPrivateValue(myMetaObjectHandler, "loginUserContext", new MockUserContext());
        myMetaObjectHandler.updateFill(param);
        verify(param, times(1)).hasSetter(anyString());
    }
}
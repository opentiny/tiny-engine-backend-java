package com.tinyengine.it.config.handler;

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
        verify(param, times(6)).hasSetter(anyString());
    }

    @Test
    void testUpdateFill() {
        MetaObject param = Mockito.mock(MetaObject.class);
        when(param.hasSetter("lastUpdatedTime")).thenReturn(true);
        myMetaObjectHandler.updateFill(param);
        verify(param, times(1)).hasSetter(anyString());
    }
}
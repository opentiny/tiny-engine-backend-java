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

package com.tinyengine.it.common.log;

import static org.mockito.Mockito.when;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;

/**
 * test case
 *
 * @since 2024-10-29
 */
class SystemLogAspectTest {
    SystemLogAspect systemLogAspect = new SystemLogAspect();

    @Test
    void testGetServiceMethodDescription() throws Exception {
        JoinPoint joinPoint = Mockito.mock(JoinPoint.class);
        MethodSignature signature = Mockito.mock(MethodSignature.class);
        when(joinPoint.getSignature()).thenReturn(signature);
        Method mockMethod = getMockServiceMethod();
        when(signature.getMethod()).thenReturn(mockMethod);
        String result = SystemLogAspect.getServiceMethodDescription(joinPoint);

        Assertions.assertEquals("ServiceLog", result);
    }

    private Method getMockServiceMethod() throws NoSuchMethodException {
        return TestClass.class.getMethod("serviceLog");
    }

    private Method getMockControllerLog() throws NoSuchMethodException {
        return TestClass.class.getMethod("controllerLog");
    }

    @Test
    void testGetControllerMethodDescription() throws Exception {
        JoinPoint joinPoint = Mockito.mock(JoinPoint.class);
        MethodSignature signature = Mockito.mock(MethodSignature.class);
        when(joinPoint.getSignature()).thenReturn(signature);
        Method mockMethod = getMockControllerLog();
        when(signature.getMethod()).thenReturn(mockMethod);
        String result = SystemLogAspect.getControllerMethodDescription(joinPoint);

        Assertions.assertEquals("ControllerLog", result);
    }

    private class TestClass {
        @SystemServiceLog(description = "ServiceLog")
        public String serviceLog() {
            return "ServiceLog";
        }

        @SystemControllerLog(description = "ControllerLog")
        public String controllerLog() {
            return "ControllerLog";
        }
    }

    @Test
    void testServiceAspect() {
        systemLogAspect.serviceAspect();
    }

    @Test
    void testControllerAspect() {
        systemLogAspect.controllerAspect();
    }

    @Test
    void testDoBefore() {
        JoinPoint joinPoint = Mockito.mock(JoinPoint.class);
        MethodSignature signature = Mockito.mock(MethodSignature.class);
        when(joinPoint.getSignature()).thenReturn(signature);
        systemLogAspect.doBefore(joinPoint);
    }

    @Test
    void testDoAfterThrowing() {
        JoinPoint joinPoint = Mockito.mock(JoinPoint.class);
        MethodSignature signature = Mockito.mock(MethodSignature.class);
        when(joinPoint.getSignature()).thenReturn(signature);
        systemLogAspect.doAfterThrowing(joinPoint, new Throwable("message"));
    }
}


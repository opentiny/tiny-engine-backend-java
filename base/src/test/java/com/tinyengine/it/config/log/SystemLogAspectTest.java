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
}


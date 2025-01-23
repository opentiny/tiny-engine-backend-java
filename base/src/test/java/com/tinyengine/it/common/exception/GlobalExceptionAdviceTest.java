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

package com.tinyengine.it.common.exception;

import static org.mockito.Mockito.when;

import com.tinyengine.it.common.base.Result;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

/**
 * test case
 *
 * @since 2024-10-29
 */
class GlobalExceptionAdviceTest {
    private GlobalExceptionAdvice globalExceptionAdvice = new GlobalExceptionAdvice();

    @Test
    void testHandleException() {
        // 创建一个新的异常对象并传递给 handleException
        Exception e = new Exception("message", new Throwable("message"));
        Result<Map<String, String>> result = globalExceptionAdvice.handleException(e);
        Assertions.assertEquals(ExceptionEnum.CM001.getResultMsg(), result.getMessage());
        Assertions.assertEquals(ExceptionEnum.CM001.getResultCode(), result.getCode());
    }

    @Test
    void testHandleNullPointerException() {
        Result<Map<String, String>> result = globalExceptionAdvice.handleNullPointerException(null, null);
        Assertions.assertEquals(ExceptionEnum.CM001.getResultMsg(), result.getMessage());
        Assertions.assertEquals(ExceptionEnum.CM001.getResultCode(), result.getCode());
    }

    @Test
    void testHandleServiceException() {
        ServiceException exception = new ServiceException("code", "message");
        Result<Map<String, String>> result = globalExceptionAdvice.handleServiceException(exception);
        Assertions.assertEquals(exception.getMessage(), result.getMessage());
        Assertions.assertEquals("code", result.getCode());
    }

    @Test
    void testHandleValidationExceptions() throws NoSuchMethodException {
        FieldError fieldError = new FieldError("object", "field", "message");
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        when(bindingResult.getFieldError()).thenReturn(fieldError);
        MethodParameter parameter = new MethodParameter(Object.class.getMethod("toString"), -1);
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(parameter, bindingResult);

        Result<Map<String, String>> result = globalExceptionAdvice.handleValidationExceptions(exception);
        Assertions.assertEquals(fieldError.getDefaultMessage(), result.getMessage());
        Assertions.assertEquals(ExceptionEnum.CM002.getResultCode(), result.getCode());
    }
}


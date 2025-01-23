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

import com.tinyengine.it.common.base.Result;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

/**
 * The type Global exception.
 *
 * @since 2024-10-20
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionAdvice {
    /**
     * 服务异常
     *
     * @param e the e
     * @return the result
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Result<Map<String, String>> handleException(Exception e) {
        // 修改为 log.error，传递异常对象以打印堆栈信息
        log.error("Exception occurred: ", e);
        return Result.failed(ExceptionEnum.CM001);
    }

    /**
     * 处理空指针的异常
     *
     * @param req the req
     * @param e   the e
     * @return the result
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(NullPointerException.class)
    public Result<Map<String, String>> handleNullPointerException(HttpServletRequest req, NullPointerException e) {
        // 修改为 log.error，传递异常对象以打印堆栈信息
        log.error("NullPointerException occurred: ", e);
        return Result.failed(ExceptionEnum.CM001);
    }

    /**
     * 自定义业务异常
     *
     * @param e the e
     * @return the result
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ServiceException.class)
    public Result<Map<String, String>> handleServiceException(ServiceException e) {
        // 修改为 log.error，传递异常对象以打印堆栈信息
        log.error("Business exception occurred: ", e);
        return Result.failed(e.getCode(), e.getMessage());
    }

    /**
     * 处理方法参数验证异常
     *
     * @param exception the exception
     * @return the result
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException exception) {
        // 从异常对象中获取验证错误信息
        String errorMessage = Objects.requireNonNull(exception.getBindingResult().getFieldError()).getDefaultMessage();
        // 修改为 log.error，传递异常对象以打印堆栈信息
        log.error("Validation exception occurred: ", exception);
        // 返回响应实体，其中包含错误消息
        return Result.failed(ExceptionEnum.CM002.getResultCode(), errorMessage);
    }
}

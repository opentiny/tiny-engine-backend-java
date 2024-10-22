package com.tinyengine.it.common.exception;

import com.tinyengine.it.common.base.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class GlobalException {

    /**
     * 服务异常
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    public Result<Map<String, String>> handleException(Exception e) {
        log.error("Exception occurred: ", e);  // 修改为 log.error，传递异常对象以打印堆栈信息
        return Result.failed(ExceptionEnum.CM001);
    }

    /**
     * 处理空指针的异常
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(NullPointerException.class)
    public Result<Map<String, String>> handleNullPointerException(HttpServletRequest req, NullPointerException e) {
        log.error("NullPointerException occurred: ", e);  // 修改为 log.error，传递异常对象以打印堆栈信息
        return Result.failed(ExceptionEnum.CM001);
    }

    /**
     * 自定义业务异常
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ServiceException.class)
    public Result<Map<String, String>> handleServiceException(ServiceException e) {
        log.error("Business exception occurred: ", e);  // 修改为 log.error，传递异常对象以打印堆栈信息
        return Result.failed(e.getCode(), e.getMessage());
    }

    /**
     * 处理方法参数验证异常
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException e) {
        // 从异常对象中获取验证错误信息
        String errorMessage = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        log.error("Validation exception occurred: ", e);  // 修改为 log.error，传递异常对象以打印堆栈信息
        // 返回响应实体，其中包含错误消息
        return Result.failed(ExceptionEnum.CM002, errorMessage);
    }
}

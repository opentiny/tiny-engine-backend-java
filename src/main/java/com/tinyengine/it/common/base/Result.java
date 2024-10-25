
package com.tinyengine.it.common.base;

import static com.alibaba.druid.support.json.JSONUtils.toJSONString;

import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.common.exception.IBaseError;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The type Result.
 *
 * @param <T> the type parameter
 * @since 2024-10-20
 */
@Data
@NoArgsConstructor
@Schema(name = "Result<T>", description = "通用返回对象")
public class Result<T> {
    @Schema(name = "data", description = "请求返回数据")
    private T data;
    @Schema(name = "code", description = "状态码")
    private String code;
    @Schema(name = "message", description = "提示信息")
    private String message;
    @Schema(name = "success", description = "成功还是失败")
    private boolean success;
    @Schema(name = "error", description = "失败对象")
    private Map<String, Object> error;
    @Schema(name = "err_msg", description = "失败信息")
    private String err_msg;

    /**
     * Instantiates a new Result.
     *
     * @param code the code
     * @param message the message
     * @param data the data
     */
    protected Result(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.success = true;
    }

    /**
     * Instantiates a new Result.
     *
     * @param code the code
     * @param message the message
     * @param data the data
     * @param success the success
     */
    protected Result(String code, String message, T data, boolean success) {
        this(code, message, data);
        this.success = success;
        if (!success) {
            this.err_msg = message;
            this.error = Stream.of(new Object[][]{{"code", code}, {"message", message}})
                    .collect(Collectors.toMap(item -> (String) item[0], item -> item[1]));
        }
    }

    /**
     * Success result.
     *
     * @param <T> the type parameter
     * @return the result
     */
    public static <T> Result<T> success() {
        return success((T) null);
    }

    /**
     * 成功返回结果
     *
     * @param <T> the type parameter
     * @param data 获取的数据
     * @return result
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(ExceptionEnum.SUCCESS.getResultCode(), ExceptionEnum.SUCCESS.getResultMsg(), data);
    }

    /**
     * 成功返回list结果
     *
     * @param <T> the type parameter
     * @param list 获取的数据
     * @return result
     */
    public static <T> Result<List<T>> success(List<T> list) {
        return new Result<>(ExceptionEnum.SUCCESS.getResultCode(), ExceptionEnum.SUCCESS.getResultMsg(), list);
    }

    /**
     * 成功返回结果
     *
     * @param <T> the type parameter
     * @param data 获取的数据
     * @param message 提示信息
     * @return the result
     */
    public static <T> Result<T> success(T data, String message) {
        return new Result<>(ExceptionEnum.SUCCESS.getResultCode(), message, data);
    }

    /**
     * 失败返回结果
     *
     * @param <T> the type parameter
     * @param error 错误码
     * @return the result
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static <T> Result<T> failed(IBaseError error) {
        return new Result<>(error.getResultCode(), error.getResultMsg(), null, false);
    }

    /**
     * 失败返回结果
     *
     * @param <T> the type parameter
     * @param error 错误码
     * @param message 错误信息
     * @return the result
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static <T> Result<T> failed(IBaseError error, String message) {
        return new Result<>(error.getResultCode(), message, null, false);
    }

    /**
     * 失败返回结果
     *
     * @param <T> the type parameter
     * @param errorCode 错误码
     * @param message 错误信息
     * @return the result
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static <T> Result<T> failed(String errorCode, String message) {
        return new Result<>(errorCode, message, null, false);
    }

    /**
     * 失败返回结果
     *
     * @param <T> the type parameter
     * @param message 提示信息
     * @return the result
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public static <T> Result<T> failed(String message) {
        return new Result<>(ExceptionEnum.CM001.getResultCode(), message, null, false);
    }

    /**
     * 失败返回结果
     *
     * @param <T> the type parameter
     * @return the result
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public static <T> Result<T> failed() {
        return failed(ExceptionEnum.CM001);
    }

    /**
     * 参数验证失败返回结果
     *
     * @param <T> the type parameter
     * @return the result
     */
    public static <T> Result<T> validateFailed() {
        return failed(ExceptionEnum.CM002);
    }

    /**
     * 参数验证失败返回结果
     *
     * @param <T> the type parameter
     * @param message 提示信息
     * @return the result
     */
    public static <T> Result<T> validateFailed(String message) {
        return new Result<>(ExceptionEnum.CM002.getResultCode(), message, null, false);
    }

    @Override
    public String toString() {
        return toJSONString(this);
    }
}

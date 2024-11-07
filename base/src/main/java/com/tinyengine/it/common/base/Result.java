package com.tinyengine.it.common.base;

import static com.alibaba.druid.support.json.JSONUtils.toJSONString;

import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.common.exception.IBaseError;

import cn.hutool.core.map.MapUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Result.
 *
 * @param <T> the type parameter
 * @since 2024-10-20
 */
@Data
@NoArgsConstructor
@Schema(name = "Result<T extends Object>", description = "通用返回对象")
public class Result<T extends Object> {
    @Schema(name = "data", description = "请求返回数据")
    private T data;
    @Schema(name = "code", description = "状态码")
    private String code;
    @Schema(name = "message", description = "提示信息")
    private String message;
    @Schema(name = "success", description = "成功还是失败")
    private boolean isSuccess;
    @Schema(name = "error", description = "失败对象")
    private Map<String, Object> error;
    @Schema(name = "err_msg", description = "失败信息")
    private String errMsg;

    /**
     * Instantiates a new Result.
     *
     * @param code    the code
     * @param message the message
     * @param data    the data
     */
    protected Result(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.isSuccess = true;
    }

    /**
     * Instantiates a new Result.
     *
     * @param code    the code
     * @param message the message
     * @param data    the data
     * @param success the success
     */
    protected Result(String code, String message, T data, boolean success) {
        this(code, message, data);
        this.isSuccess = success;
        if (!success) {
            this.errMsg = message;
            this.error = MapUtil.builder(new HashMap<String, Object>())
                    .put("code", code)
                    .put("message", message).build();
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
     * @param <T>  the type parameter
     * @param data 获取的数据
     * @return result
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(ExceptionEnum.SUCCESS.getResultCode(), ExceptionEnum.SUCCESS.getResultMsg(), data);
    }

    /**
     * 成功返回结果
     *
     * @param <T>     the type parameter
     * @param data    获取的数据
     * @param message 提示信息
     * @return the result
     */
    public static <T> Result<T> success(T data, String message) {
        return new Result<>(ExceptionEnum.SUCCESS.getResultCode(), message, data);
    }

    /**
     * 失败返回结果
     *
     * @param <T>   the type parameter
     * @param error 错误码
     * @return the result
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static <T> Result<T> failed(IBaseError error) {
        return failed(error.getResultCode(), error.getResultMsg());
    }

    /**
     * 失败返回结果
     *
     * @param <T>     the type parameter
     * @param error   错误码
     * @param message 错误信息
     * @return the result
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static <T> Result<T> failed(IBaseError error, String message) {
        return failed(error.getResultCode(), message);
    }

    /**
     * 失败返回结果
     *
     * @param <T>       the type parameter
     * @param errorCode 错误码
     * @param message   错误信息
     * @return the result
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static <T> Result<T> failed(String errorCode, String message) {
        return new Result<>(errorCode, message, null, false);
    }

    /**
     * 失败返回结果
     *
     * @param <T>     the type parameter
     * @param message 提示信息
     * @return the result
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public static <T> Result<T> failed(String message) {
        return failed(ExceptionEnum.CM001.getResultCode(), message);
    }

    /**
     * 参数验证失败返回结果
     *
     * @param <T>     the type parameter
     * @param message 提示信息
     * @return the result
     */
    public static <T> Result<T> validateFailed(String message) {
        return failed(ExceptionEnum.CM002.getResultCode(), message);
    }

    @Override
    public String toString() {
        return toJSONString(this);
    }
}

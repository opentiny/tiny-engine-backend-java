package com.tinyengine.it.common.exception;

/**
 * The interface Base error.
 * @since 2024-10-20
 */
public interface IBaseError {
    /**
     * 错误码
     *
     * @return the result code
     */
    String getResultCode();

    /**
     * 错误描述
     *
     * @return the result msg
     */
    String getResultMsg();
}

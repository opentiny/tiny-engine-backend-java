package com.tinyengine.it.common.exception;

/**
 * The interface Base error.
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

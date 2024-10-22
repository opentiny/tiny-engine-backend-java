package com.tinyengine.it.common.exception;

public interface IBaseError {
    /**
     * 错误码
     */
    String getResultCode();

    /**
     * 错误描述
     */
    String getResultMsg();
}

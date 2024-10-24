package com.tinyengine.it.common.exception;

import lombok.Getter;

/**
 * The type Service exception.
 */
public class ServiceException extends RuntimeException {
    private final String message;
    @Getter
    private String code;

    /**
     * Instantiates a new Service exception.
     *
     * @param code    the code
     * @param message the message
     */
    public ServiceException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * Instantiates a new Service exception.
     *
     * @param message the message
     */
    public ServiceException(String message) {
        this.code = "400";
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    /**
     * Sets code.
     *
     * @param code the code
     */
    public void setCode(String code) {
        this.code = code;
    }
}


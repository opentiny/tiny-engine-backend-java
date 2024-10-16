package com.tinyengine.it.exception;

import lombok.Getter;

public class ServiceException extends RuntimeException {
    @Getter
    private String code;
    private final String message;

    public ServiceException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ServiceException(String message) {
        this.code = "400";
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setCode(String code) {
        this.code = code;
    }
}


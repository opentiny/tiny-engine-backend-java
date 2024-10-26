
package com.tinyengine.it.common.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * The type Service exception.
 *
 * @since 2024-10-20
 */
@Getter
@Setter
public class ServiceException extends RuntimeException {
    private final String message;
    @Getter
    private String code;

    /**
     * Instantiates a new Service exception.
     *
     * @param code the code
     * @param message the message
     */
    public ServiceException(String code, String message) {
        this.code = code;
        this.message = message;
    }
}

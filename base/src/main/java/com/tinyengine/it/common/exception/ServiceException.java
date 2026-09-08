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
    private static final long serialVersionUID = 1L;

    private final String message;
    @Getter
    private String code;

    /**
     * Instantiates a new Service exception.
     *
     * @param code    the code
     * @param message the message
     */
    public ServiceException(final String code, final String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public ServiceException(final String code, final String message, final Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }
}

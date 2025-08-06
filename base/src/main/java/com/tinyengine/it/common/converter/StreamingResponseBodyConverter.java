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

package com.tinyengine.it.common.converter;


import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.OutputStream;

/**
 * The type StreamingResponseBodyConverter.
 *
 * @since 2025-08-06
 */
@Component
public class StreamingResponseBodyConverter extends AbstractHttpMessageConverter<StreamingResponseBody> {

    public StreamingResponseBodyConverter() {
        super(MediaType.TEXT_EVENT_STREAM);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return StreamingResponseBody.class.isAssignableFrom(clazz);
    }

    @Override
    protected StreamingResponseBody readInternal(Class<? extends StreamingResponseBody> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        throw new UnsupportedOperationException("Streaming response body does not support input.");
    }

    @Override
    protected void writeInternal(StreamingResponseBody responseBody, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        OutputStream outputStream = outputMessage.getBody();
        responseBody.writeTo(outputStream); // 使用 StreamingResponseBody 的 writeTo 方法
    }
}
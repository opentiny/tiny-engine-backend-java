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

package com.tinyengine.it.common.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.common.log.SystemLogAspect;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * The type json Utils.
 *
 * @since 2025-06-19
 */
public class JsonUtils {
    public static final ObjectMapper MAPPER = MapperFactory.getDefaultMapper();
    public static final ObjectMapper PRETTY_MAPPER = MapperFactory.getDefaultMapper();
    public static final DefaultPrettyPrinter PRINTER = new DefaultPrettyPrinter();
    private static final Logger LOGGER = LoggerFactory.getLogger(SystemLogAspect.class);

    public JsonUtils() {
    }

    private static void initialize() {
        SimpleModule module = new SimpleModule();
        MAPPER.findAndRegisterModules();
        MAPPER.registerModule(module);
        MAPPER.registerModule(new JavaTimeModule());
        PRETTY_MAPPER.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        PRETTY_MAPPER.configure(SerializationFeature.INDENT_OUTPUT, true);
        PRETTY_MAPPER.registerModule(module);
        PRETTY_MAPPER.findAndRegisterModules();
        PRINTER.indentObjectsWith(new DefaultIndenter(" ", "\n"));
        PRETTY_MAPPER.setDefaultPrettyPrinter(PRINTER);
    }

    private static <T> T invoke(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception var2) {
            Exception e = var2;
            throw new ServiceException(ExceptionEnum.CM001.getResultCode(), ExceptionEnum.CM001.getResultMsg() + e.getMessage());
        }
    }

    /**
     * To mapper.
     *
     * @return the MAPPER
     */
    public static ObjectMapper mapper() {
        return MAPPER;
    }

    /**
     * To pretty mapper.
     *
     * @return the string
     */
    public static ObjectMapper prettyMapper() {
        return PRETTY_MAPPER;
    }

    /**
     * To encode.
     *
     * @param obj the obj
     * @return the string
     */
    public static String encode(Object obj) {
        return (String) invoke(() -> {
            return MAPPER.writeValueAsString(obj);
        });
    }

    /**
     * To encode prettily.
     *
     * @param obj the obj
     * @return the string
     */
    public static String encodePrettily(Object obj) {
        return (String) invoke(() -> {
            return PRETTY_MAPPER.writeValueAsString(obj);
        });
    }

    /**
     * To encode as bytes.
     *
     * @param obj the obj
     * @return the byte
     */
    public static byte[] encodeAsBytes(Object obj) {
        return (byte[]) invoke(() -> {
            return MAPPER.writeValueAsBytes(obj);
        });
    }

    /**
     * To decode.
     *
     * @param src the src
     * @return the T
     */
    public static <T> T decode(byte[] src) {
        return (T) decode(src, Object.class);
    }

    /**
     * To decode.
     *
     * @param src the src
     * @param valueType the valueType
     * @return the T
     */
    public static <T> T decode(byte[] src, Class<T> valueType) {
        return invoke(() -> {
            return MAPPER.readValue(src, valueType);
        });
    }

    /**
     * To decode.
     *
     * @param src the src
     * @param valueTypeRef the valueTypeRef
     * @return the T
     */
    public static <T> T decode(byte[] src, TypeReference<T> valueTypeRef) {
        return invoke(() -> {
            return MAPPER.readValue(src, valueTypeRef);
        });
    }

    /**
     * To decode.
     *
     * @param url the url
     * @param valueType the valueType
     * @return the T
     */
    public static <T> T decode(URL url, Class<T> valueType) {
        return invoke(() -> {
            return MAPPER.readValue(url, valueType);
        });
    }

    /**
     * To decode.
     *
     * @param src the src
     * @return the T
     */
    public static <T> T decode(String src) {
        return (T) decode(src, Object.class);
    }

    /**
     * To decode.
     *
     * @param src the src
     * @param valueType the valueType
     * @return the T
     */
    public static <T> T decode(String src, Class<T> valueType) {
        return invoke(() -> {
            return MAPPER.readValue(src, valueType);
        });
    }

    /**
     * To decode.
     *
     * @param src the src
     * @param valueType the valueType
     * @return the T
     */
    public static <T> T decode(String src, JavaType valueType) {
        return invoke(() -> {
            return MAPPER.readValue(src, valueType);
        });
    }

    /**
     * To decode.
     *
     * @param src the src
     * @param valueTypeRef the valueTypeRef
     * @return the T
     */
    public static <T> T decode(String src, TypeReference<T> valueTypeRef) {
        return invoke(() -> {
            return MAPPER.readValue(src, valueTypeRef);
        });
    }

    /**
     * To decode.
     *
     * @param src the src
     * @param valueType the valueType
     * @return the T
     */
    public static <T> T decode(ByteBuf src, Class<T> valueType) {
        return (T) invoke(() -> {
            InputStream inputStream = new ByteBufInputStream(src);
            Object var3;
            try {
                var3 = MAPPER.readValue(inputStream, valueType);
            } catch (Throwable var6) {
                try {
                    inputStream.close();
                } catch (Throwable var5) {
                    var6.addSuppressed(var5);
                }
                throw new ServiceException(ExceptionEnum.CM001.getResultCode(),var6.getMessage());
            }
            inputStream.close();
            return var3;
        });
    }

    /**
     * To decode.
     *
     * @param src the src
     * @param valueType the valueType
     * @return the T
     */
    public static <T> T decode(InputStream src, Class<T> valueType) {
        return invoke(() -> {
            return MAPPER.readValue(src, valueType);
        });
    }

    /**
     * To convert value.
     *
     * @param fromValue the fromValue
     * @param toValueType the toValueType
     * @return the T
     */
    public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
        return invoke(() -> {
            return MAPPER.convertValue(fromValue, toValueType);
        });
    }

    /**
     * To convert value.
     *
     * @param fromValue the fromValue
     * @param toValueType the toValueType
     * @return the T
     */
    public static <T> T convertValue(Object fromValue, JavaType toValueType) {
        return invoke(() -> {
            return MAPPER.convertValue(fromValue, toValueType);
        });
    }

    /**
     * To convert value.
     *
     * @param fromValue the fromValue
     * @param toValueType the toValueType
     * @return the T
     */
    public static <T> T convertValue(Object fromValue, TypeReference<T> toValueType) {
        return invoke(() -> {
            return MAPPER.convertValue(fromValue, toValueType);
        });
    }

    /**
     * Converts various input types (String, Array, List) to a trimmed List of Strings.
     *
     * @param inputs the inputs
     * @return List of trimmed strings, empty list if input is null or unsupported type
     */
    public static List<String> getList(Object inputs) {
        if (inputs == null) {
            return Collections.emptyList();
        }

        // Handle Array → List
        if (inputs.getClass().isArray()) {
            if (inputs instanceof Object[]) {
                inputs = Arrays.asList((Object[]) inputs);
            } else {
                // Handle primitive arrays by converting to string representation
                inputs = Collections.singletonList(inputs.toString());
            }
        }

        // Handle String → List
        if (inputs instanceof String) {
            return Arrays.stream(((String) inputs).split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());
        }

        // Handle List → List<String>
        if (inputs instanceof List) {
            return ((List<?>) inputs).stream()
                    .map(Object::toString)
                    .map(String::trim)
                    .collect(Collectors.toList());
        }

        LOGGER.error("Bad data type: {}, it should be String, Array, or List.", inputs.getClass().getName());
        return Collections.emptyList();
    }

    static {
        initialize();
    }
}

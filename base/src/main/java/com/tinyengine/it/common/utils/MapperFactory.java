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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * The type mapper Factory.
 *
 * @since 2025-06-19
 */
public class MapperFactory {
    public MapperFactory() {
    }

    /**
     * To register modules.
     *
     * @param mapper the mapper
     * @return
     */
    public static void registerModules(ObjectMapper mapper) {
        List<Module> modules = SpringFactoriesLoader.loadFactories(Module.class, (ClassLoader) null);
        mapper.registerModules((Module[]) modules.toArray(new Module[0]));
        mapper.registerModule(new SimpleModule());
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModules(new Module[]{(new SimpleModule()).addDeserializer(OffsetDateTime.class,
            new OffsetDateTimeDeserializer())});
    }

    /**
     * To get default builder.
     *
     * @return JsonMapper.Builder the JsonMapper.Builder
     */
    public static JsonMapper.Builder getDefaultBuilder() {
        return (JsonMapper.Builder) ((JsonMapper.Builder) ((JsonMapper.Builder)
            ((JsonMapper.Builder) ((JsonMapper.Builder) ((JsonMapper.Builder)
            ((JsonMapper.Builder) ((JsonMapper.Builder) ((JsonMapper.Builder) JsonMapper.builder()
            .disable(new DeserializationFeature[]{DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES}))
            .disable(new SerializationFeature[] {SerializationFeature.FAIL_ON_EMPTY_BEANS}))
            .disable(new MapperFeature[]{MapperFeature.DEFAULT_VIEW_INCLUSION}))
            .disable(new SerializationFeature[]{SerializationFeature.WRITE_DATES_AS_TIMESTAMPS}))
            .disable(new JsonParser.Feature[]{JsonParser.Feature.AUTO_CLOSE_SOURCE}))
            .serializationInclusion(JsonInclude.Include.NON_NULL))
            .enable(new DeserializationFeature[]{DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS}))
            .enable(new DeserializationFeature[]{DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY}))
            .enable(new DeserializationFeature[]{DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT});
    }

    /**
     * To get default mapper.
     *
     * @return ObjectMapper the ObjectMapper.
     */
    public static ObjectMapper getDefaultMapper() {
        return getDefaultBuilder().build();
    }

    /**
     * To get modules registered mapper.
     *
     * @return ObjectMapper the ObjectMapper.
     */
    public static ObjectMapper getModulesRegisteredMapper() {
        ObjectMapper mapper = getDefaultMapper();
        registerModules(mapper);
        return mapper;
    }
}

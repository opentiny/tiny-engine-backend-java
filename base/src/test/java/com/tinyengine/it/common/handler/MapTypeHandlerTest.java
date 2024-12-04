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

package com.tinyengine.it.common.handler;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;

import cn.hutool.json.JSONUtil;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * test case
 *
 * @since 2024-10-29
 */
class MapTypeHandlerTest {
    @InjectMocks
    private MapTypeHandler mapTypeHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSetNonNullParameter() throws SQLException, JsonProcessingException {
        try(PreparedStatement ps = Mockito.mock(PreparedStatement.class)) {
            Map<String, Object> mapData = new HashMap<String, Object>() {
                {
                    put("parameter", "parameter");
                }
            };
            String json = JSONUtil.toJsonStr(mapData);
            mapTypeHandler.setNonNullParameter(ps, 0, mapData, null);
            verify(ps, times(1)).setString(0, json);
        }
    }

    @Test
    void testGetNullableResult() throws SQLException, JsonProcessingException {
        Map<String, Object> mapData = new HashMap<String, Object>() {
            {
                put("key", "value");
            }
        };
        String json = JSONUtil.toJsonStr(mapData);
        String columnName = "columnName";

        try(ResultSet rs = Mockito.mock(ResultSet.class)) {
            when(rs.getString(columnName)).thenReturn(json);

            Map<String, Object> result = mapTypeHandler.getNullableResult(rs, "columnName");
            Assertions.assertEquals("value", result.get("key"));
        }
    }
}


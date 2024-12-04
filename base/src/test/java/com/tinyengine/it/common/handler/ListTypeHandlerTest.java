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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cn.hutool.json.JSONUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * test case
 *
 * @since 2024-10-29
 */
class ListTypeHandlerTest {
    @InjectMocks
    private ListTypeHandler listTypeHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSetNonNullParameter() throws SQLException {
        try (PreparedStatement ps = Mockito.mock(PreparedStatement.class)) {
            List<String> param = Arrays.<String>asList("a");
            listTypeHandler.setNonNullParameter(ps, 0, param, null);
            verify(ps, times(1)).setString(0, JSONUtil.toJsonStr(param));
        }
        // 确保关闭资源
    }

    @Test
    void testGetNullableResult() throws SQLException {
        try (ResultSet rs = Mockito.mock(ResultSet.class)) {
            String columnName = "columnName";
            when(rs.getString(columnName)).thenReturn("[\"a\"]");
            List<?> result = listTypeHandler.getNullableResult(rs, columnName);
            assertEquals(1, result.size());
            assertEquals("a", result.get(0));
        }
    }

    @Test
    void testGetNullableResult2() throws SQLException {
        try (ResultSet rs = Mockito.mock(ResultSet.class)) {
            int columnName = 1;
            when(rs.getString(columnName)).thenReturn("[\"a\"]");
            List<?> result = listTypeHandler.getNullableResult(rs, columnName);
            assertEquals(1, result.size());
            assertEquals("a", result.get(0));
        }
    }

    @Test
    void testGetNullableResult3() throws SQLException {
        try (CallableStatement cs = Mockito.mock(CallableStatement.class)) {
            int columnName = 1;
            when(cs.getString(columnName)).thenReturn("[{\"key\":\"value\"}]");
            List<Map> result = (List<Map>) listTypeHandler.getNullableResult(cs, columnName);
            assertEquals(1, result.size());
            assertEquals("value", result.get(0).get("key"));
        }
    }
}


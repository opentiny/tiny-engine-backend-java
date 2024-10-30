package com.tinyengine.it.config.handler;

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
        PreparedStatement ps = Mockito.mock(PreparedStatement.class);
        Map<String, Object> mapData = new HashMap<String, Object>() {{
            put("parameter", "parameter");
        }};
        String json = JSONUtil.toJsonStr(mapData);
        mapTypeHandler.setNonNullParameter(ps, 0, mapData, null);
        verify(ps, times(1)).setString(0, json);

    }

    @Test
    void testGetNullableResult() throws SQLException, JsonProcessingException {
        Map<String, Object> mapData = new HashMap<String, Object>() {{
            put("key", "value");
        }};
        String json = JSONUtil.toJsonStr(mapData);
        String columnName = "columnName";

        ResultSet rs = Mockito.mock(ResultSet.class);
        when(rs.getString(columnName)).thenReturn(json);

        Map<String, Object> result = mapTypeHandler.getNullableResult(rs, "columnName");
        Assertions.assertEquals("value", result.get("key"));
    }
}


package com.tinyengine.it.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ListTypeHandler extends BaseTypeHandler<List<?>> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<?> parameter, JdbcType jdbcType) throws SQLException {
        // 将 List<?> 转换为字符串，并设置到 PreparedStatement 中的相应参数
        try {
            String json = objectMapper.writeValueAsString(parameter);
            ps.setString(i, json);
        } catch (IOException e) {
            throw new SQLException("Error converting List<?> to JSON", e);
        }
    }

    @Override
    public List<?> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        // 从 ResultSet 中获取字符串数据并将其转换为 List<?>
        String jsonString = rs.getString(columnName);
        return convertJsonToList(jsonString);
    }

    @Override
    public List<?> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        // 从 ResultSet 中获取字符串数据并将其转换为 List<?>
        String jsonString = rs.getString(columnIndex);
        return convertJsonToList(jsonString);
    }

    @Override
    public List<?> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        // 从 CallableStatement 中获取字符串数据并将其转换为 List<?>
        String jsonString = cs.getString(columnIndex);
        return convertJsonToList(jsonString);
    }

    private List<?> convertJsonToList(String jsonString) throws SQLException {
        try {
            if (jsonString != null && !jsonString.isEmpty()) {
                if ("[]".equals(jsonString)) {
                    // 空列表的情况，返回空的 List
                    return Collections.emptyList();
                } else if (jsonString.startsWith("[{") && jsonString.endsWith("}]")) {
                    // 尝试将 JSON 字符串转换为 List<Map<String, Object>>
                    return objectMapper.readValue(jsonString, new TypeReference<List<Map<String, Object>>>() {
                    });
                } else {
                    // 尝试将 JSON 字符串转换为 List<String>
                    List<String> list = objectMapper.readValue(jsonString, new TypeReference<List<String>>() {
                    });
                    return list;
                }
            }
            return null;
        } catch (IOException e) {
            throw new SQLException("Error converting JSON to List", e);
        }
    }
}


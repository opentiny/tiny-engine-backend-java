package com.tinyengine.it.dynamic.dao;

import org.apache.ibatis.jdbc.SQL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
@SuppressWarnings({
    "PMD.CognitiveComplexity",
    "PMD.CyclomaticComplexity",
    "PMD.DataflowAnomalyAnalysis",
    "PMD.GodClass",
    "PMD.LawOfDemeter",
    "PMD.OnlyOneReturn",
    "PMD.TooManyMethods"
})
public class DynamicSqlProvider {

    private static final String COUNT_SELECT = "COUNT(*) AS count";
    private static final String LEGACY_COUNT = "COUNT(*) as count";
    private static final String TABLE_NAME_PARAM = "tableName";
    private static final String CONDITIONS_PARAM = "conditions";
    private static final String DATA_PARAM = "data";

    @SuppressWarnings("PMD.UnnecessaryConstructor")
    public DynamicSqlProvider() {
        // MyBatis instantiates providers through the default constructor.
    }

    public String select(final Map<String, Object> params) {
        final String tableName = requireIdentifier(params.get(TABLE_NAME_PARAM), TABLE_NAME_PARAM);
        final List<?> fields = getList(params.get("fields"));
        final Map<?, ?> conditions = getMap(params.get(CONDITIONS_PARAM));
        final Integer pageNum = (Integer) params.get("pageNum");
        final Integer pageSize = (Integer) params.get("pageSize");
        final String orderBy = getOptionalIdentifier(params.get("orderBy"), "orderBy");
        final String orderType = getOrderType(params.get("orderType"));

        final SQL sql = new SQL();

        if (fields != null && !fields.isEmpty()) {
            for (final Object field : fields) {
                sql.SELECT(getSelectField(field));
            }
        } else {
            sql.SELECT("*");
        }

        sql.FROM(tableName);

        if (conditions != null && !conditions.isEmpty()) {
            final List<Object> conditionValues = new ArrayList<>();
            int index = 0;
            for (final Map.Entry<?, ?> entry : conditions.entrySet()) {
                if (entry.getValue() != null) {
                    final String columnName = requireIdentifier(entry.getKey(), "condition key");
                    conditionValues.add(entry.getValue());
                    sql.WHERE(columnName + " = #{conditionValues[" + index + "]}");
                    index++;
                }
            }
            params.put("conditionValues", conditionValues);
        }

        if (orderBy != null && !orderBy.isEmpty()) {
            sql.ORDER_BY(orderBy + " " + orderType);
        }

        if (pageNum != null && pageSize != null) {
            final int safePageNum = requirePositiveInt(pageNum, "pageNum");
            final int safePageSize = requirePositiveInt(pageSize, "pageSize");
            params.put("offset", (safePageNum - 1) * safePageSize);
            params.put("limit", safePageSize);
            return sql + " LIMIT #{offset}, #{limit}";
        }

        return sql.toString();
    }

    public String insert(final Map<String, Object> params) {
        final String tableName = requireIdentifier(params.get(TABLE_NAME_PARAM), TABLE_NAME_PARAM);
        final Map<?, ?> data = getRequiredMap(params.get(DATA_PARAM), DATA_PARAM);
        final List<Object> dataValues = new ArrayList<>();

        final SQL sql = new SQL();
        sql.INSERT_INTO(tableName);

        int index = 0;
        for (final Map.Entry<?, ?> entry : data.entrySet()) {
            final String columnName = requireIdentifier(entry.getKey(), "data key");
            dataValues.add(entry.getValue());
            sql.VALUES(columnName, "#{dataValues[" + index + "]}");
            index++;
        }
        params.put("dataValues", dataValues);

        return sql.toString();
    }

    public String update(final Map<String, Object> params) {
        final String tableName = requireIdentifier(params.get(TABLE_NAME_PARAM), TABLE_NAME_PARAM);
        final Map<?, ?> data = getRequiredMap(params.get(DATA_PARAM), DATA_PARAM);
        final Map<?, ?> conditions = getRequiredMap(params.get(CONDITIONS_PARAM), CONDITIONS_PARAM);
        final List<Object> dataValues = new ArrayList<>();
        final List<Object> conditionValues = new ArrayList<>();

        final SQL sql = new SQL();
        sql.UPDATE(tableName);

        int dataIndex = 0;
        for (final Map.Entry<?, ?> entry : data.entrySet()) {
            final String columnName = requireIdentifier(entry.getKey(), "data key");
            dataValues.add(entry.getValue());
            sql.SET(columnName + " = #{dataValues[" + dataIndex + "]}");
            dataIndex++;
        }
        params.put("dataValues", dataValues);

        int conditionIndex = 0;
        for (final Map.Entry<?, ?> entry : conditions.entrySet()) {
            if (entry.getValue() != null) {
                final String columnName = requireIdentifier(entry.getKey(), "condition key");
                conditionValues.add(entry.getValue());
                sql.WHERE(columnName + " = #{conditionValues[" + conditionIndex + "]}");
                conditionIndex++;
            }
        }
        if (conditionValues.isEmpty()) {
            throw new IllegalArgumentException("conditions cannot be empty");
        }
        params.put("conditionValues", conditionValues);

        return sql.toString();
    }

    public String delete(final Map<String, Object> params) {
        final String tableName = requireIdentifier(params.get(TABLE_NAME_PARAM), TABLE_NAME_PARAM);
        final Map<?, ?> conditions = getRequiredMap(params.get(CONDITIONS_PARAM), CONDITIONS_PARAM);
        final List<Object> conditionValues = new ArrayList<>();

        final SQL sql = new SQL();
        sql.DELETE_FROM(tableName);

        int index = 0;
        for (final Map.Entry<?, ?> entry : conditions.entrySet()) {
            if (entry.getValue() != null) {
                final String columnName = requireIdentifier(entry.getKey(), "condition key");
                conditionValues.add(entry.getValue());
                sql.WHERE(columnName + " = #{conditionValues[" + index + "]}");
                index++;
            }
        }
        if (conditionValues.isEmpty()) {
            throw new IllegalArgumentException("conditions cannot be empty");
        }
        params.put("conditionValues", conditionValues);

        return sql.toString();
    }

    private String getSelectField(final Object field) {
        if (field instanceof String
                && LEGACY_COUNT.equalsIgnoreCase(((String) field).trim())) {
            return COUNT_SELECT;
        }
        return requireIdentifier(field, "field");
    }

    private String getOptionalIdentifier(final Object value, final String name) {
        if (value == null) {
            return null;
        }
        final String identifier = requireString(value, name);
        if (identifier.isEmpty()) {
            return null;
        }
        return requireIdentifier(identifier, name);
    }

    private String requireIdentifier(final Object value, final String name) {
        final String identifier = requireString(value, name);
        if (!identifier.matches("^[A-Za-z_][A-Za-z0-9_]*$")) {
            throw new IllegalArgumentException(name + " must be a valid SQL identifier");
        }
        return identifier;
    }

    private String requireString(final Object value, final String name) {
        if (!(value instanceof String)) {
            throw new IllegalArgumentException(name + " must be a string");
        }
        return (String) value;
    }

    private String getOrderType(final Object value) {
        if (value == null || value instanceof String stringValue && stringValue.isEmpty()) {
            return "ASC";
        }
        final String orderType = requireString(value, "orderType");
        if (!"ASC".equalsIgnoreCase(orderType) && !"DESC".equalsIgnoreCase(orderType)) {
            throw new IllegalArgumentException("orderType must be ASC or DESC");
        }
        return orderType.toUpperCase(Locale.ROOT);
    }

    private int requirePositiveInt(final Integer value, final String name) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException(name + " must be positive");
        }
        return value;
    }

    private List<?> getList(final Object value) {
        if (value == null) {
            return Collections.emptyList();
        }
        if (!(value instanceof List<?>)) {
            throw new IllegalArgumentException("fields must be a list");
        }
        return (List<?>) value;
    }

    private Map<?, ?> getMap(final Object value) {
        if (value == null) {
            return Collections.emptyMap();
        }
        if (!(value instanceof Map<?, ?>)) {
            throw new IllegalArgumentException("conditions must be a map");
        }
        return (Map<?, ?>) value;
    }

    private Map<?, ?> getRequiredMap(final Object value, final String name) {
        if (!(value instanceof Map<?, ?>) || ((Map<?, ?>) value).isEmpty()) {
            throw new IllegalArgumentException(name + " cannot be empty");
        }
        return (Map<?, ?>) value;
    }
}

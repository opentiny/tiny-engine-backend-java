package com.tinyengine.it.dynamic.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.tinyengine.it.common.context.LoginUserContext;
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.common.utils.SqlIdentifierValidator;
import com.tinyengine.it.dynamic.dto.DynamicDelete;
import com.tinyengine.it.dynamic.dto.DynamicInsert;
import com.tinyengine.it.dynamic.dto.DynamicQuery;
import com.tinyengine.it.dynamic.dto.DynamicUpdate;
import com.tinyengine.it.model.dto.ParametersDto;
import com.tinyengine.it.model.entity.Model;
import com.tinyengine.it.service.material.ModelService;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@SuppressWarnings({
    "PMD.AppendCharacterWithChar",
    "PMD.AvoidDuplicateLiterals",
    "PMD.AvoidReassigningParameters",
    "PMD.CognitiveComplexity",
    "PMD.ConfusingTernary",
    "PMD.ConsecutiveAppendsShouldReuse",
    "PMD.ConsecutiveLiteralAppends",
    "PMD.CyclomaticComplexity",
    "PMD.DataflowAnomalyAnalysis",
    "PMD.ExcessiveImports",
    "PMD.GodClass",
    "PMD.ImplicitSwitchFallThrough",
    "PMD.InefficientStringBuffering",
    "PMD.InsufficientStringBufferDeclaration",
    "PMD.LawOfDemeter",
    "PMD.LinguisticNaming",
    "PMD.LiteralsFirstInComparisons",
    "PMD.LocalVariableCouldBeFinal",
    "PMD.MethodArgumentCouldBeFinal",
    "PMD.ModifiedCyclomaticComplexity",
    "PMD.NPathComplexity",
    "PMD.OnlyOneReturn",
    "PMD.PositionLiteralsFirstInComparisons",
    "PMD.PrematureDeclaration",
    "PMD.ShortVariable",
    "PMD.StdCyclomaticComplexity",
    "PMD.TooManyMethods",
    "PMD.UnusedPrivateMethod",
    "PMD.UseStringBufferForStringAppends"
})
@SuppressFBWarnings(
        value = "EI_EXPOSE_REP2",
        justification = "The constructor stores Spring-managed collaborators without exposing them.")
public class DynamicModelService {

    private static final Set<String> SYSTEM_FIELDS =
            Set.of("id", "created_at", "updated_at", "deleted_at", "created_by", "updated_by");
    private static final int DEFAULT_VARCHAR = 255;
    private static final int MAX_VARCHAR = 65_535;
    private static final int ASC_SUFFIX_LEN = 4;
    private static final int DESC_SUFFIX_LEN = 5;

    private enum AlterOperationType {
        ADD,
        MODIFY,
        DROP
    }

    private record AlterOperation(
            AlterOperationType type,
            String columnName,
            ParametersDto parameter,
            String columnType,
            String afterColumn) {
    }

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final LoginUserContext loginUserContext;
    private final ModelService modelService;

    public DynamicModelService(
            JdbcTemplate jdbcTemplate,
            NamedParameterJdbcTemplate namedJdbcTemplate,
            LoginUserContext loginUserContext,
            @Lazy ModelService modelService) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedJdbcTemplate = namedJdbcTemplate;
        this.loginUserContext = loginUserContext;
        this.modelService = modelService;
    }

    /** 创建动态表 */
    @Transactional
    public void createDynamicTable(Model modelMetadata) {
        if (modelMetadata.getParameters() == null || modelMetadata.getParameters().isEmpty()) {
            throw new ServiceException(
                    ExceptionEnum.CM001.getResultCode(),
                    "Model parameters cannot be null or empty");
        }
        String tableName = getTableName(modelMetadata.getNameEn());
        String sql = generateCreateTableSQL(tableName, modelMetadata);

        log.info("createDynamicTable SQL: \n{}", sql);

        try {
            jdbcTemplate.execute(sql);
            log.info("createDynamicTable ok: {}", tableName);

        } catch (DataAccessException exception) {
            log.error("createDynamicTable failed: {}", tableName, exception);
            throw new ServiceException(
                    ExceptionEnum.CM001.getResultCode(),
                    ExceptionEnum.CM001.getResultCode(),
                    exception);
        }
    }

    private String generateDropTableSQL(String tableName) {
        SqlIdentifierValidator.validate(tableName);
        StringBuilder sql = new StringBuilder();
        sql.append("DROP TABLE IF EXISTS ").append(tableName).append(";");
        return sql.toString();
    }

    public void dropDynamicTable(Model modelMetadata) {
        if (modelMetadata == null
                || modelMetadata.getNameEn() == null
                || modelMetadata.getNameEn().isEmpty()) {
            throw new IllegalArgumentException(
                    "Model metadata or table name cannot be null or empty");
        }
        String tableName = getTableName(modelMetadata.getNameEn());

        String sql = generateDropTableSQL(tableName);
        try {
            jdbcTemplate.execute(sql);
            log.info("Successfully dropped table: {}", tableName);
        } catch (DataAccessException exception) {
            log.error("Failed to drop table: {}", tableName, exception);
            throw new ServiceException(
                    ExceptionEnum.CM001.getResultCode(),
                    ExceptionEnum.CM001.getResultCode(),
                    exception);
        }
    }

    /**
     * 生成创建表的SQL.
     *
     * @return SQL used to create the dynamic table
     */
    private String generateCreateTableSQL(String tableName, Model model) {
        SqlIdentifierValidator.validate(tableName);
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS ").append(tableName).append(" (\n");

        // 基础字段
        List<String> columns = new ArrayList<>();
        columns.add("id INT PRIMARY KEY AUTO_INCREMENT");

        // 用户定义字段
        for (ParametersDto field : model.getParameters()) {
            if (!Objects.equals(field.getProp(), "id")) {
                SqlIdentifierValidator.validate(field.getProp());
                String columnDef = generateColumnDefinition(field, "init");
                columns.add(columnDef);
            }
        }
        // 基础字段
        columns.add("created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP");
        columns.add("updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP");
        columns.add("deleted_at TIMESTAMP NULL");
        columns.add("created_by INT NOT NULL");
        columns.add("updated_by INT NOT NULL");

        sql.append(String.join(",\n", columns));
        sql.append("\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci");

        return sql.toString();
    }

    public void initializeDynamicTable(Model model, Long userId) {
        if (model == null || CollectionUtils.isEmpty(model.getParameters())) {
            throw new IllegalArgumentException("Model or parameters cannot be null or empty");
        }

        String tableName = getTableName(model.getNameEn());
        List<ParametersDto> parameters = model.getParameters();

        // Prepare columns and values
        List<String> columns = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        for (ParametersDto param : parameters) {
            String columnName = param.getProp();
            SqlIdentifierValidator.validate(columnName);
            String fieldType = param.getType();
            param.setDefaultValue("1");
            String value = param.getDefaultValue();

            if (value == null && Boolean.TRUE.equals(param.getRequired())) {
                throw new IllegalArgumentException(
                        "Missing required parameter defaultValue: " + columnName);
            }

            columns.add(columnName);
            values.add(convertValueByType(value, fieldType, columnName));
        }

        // Add system fields
        columns.add("created_by");
        columns.add("updated_by");
        values.add(userId);
        values.add(userId);

        // Construct SQL
        String sql =
                String.format(
                        "INSERT INTO %s (%s) VALUES (%s)",
                        tableName,
                        String.join(", ", columns),
                        columns.stream().map(c -> "?").collect(Collectors.joining(", ")));

        // Execute SQL
        jdbcTemplate.update(sql, values.toArray());
    }

    /**
     * 通用查询方法，避免TypeHandler冲突.
     *
     * @return query results
     */
    public List<Map<String, Object>> dynamicQuery(
            String tableName,
            List<String> fields,
            Map<String, Object> conditions,
            String orderBy,
            Integer limit) {

        SqlIdentifierValidator.validate(tableName);
        SqlIdentifierValidator.validateAll(fields);
        if (conditions != null && !conditions.isEmpty()) {
            for (String key : conditions.keySet()) {
                SqlIdentifierValidator.validate(key);
            }
        }
        if (orderBy != null && !orderBy.isEmpty()) {
            validateOrderBy(orderBy);
        }

        // 1. 构建SQL
        StringBuilder sql = new StringBuilder("SELECT ");

        if (fields != null && !fields.isEmpty()) {
            sql.append(String.join(", ", fields));
        } else {
            sql.append("*");
        }

        sql.append(" FROM ").append(tableName);

        // 2. 构建WHERE条件
        if (conditions != null && !conditions.isEmpty()) {
            List<String> whereClauses = new ArrayList<>();
            boolean whereAdded = false;
            getWhereCondition(conditions, sql, whereAdded, whereClauses);
        }

        // 3. 排序
        if (orderBy != null && !orderBy.isEmpty()) {
            sql.append(" ORDER BY ").append(orderBy);
        }

        // 4. 分页
        if (limit != null && limit > 0) {
            sql.append(" LIMIT ").append(limit);
        }

        // 5. 执行查询
        if (conditions != null && !conditions.isEmpty()) {
            return namedJdbcTemplate.queryForList(sql.toString(), conditions);
        } else {
            return jdbcTemplate.queryForList(sql.toString());
        }
    }

    public List<Map<String, Object>> dynamicCount(
            String tableName, Map<String, Object> conditions) {

        // 1. 构建SQL
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) as count");

        sql.append(" FROM ").append(tableName);

        // 2. 构建WHERE条件
        if (conditions != null && !conditions.isEmpty()) {
            boolean whereAdded = false;
            List<String> whereClauses = new ArrayList<>();
            getWhereCondition(conditions, sql, whereAdded, whereClauses);
        }

        // 5. 执行查询
        if (conditions != null && !conditions.isEmpty()) {
            return namedJdbcTemplate.queryForList(sql.toString(), conditions);
        } else {
            return jdbcTemplate.queryForList(sql.toString());
        }
    }

    private void getWhereCondition(
            Map<String, Object> conditions,
            StringBuilder sql,
            boolean whereAdded,
            List<String> whereClauses) {
        for (Map.Entry<String, Object> entry : conditions.entrySet()) {
            if (entry.getValue() != null) {
                whereAdded = true;
                whereClauses.add(entry.getKey() + " = :" + entry.getKey());
            }
        }
        if (whereAdded) {
            sql.append(" WHERE ");
            sql.append(String.join(" AND ", whereClauses));
        }
    }

    /**
     * 查询总数.
     *
     * @return record count
     */
    public Long count(String tableName, Map<String, Object> conditions) {
        List<Map<String, Object>> result = dynamicCount(tableName, conditions);
        return Long.parseLong(result.get(0).get("count").toString());
    }

    /**
     * 分页查询.
     *
     * @return paginated query result
     */
    public Map<String, Object> queryWithPage(DynamicQuery dto) {
        String tableName = getTableName(dto.getNameEn());
        List<String> fields = dto.getFields();
        Map<String, Object> conditions = dto.getParams();
        String orderBy = dto.getOrderBy();
        Integer pageNum = dto.getCurrentPage();
        Integer pageSize = dto.getPageSize();

        validateQueryFields(dto);

        // 计算分页
        Integer limit = null;
        if (pageNum != null && pageSize != null) {
            limit = pageSize;
        }

        // 执行查询
        List<Map<String, Object>> data =
                dynamicQuery(tableName, fields, conditions, orderBy, limit);
        Long count = count(tableName, conditions);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", data);
        result.put("total", count);

        return result;
    }

    private Set<String> getAllowedFields(String nameEn) {
        List<Model> modelList = modelService.getModelByEnName(nameEn);
        if (modelList == null || modelList.isEmpty()) {
            return Collections.emptySet();
        }
        Model model = modelList.get(0);
        Set<String> allowed = new HashSet<>(SYSTEM_FIELDS);
        if (model.getParameters() != null) {
            for (Object param : model.getParameters()) {
                String prop = extractProp(param);
                if (prop != null) {
                    allowed.add(prop);
                }
            }
        }
        return allowed;
    }

    @SuppressWarnings("unchecked")
    private String extractProp(Object param) {
        if (param instanceof ParametersDto) {
            return ((ParametersDto) param).getProp();
        }
        if (param instanceof Map) {
            Object value = ((Map<String, Object>) param).get("prop");
            return value != null ? value.toString() : null;
        }
        return null;
    }

    private void validateQueryFields(DynamicQuery dto) {
        Set<String> allowedFields = getAllowedFields(dto.getNameEn());

        if (dto.getFields() != null && !dto.getFields().isEmpty()) {
            for (String field : dto.getFields()) {
                SqlIdentifierValidator.validate(field);
                if (!allowedFields.contains(field)) {
                    throw new IllegalArgumentException("不允许的字段: " + field);
                }
            }
        }

        if (dto.getOrderBy() != null && !dto.getOrderBy().isEmpty()) {
            SqlIdentifierValidator.validate(dto.getOrderBy());
            if (!allowedFields.contains(dto.getOrderBy())) {
                throw new IllegalArgumentException("不允许的排序字段: " + dto.getOrderBy());
            }
        }

        if (dto.getOrderType() != null) {
            SqlIdentifierValidator.validateOrderType(dto.getOrderType());
        }
    }

    private Object convertValueByType(Object value, String fieldType, String columnName) {
        try {
            return switch (fieldType) {
                case "String" -> value != null ? value.toString() : null;
                case "Number" -> value != null ? Integer.parseInt(value.toString()) : null;
                case "Boolean" -> value != null ? Boolean.parseBoolean(value.toString()) : null;
                case "Date" ->
                        value != null
                                ? Date.valueOf(value.toString())
                                : null; // Assume proper date formatting is handled elsewhere
                case "DateTime" -> value; // Assume proper date formatting is handled elsewhere
                case "Enum" -> value; // Validation for enums should be handled before this
                default -> value;
            };
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Invalid value for field: " + columnName, exception);
        }
    }

    @Transactional
    public void modifyTableStructure(Model model) {
        String tableName = getTableName(model.getNameEn());
        SqlIdentifierValidator.validate(tableName);
        List<ParametersDto> parameters = model.getParameters();
        if (parameters == null || parameters.isEmpty()) {
            throw new IllegalArgumentException("Model parameters cannot be null or empty");
        }

        // Fetch existing table structure
        String fetchColumnsSql =
                "SELECT COLUMN_NAME, DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME ="
                        + " ?";
        List<Map<String, String>> existingColumns =
                jdbcTemplate.query(
                        fetchColumnsSql,
                        new Object[] {tableName},
                        (rs, rowNum) -> {
                            Map<String, String> column = new HashMap<>();
                            column.put("COLUMN_NAME", rs.getString("COLUMN_NAME"));
                            column.put("DATA_TYPE", rs.getString("DATA_TYPE"));
                            return column;
                        });

        Map<String, String> existingColumnMap =
                existingColumns.stream()
                        .collect(
                                Collectors.toMap(
                                        col -> col.get("COLUMN_NAME"),
                                        col -> col.get("DATA_TYPE")));

        // Generate ALTER TABLE statements
        List<AlterOperation> alterOperations = new ArrayList<>();
        // Add or modify columns based on parameters
        for (int i = 0; i < parameters.size(); i++) {
            String afterColumn = null;
            if (i > 0) {
                afterColumn = parameters.get(i - 1).getProp();
                SqlIdentifierValidator.validate(afterColumn);
            }
            ParametersDto param = parameters.get(i);
            String columnName = param.getProp();
            SqlIdentifierValidator.validate(columnName);
            String columnType = mapJavaTypeToSQL(param.getType());

            if (!existingColumnMap.containsKey(columnName)) {
                // Add new column
                alterOperations.add(
                        new AlterOperation(
                                AlterOperationType.ADD,
                                columnName,
                                param,
                                null,
                                afterColumn));
            } else if (!existingColumnMap.get(columnName).equalsIgnoreCase(columnType)) {
                // Modify existing column
                alterOperations.add(
                        new AlterOperation(
                                AlterOperationType.MODIFY,
                                columnName,
                                null,
                                columnType,
                                null));
            }
        }

        addCommonFields(parameters);

        // Drop columns that are not in the parameters
        for (String existingColumn : existingColumnMap.keySet()) {
            SqlIdentifierValidator.validate(existingColumn);
            if (parameters.stream().noneMatch(param -> param.getProp().equals(existingColumn))) {
                alterOperations.add(
                        new AlterOperation(
                                AlterOperationType.DROP,
                                existingColumn,
                                null,
                                null,
                                null));
            }
        }

        // Execute ALTER TABLE statements
        for (AlterOperation operation : alterOperations) {
            executeAlterTableStatement(tableName, operation);
        }
    }

    private void executeAlterTableStatement(
            final String tableName, final AlterOperation operation) {
        String safeTable = requireAlterIdentifier(tableName, "tableName");
        String sql;
        switch (operation.type()) {
            case ADD:
                sql = buildAddColumnSql(operation, safeTable);
                break;
            case MODIFY:
                sql = buildModifyColumnSql(operation, safeTable);
                break;
            case DROP:
                sql =
                        "ALTER TABLE "
                                + safeTable
                                + " DROP COLUMN "
                                + requireAlterIdentifier(operation.columnName(), "columnName");
                break;
            default:
                throw new IllegalArgumentException("Unsupported ALTER TABLE operation");
        }
        jdbcTemplate.execute(sql);
    }

    private String buildAddColumnSql(
            final AlterOperation operation, final String tableName) {
        requireAlterIdentifier(operation.columnName(), "columnName");
        final String columnDefinition = generateColumnDefinition(operation.parameter(), "add");
        final String afterColumn = operation.afterColumn();
        return afterColumn == null
                ? "ALTER TABLE " + tableName + " " + columnDefinition
                : "ALTER TABLE "
                        + tableName
                        + " "
                        + columnDefinition
                        + " AFTER "
                        + requireAlterIdentifier(afterColumn, "afterColumn");
    }

    private String buildModifyColumnSql(
            final AlterOperation operation, final String tableName) {
        return "ALTER TABLE "
                + tableName
                + " MODIFY COLUMN "
                + requireAlterIdentifier(operation.columnName(), "columnName")
                + " "
                + requireColumnType(operation.columnType());
    }

    private String requireAlterIdentifier(final String value, final String name) {
        if (value == null || !value.matches("^[A-Za-z_][A-Za-z0-9_]*$")) {
            throw new IllegalArgumentException(name + " must be a valid SQL identifier");
        }
        return value;
    }

    private String requireColumnType(final String value) {
        if (value == null
                || !(value.equals("INT")
                        || value.equals("TINYINT")
                        || value.equals("DATE")
                        || value.equals("DATETIME")
                        || value.equals("VARCHAR")
                        || value.equals("ENUM")
                        || value.equals("TEXT")
                        || value.matches("^VARCHAR\\([1-9][0-9]{0,4}\\)$"))) {
            throw new IllegalArgumentException("Invalid SQL column type");
        }
        return value;
    }

    private void addCommonFields(List<ParametersDto> parameters) {
        ParametersDto id = new ParametersDto();
        id.setProp("id");
        parameters.add(id);
        ParametersDto createdAt = new ParametersDto();
        createdAt.setProp("created_at");
        parameters.add(createdAt);
        ParametersDto updatedAt = new ParametersDto();
        updatedAt.setProp("updated_at");
        parameters.add(updatedAt);
        ParametersDto deletedAt = new ParametersDto();
        deletedAt.setProp("deleted_at");
        parameters.add(deletedAt);
        ParametersDto createdBy = new ParametersDto();
        createdBy.setProp("created_by");
        parameters.add(createdBy);
        ParametersDto updatedBy = new ParametersDto();
        updatedBy.setProp("updated_by");
        parameters.add(updatedBy);
    }

    private static String mapJavaTypeToSQL(String javaType) {
        if (javaType == null) {
            return "VARCHAR"; // 默认处理
        }
        return switch (javaType) {
            case "String", "ModelRef" -> "VARCHAR";
            case "Integer", "Number" -> "INT";
            case "Boolean" -> "TINYINT";
            case "Date" -> "DATE";
            case "DateTime" -> "DATETIME";
            case "Enum" -> "ENUM";
            default -> "TEXT"; // 默认处理
        };
    }

    /**
     * 生成字段定义.
     *
     * @return column definition SQL
     */
    private String generateColumnDefinition(ParametersDto field, String type) {
        StringBuilder sb = new StringBuilder();
        if (type.equals("add")) {
            sb.append("ADD COLUMN ");
        } else if (type.equals("modify")) {
            sb.append("MODIFY COLUMN ");
        }
        String columnName = SqlIdentifierValidator.requireValidIdentifier(field.getProp());
        sb.append(columnName).append(" ");

        // 映射数据类型
        if (field.getType() == null) {
            sb.append("VARCHAR(").append(DEFAULT_VARCHAR).append(")");
        } else {
            switch (field.getType()) {
                case "String":
                    int maxLength =
                            field.getMaxLength() != null ? field.getMaxLength() : DEFAULT_VARCHAR;
                    if (maxLength <= 0 || maxLength > MAX_VARCHAR) {
                        throw new IllegalArgumentException("Invalid VARCHAR length");
                    }
                    sb.append("VARCHAR(").append(maxLength).append(")");
                    break;
                case "Integer", "Number":
                    sb.append("INT");
                    break;
                case "Boolean":
                    sb.append("TINYINT(1)");
                    break;
                case "Date":
                    sb.append("DATE");
                    break;
                case "DateTime":
                    sb.append("DATETIME");
                    break;
                case "Enum":
                    sb.append("ENUM")
                            .append("(")
                            .append(getEnumOptions(field.getOptions()))
                            .append(")");
                    break;
                case "ModelRef":
                    sb.append("VARCHAR(255)"); // 存储JSON字符串，长度可根据实际需求调整
                    break;
                default:
                    sb.append("TEXT");
            }
        }

        if (Boolean.TRUE.equals(field.getRequired())) {
            sb.append(" NOT NULL");
        }

        if (field.getDefaultValue() != null) {
            sb.append(" DEFAULT '")
                    .append(SqlIdentifierValidator.escapeSqlLiteral(field.getDefaultValue()))
                    .append("'");
        }
        if (field.getDescription() != null && !field.getDescription().isEmpty()) {
            sb.append(" COMMENT '")
                    .append(SqlIdentifierValidator.escapeSqlLiteral(field.getDescription()))
                    .append("'");
        }

        return sb.toString();
    }

    private String getEnumOptions(String optionStr) {
        List<String> options = new ArrayList<>();
        if (optionStr == null || optionStr.isBlank()) {
            throw new IllegalArgumentException("Enum options cannot be null or empty");
        }
        JSONArray jsonList;
        try {
            jsonList = JSON.parseArray(optionStr);
        } catch (JSONException exception) {
            throw new IllegalArgumentException(
                    "Invalid enum options format, expected JSON array string", exception);
        }
        for (int i = 0; i < jsonList.size(); i++) {
            String value = jsonList.getJSONObject(i).getString("value");
            if (value == null || value.contains("\r") || value.contains("\n")) {
                throw new IllegalArgumentException("Invalid enum option");
            }
            options.add(SqlIdentifierValidator.escapeSqlLiteral(value));
        }

        return options.stream().map(opt -> "'" + opt + "'").collect(Collectors.joining(", "));
    }

    /** 验证表和数据 */
    /**
     * 创建数据.
     *
     * @return created record data
     */
    public Map<String, Object> createData(DynamicInsert dataDto) {

        String tableName = getTableName(dataDto.getNameEn());
        Map<String, Object> record = new HashMap<>(dataDto.getParams());
        for (String col : record.keySet()) {
            SqlIdentifierValidator.validate(col);
        }
        String userId = loginUserContext.getLoginUserId();
        // 添加系统字段
        record.put("created_by", userId);
        record.put("updated_by", userId);

        // 构建SQL
        String columns = String.join(", ", record.keySet());
        String placeholders =
                record.keySet().stream().map(k -> "?").collect(Collectors.joining(", "));

        String sql =
                String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, columns, placeholders);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int[] parameterTypes = new int[record.size()];
        Arrays.fill(parameterTypes, SqlTypeValue.TYPE_UNKNOWN);
        PreparedStatementCreatorFactory creatorFactory =
                new PreparedStatementCreatorFactory(sql, parameterTypes);
        creatorFactory.setReturnGeneratedKeys(true);

        jdbcTemplate.update(
                creatorFactory.newPreparedStatementCreator(record.values().toArray()),
                keyHolder);

        Long generatedId = keyHolder.getKey() != null ? keyHolder.getKey().longValue() : null;

        if (generatedId != null) {
            record.put("id", generatedId);
        }

        return record;
    }

    /**
     * 获取表名.
     *
     * @return validated dynamic table name
     */
    private String getTableName(String modelId) {
        if (modelId == null || modelId.isBlank()) {
            throw new IllegalArgumentException("Model name cannot be null or empty");
        }
        String tableName = "dynamic_" + modelId.toLowerCase(Locale.ROOT);
        return SqlIdentifierValidator.requireValidIdentifier(tableName);
    }

    private void validateOrderBy(String orderBy) {
        String trimmed = orderBy.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Invalid order by: " + orderBy);
        }
        String upper = trimmed.toUpperCase(Locale.ROOT);
        String identifier = trimmed;
        if (upper.endsWith(" ASC")) {
            identifier = trimmed.substring(0, trimmed.length() - ASC_SUFFIX_LEN).trim();
        } else if (upper.endsWith(" DESC")) {
            identifier = trimmed.substring(0, trimmed.length() - DESC_SUFFIX_LEN).trim();
        }
        SqlIdentifierValidator.validate(identifier);
    }

    public Map<String, Object> getDataById(String modelId, Long id) {
        String tableName = getTableName(modelId);
        String sql = "SELECT * FROM " + tableName + " WHERE id = ?";

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, id);

        return results.isEmpty() ? Collections.emptyMap() : results.get(0);
    }

    public Map<String, Object> updateDateById(DynamicUpdate dto) {
        String modelId = dto.getNameEn();
        Map<String, Object> params1 = dto.getParams();
        if (params1 == null || !params1.containsKey("id")) {
            throw new IllegalArgumentException("更新操作必须指定ID");
        }
        if (dto.getData() == null || dto.getData().isEmpty()) {
            throw new IllegalArgumentException("更新操作必须指定更新数据");
        }
        if (modelId == null || modelId.isBlank()) {
            throw new IllegalArgumentException("模型ID不能为空");
        }
        Long id = Long.parseLong(params1.get("id").toString());
        Map<String, Object> updateFields = dto.getData();
        for (String col : updateFields.keySet()) {
            SqlIdentifierValidator.validate(col);
        }
        String tableName = getTableName(modelId);
        StringBuilder sql = new StringBuilder("UPDATE " + tableName + " SET ");
        List<Object> params = new ArrayList<>();

        for (Map.Entry<String, Object> entry : updateFields.entrySet()) {
            sql.append(entry.getKey()).append(" = ?, ");
            params.add(entry.getValue());
        }

        // 去掉最后的逗号和空格
        sql.setLength(sql.length() - 2);
        sql.append(" WHERE id = ?");
        params.add(id);

        int rowsAffected = jdbcTemplate.update(sql.toString(), params.toArray());

        Map<String, Object> result = new HashMap<>();
        result.put("rowsAffected", rowsAffected);
        return result;
    }

    public Map<String, Object> deleteDataById(DynamicDelete dto) {
        String modelId = dto.getNameEn();
        if (modelId == null || modelId.isBlank()) {
            throw new IllegalArgumentException("模型ID不能为空");
        }
        if (dto.getId() == null) {
            throw new IllegalArgumentException("删除操作必须指定ID");
        }
        Long id = Long.valueOf(dto.getId());

        String tableName = getTableName(modelId);
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";
        int update = jdbcTemplate.update(sql, id);
        Map<String, Object> result = new HashMap<>();
        result.put("rowsAffected", update);
        return result;
    }
}

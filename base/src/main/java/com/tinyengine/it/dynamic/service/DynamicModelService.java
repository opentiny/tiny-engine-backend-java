package com.tinyengine.it.dynamic.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.tinyengine.it.common.context.LoginUserContext;
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.dynamic.dto.DynamicDelete;
import com.tinyengine.it.dynamic.dto.DynamicInsert;
import com.tinyengine.it.dynamic.dto.DynamicQuery;
import com.tinyengine.it.dynamic.dto.DynamicUpdate;
import com.tinyengine.it.model.dto.ParametersDto;
import com.tinyengine.it.model.entity.Model;
import lombok.extern.slf4j.Slf4j;

import com.tinyengine.it.common.utils.SqlIdentifierValidator;
import com.tinyengine.it.service.material.ModelService;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class DynamicModelService {

	private static final Set<String> SYSTEM_FIELDS = Set.of(
		"id", "created_at", "updated_at", "deleted_at", "created_by", "updated_by"
	);

	private final JdbcTemplate jdbcTemplate;
	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	private final LoginUserContext loginUserContext;
	private final ModelService modelService;

	public DynamicModelService(JdbcTemplate jdbcTemplate,
	                           NamedParameterJdbcTemplate namedParameterJdbcTemplate,
	                           LoginUserContext loginUserContext,
	                           @Lazy ModelService modelService) {
		this.jdbcTemplate = jdbcTemplate;
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
		this.loginUserContext = loginUserContext;
		this.modelService = modelService;
	}


	/**
	 * 创建动态表
	 */
	@Transactional
	public void createDynamicTable(Model modelMetadata)  {
		if(modelMetadata.getParameters()==null || modelMetadata.getParameters().isEmpty()){
			throw new ServiceException(ExceptionEnum.CM001.getResultCode(), "Model parameters cannot be null or empty");

		}
		String tableName = getTableName(modelMetadata.getNameEn());
		String sql = generateCreateTableSQL(tableName, modelMetadata);

		log.info("createDynamicTable SQL: \n{}", sql);

		try {
			jdbcTemplate.execute(sql);
			log.info("createDynamicTable ok: {}", tableName);

		} catch (Exception e) {
			log.error("createDynamicTable failed: {}", tableName, e);
			throw new ServiceException(ExceptionEnum.CM001.getResultCode(), ExceptionEnum.CM001.getResultCode());

		}
	}
    private String generateDropTableSQL(String tableName) {
	    if (tableName == null || tableName.isEmpty()) {
		    throw new IllegalArgumentException("Table name cannot be null or empty");
	    }

	    // Validate table name to prevent SQL injection
	    if (!tableName.matches("^[a-zA-Z0-9_]+$")) {
		    throw new IllegalArgumentException("Invalid table name: " + tableName);
	    }
	    StringBuilder sql = new StringBuilder();
	    sql.append("DROP TABLE IF EXISTS ").append(tableName).append(";");
	    return sql.toString();
	}

	public void dropDynamicTable(Model modelMetadata) {
		if (modelMetadata == null || modelMetadata.getNameEn() == null || modelMetadata.getNameEn().isEmpty()) {
			throw new IllegalArgumentException("Model metadata or table name cannot be null or empty");
		}
		String tableName = getTableName(modelMetadata.getNameEn());

		String sql = generateDropTableSQL(tableName);
		try {
			jdbcTemplate.execute(sql);
			log.info("Successfully dropped table: {}", tableName);
		} catch (Exception e) {
			log.error("Failed to drop table: {}", tableName, e);
			throw new ServiceException(ExceptionEnum.CM001.getResultCode(), ExceptionEnum.CM001.getResultCode());

		}
	}
	/**
	 * 生成创建表的SQL
	 */
	private String generateCreateTableSQL(String tableName, Model model) {
		StringBuilder sql = new StringBuilder();
		sql.append("CREATE TABLE IF NOT EXISTS ").append(tableName).append(" (\n");

		// 基础字段
		List<String> columns = new ArrayList<>();
		columns.add("id INT PRIMARY KEY AUTO_INCREMENT");


		// 用户定义字段
		for (ParametersDto field : model.getParameters()) {
			if(!Objects.equals(field.getProp(), "id")){
				String columnDef = generateColumnDefinition(field,"init");
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
			String fieldType = param.getType();
			param.setDefaultValue("1");
			String value = param.getDefaultValue();

			if (value == null && Boolean.TRUE.equals(param.getRequired())) {
				throw new IllegalArgumentException("Missing required parameter defaultValue: " + columnName);
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
		String sql = String.format(
			"INSERT INTO %s (%s) VALUES (%s)",
			tableName,
			String.join(", ", columns),
			columns.stream().map(c -> "?").collect(Collectors.joining(", "))
		);

		// Execute SQL
		jdbcTemplate.update(sql, values.toArray());
	}





	/**
	 * 通用查询方法，避免TypeHandler冲突
	 */
	public List<Map<String, Object>> dynamicQuery(String tableName,
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
			SqlIdentifierValidator.validate(orderBy.replaceAll("(?i)\\s+(ASC|DESC)$", ""));
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
			return namedParameterJdbcTemplate.queryForList(sql.toString(), conditions);
		} else {
			return jdbcTemplate.queryForList(sql.toString());
		}
	}

	public List<Map<String, Object>> dynamicCount(String tableName, Map<String, Object> conditions) {

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
			return namedParameterJdbcTemplate.queryForList(sql.toString(), conditions);
		} else {
			return jdbcTemplate.queryForList(sql.toString());
		}
	}

	private void getWhereCondition(Map<String, Object> conditions, StringBuilder sql, boolean whereAdded, List<String> whereClauses) {
		for (Map.Entry<String, Object> entry : conditions.entrySet()) {
			if (entry.getValue() != null) {
				whereAdded = true;
				whereClauses.add(entry.getKey() + " = :" + entry.getKey());
			}
		}
		if(whereAdded){
			sql.append(" WHERE ");
			sql.append(String.join(" AND ", whereClauses));

		}
	}

	/**
	 * 查询总数
	 */
	public Long count(String tableName, Map<String, Object> conditions) {
		List<Map<String, Object>> result = dynamicCount(tableName, conditions);
		return Long.parseLong(result.get(0).get("count").toString());
	}
	/**
	 * 分页查询
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
		List<Map<String, Object>> data = dynamicQuery(
			tableName, fields, conditions, orderBy, limit);
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
			switch (fieldType) {
				case "String":
					return value != null ? value.toString() : null;
				case "Number":
					return value != null ? Integer.parseInt(value.toString()) : null;
				case "Boolean":
					return value != null ? Boolean.parseBoolean(value.toString()) : null;
				case "Date":
					return value !=null ? Date.valueOf(value.toString()):null; // Assume proper date formatting is handled elsewhere
				case "DateTime":
					return value; // Assume proper date formatting is handled elsewhere
				case "Enum":
					return value; // Validation for enums should be handled before this
				default:
					return value;
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid value for field: " + columnName, e);
		}
	}
	@Transactional
	public void modifyTableStructure(Model model) {
		String tableName = getTableName(model.getNameEn());
		List<ParametersDto> parameters = model.getParameters();
		if(parameters == null || parameters.isEmpty()){
			throw new IllegalArgumentException("Model parameters cannot be null or empty");
		}

		// Fetch existing table structure
		String fetchColumnsSql = "SELECT COLUMN_NAME, DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = ?";
		List<Map<String, String>> existingColumns = jdbcTemplate.query(fetchColumnsSql, new Object[]{tableName}, (rs, rowNum) -> {
			Map<String, String> column = new HashMap<>();
			column.put("COLUMN_NAME", rs.getString("COLUMN_NAME"));
			column.put("DATA_TYPE", rs.getString("DATA_TYPE"));
			return column;
		});

		Map<String, String> existingColumnMap = existingColumns.stream()
			.collect(Collectors.toMap(col -> col.get("COLUMN_NAME"), col -> col.get("DATA_TYPE")));

		// Generate ALTER TABLE statements
		List<String> alterStatements = new ArrayList<>();
       // Add or modify columns based on parameters
		for (int i = 0; i < parameters.size(); i++) {
			String afterColumn = null;
			if(i>0){
				afterColumn = parameters.get(i - 1).getProp();
			}
			ParametersDto param = parameters.get(i);
			String columnName = param.getProp();
			String columnType = mapJavaTypeToSQL(param.getType());

			if (!existingColumnMap.containsKey(columnName)) {
				// Add new column
				String addSql = generateColumnDefinition(param,"add");
				if(afterColumn != null){
					addSql += " AFTER " + afterColumn;
				}
				alterStatements.add(addSql);
			} else if (!existingColumnMap.get(columnName).equalsIgnoreCase(columnType)) {
				// Modify existing column
				alterStatements.add(String.format("MODIFY COLUMN %s %s", columnName, columnType));
			}
		}

		addCommonFields(parameters);

		// Drop columns that are not in the parameters
		for (String existingColumn : existingColumnMap.keySet()) {
			if (parameters.stream().noneMatch(param -> param.getProp().equals(existingColumn))) {
				alterStatements.add(String.format("DROP COLUMN %s", existingColumn));
			}
		}

		// Execute ALTER TABLE statements
		for (String alterStatement : alterStatements) {
			String sql = String.format("ALTER TABLE %s %s", tableName, alterStatement);
			jdbcTemplate.execute(sql);
		}
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
			return "VARCHAR(255)"; // 默认处理
		}
		switch (javaType) {
			case "String", "ModelRef":
				return "VARCHAR";
			case "Number":
				return "INT";
			case "Boolean":
				return "TINYINT";
			case "Date":
				return "TIMESTAMP";
			case "Enum":
				return "Enum";
			default:
				return "TEXT"; // 默认处理
		}
	}


	/**
	 * 生成字段定义
	 */
	private String generateColumnDefinition(ParametersDto field,String type) {
		StringBuilder sb = new StringBuilder();
		if(type.equals("add")) {
			sb.append("ADD COLUMN ");
		} else if(type.equals("modify")){
			sb.append("MODIFY COLUMN ");
		}
		sb.append(field.getProp()).append(" ");

		// 映射数据类型
		switch (field.getType()) {
			case "String":
				int maxLength = field.getMaxLength() != null ? field.getMaxLength() : 255;
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
				sb.append("ENUM").append("(").append(getEnumOptions(field.getOptions())).append(")");
				break;
			case "ModelRef":
				sb.append("VARCHAR(255)"); // 存储JSON字符串，长度可根据实际需求调整
				break;
			default:
				sb.append("TEXT");
		}

		if (Boolean.TRUE.equals(field.getRequired())) {
			sb.append(" NOT NULL");
		}

		if (field.getDefaultValue() != null) {
			sb.append(" DEFAULT '").append(field.getDefaultValue()).append("'");
		}
		if(field.getDescription()!=null && !field.getDescription().isEmpty()){
			sb.append(" COMMENT '").append(field.getDescription()).append("'");
		}

		return sb.toString();
	}

	private String getEnumOptions(String optionStr) {
		List<String> options= new ArrayList<>();
		if(optionStr == null || optionStr.trim().isEmpty()){
			throw new IllegalArgumentException("Enum options cannot be null or empty");
		}
		JSONArray jsonList;
		try {
			 jsonList = JSON.parseArray(optionStr);
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid enum options format, expected JSON array string", e);
		}
		for (int i = 0; i < jsonList.size(); i++) {
			String value = jsonList.getJSONObject(i).getString("value");
			options.add(value);
		}

		return options.stream()
			.map(opt -> "'" + opt + "'")
			.collect(Collectors.joining(", "));
	}


	/**
	 * 验证表和数据
	 */
	private void validateTableAndData(String tableName, Map<String, Object> data) {
		if (tableName == null || tableName.trim().isEmpty()) {
			throw new IllegalArgumentException("表名不能为空");
		}

		// 防止SQL注入，验证表名格式
		if (!tableName.matches("^[a-zA-Z_][a-zA-Z0-9_]*$")) {
			throw new IllegalArgumentException("表名格式不正确");
		}

		if (data == null || data.isEmpty()) {
			throw new IllegalArgumentException("数据不能为空");
		}

		// 验证字段名格式
		for (String field : data.keySet()) {
			if (!field.matches("^[a-zA-Z_][a-zA-Z0-9_]*$")) {
				throw new IllegalArgumentException("字段名格式不正确: " + field);
			}
		}
	}


	/**
	 * 创建数据
	 */
	public Map<String, Object> createData(DynamicInsert dataDto) {


		String tableName = getTableName(dataDto.getNameEn());
		Map<String, Object> record = new HashMap<>(dataDto.getParams());
		for (String col : record.keySet()) {
			SqlIdentifierValidator.validate(col);
		}
		String userId = loginUserContext.getLoginUserId();
		// 添加系统字段
		record.put("created_by",userId);
		record.put("updated_by", userId);

		// 构建SQL
		String columns = String.join(", ", record.keySet());
		String placeholders = record.keySet().stream()
			.map(k -> "?")
			.collect(Collectors.joining(", "));

		String sql = String.format(
			"INSERT INTO %s (%s) VALUES (%s)",
			tableName, columns, placeholders
		);

		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

				int index = 1;
				for (Object value : record.values()) {
					ps.setObject(index++, value);
				}

				return ps;
			}
		}, keyHolder);

		Long generatedId = keyHolder.getKey() != null ? keyHolder.getKey().longValue() : null;

		if (generatedId != null) {
			record.put("id", generatedId);
		}

		return record;
	}





	/**
	 * 获取表名
	 */
	private String getTableName(String modelId) {
		return "dynamic_" + modelId.toLowerCase(Locale.ROOT);
	}



	public Map<String, Object> getDataById(String modelId, Long id) {
		String tableName = getTableName(modelId);
		String sql = "SELECT * FROM " + tableName + " WHERE id = ?";

		List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, id);

		if (results.isEmpty()) {
			return null;
		} else {
			return results.get(0);
		}
	}

	public Map<String,Object> updateDateById(DynamicUpdate dto) {
		String modelId = dto.getNameEn();
		Map<String, Object> params1 = dto.getParams();
		if(params1 == null || !params1.containsKey("id")) {
			throw new IllegalArgumentException("更新操作必须指定ID");
		}
		if(dto.getData() == null || dto.getData().isEmpty()) {
			throw new IllegalArgumentException("更新操作必须指定更新数据");
		}
		if(modelId == null || modelId.trim().isEmpty()) {
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

		Map<String,Object> result = new HashMap<>();
		result.put("rowsAffected", rowsAffected);
		return result;
	}

	public Map<String,Object> deleteDataById(DynamicDelete dto) {
		String modelId = dto.getNameEn();
		if(modelId == null || modelId.trim().isEmpty()) {
			throw new IllegalArgumentException("模型ID不能为空");
		}
		if(dto.getId() == null) {
			throw new IllegalArgumentException("删除操作必须指定ID");
		}
		Long id = Long.valueOf(dto.getId());

		String tableName = getTableName(modelId);
		String sql = "DELETE FROM " + tableName + " WHERE id = ?";
		int update = jdbcTemplate.update(sql, id);
		Map<String,Object> result = new HashMap<>();
		result.put("rowsAffected", update);
		return result;
	}





}

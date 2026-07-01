package com.tinyengine.it.modeldata.service;

import com.tinyengine.it.model.dto.ParametersDto;
import com.tinyengine.it.modeldata.exception.BusinessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ValidationService {
	public void validate(Map<String, Object> data, List<ParametersDto> fieldDefs) throws Exception{
		if(fieldDefs == null || fieldDefs.isEmpty()) {
			throw new BusinessException("字段定义不能为空" );
		}
		if(data == null || data.isEmpty()) {
			throw new BusinessException("数据不能为空" );
		}
		// 1. 必填校验
		Set<String> requiredProps = fieldDefs.stream()
			.filter(ParametersDto::getRequired)
			.map(ParametersDto::getProp)
			.collect(Collectors.toSet());
		for (String required : requiredProps) {
			// 跳过 id 字段（通常由数据库自动生成，不需要客户端传入）
			if ("id".equalsIgnoreCase(required)) {
				continue;
			}
			if (!data.containsKey(required) || data.get(required) == null) {
				throw new BusinessException("缺少必填字段: " + required);
			}
		}

		// 2. 类型校验
		for (ParametersDto def : fieldDefs) {
			Object value = data.get(def.getProp());
			if (value != null) {
				checkType(value, def.getType(), def.getProp());
			}
		}

		// 3. 不允许出现未定义字段（可选）
		Set<String> definedProps = fieldDefs.stream().map(ParametersDto::getProp).collect(Collectors.toSet());
		// 定义系统字段忽略列表（这些字段允许在 data 中出现，即使不在模型定义中）
		Set<String> ignoredFields = Set.of("created_by", "tenantId","updated_by");
		for (String prop : data.keySet()) {
			if (ignoredFields.contains(prop)) {
				continue; // 跳过系统字段，不校验
			}
			if (!definedProps.contains(prop)) {
				throw new BusinessException("未定义的字段: " + prop);
			}
		}
	}

	private void checkType(Object value, String expectedType, String fieldName) {
		System.out.println("Validating field: " + fieldName + ", Expected type: " + expectedType + ", Actual value: " + value);
		switch (expectedType) {
			case "String":
				if (!(value instanceof String)) throw new BusinessException(fieldName + " 应为字符串类型");
				break;
			case "Number":
				if (!(value instanceof Number)) throw new BusinessException(fieldName + " 应为数字类型");
				break;
			case "Boolean":
				if (!(value instanceof Boolean)) throw new BusinessException(fieldName + " 应为布尔类型");
				break;
			case "Date":
				if (!(value instanceof String)) throw new BusinessException(fieldName + " 应为日期字符串");

                // 校验格式 yyyy-MM-dd
				if (!((String) value).matches("\\d{4}-\\d{2}-\\d{2}")) {
					throw new BusinessException(fieldName + " 格式应为 yyyy-MM-dd");
				}
				break;
			case "DateTime"	:
				if (!(value instanceof String)) {
					throw new BusinessException(fieldName + " 应为日期时间字符串");
				}
				// 校验格式 yyyy-MM-dd HH:mm:ss
				if (!((String) value).matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
					throw new BusinessException(fieldName + " 格式应为 yyyy-MM-dd HH:mm:ss");
				}
				break;
			// 新增：枚举类型校验
			case "Enum"	:

				if (!(value instanceof String enumStr)) {
					throw new BusinessException(fieldName + " 应传入数字字符串");
				}

				// 使用正则校验：仅包含数字（至少一位）
				if (!enumStr.matches("\\d+")) {
					throw new BusinessException(fieldName + " 应为非负整数（纯数字字符串）");
				}
				break;

			// 可扩展 Date, Array, Object 等
			default:
				// 忽略自定义类型
		}
	}
}

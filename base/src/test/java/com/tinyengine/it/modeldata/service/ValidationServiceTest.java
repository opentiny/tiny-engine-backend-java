package com.tinyengine.it.modeldata.service;

import com.tinyengine.it.model.dto.ParametersDto;
import com.tinyengine.it.modeldata.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ValidationServiceTest {

	private ValidationService validationService;

	@BeforeEach
	void setUp() {
		validationService = new ValidationService();
	}

	@Test
	void testValidate_Success() throws Exception {
		// Arrange
		Map<String, Object> data = new HashMap<>();
		data.put("field1", "value1");
		data.put("field2", 123);
		data.put("field3", true);
		ParametersDto parametersDto = new ParametersDto();
		parametersDto.setProp("field1");
		parametersDto.setType("String");
		parametersDto.setRequired(true);

		ParametersDto parametersDto2 = new ParametersDto();
		parametersDto2.setProp("field2");
		parametersDto2.setType("Number");
		parametersDto2.setRequired(true);


		ParametersDto parametersDto3 = new ParametersDto();
		parametersDto3.setProp("field3");
		parametersDto3.setType("Boolean");
		parametersDto3.setRequired(true);

		List<ParametersDto> fieldDefs = List.of(
			parametersDto,parametersDto2,parametersDto3
		);

		// Act & Assert
		assertDoesNotThrow(() -> validationService.validate(data, fieldDefs));
	}

	@Test
	void testValidate_MissingRequiredField() {
		// Arrange
		Map<String, Object> data = new HashMap<>();
		data.put("field1", "value1");
		ParametersDto parametersDto = new ParametersDto();
		parametersDto.setProp("field1");
		parametersDto.setType("String");
		parametersDto.setRequired(true);

		ParametersDto parametersDto2 = new ParametersDto();
		parametersDto2.setProp("field2");
		parametersDto2.setType("Number");
		parametersDto2.setRequired(true);
		List<ParametersDto> fieldDefs = List.of(
			parametersDto,
			parametersDto2
		);

		// Act & Assert
		BusinessException exception = assertThrows(BusinessException.class, () ->
			validationService.validate(data, fieldDefs)
		);
		assertEquals("缺少必填字段: field2", exception.getMessage());
	}

	@Test
	void testValidate_InvalidFieldType() {
		// Arrange
		Map<String, Object> data = new HashMap<>();
		data.put("field1", 123); // Invalid type, should be String
		ParametersDto parametersDto = new ParametersDto();
		parametersDto.setProp("field1");
		parametersDto.setType("String");
		parametersDto.setRequired(true);
		List<ParametersDto> fieldDefs = List.of(
			parametersDto
		);

		// Act & Assert
		BusinessException exception = assertThrows(BusinessException.class, () ->
			validationService.validate(data, fieldDefs)
		);
		assertEquals("field1 应为字符串类型", exception.getMessage());
	}

	@Test
	void testValidate_UndefinedField() {
		// Arrange
		Map<String, Object> data = new HashMap<>();
		data.put("field1", "value1");
		data.put("undefinedField", "value");

		ParametersDto parametersDto = new ParametersDto();
		parametersDto.setProp("field1");
		parametersDto.setType("String");
		parametersDto.setRequired(true);

		List<ParametersDto> fieldDefs = List.of(
			parametersDto
		);

		// Act & Assert
		BusinessException exception = assertThrows(BusinessException.class, () ->
			validationService.validate(data, fieldDefs)
		);
		assertEquals("未定义的字段: undefinedField", exception.getMessage());
	}
	@Test
	void testValidate_EmptyData() {
		// Arrange
		Map<String, Object> data = new HashMap<>();

		ParametersDto parametersDto = new ParametersDto();
		parametersDto.setProp("field1");
		parametersDto.setType("String");
		parametersDto.setRequired(true);

		List<ParametersDto> fieldDefs = List.of(
			parametersDto
		);

		// Act & Assert
		BusinessException exception = assertThrows(BusinessException.class, () ->
			validationService.validate(data, fieldDefs)
		);
		assertEquals("数据不能为空", exception.getMessage());
	}

	@Test
	void testValidate_EmptyFieldDefinitions() {
		// Arrange
		Map<String, Object> data = new HashMap<>();
		data.put("field1", "value1");

		List<ParametersDto> fieldDefs = Collections.emptyList();

		// Act & Assert
		BusinessException exception = assertThrows(BusinessException.class, () ->
			validationService.validate(data, fieldDefs)
		);
		assertEquals("字段定义不能为空", exception.getMessage());
	}

	@Test
	void testValidate_NullData() {
		// Arrange
		Map<String, Object> data = null;

		ParametersDto parametersDto = new ParametersDto();
		parametersDto.setProp("field1");
		parametersDto.setType("String");
		parametersDto.setRequired(true);

		List<ParametersDto> fieldDefs = List.of(
			parametersDto
		);

		// Act & Assert
		BusinessException exception = assertThrows(BusinessException.class, () ->
			validationService.validate(data, fieldDefs)
		);
		assertEquals("数据不能为空", exception.getMessage());
	}

	@Test
	void testValidate_NullFieldDefinitions() {
		// Arrange
		Map<String, Object> data = new HashMap<>();
		data.put("field1", "value1");

		List<ParametersDto> fieldDefs = null;

		// Act & Assert
		BusinessException exception = assertThrows(BusinessException.class, () ->
			validationService.validate(data, fieldDefs)
		);
		assertEquals("字段定义不能为空", exception.getMessage());
	}

	@Test
	void testValidate_OptionalField() {
		// Arrange
		Map<String, Object> data = new HashMap<>();
		data.put("field1", "value1");
		data.put("field2", 18);


		ParametersDto parametersDto = new ParametersDto();
		parametersDto.setProp("field1");
		parametersDto.setType("String");
		parametersDto.setRequired(true);

		ParametersDto parametersDto2 = new ParametersDto();
		parametersDto2.setProp("field2");
		parametersDto2.setType("Number");
		parametersDto2.setRequired(true);

		List<ParametersDto> fieldDefs = List.of(
			parametersDto,parametersDto2
		);



		// Act & Assert
		assertDoesNotThrow(() -> validationService.validate(data, fieldDefs));
	}
}
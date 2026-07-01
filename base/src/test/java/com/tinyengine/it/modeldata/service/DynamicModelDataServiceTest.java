package com.tinyengine.it.modeldata.service;

import com.tinyengine.it.model.dto.ParametersDto;
import com.tinyengine.it.model.entity.Model;
import com.tinyengine.it.modeldata.dao.ModelDataRepository;
import com.tinyengine.it.modeldata.entity.ModelData;
import com.tinyengine.it.modeldata.exception.BusinessException;
import com.tinyengine.it.service.material.ModelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DynamicModelDataServiceTest {
	@Mock
	private ModelService modelService;

	@Mock
	private ModelDataRepository modelDataRepo;

	@Mock
	private ValidationService validationService;

	@InjectMocks
	private DynamicModelDataService dynamicModelDataService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testCreate_Success() throws Exception {
		// Arrange
		Integer modelId = 1;
		String tenantId = "tenant1";
		String userId = "user1";
		Map<String, Object> data = new HashMap<>();
		data.put("field1", "value1");

		Model model = new Model();
		model.setId(modelId);
		model.setVersion("0.01");
		ParametersDto parametersDto = new ParametersDto();
		parametersDto.setDescription("Test field");
		parametersDto.setRequired(true);
		parametersDto.setType("String");
		parametersDto.setProp("field1");
		model.setParameters(Collections.singletonList(parametersDto));

		when(modelService.queryModelById(modelId)).thenReturn(model);
		doNothing().when(validationService).validate(data, model.getParameters());

		ModelData savedEntity = new ModelData();
		savedEntity.setId(1);
		when(modelDataRepo.save(any(ModelData.class))).thenReturn(savedEntity);

		// Act
		ModelData result = dynamicModelDataService.create(modelId, data, tenantId, userId);

		// Assert
		assertNotNull(result);
		assertEquals(1, result.getId());
		verify(modelService, times(1)).queryModelById(modelId);
		verify(validationService, times(1)).validate(data, model.getParameters());
		verify(modelDataRepo, times(1)).save(any(ModelData.class));
	}

	@Test
	void testCreate_ModelNotFound() {
		// Arrange
		Integer modelId = 1;
		when(modelService.queryModelById(modelId)).thenReturn(null);

		// Act & Assert
		BusinessException exception = assertThrows(BusinessException.class, () ->
			dynamicModelDataService.create(modelId, new HashMap<>(), "tenant1", "user1")
		);
		assertEquals("模型不存在", exception.getMessage());
		verify(modelService, times(1)).queryModelById(modelId);
	}

	@Test
	void testUpdate_Success() throws Exception {
		// Arrange
		Integer dataId = 1;
		String userId = "user1";
		Map<String, Object> data = new HashMap<>();
		data.put("field1", "newValue");

		ModelData existing = new ModelData();
		existing.setId(1);
		existing.setModelId(1);
		existing.setDataJson(new HashMap<>());

		Model model = new Model();
		model.setId(1);
		ParametersDto parametersDto = new ParametersDto();
		parametersDto.setDescription("Test field");
		parametersDto.setRequired(true);
		parametersDto.setType("String");
		parametersDto.setProp("field1");
		model.setParameters(Collections.singletonList(parametersDto));

		when(modelDataRepo.findById(1L)).thenReturn(Optional.of(existing));
		when(modelService.queryModelById(1)).thenReturn(model);
		doNothing().when(validationService).validate(anyMap(), anyList());
		when(modelDataRepo.save(any(ModelData.class))).thenReturn(existing);

		// Act
		ModelData result = dynamicModelDataService.update(dataId, data, userId);

		// Assert
		assertNotNull(result);
		verify(modelDataRepo, times(1)).findById(1L);
		verify(modelService, times(1)).queryModelById(1);
		verify(validationService, times(1)).validate(anyMap(), anyList());
		verify(modelDataRepo, times(1)).save(any(ModelData.class));
	}

	@Test
	void testDelete_Success() {
		// Arrange
		Long dataId = 1L;

		// Act
		dynamicModelDataService.delete(dataId);

		// Assert
		verify(modelDataRepo, times(1)).deleteById(dataId);
	}

	@Test
	void testGet_Success() {
		// Arrange
		Integer dataId = 1;
		ModelData entity = new ModelData();
		entity.setId(dataId);
		when(modelDataRepo.findById(Long.valueOf(dataId))).thenReturn(Optional.of(entity));

		// Act
		ModelData result = dynamicModelDataService.get(Long.valueOf(dataId));

		// Assert
		assertNotNull(result);
		assertEquals(dataId, result.getId());
		verify(modelDataRepo, times(1)).findById(Long.valueOf(dataId));
	}

	@Test
	void testGet_NotFound() {
		// Arrange
		Long dataId = 1L;
		when(modelDataRepo.findById(dataId)).thenReturn(Optional.empty());

		// Act
		ModelData result = dynamicModelDataService.get(dataId);

		// Assert
		assertNull(result);
		verify(modelDataRepo, times(1)).findById(dataId);
	}

	@Test
	void testCreate_Failure() throws Exception {
		// Arrange
		Integer modelId = 1;
		String tenantId = "tenant1";
		String userId = "user1";
		Map<String, Object> data = new HashMap<>();
		data.put("field1", "value1");

		when(modelService.queryModelById(modelId)).thenThrow(new BusinessException("Model service failure"));

		// Act & Assert
		BusinessException exception = assertThrows(BusinessException.class, () ->
			dynamicModelDataService.create(modelId, data, tenantId, userId)
		);
		assertEquals("Model service failure", exception.getMessage());
		verify(modelService, times(1)).queryModelById(modelId);
		verify(validationService, never()).validate(anyMap(), anyList());
		verify(modelDataRepo, never()).save(any(ModelData.class));
	}

}
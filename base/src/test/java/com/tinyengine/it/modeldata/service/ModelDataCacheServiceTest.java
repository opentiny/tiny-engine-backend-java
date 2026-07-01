package com.tinyengine.it.modeldata.service;

import com.tinyengine.it.modeldata.dao.ModelDataRepository;
import com.tinyengine.it.modeldata.entity.ModelData;
import com.tinyengine.it.modeldata.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ModelDataCacheServiceTest {

	@Mock
	private DynamicModelDataService dynamicModelDataService;

	@Mock
	private StringRedisTemplate redisTemplate;

	@Mock
	private ModelDataRepository modelDataRepo;

	@InjectMocks
	private ModelDataCacheService modelDataCacheService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		when(redisTemplate.delete(anyString())).thenReturn(true);
		// Mock ValueOperations
		ValueOperations<String, String> valueOperations = mock(ValueOperations.class);
		when(redisTemplate.opsForValue()).thenReturn(valueOperations);

		// Configure behavior for redisTemplate.delete()
		when(redisTemplate.delete(anyString())).thenReturn(true);

		// Configure behavior for ValueOperations.get()
		when(valueOperations.get(anyString())).thenReturn(null);

	}



	@Test
	void testAdd_Success() throws Exception {
		// Arrange
		Integer modelId = 1;
		String tenantId = "tenant1";
		String userId = "user1";
		Map<String, Object> data = new HashMap<>();
		data.put("field1", "value1");

		ModelData mockModelData = new ModelData();
		mockModelData.setId(1);
		mockModelData.setVersion("0.01");
		mockModelData.setDataJson(data);

		when(dynamicModelDataService.create(modelId, data, tenantId, userId)).thenReturn(mockModelData);

		// Act
		Map<String, Object> result = modelDataCacheService.add(modelId, tenantId, data, userId);

		// Assert
		assertNotNull(result);
		assertEquals(1, result.get("_id"));
		verify(dynamicModelDataService, times(1)).create(modelId, data, tenantId, userId);
		verify(redisTemplate, times(1)).delete(anyString());
	}

	@Test
	void testUpdate_Success() throws Exception {
		// Arrange
		Integer dataId = 1;
		Integer modelId = 1;
		String tenantId = "1";
		String userId = "user1";
		Map<String, Object> newData = new HashMap<>();
		newData.put("field1", "newValue");

		ModelData mockModelData = new ModelData();
		mockModelData.setId(1);
		mockModelData.setVersion("0.0.2");
		mockModelData.setModelId(1);
		mockModelData.setDataJson(newData);
		mockModelData.setTenantId("1");

       when(modelDataRepo.findByModelIdAndTenantIdAndId(modelId, tenantId, dataId)).thenReturn(mockModelData);
		when(dynamicModelDataService.update(dataId, newData, userId)).thenReturn(mockModelData);

		// Act
		Map<String, Object> result = modelDataCacheService.update(modelId,tenantId,dataId, newData, userId);

		// Assert
		assertNotNull(result);
		assertEquals(1, result.get("_id"));
		verify(dynamicModelDataService, times(1)).update(dataId, newData, userId);
		verify(redisTemplate, times(1)).delete(anyString());
	}

	@Test
	void testDelete_Success() throws Exception {
		// Arrange
		Integer dataId = 1;
		Integer modelId = 1;
		String tenantId = "1";
		ModelData mockModelData = new ModelData();
		mockModelData.setId(Math.toIntExact(dataId));
		mockModelData.setModelId(1);
		mockModelData.setTenantId("1");
        when(modelDataRepo.findByModelIdAndTenantIdAndId(modelId, tenantId, Math.toIntExact(dataId))).thenReturn(mockModelData);

		// Act
		Map<String, Object> result = modelDataCacheService.delete(modelId,tenantId,Long.valueOf(dataId));

		// Assert
		assertNotNull(result);
		assertEquals(dataId, result.get("_id"));
		verify(modelDataRepo, times(1)).deleteById(Long.valueOf(dataId));
		verify(redisTemplate, times(1)).delete(anyString());
	}
	@Test
	void testAdd_Failure() throws Exception {
		// Arrange
		Integer modelId = 1;
		String tenantId = "tenant1";
		String userId = "user1";
		Map<String, Object> data = new HashMap<>();
		data.put("field1", "value1");

		when(dynamicModelDataService.create(modelId, data, tenantId, userId))
			.thenThrow(new BusinessException("Failed to create model data"));

		// Act & Assert
		BusinessException exception = assertThrows(BusinessException.class, () ->
			modelDataCacheService.add(modelId, tenantId, data, userId)
		);
		assertEquals("Failed to create model data", exception.getMessage());
		verify(dynamicModelDataService, times(1)).create(modelId, data, tenantId, userId);
		verify(redisTemplate, never()).delete(anyString());
	}

}
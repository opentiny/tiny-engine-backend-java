package com.tinyengine.it.service.platform.impl;

import cn.hutool.core.util.ReflectUtil;
import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.mapper.PlatformHistoryMapper;
import com.tinyengine.it.model.entity.PlatformHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlatformHistoryServiceImplTest {

	@Mock
	private PlatformHistoryMapper platformHistoryMapper;

	@InjectMocks
	private PlatformHistoryServiceImpl platformHistoryServiceImpl;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		ReflectUtil.setFieldValue(platformHistoryServiceImpl, "baseMapper", platformHistoryMapper);
	}
	/**
	 * Tests the `queryAllPlatformHistory` method to ensure it returns a non-empty list of PlatformHistory records.
	 *
	 * This test verifies that when the `queryAllPlatformHistory` method is called, it interacts with the
	 * `platformHistoryMapper` to retrieve a list of PlatformHistory records. It checks that the returned list
	 * is not null and contains the expected number of records, confirming that the method functions correctly
	 * when there are records in the database.
	 */
	@Test
	void queryAllPlatformHistoryReturnsNonEmptyList() {
		// Mock behavior
		List<PlatformHistory> mockList = List.of(new PlatformHistory(), new PlatformHistory());
		when(platformHistoryMapper.queryAllPlatformHistory()).thenReturn(mockList);

		// Call the method
		List<PlatformHistory> result = platformHistoryServiceImpl.queryAllPlatformHistory();

		// Assertions
		assertNotNull(result);
		assertEquals(2, result.size());
		verify(platformHistoryMapper, times(1)).queryAllPlatformHistory();
	}

	@Test
	void queryAllPlatformHistoryReturnsEmptyList() {
		// Mock behavior
		when(platformHistoryMapper.queryAllPlatformHistory()).thenReturn(List.of());

		// Call the method
		List<PlatformHistory> result = platformHistoryServiceImpl.queryAllPlatformHistory();

		// Assertions
		assertNotNull(result);
		assertTrue(result.isEmpty());
		verify(platformHistoryMapper, times(1)).queryAllPlatformHistory();
	}

	@Test
	void queryAllPlatformHistoryHandlesNullResult() {
		when(platformHistoryMapper.queryAllPlatformHistory()).thenReturn(null);

		List<PlatformHistory> result = platformHistoryServiceImpl.queryAllPlatformHistory();

		assertTrue(result == null || result.isEmpty());
		verify(platformHistoryMapper, times(1)).queryAllPlatformHistory();
	}

	/**
	 * Tests the `queryPlatformHistoryById` method to ensure it returns a PlatformHistory record when a valid ID is provided.
	 */
	@Test
	void queryPlatformHistoryByIdReturnsPlatformHistoryWhenIdExists() {
		PlatformHistory mockPlatformHistory = new PlatformHistory();
		mockPlatformHistory.setId(1);
		when(platformHistoryMapper.queryPlatformHistoryById(1)).thenReturn(mockPlatformHistory);

		PlatformHistory result = platformHistoryServiceImpl.queryPlatformHistoryById(1);

		assertNotNull(result);
		assertEquals(1, result.getId());
		verify(platformHistoryMapper, times(1)).queryPlatformHistoryById(1);
	}
	/**
	 * Tests the `queryPlatformHistoryById` method to ensure it returns null when the specified ID does not exist.
	 *
	 * This test verifies that when a non-existent ID is provided to the `queryPlatformHistoryById` method, it interacts with the
	 * `platformHistoryMapper` to attempt to retrieve a PlatformHistory record. It checks that the returned result is null, confirming
	 * that the method correctly handles cases where the specified ID does not correspond to any existing record in the database.
	 */
	@Test
	void queryPlatformHistoryByIdReturnsNullWhenIdDoesNotExist() {
		when(platformHistoryMapper.queryPlatformHistoryById(999)).thenReturn(null);

		PlatformHistory result = platformHistoryServiceImpl.queryPlatformHistoryById(999);

		assertNull(result);
		verify(platformHistoryMapper, times(1)).queryPlatformHistoryById(999);
	}

	@Test
	void queryPlatformHistoryByIdThrowsExceptionWhenIdIsNull() {
		assertThrows(IllegalArgumentException.class, () -> platformHistoryServiceImpl.queryPlatformHistoryById(null));
		verify(platformHistoryMapper, never()).queryPlatformHistoryById(any());
	}
	/**
	 * Tests the `queryPlatformHistoryByCondition` method to ensure it returns matching results based on the provided condition.
	 *
	 * This test verifies that when a specific condition is passed to the `queryPlatformHistoryByCondition` method, it interacts with the
	 * `platformHistoryMapper` to retrieve a list of PlatformHistory records that match the condition. It checks that the returned list is not null
	 * and contains the expected number of records, confirming that the method correctly filters results based on the provided condition.
	 */
	@Test
	void queryPlatformHistoryByConditionReturnsMatchingResults() {
		PlatformHistory condition = new PlatformHistory();
		condition.setName("TestName");
		List<PlatformHistory> mockList = List.of(new PlatformHistory(), new PlatformHistory());
		when(platformHistoryMapper.queryPlatformHistoryByCondition(condition)).thenReturn(mockList);

		List<PlatformHistory> result = platformHistoryServiceImpl.queryPlatformHistoryByCondition(condition);

		assertNotNull(result);
		assertEquals(2, result.size());
		verify(platformHistoryMapper, times(1)).queryPlatformHistoryByCondition(condition);
	}

	@Test
	void queryPlatformHistoryByConditionReturnsEmptyListWhenNoMatch() {
		PlatformHistory condition = new PlatformHistory();
		condition.setName("NonExistentName");
		when(platformHistoryMapper.queryPlatformHistoryByCondition(condition)).thenReturn(List.of());

		List<PlatformHistory> result = platformHistoryServiceImpl.queryPlatformHistoryByCondition(condition);

		assertNotNull(result);
		assertTrue(result.isEmpty());
		verify(platformHistoryMapper, times(1)).queryPlatformHistoryByCondition(condition);
	}

	@Test
	void queryPlatformHistoryByConditionHandlesNullCondition() {
		when(platformHistoryMapper.queryPlatformHistoryByCondition(null)).thenReturn(List.of());

		List<PlatformHistory> result = platformHistoryServiceImpl.queryPlatformHistoryByCondition(null);

		assertNotNull(result);
		assertTrue(result.isEmpty());
		verify(platformHistoryMapper, times(1)).queryPlatformHistoryByCondition(null);
	}
	/**
	 * Deletes a PlatformHistory record when the specified ID exists.
	 *
	 * This test verifies that the `deletePlatformHistoryById` method successfully deletes
	 * a PlatformHistory record when the provided ID exists in the database. It ensures
	 * that the correct methods are called and the result is as expected.
	 */
	@Test
	void deletePlatformHistoryByIdDeletesRecordWhenIdExists() {
		PlatformHistory mockPlatformHistory = new PlatformHistory();
		mockPlatformHistory.setId(1);
		when(platformHistoryMapper.queryPlatformHistoryById(1)).thenReturn(mockPlatformHistory);
		when(platformHistoryMapper.deletePlatformHistoryById(1)).thenReturn(1);

		Result<PlatformHistory> result = platformHistoryServiceImpl.deletePlatformHistoryById(1);

		assertNotNull(result);
		assertTrue(result.isSuccess());
		assertEquals(1, result.getData().getId());
		verify(platformHistoryMapper, times(1)).queryPlatformHistoryById(1);
		verify(platformHistoryMapper, times(1)).deletePlatformHistoryById(1);
	}
	/**
	 * Verifies the behavior of the `deletePlatformHistoryById` method when the specified ID does not exist.
	 *
	 * This test ensures that the method returns a successful result even when the provided ID
	 * does not correspond to any existing PlatformHistory record in the database. It also verifies
	 * that the delete operation is not invoked in such cases.
	 */
	@Test
	void deletePlatformHistoryByIdReturnsSuccessWhenIdDoesNotExist() {
		when(platformHistoryMapper.queryPlatformHistoryById(999)).thenReturn(null);

		Result<PlatformHistory> result = platformHistoryServiceImpl.deletePlatformHistoryById(999);

		assertNotNull(result);
		assertTrue(result.isSuccess());
		assertNull(result.getData());
		verify(platformHistoryMapper, times(1)).queryPlatformHistoryById(999);
		verify(platformHistoryMapper, never()).deletePlatformHistoryById(any());
	}
	/**
	 * Verifies the behavior of the `deletePlatformHistoryById` method when the delete operation fails.
	 *
	 * This test ensures that the method correctly handles a scenario where the delete operation
	 * does not succeed, even though the specified ID exists in the database. It validates that
	 * the appropriate error code is returned and the expected methods are invoked.
	 */
	@Test
	void deletePlatformHistoryByIdFailsWhenDeleteOperationFails() {
		PlatformHistory mockPlatformHistory = new PlatformHistory();
		mockPlatformHistory.setId(1);
		when(platformHistoryMapper.queryPlatformHistoryById(1)).thenReturn(mockPlatformHistory);
		when(platformHistoryMapper.deletePlatformHistoryById(1)).thenReturn(0);

		Result<PlatformHistory> result = platformHistoryServiceImpl.deletePlatformHistoryById(1);

		assertNotNull(result);
		assertFalse(result.isSuccess());
		assertEquals(ExceptionEnum.CM008.getResultCode(), result.getCode());
		verify(platformHistoryMapper, times(1)).queryPlatformHistoryById(1);
		verify(platformHistoryMapper, times(1)).deletePlatformHistoryById(1);
	}

	/**
	 * Tests the `updatePlatformHistoryById` method to ensure it updates a record when valid data is provided.
	 */
	@Test
	void updatePlatformHistoryByIdUpdatesRecordWhenValidDataProvided() {
		PlatformHistory mockPlatformHistory = new PlatformHistory();
		mockPlatformHistory.setId(1);
		mockPlatformHistory.setName("UpdatedName");
		when(platformHistoryMapper.updatePlatformHistoryById(mockPlatformHistory)).thenReturn(1);
		when(platformHistoryMapper.queryPlatformHistoryById(1)).thenReturn(mockPlatformHistory);

		Result<PlatformHistory> result = platformHistoryServiceImpl.updatePlatformHistoryById(mockPlatformHistory);

		assertNotNull(result);
		assertTrue(result.isSuccess());
		assertEquals("UpdatedName", result.getData().getName());
		verify(platformHistoryMapper, times(1)).updatePlatformHistoryById(mockPlatformHistory);
		verify(platformHistoryMapper, times(1)).queryPlatformHistoryById(1);
	}

	@Test
	void updatePlatformHistoryByIdFailsWhenPlatformHistoryIsNull() {
		Result<PlatformHistory> result = platformHistoryServiceImpl.updatePlatformHistoryById(null);

		assertNotNull(result);
		assertFalse(result.isSuccess());
		assertEquals(ExceptionEnum.CM002.getResultCode(), result.getCode());
		verify(platformHistoryMapper, never()).updatePlatformHistoryById(any());
	}

	@Test
	void updatePlatformHistoryByIdFailsWhenIdIsNull() {
		PlatformHistory mockPlatformHistory = new PlatformHistory();
		mockPlatformHistory.setName("UpdatedName");

		Result<PlatformHistory> result = platformHistoryServiceImpl.updatePlatformHistoryById(mockPlatformHistory);

		assertNotNull(result);
		assertFalse(result.isSuccess());
		assertEquals(ExceptionEnum.CM002.getResultCode(), result.getCode());
		verify(platformHistoryMapper, never()).updatePlatformHistoryById(any());
	}

	@Test
	void updatePlatformHistoryByIdFailsWhenUpdateOperationFails() {
		PlatformHistory mockPlatformHistory = new PlatformHistory();
		mockPlatformHistory.setId(1);
		mockPlatformHistory.setName("UpdatedName");
		when(platformHistoryMapper.updatePlatformHistoryById(mockPlatformHistory)).thenReturn(0);

		Result<PlatformHistory> result = platformHistoryServiceImpl.updatePlatformHistoryById(mockPlatformHistory);

		assertNotNull(result);
		assertFalse(result.isSuccess());
		assertEquals(ExceptionEnum.CM008.getResultCode(), result.getCode());
		verify(platformHistoryMapper, times(1)).updatePlatformHistoryById(mockPlatformHistory);
	}

	/**
	 * Tests the `createPlatformHistory` method to ensure it creates a record when valid data is provided.
	 *
	 * This test verifies that when a valid `PlatformHistory` object is passed to the `createPlatformHistory` method,
	 * it successfully creates a new record in the database. It checks that the returned result indicates success,
	 * contains the expected data, and that the mapper's `createPlatformHistory` method is called with the correct parameters.
	 */
	@Test
	void createPlatformHistoryCreatesRecordWhenValidDataProvided() {
		PlatformHistory mockPlatformHistory = new PlatformHistory();
		mockPlatformHistory.setRefId(1);
		mockPlatformHistory.setName("ValidName");
		mockPlatformHistory.setVersion("1.0");
		mockPlatformHistory.setMaterialHistoryId(100);
		when(platformHistoryMapper.createPlatformHistory(mockPlatformHistory)).thenReturn(1);

		Result<PlatformHistory> result = platformHistoryServiceImpl.createPlatformHistory(mockPlatformHistory);

		assertNotNull(result);
		assertTrue(result.isSuccess());
		assertEquals(mockPlatformHistory, result.getData());
		verify(platformHistoryMapper, times(1)).createPlatformHistory(mockPlatformHistory);
	}

	@Test
	void createPlatformHistoryFailsWhenPlatformHistoryIsNull() {
		Result<PlatformHistory> result = platformHistoryServiceImpl.createPlatformHistory(null);

		assertNotNull(result);
		assertFalse(result.isSuccess());
		assertEquals(ExceptionEnum.CM002.getResultCode(), result.getCode());
		verify(platformHistoryMapper, never()).createPlatformHistory(any());
	}

	@Test
	void createPlatformHistoryFailsWhenRefIdIsNull() {
		PlatformHistory mockPlatformHistory = new PlatformHistory();
		mockPlatformHistory.setName("ValidName");
		mockPlatformHistory.setVersion("1.0");
		mockPlatformHistory.setMaterialHistoryId(100);

		Result<PlatformHistory> result = platformHistoryServiceImpl.createPlatformHistory(mockPlatformHistory);

		assertNotNull(result);
		assertFalse(result.isSuccess());
		assertEquals(ExceptionEnum.CM002.getResultCode(), result.getCode());
		verify(platformHistoryMapper, never()).createPlatformHistory(any());
	}

	@Test
	void createPlatformHistoryFailsWhenNameIsNullOrEmpty() {
		PlatformHistory mockPlatformHistory = new PlatformHistory();
		mockPlatformHistory.setRefId(1);
		mockPlatformHistory.setVersion("1.0");
		mockPlatformHistory.setMaterialHistoryId(100);

		Result<PlatformHistory> result = platformHistoryServiceImpl.createPlatformHistory(mockPlatformHistory);

		assertNotNull(result);
		assertFalse(result.isSuccess());
		assertEquals(ExceptionEnum.CM002.getResultCode(), result.getCode());
		verify(platformHistoryMapper, never()).createPlatformHistory(any());
	}

	@Test
	void createPlatformHistoryFailsWhenVersionIsNullOrEmpty() {
		PlatformHistory mockPlatformHistory = new PlatformHistory();
		mockPlatformHistory.setRefId(1);
		mockPlatformHistory.setName("ValidName");
		mockPlatformHistory.setMaterialHistoryId(100);

		Result<PlatformHistory> result = platformHistoryServiceImpl.createPlatformHistory(mockPlatformHistory);

		assertNotNull(result);
		assertFalse(result.isSuccess());
		assertEquals(ExceptionEnum.CM002.getResultCode(), result.getCode());
		verify(platformHistoryMapper, never()).createPlatformHistory(any());
	}

	@Test
	void createPlatformHistoryFailsWhenMaterialHistoryIdIsNull() {
		PlatformHistory mockPlatformHistory = new PlatformHistory();
		mockPlatformHistory.setRefId(1);
		mockPlatformHistory.setName("ValidName");
		mockPlatformHistory.setVersion("1.0");

		Result<PlatformHistory> result = platformHistoryServiceImpl.createPlatformHistory(mockPlatformHistory);

		assertNotNull(result);
		assertFalse(result.isSuccess());
		assertEquals(ExceptionEnum.CM002.getResultCode(), result.getCode());
		verify(platformHistoryMapper, never()).createPlatformHistory(any());
	}

	@Test
	void createPlatformHistoryFailsWhenCreateOperationFails() {
		PlatformHistory mockPlatformHistory = new PlatformHistory();
		mockPlatformHistory.setRefId(1);
		mockPlatformHistory.setName("ValidName");
		mockPlatformHistory.setVersion("1.0");
		mockPlatformHistory.setMaterialHistoryId(100);
		when(platformHistoryMapper.createPlatformHistory(mockPlatformHistory)).thenReturn(0);

		Result<PlatformHistory> result = platformHistoryServiceImpl.createPlatformHistory(mockPlatformHistory);

		assertNotNull(result);
		assertFalse(result.isSuccess());
		assertEquals(ExceptionEnum.CM008.getResultCode(), result.getCode());
		verify(platformHistoryMapper, times(1)).createPlatformHistory(mockPlatformHistory);
	}


	@Test
	void deletePlatformHistoryByIdHandlesNullId() {
		assertThrows(IllegalArgumentException.class, () -> platformHistoryServiceImpl.deletePlatformHistoryById(null));

		verify(platformHistoryMapper, never()).queryPlatformHistoryById(any());
		verify(platformHistoryMapper, never()).deletePlatformHistoryById(any());
	}

	@Test
	void deletePlatformHistoryByIdHandlesDatabaseException() {
		when(platformHistoryMapper.queryPlatformHistoryById(1)).thenThrow(new RuntimeException("Database error"));

		assertThrows(RuntimeException.class, () -> platformHistoryServiceImpl.deletePlatformHistoryById(1));
		verify(platformHistoryMapper, times(1)).queryPlatformHistoryById(1);
		verify(platformHistoryMapper, never()).deletePlatformHistoryById(any());
	}

	@Test
	void updatePlatformHistoryByIdHandlesDatabaseException() {
		PlatformHistory mockPlatformHistory = new PlatformHistory();
		mockPlatformHistory.setId(1);
		mockPlatformHistory.setName("UpdatedName");
		when(platformHistoryMapper.updatePlatformHistoryById(mockPlatformHistory)).thenThrow(new RuntimeException("Database error"));

		assertThrows(RuntimeException.class, () -> platformHistoryServiceImpl.updatePlatformHistoryById(mockPlatformHistory));
		verify(platformHistoryMapper, times(1)).updatePlatformHistoryById(mockPlatformHistory);
		verify(platformHistoryMapper, never()).queryPlatformHistoryById(any());
	}

	@Test
	void createPlatformHistoryHandlesDuplicateEntry() {
		PlatformHistory mockPlatformHistory = new PlatformHistory();
		mockPlatformHistory.setRefId(1);
		mockPlatformHistory.setName("DuplicateName");
		mockPlatformHistory.setVersion("1.0");
		mockPlatformHistory.setMaterialHistoryId(100);
		when(platformHistoryMapper.createPlatformHistory(mockPlatformHistory)).thenThrow(new RuntimeException("Duplicate entry"));

		assertThrows(RuntimeException.class, () -> platformHistoryServiceImpl.createPlatformHistory(mockPlatformHistory));
		verify(platformHistoryMapper, times(1)).createPlatformHistory(mockPlatformHistory);
	}

	@Test
	void queryPlatformHistoryByConditionHandlesEmptyCondition() {
		PlatformHistory emptyCondition = new PlatformHistory();
		when(platformHistoryMapper.queryPlatformHistoryByCondition(emptyCondition)).thenReturn(List.of());

		List<PlatformHistory> result = platformHistoryServiceImpl.queryPlatformHistoryByCondition(emptyCondition);

		assertNotNull(result);
		assertTrue(result.isEmpty());
		verify(platformHistoryMapper, times(1)).queryPlatformHistoryByCondition(emptyCondition);
	}

	@Test
	void queryPlatformHistoryByConditionHandlesNullResultFromMapper() {
		PlatformHistory condition = new PlatformHistory();
		condition.setName("TestName");
		when(platformHistoryMapper.queryPlatformHistoryByCondition(condition)).thenReturn(null);

		List<PlatformHistory> result = platformHistoryServiceImpl.queryPlatformHistoryByCondition(condition);

		assertTrue(result == null || result.isEmpty());
		verify(platformHistoryMapper, times(1)).queryPlatformHistoryByCondition(condition);
	}

	@Test
	void queryAllPlatformHistoryHandlesLargeDataSet() {
		List<PlatformHistory> mockList = List.of(new PlatformHistory(), new PlatformHistory(), new PlatformHistory());
		when(platformHistoryMapper.queryAllPlatformHistory()).thenReturn(mockList);

		List<PlatformHistory> result = platformHistoryServiceImpl.queryAllPlatformHistory();

		assertNotNull(result);
		assertEquals(3, result.size());
		verify(platformHistoryMapper, times(1)).queryAllPlatformHistory();
	}

	@Test
	void queryPlatformHistoryByIdHandlesNegativeId() {
		assertThrows(IllegalArgumentException.class, () -> platformHistoryServiceImpl.queryPlatformHistoryById(-1));
		verify(platformHistoryMapper, never()).queryPlatformHistoryById(any());
	}

	@Test
	void createPlatformHistoryFailsWhenAllFieldsAreNull() {
		PlatformHistory mockPlatformHistory = new PlatformHistory();

		Result<PlatformHistory> result = platformHistoryServiceImpl.createPlatformHistory(mockPlatformHistory);

		assertNotNull(result);
		assertFalse(result.isSuccess());
		assertEquals(ExceptionEnum.CM002.getResultCode(), result.getCode());
		verify(platformHistoryMapper, never()).createPlatformHistory(any());
	}

	@Test
	void deletePlatformHistoryByIdHandlesNonExistentId() {
		when(platformHistoryMapper.queryPlatformHistoryById(999)).thenReturn(null);

		Result<PlatformHistory> result = platformHistoryServiceImpl.deletePlatformHistoryById(999);

		assertNotNull(result);
		assertTrue(result.isSuccess());
		assertNull(result.getData());
		verify(platformHistoryMapper, times(1)).queryPlatformHistoryById(999);
		verify(platformHistoryMapper, never()).deletePlatformHistoryById(any());
	}

	@Test
	void updatePlatformHistoryByIdFailsWhenNameIsEmpty() {
		PlatformHistory mockPlatformHistory = new PlatformHistory();
		mockPlatformHistory.setId(1);
		mockPlatformHistory.setName("");

		Result<PlatformHistory> result = platformHistoryServiceImpl.updatePlatformHistoryById(mockPlatformHistory);

		assertNotNull(result);
		assertFalse(result.isSuccess());
		assertEquals(ExceptionEnum.CM008.getResultCode(), result.getCode());
	}

	@Test
	void createPlatformHistoryFailsWhenVersionIsWhitespace() {
		PlatformHistory mockPlatformHistory = new PlatformHistory();
		mockPlatformHistory.setRefId(1);
		mockPlatformHistory.setName("ValidName");
		mockPlatformHistory.setVersion("   ");
		mockPlatformHistory.setMaterialHistoryId(100);

		Result<PlatformHistory> result = platformHistoryServiceImpl.createPlatformHistory(mockPlatformHistory);

		assertNotNull(result);
		assertFalse(result.isSuccess());
		assertEquals(ExceptionEnum.CM002.getResultCode(), result.getCode());
		verify(platformHistoryMapper, never()).createPlatformHistory(any());
	}

	@Test
	void createPlatformHistoryFailsWhenNameContainsOnlyWhitespace() {
		PlatformHistory mockPlatformHistory = new PlatformHistory();
		mockPlatformHistory.setRefId(1);
		mockPlatformHistory.setName("   ");
		mockPlatformHistory.setVersion("1.0");
		mockPlatformHistory.setMaterialHistoryId(100);

		Result<PlatformHistory> result = platformHistoryServiceImpl.createPlatformHistory(mockPlatformHistory);

		assertNotNull(result);
		assertFalse(result.isSuccess());
		assertEquals(ExceptionEnum.CM002.getResultCode(), result.getCode());
		verify(platformHistoryMapper, never()).createPlatformHistory(any());
	}

	@Test
	void updatePlatformHistoryByIdFailsWhenVersionIsNull() {
		PlatformHistory mockPlatformHistory = new PlatformHistory();
		mockPlatformHistory.setId(1);
		mockPlatformHistory.setName("ValidName");
		mockPlatformHistory.setVersion(null);

		Result<PlatformHistory> result = platformHistoryServiceImpl.updatePlatformHistoryById(mockPlatformHistory);

		assertNotNull(result);
		assertFalse(result.isSuccess());
		assertEquals(ExceptionEnum.CM008.getResultCode(), result.getCode());
	}

	@Test
	void deletePlatformHistoryByIdHandlesInvalidId() {
		assertThrows(IllegalArgumentException.class, () -> platformHistoryServiceImpl.deletePlatformHistoryById(-10));
		verify(platformHistoryMapper, never()).queryPlatformHistoryById(any());
		verify(platformHistoryMapper, never()).deletePlatformHistoryById(any());
	}

	@Test
	void queryPlatformHistoryByConditionHandlesMultipleConditions() {
		PlatformHistory condition = new PlatformHistory();
		condition.setName("TestName");
		condition.setVersion("1.0");
		List<PlatformHistory> mockList = List.of(new PlatformHistory(), new PlatformHistory());
		when(platformHistoryMapper.queryPlatformHistoryByCondition(condition)).thenReturn(mockList);

		List<PlatformHistory> result = platformHistoryServiceImpl.queryPlatformHistoryByCondition(condition);

		assertNotNull(result);
		assertEquals(2, result.size());
		verify(platformHistoryMapper, times(1)).queryPlatformHistoryByCondition(condition);
	}

	@Test
	void createPlatformHistoryHandlesLongName() {
		PlatformHistory mockPlatformHistory = new PlatformHistory();
		mockPlatformHistory.setRefId(1);
		mockPlatformHistory.setName("A".repeat(256)); // Simulate a long name
		mockPlatformHistory.setVersion("1.0");
		mockPlatformHistory.setMaterialHistoryId(100);

		Result<PlatformHistory> result = platformHistoryServiceImpl.createPlatformHistory(mockPlatformHistory);

		assertNotNull(result);
		assertFalse(result.isSuccess());
		assertEquals(ExceptionEnum.CM008.getResultCode(), result.getCode());
	}

	@Test
	void updatePlatformHistoryByIdHandlesEmptyVersion() {
		PlatformHistory mockPlatformHistory = new PlatformHistory();
		mockPlatformHistory.setId(1);
		mockPlatformHistory.setName("ValidName");
		mockPlatformHistory.setVersion("");

		Result<PlatformHistory> result = platformHistoryServiceImpl.updatePlatformHistoryById(mockPlatformHistory);

		assertNotNull(result);
		assertFalse(result.isSuccess());
		assertEquals(ExceptionEnum.CM008.getResultCode(), result.getCode());
	}

	@Test
	void queryAllPlatformHistoryHandlesEmptyDatabase() {
		when(platformHistoryMapper.queryAllPlatformHistory()).thenReturn(List.of());

		List<PlatformHistory> result = platformHistoryServiceImpl.queryAllPlatformHistory();

		assertNotNull(result);
		assertTrue(result.isEmpty());
		verify(platformHistoryMapper, times(1)).queryAllPlatformHistory();
	}

	@Test
	void createPlatformHistoryHandlesSpecialCharactersInName() {
		PlatformHistory mockPlatformHistory = new PlatformHistory();
		mockPlatformHistory.setRefId(1);
		mockPlatformHistory.setName("!@#$%^&*()");
		mockPlatformHistory.setVersion("1.0");
		mockPlatformHistory.setMaterialHistoryId(100);

		Result<PlatformHistory> result = platformHistoryServiceImpl.createPlatformHistory(mockPlatformHistory);

		assertNotNull(result);
		assertTrue(!result.isSuccess());
		assertEquals(null, result.getData());
		verify(platformHistoryMapper, times(1)).createPlatformHistory(mockPlatformHistory);
	}

	/**
	 * Tests the `createPlatformHistory` method to ensure it fails when the name exceeds the maximum allowed length.
	 *
	 * This test verifies that when a `PlatformHistory` object with a name exceeding the maximum length is provided,
	 * the method does not create the record and returns an appropriate error code. It ensures that the validation
	 * logic for the name length is functioning correctly.
	 *
	 * Test Scenario:
	 * - A `PlatformHistory` object is created with a name longer than the allowed maximum length.
	 * - The `createPlatformHistory` method is called with this object.
	 * - The result is verified to indicate failure.
	 * - The mapper's `createPlatformHistory` method is not invoked.
	 */
	@Test
	void createPlatformHistoryFailsWhenNameExceedsMaxLength() {
		PlatformHistory mockPlatformHistory = new PlatformHistory();
		mockPlatformHistory.setRefId(1);
		mockPlatformHistory.setName("A".repeat(300)); // Simulate a name exceeding max length
		mockPlatformHistory.setVersion("1.0");
		mockPlatformHistory.setMaterialHistoryId(100);

		Result<PlatformHistory> result = platformHistoryServiceImpl.createPlatformHistory(mockPlatformHistory);

		assertNotNull(result);
		assertFalse(result.isSuccess());
		assertEquals(ExceptionEnum.CM008.getResultCode(), result.getCode());
	}

	/**
	 * Tests the `updatePlatformHistoryById` method to ensure it fails when the name exceeds the maximum allowed length.
	 */
	@Test
	void updatePlatformHistoryByIdFailsWhenNameExceedsMaxLength() {
		PlatformHistory mockPlatformHistory = new PlatformHistory();
		mockPlatformHistory.setId(1);
		mockPlatformHistory.setName("A".repeat(300)); // Simulate a name exceeding max length

		Result<PlatformHistory> result = platformHistoryServiceImpl.updatePlatformHistoryById(mockPlatformHistory);

		assertNotNull(result);
		assertFalse(result.isSuccess());
		assertEquals(ExceptionEnum.CM008.getResultCode(), result.getCode());
	}
    /**
	 * Tests the `deletePlatformHistoryById` method to ensure it handles a zero ID correctly.
	 *
	 * This test verifies that when a zero ID is provided to the `deletePlatformHistoryById` method, it throws
	 * an `IllegalArgumentException`. It also checks that the method does not interact with the `platformHistoryMapper`
	 * to query or delete any records, ensuring that invalid input is handled gracefully without unintended side effects.
	 */
	@Test
	void deletePlatformHistoryByIdHandlesZeroId() {
		assertThrows(IllegalArgumentException.class, () -> platformHistoryServiceImpl.deletePlatformHistoryById(0));
		verify(platformHistoryMapper, never()).queryPlatformHistoryById(any());
		verify(platformHistoryMapper, never()).deletePlatformHistoryById(any());
	}
	/**
	 * Tests the `queryPlatformHistoryByCondition` method to ensure it handles null fields in the condition object.
	 *
	 * This test verifies that when the condition object contains null fields, the method interacts with the
	 * `platformHistoryMapper` correctly and returns an empty list. It ensures that the method can handle
	 * null values gracefully without throwing exceptions or returning invalid results.
	 *
	 * Test Scenario:
	 * - The condition object has `null` values for its fields.
	 * - The mapper returns an empty list for the given condition.
	 * - The result is verified to be an empty list.
	 * - The mapper's `queryPlatformHistoryByCondition` method is called exactly once.
	 */
	@Test
	void queryPlatformHistoryByConditionHandlesNullFieldsInCondition() {
		PlatformHistory condition = new PlatformHistory();
		condition.setName(null);
		condition.setVersion(null);
		when(platformHistoryMapper.queryPlatformHistoryByCondition(condition)).thenReturn(List.of());

		List<PlatformHistory> result = platformHistoryServiceImpl.queryPlatformHistoryByCondition(condition);

		assertNotNull(result);
		assertTrue(result.isEmpty());
		verify(platformHistoryMapper, times(1)).queryPlatformHistoryByCondition(condition);
	}

	/**
	 * Tests the `createPlatformHistory` method to ensure it handles a null `materialHistoryId` correctly.
	 */
	@Test
	void createPlatformHistoryHandlesEmptyMaterialHistoryId() {
		PlatformHistory mockPlatformHistory = new PlatformHistory();
		mockPlatformHistory.setRefId(1);
		mockPlatformHistory.setName("ValidName");
		mockPlatformHistory.setVersion("1.0");
		mockPlatformHistory.setMaterialHistoryId(null);

		Result<PlatformHistory> result = platformHistoryServiceImpl.createPlatformHistory(mockPlatformHistory);

		assertNotNull(result);
		assertFalse(result.isSuccess());
		assertEquals(ExceptionEnum.CM002.getResultCode(), result.getCode());
		verify(platformHistoryMapper, never()).createPlatformHistory(any());
	}

}
package com.tinyengine.it.modeldata.service;

import com.tinyengine.it.modeldata.dao.ApiKeyRepository;
import com.tinyengine.it.modeldata.entity.ApiKeyEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApiKeyServiceTest {
	@Mock
	private ApiKeyRepository repository;

	@InjectMocks
	private ApiKeyService apiKeyService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGetValidKey_ValidKey() {
		// Arrange
		String key = "validApiKey";
		ApiKeyEntity entity = new ApiKeyEntity();

		entity.setApiKey(key);
		entity.setStatus(1);
		entity.setExpireTime(LocalDateTime.now().plusDays(1));
		when(repository.findByApiKeyAndStatus(key, 1)).thenReturn(Optional.of(entity));

		// Act
		ApiKeyEntity result = apiKeyService.getValidKey(key);

		// Assert
		assertNotNull(result);
		assertEquals(key, result.getApiKey());
		verify(repository, times(1)).findByApiKeyAndStatus(key, 1);
	}

	@Test
	void testGetValidKey_ExpiredKey() {
		// Arrange
		String key = "expiredApiKey";
		ApiKeyEntity entity = new ApiKeyEntity();
		entity.setApiKey(key);
		entity.setStatus(1);
		entity.setExpireTime(LocalDateTime.now().minusDays(1));
		when(repository.findByApiKeyAndStatus(key, 1)).thenReturn(Optional.of(entity));

		// Act
		ApiKeyEntity result = apiKeyService.getValidKey(key);

		// Assert
		assertNull(result);
		verify(repository, times(1)).findByApiKeyAndStatus(key, 1);
	}

	@Test
	void testGetValidKey_NonExistentKey() {
		// Arrange
		String key = "nonExistentKey";
		when(repository.findByApiKeyAndStatus(key, 1)).thenReturn(Optional.empty());

		// Act
		ApiKeyEntity result = apiKeyService.getValidKey(key);

		// Assert
		assertNull(result);
		verify(repository, times(1)).findByApiKeyAndStatus(key, 1);
	}

}
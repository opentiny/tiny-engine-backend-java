package com.tinyengine.it.modeldata.service;

import com.tinyengine.it.modeldata.dao.ApiKeyRepository;
import com.tinyengine.it.modeldata.entity.ApiKeyEntity;
import com.tinyengine.it.modeldata.util.AESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
@Service
public class ApiKeyService {
	@Autowired
	private ApiKeyRepository repository;
	private static final String MODEL_DATA_SECRET_ENCRYPT_KEY = "MODEL_DATA_SECRET_ENCRYPT_KEY";


	public ApiKeyEntity getValidKey(String key)  {
		Optional<ApiKeyEntity> opt = repository.findByApiKeyAndStatus(key, 1);
		if (opt.isEmpty()) return null;
		ApiKeyEntity entity = opt.get();
		String encryptKey = System.getenv(MODEL_DATA_SECRET_ENCRYPT_KEY);
		if(encryptKey == null || encryptKey.isEmpty()) {
			throw new RuntimeException("Missing environment variable: " + MODEL_DATA_SECRET_ENCRYPT_KEY);
		}

		// 解密 Secret
		String plainSecret = null;
		try {
			plainSecret = AESUtil.decrypt(entity.getApiSecret(), encryptKey);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		entity.setApiSecret(plainSecret); // 此时内存中为明文，用于 HMAC 计算
		if (entity.getExpireTime() != null && entity.getExpireTime().isBefore(LocalDateTime.now())) {
			return null;
		}
		return entity;
	}
}

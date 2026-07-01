package com.tinyengine.it.modeldata.dao;

import com.tinyengine.it.modeldata.entity.ApiKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKeyEntity, Long> {
	Optional<ApiKeyEntity> findByApiKeyAndStatus(String apiKey, Integer status);

}

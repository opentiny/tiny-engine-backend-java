package com.tinyengine.it.modeldata.dao;

import com.tinyengine.it.modeldata.entity.ModelData;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ModelDataRepository extends JpaRepository<ModelData, Long>, JpaSpecificationExecutor<ModelData> {
	/**
	 * 根据模型ID和租户ID分页查询（最常用）
	 */
	Page<ModelData> findByModelIdAndTenantId(Integer modelId, String tenantId, Pageable pageable);

	/**
	 * 根据模型ID查询所有数据（不带租户过滤，谨慎使用）
	 */
	List<ModelData> findByModelId(Integer modelId);

	/**
	 * 根据模型ID和租户ID查询所有数据
	 */
	List<ModelData> findByModelIdAndTenantId(Integer modelId, String tenantId);

	ModelData findByModelIdAndTenantIdAndId(Integer modelId, String tenantId, Integer id);


	/**
	 * 统计某模型下的数据条数
	 */
	long countByModelIdAndTenantId(Integer modelId, String tenantId);
}

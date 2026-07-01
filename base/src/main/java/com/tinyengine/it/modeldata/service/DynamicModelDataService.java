package com.tinyengine.it.modeldata.service;

import com.alibaba.fastjson.TypeReference;
import com.tinyengine.it.model.dto.ParametersDto;
import com.tinyengine.it.model.entity.Model;
import com.tinyengine.it.modeldata.dao.ModelDataRepository;
import com.tinyengine.it.modeldata.entity.ModelData;
import com.tinyengine.it.modeldata.exception.BusinessException;
import com.tinyengine.it.service.material.ModelService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import  com.alibaba.fastjson.JSON;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DynamicModelDataService {

	@Autowired
	private ModelService modelService;
	@Autowired
	private ModelDataRepository modelDataRepo;
	@Autowired
	private ValidationService validationService;
	@Autowired
	private JdbcTemplate jdbcTemplate;   // 用于复杂查询

	// 创建数据实例
	@Transactional
	public ModelData create(Integer modelId, Map<String, Object> data, String tenantId, String userId) throws Exception {
		Model model = modelService.queryModelById(modelId);
		if (model == null) {
			throw new BusinessException("模型不存在");
		}
		List<ParametersDto> fieldDefs = model.getParameters();
		if (!fieldDefs.isEmpty() && !(fieldDefs.get(0) instanceof ParametersDto)) {
			String json = JSON.toJSONString(fieldDefs);
			fieldDefs = JSON.parseObject(json, new TypeReference<List<ParametersDto>>() {});
			model.setParameters(fieldDefs);
		}

		fieldDefs = model.getParameters();
		validationService.validate(data, fieldDefs);

		ModelData entity = new ModelData();
		entity.setModelId(modelId);
		entity.setVersion(model.getVersion());
		entity.setTenantId(tenantId);
		entity.setDataJson(data);
		entity.setCreatedBy(userId);
		entity.setLastUpdatedBy(userId);
		entity.setCreatedTime(LocalDateTime.now());
		entity.setLastUpdatedTime(LocalDateTime.now());
		entity.setLastUpdatedBy(userId);
		return modelDataRepo.save(entity);
	}

	// 更新数据实例
	@Transactional
	public ModelData update(Integer dataId, Map<String, Object> data, String userId) throws Exception {
		ModelData existing = modelDataRepo.findById(Long.valueOf(dataId))
			.orElseThrow(() -> new BusinessException("数据不存在"));
		Model model = modelService.queryModelById(existing.getModelId());
		if (model == null) {
			throw new BusinessException("模型不存在");
		}

		List<ParametersDto> fieldDefs = model.getParameters();
		if (!fieldDefs.isEmpty() && !(fieldDefs.get(0) instanceof ParametersDto)) {
			String json = JSON.toJSONString(fieldDefs);
			fieldDefs = JSON.parseObject(json, new TypeReference<List<ParametersDto>>() {});
			// 将转换后的列表设置回 model（可选，以便后续使用）
			model.setParameters(fieldDefs);
		}

		// 合并数据
		Map<String, Object> merged = new HashMap<>(existing.getDataJson());
		merged.putAll(data);
		validationService.validate(merged, fieldDefs);

		existing.setDataJson(merged);
		existing.setLastUpdatedBy(userId);
		existing.setLastUpdatedTime(LocalDateTime.now());
		return modelDataRepo.save(existing);
	}

	// 删除
	@Transactional
	public void delete(Long dataId) {
		modelDataRepo.deleteById(dataId);
	}

	// 按 ID 查询
	public ModelData get(Long dataId) {
		return modelDataRepo.findById(dataId).orElse(null);
	}

	// 分页查询（支持动态过滤）
	public Page<Map<String, Object>> query(Integer modelId, Map<String, String> filters, String tenantId, Pageable pageable) {
		// 使用 JdbcTemplate 实现动态 JSON 查询
		String tableName = "t_model_data";
		StringBuilder where = new StringBuilder("WHERE model_id = ? AND tenant_id = ?");
		List<Object> args = new ArrayList<>();
		args.add(modelId);
		args.add(tenantId);

		for (Map.Entry<String, String> entry : filters.entrySet()) {
			String field = entry.getKey();
			String value = entry.getValue();
			where.append(" AND JSON_EXTRACT(data_json, '$.\"").append(field).append("\"') = ?");
			args.add(value);
		}

		// 分页查询
		String countSql = "SELECT COUNT(*) FROM " + tableName + " " + where;
		long total = jdbcTemplate.queryForObject(countSql, Long.class, args.toArray());

		String selectSql = "SELECT id, data_json, created_time FROM " + tableName + " " + where +
			" ORDER BY created_time DESC LIMIT ? OFFSET ?";
		args.add(pageable.getPageSize());
		args.add(pageable.getOffset());

		List<Map<String, Object>> list = jdbcTemplate.query(selectSql, args.toArray(), (rs, rowNum) -> {
			Map<String, Object> row = new HashMap<>();
			row.put("id", rs.getLong("id"));
			row.put("data", rs.getString("data_json")); // 可转为 Map
			row.put("createdTime", rs.getTimestamp("created_time"));
			return row;
		});

		return new PageImpl<>(list, pageable, total);
	}
}

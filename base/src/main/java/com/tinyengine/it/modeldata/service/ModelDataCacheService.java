package com.tinyengine.it.modeldata.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tinyengine.it.dynamic.dto.DynamicQuery;
import com.tinyengine.it.modeldata.dao.ModelDataRepository;
import com.tinyengine.it.modeldata.entity.ModelData;

import com.tinyengine.it.modeldata.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@Service
public class ModelDataCacheService {

	@Autowired
	private ModelDataRepository modelDataRepo;

	@Autowired
	private DynamicModelDataService dynamicModelDataService; // 原有的业务服务（含校验）

	@Autowired
	private StringRedisTemplate redisTemplate;

	private static final String CACHE_KEY_PREFIX = "model:data:";
	// TTL 设置为 10 分钟，可根据业务调整
	private static final Duration CACHE_TTL = Duration.ofMinutes(10);

	private String cacheKey(Integer modelId, String tenantId) {
		return CACHE_KEY_PREFIX + modelId + ":" + tenantId;
	}

	/**
	 * 加载数据列表（缓存优先）
	 */
	public List<Map<String, Object>> loadDataList(Integer modelId, String tenantId) {
		String key = cacheKey(modelId, tenantId);
		String cachedJson = redisTemplate.opsForValue().get(key);
		if (cachedJson != null) {
			return JSON.parseObject(cachedJson, new TypeReference<List<Map<String, Object>>>() {});
		}
		List<ModelData> entities = modelDataRepo.findByModelIdAndTenantId(modelId, tenantId);
		List<Map<String, Object>> dataList = entities.stream()
			.map(entity -> {
				Map<String, Object> item = new HashMap<>(entity.getDataJson());
				item.put("_id", entity.getId());
				item.put("_version", entity.getVersion());
				return item;
			})
			.collect(Collectors.toList());
		redisTemplate.opsForValue().set(key, JSON.toJSONString(dataList), CACHE_TTL);
		return dataList;
	}

	private void invalidateCache(Integer modelId, String tenantId) {
		redisTemplate.delete(cacheKey(modelId, tenantId));
	}

	// ========== 核心查询方法 ==========

	/**
	 * 根据 DynamicQuery 进行动态查询（缓存 + 内存过滤）
	 */
	public Page<Map<String, Object>> query(Integer modelId, String tenantId, DynamicQuery query) {
		List<Map<String, Object>> allData = loadDataList(modelId, tenantId);

		Stream<Map<String, Object>> stream = allData.stream();

		// 1. 条件过滤（params）
		if (query.getParams() != null && !query.getParams().isEmpty()) {
			for (Map.Entry<String, Object> entry : query.getParams().entrySet()) {
				String field = entry.getKey();
				Object rawValue = entry.getValue();
				if (rawValue == null) continue;
				String strValue = rawValue.toString();
				String operator = "eq";
				String value = strValue;
				if (strValue.contains(":")) {
					String[] parts = strValue.split(":", 2);
					operator = parts[0];
					value = parts[1];
				}else{
					// 如果字段是 name（不区分大小写）且未指定操作符，默认使用模糊查询
					if ("name".equalsIgnoreCase(field)) {
						operator = "like";
					}
				}
				final String op = operator;
				final String val = value;
				stream = stream.filter(item -> {
					Object fieldValue = item.get(field);
					if (fieldValue == null) return false;
					switch (op) {
						case "eq": return fieldValue.toString().equals(val);
						case "ne": return !fieldValue.toString().equals(val);
						case "gt": return compareNumber(fieldValue, val) > 0;
						case "lt": return compareNumber(fieldValue, val) < 0;
						case "gte": return compareNumber(fieldValue, val) >= 0;
						case "lte": return compareNumber(fieldValue, val) <= 0;
						case "like": return fieldValue.toString().contains(val);
						case "startswith": return fieldValue.toString().startsWith(val);
						case "endswith": return fieldValue.toString().endsWith(val);
						case "in": {
							String[] values = val.split(",");
							for (String v : values) {
								if (fieldValue.toString().equals(v.trim())) return true;
							}
							return false;
						}
						default: return true;
					}
				});
			}
		}

		// 2. 字段选择（fields）
		List<String> fields = query.getFields();
		boolean selectFields = (fields != null && !fields.isEmpty());

		// 先收集过滤后的完整数据（用于分页和排序）
		List<Map<String, Object>> filteredFull = stream.collect(Collectors.toList());

		// 3. 排序
		if (query.getOrderBy() != null && !query.getOrderBy().isEmpty()) {
			String sortField = query.getOrderBy();
			boolean ascending = "ASC".equalsIgnoreCase(query.getOrderType());
			filteredFull.sort((a, b) -> {
				Object va = a.get(sortField);
				Object vb = b.get(sortField);
				if (va == null && vb == null) return 0;
				if (va == null) return ascending ? -1 : 1;
				if (vb == null) return ascending ? 1 : -1;
				int cmp = compare(va, vb);
				return ascending ? cmp : -cmp;
			});
		}

		// 4. 分页
		int total = filteredFull.size();
		int page = Math.max(query.getCurrentPage(), 1);
		int size = query.getPageSize() != null ? query.getPageSize() : 10;
		int start = (page - 1) * size;
		int end = Math.min(start + size, total);
		List<Map<String, Object>> pageData = (start < total)
			? filteredFull.subList(start, end)
			: Collections.emptyList();

		// 5. 字段裁剪（只返回指定字段）
		List<Map<String, Object>> resultList;
		if (selectFields) {
			resultList = pageData.stream()
				.map(item -> {
					Map<String, Object> filteredItem = new LinkedHashMap<>();
					// 始终返回 id
					filteredItem.put("id", item.get("_id"));
					for (String field : fields) {
						if (item.containsKey(field)) {
							filteredItem.put(field, item.get(field));
						}
					}
					return filteredItem;
				})
				.collect(Collectors.toList());
		} else {
			resultList = pageData;
		}

		return new PageImpl<>(resultList, PageRequest.of(page - 1, size), total);
	}

	// 辅助比较
	private int compare(Object a, Object b) {
		if (a instanceof Comparable && b instanceof Comparable) {
			return ((Comparable) a).compareTo(b);
		}
		return a.toString().compareTo(b.toString());
	}

	private int compareNumber(Object fieldValue, String val) {
		try {
			double d1 = Double.parseDouble(fieldValue.toString());
			double d2 = Double.parseDouble(val);
			return Double.compare(d1, d2);
		} catch (NumberFormatException e) {
			return fieldValue.toString().compareTo(val);
		}
	}

	// ========== 增删改 ==========

	public Map<String, Object> add(Integer modelId, String tenantId, Map<String, Object> data, String userId) throws Exception {
		ModelData saved = dynamicModelDataService.create(modelId, data, tenantId, userId);
		invalidateCache(modelId, tenantId);
		Map<String, Object> result = new HashMap<>(saved.getDataJson());
		result.put("_id", saved.getId());
		result.put("_version", saved.getVersion());
		result.put("_row", 1);

		return result;
	}

	public  Map<String, Object>  update(Integer modelId,String tenantId,Integer dataId, Map<String, Object> newData, String userId) throws Exception {
		ModelData entity = modelDataRepo.findByModelIdAndTenantIdAndId(modelId, tenantId, dataId);
		if (entity == null) {
			throw new BusinessException("数据不存在");
		}
		ModelData updated = dynamicModelDataService.update(dataId, newData, userId);
		invalidateCache(updated.getModelId(), updated.getTenantId());
		Map<String, Object> result = new HashMap<>(updated.getDataJson());
		result.put("_id", updated.getId());
		result.put("_version", updated.getVersion());
		result.put("_row", 1);

		return result;
	}


	public Map<String, Object>  delete(Integer modelId,String tenantId,Long dataId)throws Exception {
		ModelData entity = modelDataRepo.findByModelIdAndTenantIdAndId(modelId, tenantId, Math.toIntExact(dataId));
		if (entity == null) {
			throw new BusinessException("数据不存在");
		}
		modelDataRepo.deleteById(dataId);
		invalidateCache(entity.getModelId(), entity.getTenantId());
		Map<String, Object> result = new HashMap<>();
		result.put("_id", entity.getId());
		result.put("_version", entity.getVersion());
		result.put("_row", 1);
		return result;
	}

}

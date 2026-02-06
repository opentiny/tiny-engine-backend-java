package com.tinyengine.it.dynamic.service;

import com.alibaba.fastjson.JSONObject;
import com.tinyengine.it.common.context.LoginUserContext;
import com.tinyengine.it.dynamic.dao.ModelDataDao;
import com.tinyengine.it.dynamic.dto.*;
import com.tinyengine.it.model.entity.Model;
import com.tinyengine.it.service.material.ModelService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;
@Service
public class DynamicService {
	@Autowired
	private ModelDataDao dynamicDao;
	@Autowired
	private ModelService modelService;
    @Autowired
	private  LoginUserContext loginUserContext;


	// 操作类型常量
	private static final String OPERATION_SELECT = "SELECT";
	private static final String OPERATION_INSERT = "INSERT";
	private static final String OPERATION_UPDATE = "UPDATE";
	private static final String OPERATION_DELETE = "DELETE";

	/**
	 * 查询数据
	 * @param dto
	 * @return list
	 */
	public List<JSONObject> query(DynamicQuery dto) {
		String tableName = getTableName(dto.getNameEn());
		Map<String, Object> params = new HashMap<>();
		params.put("tableName", tableName);
		params.put("fields", dto.getFields());
		params.put("conditions", dto.getParams());
		params.put("pageNum", dto.getCurrentPage());
		params.put("pageSize", dto.getPageSize());
		params.put("orderBy", dto.getOrderBy());
		params.put("orderType", dto.getOrderType());

		return dynamicDao.select(params);
	}

	/**
	 * 统计数量
	 * @param tableName
	 * @param conditions
	 * @return long
	 */
	public Long count(String tableName, Map<String, Object> conditions) {
		Map<String, Object> params = new HashMap<>();
		params.put("tableName", tableName);
		params.put("fields", Arrays.asList("COUNT(*) as count"));
		params.put("conditions", conditions);

		List<JSONObject> result = dynamicDao.select(params);
		return Long.parseLong(result.get(0).get("count").toString());
	}

	/**
	 * 分页查询
	 * @param dto
	 * @return map
	 */
	public Map<String, Object> queryWithPage(DynamicQuery dto) {
		if( dto.getNameEn() == null || dto.getNameEn().trim().isEmpty()) {
			throw new IllegalArgumentException("查询操作必须指定模型名称");
		}
		if( dto.getCurrentPage() == null || dto.getCurrentPage() <= 0) {
			dto.setCurrentPage(1);
		}
		if( dto.getPageSize() == null || dto.getPageSize() <= 0) {
			dto.setPageSize(10);
		}
		validateTableExists(dto.getNameEn());
		validateTableAndData(dto.getNameEn(), dto.getParams());
		List<JSONObject> list = query(dto);
		String tableName = getTableName(dto.getNameEn());
		Long total = count(tableName, dto.getParams());

		Map<String, Object> result = new HashMap<>();
		result.put("list", list);
		result.put("total", total);
		result.put("pageNum", dto.getCurrentPage());
		result.put("pageSize", dto.getPageSize());
		result.put("pages", (int) Math.ceil((double) total / dto.getPageSize()));

		return result;
	}

	/**
	 * 插入数据
	 * @param dto
	 * @return map
	 */
	@Transactional
	public Map<String, Object> insert(DynamicInsert dto) {
		if( dto.getNameEn() == null || dto.getNameEn().trim().isEmpty()) {
			throw new IllegalArgumentException("插入操作必须指定模型名称");
		}
        if( dto.getParams() == null || dto.getParams().isEmpty()) {
			throw new IllegalArgumentException("插入数据不能为空");
        }
		validateTableExists(dto.getNameEn());
		validateTableAndData(dto.getNameEn(), dto.getParams());
		String tableName = getTableName(dto.getNameEn());
		Map<String, Object> params = new HashMap<>();
		params.put("tableName", tableName);
		params.put("data", dto.getParams());


		String userId = loginUserContext.getLoginUserId();
		if( userId == null || userId.trim().isEmpty()) {
			List<Model> modelList = modelService.getModelByEnName(dto.getNameEn());
			if( modelList.isEmpty()) {
				throw new IllegalArgumentException("模型不存在: " + dto.getNameEn());
			}else {
				userId=modelList.get(0).getCreatedBy();
			}
		}

		// 添加系统字段
		dto.getParams().put("created_by", userId);
		dto.getParams().put("updated_by", userId);

		Map<String, Object> result = new HashMap<>();
		Long  insertRow = dynamicDao.insert(params);
		BigInteger id = (BigInteger) params.get("id");
		result.put("insert", insertRow);
		result.put("id", id.longValue());
		return result;
	}


	/**
	 * 更新数据
	 * @param dto
	 * @return
	 */
	@Transactional
	public Map<String, Object> update(DynamicUpdate dto) {
		if( dto.getNameEn() == null || dto.getNameEn().trim().isEmpty()) {
			throw new IllegalArgumentException("更新操作必须指定模型名称");
		}
		if (dto.getParams() == null || dto.getParams().isEmpty()) {
			throw new IllegalArgumentException("更新操作必须指定条件");
		}
		if( dto.getData() == null || dto.getData().isEmpty()) {
			throw new IllegalArgumentException("更新数据不能为空");
		}
		validateTableExists(dto.getNameEn());
		validateTableAndData(dto.getNameEn(), dto.getData());
		String tableName = getTableName(dto.getNameEn());
		Map<String, Object> params = new HashMap<>();
		params.put("tableName", tableName);
		params.put("data", dto.getData());
		params.put("conditions", dto.getParams());
		Map<String, Object> result = new HashMap<>();
		Integer update = dynamicDao.update(params);
		result.put("update", update);
		return result;
	}

	/**
	 * 删除数据
	 */
	@Transactional
	public Map<String, Object> delete(DynamicDelete dto) {
		if( dto.getNameEn() == null || dto.getNameEn().trim().isEmpty()) {
			throw new IllegalArgumentException("删除操作必须指定模型名称");
		}
		if (dto.getId() == null ) {
			throw new IllegalArgumentException("删除操作必须指定id");
		}
		validateTableExists(dto.getNameEn());
		String tableName = getTableName(dto.getNameEn());

		Map<String, Object> params = new HashMap<>();
		Map<Object, Object> conditions = new HashMap<>();
		conditions.put("id", dto.getId());
		params.put("tableName", tableName);
		params.put("conditions",conditions);
		Map<String, Object> result = new HashMap<>();
		Integer delete = dynamicDao.delete(params);
		result.put("delete", delete);
		return result;
	}



	/**
	 * 获取表结构
	 */
	public List<Map<String, Object>> getTableStructure(String tableName) {
		validateTableExists(tableName);
		return dynamicDao.getTableStructure(tableName);
	}

	/**
	 * 验证表和数据
	 */
	private void validateTableAndData(String tableName, Map<String, Object> data) {
		if (tableName == null || tableName.trim().isEmpty()) {
			throw new IllegalArgumentException("表名不能为空");
		}

		// 防止SQL注入，验证表名格式
		if (!tableName.matches("^[a-zA-Z_][a-zA-Z0-9_]*$")) {
			throw new IllegalArgumentException("表名格式不正确");
		}

		if (data == null || data.isEmpty()) {
			throw new IllegalArgumentException("数据不能为空");
		}

		// 验证字段名格式
		for (String field : data.keySet()) {
			if (!field.matches("^[a-zA-Z_][a-zA-Z0-9_]*$")) {
				throw new IllegalArgumentException("字段名格式不正确: " + field);
			}
		}
	}
	/**
	 * 验证表是否存在
	 */
	public void validateTableExists(String tableName) {
		List<String> tables = modelService.getAllModelName();
		if (!tables.contains(tableName)) {
			throw new IllegalArgumentException("模型不存在: " + tableName);
		}
	}
	private String getTableName(String modelId) {
		return "dynamic_" + modelId.toLowerCase(Locale.ROOT);
	}


}

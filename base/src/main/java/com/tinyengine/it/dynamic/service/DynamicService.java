package com.tinyengine.it.dynamic.service;

import com.alibaba.fastjson.JSONObject;
import com.tinyengine.it.common.context.LoginUserContext;
import com.tinyengine.it.common.utils.SqlIdentifierValidator;
import com.tinyengine.it.dynamic.dao.ModelDataDao;
import com.tinyengine.it.dynamic.dto.*;
import com.tinyengine.it.login.config.context.DefaultLoginUserContext;
import com.tinyengine.it.model.dto.ParametersDto;
import com.tinyengine.it.model.entity.Model;
import com.tinyengine.it.modeldata.service.ModelDataCacheService;
import com.tinyengine.it.service.material.ModelService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;
@Slf4j
@Service
public class DynamicService {
    @Autowired
    private ModelDataDao dynamicDao;
    @Autowired
    private ModelService modelService;
    @Autowired
    private LoginUserContext loginUserContext;
    @Autowired
    private ModelDataCacheService modelDataCacheService;

    private static final Set<String> SYSTEM_FIELDS = Set.of(
            "id", "created_at", "updated_at", "deleted_at", "created_by", "updated_by"
    );

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
    public Page<Map<String, Object>> queryPage(DynamicQuery dto) {
        String tableName = getTableName(dto.getNameEn());
        Map<String, Object> params = new HashMap<>();
        params.put("tableName", tableName);
        params.put("fields", dto.getFields());
        params.put("conditions", dto.getParams());
        params.put("pageNum", dto.getCurrentPage());
        params.put("pageSize", dto.getPageSize());
        params.put("orderBy", dto.getOrderBy());
        params.put("orderType", dto.getOrderType());

        String tenantId = DefaultLoginUserContext.getCurrentTenant();
        Integer modelId=null;
        List<Model> modelList = modelService.getModelByEnName(dto.getNameEn());
        if (modelList.isEmpty()) {
            throw new IllegalArgumentException("模型不存在: " + dto.getNameEn());
        } else {
            modelId = modelList.get(0).getId();
        }
        log.info("Querying modelId: {}, tenantId: {}, params: {}", modelId, tenantId, params);
        Page<Map<String, Object>> query = modelDataCacheService.query(modelId, tenantId, dto);
        log.info("Queried query: {}, result size: {}", query, query.getContent().size());
        return query;
    }

    public Long count(String tableName, Map<String, Object> conditions) {
        Map<String, Object> params = new HashMap<>();
        params.put("tableName", tableName);
        params.put("fields", Arrays.asList("COUNT(*) as count"));
        params.put("conditions", conditions);

        List<JSONObject> result = dynamicDao.select(params);
        return Long.parseLong(result.get(0).get("count").toString());
    }

    public Map<String, Object> queryWithPage(DynamicQuery dto) {
        if (dto.getNameEn() == null || dto.getNameEn().trim().isEmpty()) {
            throw new IllegalArgumentException("查询操作必须指定模型名称");
        }
        if (dto.getCurrentPage() == null || dto.getCurrentPage() <= 0) {
            dto.setCurrentPage(1);
        }
        if (dto.getPageSize() == null || dto.getPageSize() <= 0) {
            dto.setPageSize(10);
        }
        validateTableExists(dto.getNameEn());
        validateConditionKeys(dto.getParams());
        validateQueryFields(dto);
        Page<Map<String, Object>> queryPage = queryPage(dto);
        List<JSONObject> list= queryPage.map(JSONObject::new).toList();
        long total = queryPage.getTotalElements();


        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        result.put("pageNum", dto.getCurrentPage());
        result.put("pageSize", dto.getPageSize());
        result.put("pages", (int) Math.ceil((double) total / dto.getPageSize()));

        return result;
    }

    @Transactional
    public Map<String, Object> insert(DynamicInsert dto) throws Exception {
        if (dto.getNameEn() == null || dto.getNameEn().trim().isEmpty()) {
            throw new IllegalArgumentException("插入操作必须指定模型名称");
        }
        if (dto.getParams() == null || dto.getParams().isEmpty()) {
            throw new IllegalArgumentException("插入数据不能为空");
        }
        validateTableExists(dto.getNameEn());
        validateTableAndData(dto.getNameEn(), dto.getParams());
        String tableName = getTableName(dto.getNameEn());
        Map<String, Object> params = new HashMap<>();
        params.put("tableName", tableName);
        params.put("data", dto.getParams());

        String tenantId = DefaultLoginUserContext.getCurrentTenant();
        Integer modelId;
        String userId;
        List<Model> modelList = modelService.getModelByEnName(dto.getNameEn());
        if (modelList.isEmpty()) {
            throw new IllegalArgumentException("模型不存在: " + dto.getNameEn());
        } else {
            userId = modelList.get(0).getCreatedBy();
            modelId = modelList.get(0).getId();
        }

        dto.getParams().put("created_by", userId);
        dto.getParams().put("tenantId", tenantId);

        Map<String, Object> result = new HashMap<>();
        log.info("Adding to modelId: {}, params: {}", modelId, dto.getParams());
        log.info("modelId: {}, tenantId: {}, userId: {}", modelId, tenantId, userId);
        Map<String, Object> add = modelDataCacheService.add(modelId, tenantId, dto.getParams(), userId);
        Integer id = (Integer) add.get("_id");
        Integer insertRow = (Integer) add.get("_row");
        result.put("insert", insertRow);
        result.put("id", id);
        return result;
    }

    @Transactional
    public Map<String, Object> update(DynamicUpdate dto) throws Exception {
        if (dto.getNameEn() == null || dto.getNameEn().trim().isEmpty()) {
            throw new IllegalArgumentException("更新操作必须指定模型名称");
        }
        if (dto.getParams() == null || dto.getParams().isEmpty()) {
            throw new IllegalArgumentException("更新操作必须指定条件");
        }
        if (dto.getData() == null || dto.getData().isEmpty()) {
            throw new IllegalArgumentException("更新数据不能为空");
        }
        if(!dto.getParams().containsKey("id")) {
            throw new IllegalArgumentException("更新操作必须指定id");
        }
        validateTableExists(dto.getNameEn());
        validateTableAndData(dto.getNameEn(), dto.getData());
        validateTableAndData(dto.getNameEn(), dto.getParams());
        String tableName = getTableName(dto.getNameEn());
        Map<String, Object> params = new HashMap<>();
        params.put("tableName", tableName);
        params.put("data", dto.getData());
        params.put("conditions", dto.getParams());
        Map<String, Object> result = new HashMap<>();
        String userId = loginUserContext.getLoginUserId();
        String tenantId = DefaultLoginUserContext.getCurrentTenant();
        Integer modelId=null;
        List<Model> modelList = modelService.getModelByEnName(dto.getNameEn());
        if (modelList.isEmpty()) {
            throw new IllegalArgumentException("模型不存在: " + dto.getNameEn());
        } else {
            userId = modelList.get(0).getCreatedBy();
            modelId = modelList.get(0).getId();
        }
        log.info("modelId: {}, tenantId: {}, userId: {}", modelId, tenantId, userId);
        log.info("Updating table: {}, with params: {}, data: {}", tableName, dto.getParams(), dto.getData());
        dto.getData().put("updated_by", userId);
        Map<String, Object> update = modelDataCacheService.update(modelId,tenantId,Integer.parseInt(dto.getParams().get("id").toString()), dto.getData(), userId);
        result.put("update", update.get("_row"));
        return result;
    }

    @Transactional
    public Map<String, Object> delete(DynamicDelete dto) throws Exception {
        if (dto.getNameEn() == null || dto.getNameEn().trim().isEmpty()) {
            throw new IllegalArgumentException("删除操作必须指定模型名称");
        }
        if (dto.getId() == null) {
            throw new IllegalArgumentException("删除操作必须指定id");
        }
        validateTableExists(dto.getNameEn());
        String tableName = getTableName(dto.getNameEn());

        Map<String, Object> params = new HashMap<>();
        Map<Object, Object> conditions = new HashMap<>();
        conditions.put("id", dto.getId());
        params.put("tableName", tableName);
        params.put("conditions", conditions);
        String tenantId = DefaultLoginUserContext.getCurrentTenant();
        Integer modelId=null;
        List<Model> modelList = modelService.getModelByEnName(dto.getNameEn());
        if (modelList.isEmpty()) {
            throw new IllegalArgumentException("模型不存在: " + dto.getNameEn());
        } else {
            modelId = modelList.get(0).getId();
        }
        log.info("Deleting from table: {}, with params: {}", tableName, params);
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> objectMap = modelDataCacheService.delete(modelId,tenantId,Long.valueOf(dto.getId()));
        result.put("delete", objectMap.get("_row"));
        return result;
    }

    public List<Map<String, Object>> getTableStructure(String tableName) {
        validateTableExists(tableName);
        return dynamicDao.getTableStructure(tableName);
    }

    private Set<String> getAllowedFields(String nameEn) {
        List<Model> modelList = modelService.getModelByEnName(nameEn);
        if (modelList == null || modelList.isEmpty()) {
            return Collections.emptySet();
        }
        Model model = modelList.get(0);
        Set<String> allowed = new HashSet<>(SYSTEM_FIELDS);
        if (model.getParameters() != null) {
            for (Object param : model.getParameters()) {
                String prop = extractProp(param);
                if (prop != null) {
                    allowed.add(prop);
                }
            }
        }
        return allowed;
    }

    @SuppressWarnings("unchecked")
    private String extractProp(Object param) {
        if (param instanceof ParametersDto) {
            return ((ParametersDto) param).getProp();
        }
        if (param instanceof Map) {
            Object value = ((Map<String, Object>) param).get("prop");
            return value != null ? value.toString() : null;
        }
        return null;
    }

    private void validateQueryFields(DynamicQuery dto) {
        Set<String> allowedFields = getAllowedFields(dto.getNameEn());

        if (dto.getFields() != null && !dto.getFields().isEmpty()) {
            for (String field : dto.getFields()) {
                SqlIdentifierValidator.validate(field);
                if (!allowedFields.contains(field)) {
                    throw new IllegalArgumentException("不允许的字段: " + field);
                }
            }
        }

        if (dto.getOrderBy() != null && !dto.getOrderBy().isEmpty()) {
            SqlIdentifierValidator.validate(dto.getOrderBy());
            if (!allowedFields.contains(dto.getOrderBy())) {
                throw new IllegalArgumentException("不允许的排序字段: " + dto.getOrderBy());
            }
        }

        if (dto.getOrderType() != null) {
            SqlIdentifierValidator.validateOrderType(dto.getOrderType());
        }
    }

    private void validateTableAndData(String tableName, Map<String, Object> data) {
        if (tableName == null || tableName.trim().isEmpty()) {
            throw new IllegalArgumentException("表名不能为空");
        }

        if (!tableName.matches("^[a-zA-Z_][a-zA-Z0-9_]*$")) {
            throw new IllegalArgumentException("表名格式不正确");
        }

        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("数据不能为空");
        }

        for (String field : data.keySet()) {
            SqlIdentifierValidator.validate(field);
        }
    }

    private void validateConditionKeys(Map<String, Object> conditions) {
        if (conditions == null || conditions.isEmpty()) {
            return;
        }
        for (String key : conditions.keySet()) {
            SqlIdentifierValidator.validate(key);
        }
    }

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

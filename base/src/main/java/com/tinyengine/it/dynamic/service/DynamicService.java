package com.tinyengine.it.dynamic.service;

import com.alibaba.fastjson.JSONObject;
import com.tinyengine.it.common.context.LoginUserContext;
import com.tinyengine.it.common.utils.SqlIdentifierValidator;
import com.tinyengine.it.dynamic.dao.ModelDataDao;
import com.tinyengine.it.dynamic.dto.DynamicDelete;
import com.tinyengine.it.dynamic.dto.DynamicInsert;
import com.tinyengine.it.dynamic.dto.DynamicQuery;
import com.tinyengine.it.dynamic.dto.DynamicUpdate;
import com.tinyengine.it.model.dto.ParametersDto;
import com.tinyengine.it.model.entity.Model;
import com.tinyengine.it.service.material.ModelService;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Service
@SuppressWarnings({
    "PMD.AtLeastOneConstructor",
    "PMD.CyclomaticComplexity",
    "PMD.DataflowAnomalyAnalysis",
    "PMD.GodClass",
    "PMD.LawOfDemeter",
    "PMD.LocalVariableCouldBeFinal",
    "PMD.MethodArgumentCouldBeFinal",
    "PMD.OnlyOneReturn",
    "PMD.ShortVariable",
    "PMD.TooManyMethods"
})
public class DynamicService {
    @Autowired private ModelDataDao dynamicDao;
    @Autowired private ModelService modelService;
    @Autowired private LoginUserContext loginUserContext;

    private static final Set<String> SYSTEM_FIELDS =
            Set.of("id", "created_at", "updated_at", "deleted_at", "created_by", "updated_by");
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final String TABLE_NAME_KEY = "tableName";
    private static final String CONDITIONS_KEY = "conditions";

    public List<JSONObject> query(DynamicQuery dto) {
        String tableName = getTableName(dto.getNameEn());
        Map<String, Object> params = new HashMap<>();
        params.put(TABLE_NAME_KEY, tableName);
        params.put("fields", dto.getFields());
        params.put(CONDITIONS_KEY, dto.getParams());
        params.put("pageNum", dto.getCurrentPage());
        params.put("pageSize", dto.getPageSize());
        params.put("orderBy", dto.getOrderBy());
        params.put("orderType", dto.getOrderType());

        return dynamicDao.select(params);
    }

    public Long count(String tableName, Map<String, Object> conditions) {
        Map<String, Object> params = new HashMap<>();
        params.put(TABLE_NAME_KEY, tableName);
        params.put("fields", Arrays.asList("COUNT(*) as count"));
        params.put(CONDITIONS_KEY, conditions);

        List<JSONObject> result = dynamicDao.select(params);
        return Long.parseLong(result.get(0).get("count").toString());
    }

    public Map<String, Object> queryWithPage(DynamicQuery dto) {
        if (dto.getNameEn() == null || dto.getNameEn().isBlank()) {
            throw new IllegalArgumentException("查询操作必须指定模型名称");
        }
        if (dto.getCurrentPage() == null || dto.getCurrentPage() <= 0) {
            dto.setCurrentPage(1);
        }
        if (dto.getPageSize() == null || dto.getPageSize() <= 0) {
            dto.setPageSize(DEFAULT_PAGE_SIZE);
        }
        validateTableExists(dto.getNameEn());
        validateConditionKeys(dto.getParams());
        validateQueryFields(dto);
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

    @Transactional
    public Map<String, Object> insert(DynamicInsert dto) {
        if (dto.getNameEn() == null || dto.getNameEn().isBlank()) {
            throw new IllegalArgumentException("插入操作必须指定模型名称");
        }
        if (dto.getParams() == null || dto.getParams().isEmpty()) {
            throw new IllegalArgumentException("插入数据不能为空");
        }
        validateTableExists(dto.getNameEn());
        validateTableAndData(dto.getNameEn(), dto.getParams());
        String tableName = getTableName(dto.getNameEn());
        Map<String, Object> params = new HashMap<>();
        params.put(TABLE_NAME_KEY, tableName);
        params.put("data", dto.getParams());

        String userId = loginUserContext.getLoginUserId();
        if (userId == null || userId.isBlank()) {
            List<Model> modelList = modelService.getModelByEnName(dto.getNameEn());
            if (modelList.isEmpty()) {
                throw new IllegalArgumentException("模型不存在: " + dto.getNameEn());
            } else {
                userId = modelList.get(0).getCreatedBy();
            }
        }

        dto.getParams().put("created_by", userId);
        dto.getParams().put("updated_by", userId);

        Map<String, Object> result = new HashMap<>();
        Long insertRow = dynamicDao.insert(params);
        BigInteger id = (BigInteger) params.get("id");
        result.put("insert", insertRow);
        result.put("id", id.longValue());
        return result;
    }

    @Transactional
    public Map<String, Object> update(DynamicUpdate dto) {
        if (dto.getNameEn() == null || dto.getNameEn().isBlank()) {
            throw new IllegalArgumentException("更新操作必须指定模型名称");
        }
        if (dto.getParams() == null || dto.getParams().isEmpty()) {
            throw new IllegalArgumentException("更新操作必须指定条件");
        }
        if (dto.getData() == null || dto.getData().isEmpty()) {
            throw new IllegalArgumentException("更新数据不能为空");
        }
        validateTableExists(dto.getNameEn());
        validateTableAndData(dto.getNameEn(), dto.getData());
        validateTableAndData(dto.getNameEn(), dto.getParams());
        String tableName = getTableName(dto.getNameEn());
        Map<String, Object> params = new HashMap<>();
        params.put(TABLE_NAME_KEY, tableName);
        params.put("data", dto.getData());
        params.put(CONDITIONS_KEY, dto.getParams());
        Map<String, Object> result = new HashMap<>();
        Integer update = dynamicDao.update(params);
        result.put("update", update);
        return result;
    }

    @Transactional
    public Map<String, Object> delete(DynamicDelete dto) {
        if (dto.getNameEn() == null || dto.getNameEn().isBlank()) {
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
        params.put(TABLE_NAME_KEY, tableName);
        params.put(CONDITIONS_KEY, conditions);
        Map<String, Object> result = new HashMap<>();
        Integer delete = dynamicDao.delete(params);
        result.put("delete", delete);
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
        SqlIdentifierValidator.validate(tableName);

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
        if (modelId == null || modelId.isBlank()) {
            throw new IllegalArgumentException("模型名称不能为空");
        }
        String tableName = "dynamic_" + modelId.toLowerCase(Locale.ROOT);
        return SqlIdentifierValidator.requireValidIdentifier(tableName);
    }
}

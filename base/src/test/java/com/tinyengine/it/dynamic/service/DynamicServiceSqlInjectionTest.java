package com.tinyengine.it.dynamic.service;

import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.JSONObject;
import com.tinyengine.it.common.context.LoginUserContext;
import com.tinyengine.it.dynamic.dao.ModelDataDao;
import com.tinyengine.it.dynamic.dto.DynamicQuery;
import com.tinyengine.it.dynamic.dto.DynamicUpdate;
import com.tinyengine.it.model.dto.ParametersDto;
import com.tinyengine.it.model.entity.Model;
import com.tinyengine.it.service.material.ModelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class DynamicServiceSqlInjectionTest {

    @Mock
    private ModelDataDao dynamicDao;

    @Mock
    private ModelService modelService;

    @Mock
    private LoginUserContext loginUserContext;

    @InjectMocks
    private DynamicService dynamicService;

    private Model testModel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectUtil.setFieldValue(dynamicService, "dynamicDao", dynamicDao);
        ReflectUtil.setFieldValue(dynamicService, "modelService", modelService);
        ReflectUtil.setFieldValue(dynamicService, "loginUserContext", loginUserContext);

        testModel = new Model();
        testModel.setNameEn("test_model");
        testModel.setCreatedBy("1");
        List<ParametersDto> params = new ArrayList<>();
        ParametersDto p1 = new ParametersDto();
        p1.setProp("id");
        params.add(p1);
        ParametersDto p2 = new ParametersDto();
        p2.setProp("username");
        params.add(p2);
        ParametersDto p3 = new ParametersDto();
        p3.setProp("email");
        params.add(p3);
        testModel.setParameters(params);

        when(modelService.getAllModelName()).thenReturn(Arrays.asList("test_model"));
        when(modelService.getModelByEnName("test_model")).thenReturn(Arrays.asList(testModel));
    }

    private void mockCountResult() {
        JSONObject countResult = new JSONObject();
        countResult.put("count", 0L);
        // First call returns empty list for query, second call returns count
        when(dynamicDao.select(any()))
                .thenReturn(Collections.emptyList())
                .thenReturn(Arrays.asList(countResult));
    }

    // --- fields injection prevention ---

    @Test
    void queryRejectsSqlExpressionInFields() {
        DynamicQuery dto = new DynamicQuery();
        dto.setNameEn("test_model");
        dto.setFields(Arrays.asList("id", "@@version AS db_version"));
        dto.setParams(Map.of("id", 1));

        assertThrows(IllegalArgumentException.class, () -> dynamicService.queryWithPage(dto));
    }

    @Test
    void queryRejectsSubqueryInFields() {
        DynamicQuery dto = new DynamicQuery();
        dto.setNameEn("test_model");
        dto.setFields(Arrays.asList("id",
                "(SELECT GROUP_CONCAT(table_name) FROM information_schema.tables WHERE table_schema=DATABASE()) AS all_tables"));
        dto.setParams(Map.of("id", 1));

        assertThrows(IllegalArgumentException.class, () -> dynamicService.queryWithPage(dto));
    }

    @Test
    void queryRejectsPasswordExtractionInFields() {
        DynamicQuery dto = new DynamicQuery();
        dto.setNameEn("test_model");
        dto.setFields(Arrays.asList("id", "(SELECT password FROM t_user WHERE id=1) AS leaked_pwd"));
        dto.setParams(Map.of("id", 1));

        assertThrows(IllegalArgumentException.class, () -> dynamicService.queryWithPage(dto));
    }

    @Test
    void queryRejectsUnknownFieldNotInModel() {
        DynamicQuery dto = new DynamicQuery();
        dto.setNameEn("test_model");
        dto.setFields(Arrays.asList("id", "nonexistent_column"));
        dto.setParams(Map.of("id", 1));

        assertThrows(IllegalArgumentException.class, () -> dynamicService.queryWithPage(dto));
    }

    @Test
    void queryAcceptsValidFields() {
        DynamicQuery dto = new DynamicQuery();
        dto.setNameEn("test_model");
        dto.setFields(Arrays.asList("id", "username"));
        dto.setParams(Map.of("id", 1));
        dto.setCurrentPage(1);
        dto.setPageSize(10);

        mockCountResult();

        assertDoesNotThrow(() -> dynamicService.queryWithPage(dto));
    }

    // --- orderBy injection prevention ---

    @Test
    void queryRejectsSqlInjectionInOrderBy() {
        DynamicQuery dto = new DynamicQuery();
        dto.setNameEn("test_model");
        dto.setParams(Map.of("id", 1));
        dto.setOrderBy("id; DROP TABLE dynamic_test_model--");

        assertThrows(IllegalArgumentException.class, () -> dynamicService.queryWithPage(dto));
    }

    @Test
    void queryRejectsSleepInOrderBy() {
        DynamicQuery dto = new DynamicQuery();
        dto.setNameEn("test_model");
        dto.setParams(Map.of("id", 1));
        dto.setOrderBy("SLEEP(5)");

        assertThrows(IllegalArgumentException.class, () -> dynamicService.queryWithPage(dto));
    }

    @Test
    void queryRejectsUnknownOrderByField() {
        DynamicQuery dto = new DynamicQuery();
        dto.setNameEn("test_model");
        dto.setParams(Map.of("id", 1));
        dto.setOrderBy("nonexistent_column");

        assertThrows(IllegalArgumentException.class, () -> dynamicService.queryWithPage(dto));
    }

    @Test
    void queryAcceptsValidOrderBy() {
        DynamicQuery dto = new DynamicQuery();
        dto.setNameEn("test_model");
        dto.setParams(Map.of("id", 1));
        dto.setOrderBy("username");
        dto.setOrderType("ASC");
        dto.setCurrentPage(1);
        dto.setPageSize(10);

        mockCountResult();

        assertDoesNotThrow(() -> dynamicService.queryWithPage(dto));
    }

    // --- orderType injection prevention ---

    @Test
    void queryRejectsInvalidOrderType() {
        DynamicQuery dto = new DynamicQuery();
        dto.setNameEn("test_model");
        dto.setParams(Map.of("id", 1));
        dto.setOrderType("; DROP TABLE users--");

        assertThrows(IllegalArgumentException.class, () -> dynamicService.queryWithPage(dto));
    }

    @Test
    void queryAcceptsAscOrderType() {
        DynamicQuery dto = new DynamicQuery();
        dto.setNameEn("test_model");
        dto.setParams(Map.of("id", 1));
        dto.setOrderType("ASC");
        dto.setCurrentPage(1);
        dto.setPageSize(10);

        mockCountResult();

        assertDoesNotThrow(() -> dynamicService.queryWithPage(dto));
    }

    @Test
    void queryAcceptsDescOrderType() {
        DynamicQuery dto = new DynamicQuery();
        dto.setNameEn("test_model");
        dto.setParams(Map.of("id", 1));
        dto.setOrderType("DESC");
        dto.setCurrentPage(1);
        dto.setPageSize(10);

        mockCountResult();

        assertDoesNotThrow(() -> dynamicService.queryWithPage(dto));
    }

    // --- update() condition fields validation ---

    @Test
    void updateRejectsInvalidConditionField() {
        DynamicUpdate dto = new DynamicUpdate();
        dto.setNameEn("test_model");
        dto.setData(new HashMap<>(Map.of("username", "newvalue")));
        dto.setParams(Map.of("id; DROP TABLE users--", 1));

        assertThrows(IllegalArgumentException.class, () -> dynamicService.update(dto));
    }

    @Test
    void updateRejectsInvalidDataField() {
        DynamicUpdate dto = new DynamicUpdate();
        dto.setNameEn("test_model");
        dto.setData(Map.of("username; DROP TABLE users--", "newvalue"));
        dto.setParams(Map.of("id", 1));

        assertThrows(IllegalArgumentException.class, () -> dynamicService.update(dto));
    }

    // --- validateTableExists ---

    @Test
    void queryRejectsUnknownModel() {
        DynamicQuery dto = new DynamicQuery();
        dto.setNameEn("nonexistent_model");
        dto.setParams(Map.of("id", 1));

        assertThrows(IllegalArgumentException.class, () -> dynamicService.queryWithPage(dto));
    }

    // --- queryWithPage accepts null/empty params ---

    @Test
    void queryAcceptsNullParams() {
        DynamicQuery dto = new DynamicQuery();
        dto.setNameEn("test_model");
        dto.setParams(null);
        dto.setCurrentPage(1);
        dto.setPageSize(10);

        mockCountResult();

        assertDoesNotThrow(() -> dynamicService.queryWithPage(dto));
    }

    @Test
    void queryAcceptsEmptyParams() {
        DynamicQuery dto = new DynamicQuery();
        dto.setNameEn("test_model");
        dto.setParams(Collections.emptyMap());
        dto.setCurrentPage(1);
        dto.setPageSize(10);

        mockCountResult();

        assertDoesNotThrow(() -> dynamicService.queryWithPage(dto));
    }
}

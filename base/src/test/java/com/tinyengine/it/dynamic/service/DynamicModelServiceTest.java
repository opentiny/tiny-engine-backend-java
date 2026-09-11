package com.tinyengine.it.dynamic.service;

import cn.hutool.core.util.ReflectUtil;
import com.tinyengine.it.common.context.LoginUserContext;
import com.tinyengine.it.dynamic.dto.DynamicDelete;
import com.tinyengine.it.dynamic.dto.DynamicInsert;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SuppressWarnings({
    "PMD.AtLeastOneConstructor",
    "PMD.AvoidDuplicateLiterals",
    "PMD.DataflowAnomalyAnalysis",
    "PMD.ExcessiveImports",
    "PMD.JUnitAssertionsShouldIncludeMessage",
    "PMD.JUnitTestContainsTooManyAsserts",
    "PMD.LawOfDemeter",
    "PMD.LinguisticNaming",
    "PMD.LocalVariableCouldBeFinal",
    "PMD.LongVariable",
    "PMD.ShortVariable",
    "PMD.SingularField",
    "PMD.TooManyMethods",
    "PMD.TooManyStaticImports"
})
class DynamicModelServiceTest {

    private static final int PAGE_SIZE = 10;
    private static final long COUNT_RESULT = 5L;
    private static final long TOTAL_COUNT = 10L;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private NamedParameterJdbcTemplate namedJdbcTemplate;

    @Mock
    private LoginUserContext loginUserContext;

    @Mock
    private ModelService modelService;

    @InjectMocks
    private DynamicModelService dynamicModelService;

    private Model testModel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectUtil.setFieldValue(dynamicModelService, "jdbcTemplate", jdbcTemplate);
        ReflectUtil.setFieldValue(dynamicModelService, "loginUserContext", loginUserContext);
        ReflectUtil.setFieldValue(dynamicModelService, "namedJdbcTemplate", namedJdbcTemplate);
        ReflectUtil.setFieldValue(dynamicModelService, "modelService", modelService);

        testModel = new Model();
        testModel.setNameEn("test_table");
        testModel.setCreatedBy("1");
        ParametersDto p1 = new ParametersDto();
        p1.setProp("id");
        ParametersDto p2 = new ParametersDto();
        p2.setProp("name");
        testModel.setParameters(Arrays.asList(p1, p2));

        when(modelService.getModelByEnName("test_table")).thenReturn(Arrays.asList(testModel));
    }


    @Test
    void createDynamicTable() {
        // Arrange
        Model model = new Model();
        model.setNameEn("test_table");
        ParametersDto parametersDto = new ParametersDto();
        parametersDto.setProp("name");
        parametersDto.setType("String");
        parametersDto.setRequired(true);
        parametersDto.setDefaultValue("1");
        parametersDto.setDescription("1");
        model.setParameters(Collections.singletonList(parametersDto));

        // Mock JdbcTemplate behavior
        doNothing().when(jdbcTemplate).execute(anyString());

        // Act & Assert
        assertDoesNotThrow(() -> dynamicModelService.createDynamicTable(model));
        verify(jdbcTemplate, times(1)).execute(anyString());
    }

    @Test
    void dropDynamicTable() {
        // Arrange
        Model model = new Model();
        model.setNameEn("test_table");

        // Mock JdbcTemplate behavior
        doNothing().when(jdbcTemplate).execute(anyString());

        // Act & Assert
        assertDoesNotThrow(() -> dynamicModelService.dropDynamicTable(model));
        verify(jdbcTemplate, times(1)).execute("DROP TABLE IF EXISTS dynamic_test_table;");
    }

    @Test
    void initializeDynamicTable() {
        // Arrange
        Model model = new Model();
        model.setNameEn("test_table");
        ParametersDto param1 = new ParametersDto();
        param1.setProp("name");
        param1.setType("String");
        param1.setDefaultValue("default_name");
        param1.setRequired(true);
        model.setParameters(Collections.singletonList(param1));

        Long userId = 1L;

        // Mock JdbcTemplate behavior
        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1);

        // Act & Assert
        assertDoesNotThrow(() -> dynamicModelService.initializeDynamicTable(model, userId));
        verify(jdbcTemplate, times(1)).update(anyString(), any(Object[].class));
    }

    @Test
    void dynamicQuery() {
        // Arrange
        String tableName = "test_table";
        List<String> fields = Arrays.asList("id", "name");
        Map<String, Object> conditions = Map.of("id", 1);
        String orderBy = "id DESC";
        Integer limit = PAGE_SIZE;

        List<Map<String, Object>> mockResult = new ArrayList<>();
        mockResult.add(Map.of("id", 1, "name", "test_name"));

        when(namedJdbcTemplate.queryForList(anyString(), anyMap())).thenReturn(mockResult);

        // Act
        List<Map<String, Object>> result =
                dynamicModelService.dynamicQuery(tableName, fields, conditions, orderBy, limit);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("test_name", result.get(0).get("name"));
        verify(namedJdbcTemplate, times(1)).queryForList(anyString(), anyMap());
    }

    @Test
    void dynamicCount() {
        // Arrange
        String tableName = "test_table";
        Map<String, Object> conditions = Map.of("id", 1);

        List<Map<String, Object>> mockResult = new ArrayList<>();
        mockResult.add(Map.of("count", COUNT_RESULT));

        when(namedJdbcTemplate.queryForList(anyString(), anyMap())).thenReturn(mockResult);

        // Act
        List<Map<String, Object>> result = dynamicModelService.dynamicCount(tableName, conditions);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(COUNT_RESULT, result.get(0).get("count"));
        verify(namedJdbcTemplate, times(1)).queryForList(anyString(), anyMap());
    }

    @Test
    void count() {
        // Arrange
        String tableName = "test_table";
        Map<String, Object> conditions = Map.of("id", 1);

        List<Map<String, Object>> mockResult = new ArrayList<>();
        mockResult.add(Map.of("count", TOTAL_COUNT));

        when(namedJdbcTemplate.queryForList(anyString(), anyMap())).thenReturn(mockResult);

        // Act
        Long result = dynamicModelService.count(tableName, conditions);

        // Assert
        assertNotNull(result);
        assertEquals(TOTAL_COUNT, result);
        verify(namedJdbcTemplate, times(1)).queryForList(anyString(), anyMap());
    }

    @Test
    void queryWithPage() {
        // Arrange
        DynamicQuery dto = new DynamicQuery();
        dto.setNameEn("test_table");
        dto.setFields(Arrays.asList("id", "name"));
        dto.setParams(Map.of("id", 1));
        dto.setOrderBy("id");
        dto.setOrderType("DESC");
        dto.setCurrentPage(1);
        dto.setPageSize(PAGE_SIZE);

        List<Map<String, Object>> mockData = new ArrayList<>();
        mockData.add(Map.of("id", 1, "name", "test_name"));

        when(namedJdbcTemplate.queryForList(anyString(), anyMap())).thenReturn(mockData);
        when(namedJdbcTemplate.queryForList(anyString(), anyMap())).thenReturn(List.of(Map.of("count", 1L)));

        // Act
        Map<String, Object> result = dynamicModelService.queryWithPage(dto);

        // Assert
        assertNotNull(result);
        assertTrue((Boolean) result.get("success"));
        assertEquals(1L, result.get("total"));
        assertEquals(1, ((List<?>) result.get("data")).size());
        verify(namedJdbcTemplate, times(2)).queryForList(anyString(), anyMap());
    }



    @Test
    void createData() {
        // Arrange
        DynamicInsert dataDto = new DynamicInsert();
        dataDto.setNameEn("test_table");
        dataDto.setParams(Map.of("name", "test"));

        when(loginUserContext.getLoginUserId()).thenReturn("1");
        when(jdbcTemplate.update(any(PreparedStatementCreator.class), any(KeyHolder.class))).thenAnswer(invocation -> {
            KeyHolder keyHolder = invocation.getArgument(1);
            keyHolder.getKeyList().add(Map.of("GENERATED_KEY", 1L));
            return 1;
        });

        // Act
        Map<String, Object> result = dynamicModelService.createData(dataDto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.get("id"));
        verify(jdbcTemplate, times(1)).update(any(PreparedStatementCreator.class), any(KeyHolder.class));
    }

    @Test
    void getDataById() {
        // Arrange
        String modelId = "test_table";
        Long id = 1L;

        List<Map<String, Object>> mockResult = new ArrayList<>();
        mockResult.add(Map.of("id", 1, "name", "test_name"));

        when(jdbcTemplate.queryForList(anyString(), Optional.ofNullable(any()))).thenReturn(mockResult);

        // Act
        Map<String, Object> result = dynamicModelService.getDataById(modelId, id);

        // Assert
        assertNotNull(result);
        assertEquals("test_name", result.get("name"));
        verify(jdbcTemplate, times(1)).queryForList(anyString(), Optional.ofNullable(any()));
    }

    @Test
    void updateDateById() {
        // Arrange
        DynamicUpdate dto = new DynamicUpdate();
        dto.setNameEn("test_table");
        dto.setParams(Map.of("id", 1));
        dto.setData(Map.of("name", "updated_name"));

        when(jdbcTemplate.update(anyString(), any(Object[].class))).thenReturn(1);

        // Act
        Map<String, Object> result = dynamicModelService.updateDateById(dto);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.get("rowsAffected"));
        verify(jdbcTemplate, times(1)).update(anyString(), any(Object[].class));
    }

    @Test
    void deleteDataById() {
        // Arrange
        DynamicDelete dto = new DynamicDelete();
        dto.setNameEn("test_table");
        dto.setId(1);

        when(jdbcTemplate.update(anyString(), Optional.ofNullable(any()))).thenReturn(1);

        // Act
        Map<String, Object> result = dynamicModelService.deleteDataById(dto);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.get("rowsAffected"));
        verify(jdbcTemplate, times(1)).update(anyString(), Optional.ofNullable(any()));
    }


    @Test
    void testCreateDynamicTable() {
        Model model = new Model();
        model.setNameEn("test_table");
        ParametersDto parametersDto = new ParametersDto();
        parametersDto.setProp("name");
        parametersDto.setType("String");
        parametersDto.setRequired(true);
        parametersDto.setDefaultValue("1");
        parametersDto.setDescription("1");
        model.setParameters(Collections.singletonList(parametersDto));

        doNothing().when(jdbcTemplate).execute(anyString());

        assertDoesNotThrow(() -> dynamicModelService.createDynamicTable(model));
        verify(jdbcTemplate, times(1)).execute(anyString());
    }

    @Test
    void testDropDynamicTable() {
        Model model = new Model();
        model.setNameEn("test_table");

        doNothing().when(jdbcTemplate).execute(anyString());

        assertDoesNotThrow(() -> dynamicModelService.dropDynamicTable(model));
        verify(jdbcTemplate, times(1)).execute(anyString());
    }

    @Test
    void testDynamicQuery() {
        String tableName = "test_table";
        List<String> fields = Arrays.asList("id", "name");
        Map<String, Object> conditions = Map.of("id", 1);

        when(namedJdbcTemplate.queryForList(anyString(), anyMap())).thenReturn(new ArrayList<>());

        List<Map<String, Object>> result = dynamicModelService.dynamicQuery(tableName, fields, conditions, null, null);
        assertNotNull(result);
        verify(namedJdbcTemplate, times(1)).queryForList(anyString(), anyMap());
    }

    @Test
    void testCreateData() {
        DynamicInsert dataDto = new DynamicInsert();
        dataDto.setNameEn("test_table");
        dataDto.setParams(Map.of("name", "test"));

        when(loginUserContext.getLoginUserId()).thenReturn("1");
        when(jdbcTemplate.update(any(PreparedStatementCreator.class), any(KeyHolder.class)))
                .thenReturn(1);

        Map<String, Object> result = dynamicModelService.createData(dataDto);
        assertNotNull(result);
        verify(jdbcTemplate, times(1))
                .update(any(PreparedStatementCreator.class), any(KeyHolder.class));
    }

    @Test
    void testDeleteDataById() {
        DynamicDelete dto = new DynamicDelete();
        dto.setNameEn("test_table");
        dto.setId(1);

        when(jdbcTemplate.update(anyString(), Optional.ofNullable(any()))).thenReturn(1);

        Map<String, Object> result = dynamicModelService.deleteDataById(dto);
        assertEquals(1, result.get("rowsAffected"));
        verify(jdbcTemplate, times(1)).update(anyString(), Optional.ofNullable(any()));
    }


}

package com.tinyengine.it.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.model.entity.Datasource;
import com.tinyengine.it.service.app.DatasourceService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

/**
 * test case
 *
 * @since 2024-10-29
 */
class DataSourceControllerTest {
    @Mock
    private DatasourceService datasourceService;
    @InjectMocks
    private DataSourceController dataSourceController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllSources() {
        List<Datasource> mockData = Arrays.asList(new Datasource());
        when(datasourceService.queryDatasourceByCondition(any(Datasource.class))).thenReturn(mockData);

        Result<List<Datasource>> result = dataSourceController.getAllSources(1);
        Assertions.assertEquals(mockData, result.getData());
    }

    @Test
    void testGetSourcesById() {
        Datasource mockData = new Datasource();
        when(datasourceService.queryDatasourceById(1)).thenReturn(mockData);

        Result<Datasource> result = dataSourceController.getSourcesById(1);
        Assertions.assertEquals(mockData, result.getData());
    }

    @Test
    void testCreateSources() {
        Result<Datasource> mockData = new Result<>();
        when(datasourceService.createDatasource(any(Datasource.class))).thenReturn(mockData);

        Result<Datasource> result = dataSourceController.createSources(new Datasource());
        Assertions.assertEquals(mockData, result);
    }

    @Test
    void testUpdateSources() {
        Result<Datasource> mockData = new Result<>();
        when(datasourceService.updateDatasourceById(any(Datasource.class))).thenReturn(mockData);

        Result<Datasource> result = dataSourceController.updateSources(1, new Datasource());
        Assertions.assertEquals(mockData, result);
    }

    @Test
    void testDeleteSources() {
        Result<Datasource> mockData = new Result<>();
        when(datasourceService.deleteDatasourceById(1)).thenReturn(mockData);

        Result<Datasource> result = dataSourceController.deleteSources(1);
        Assertions.assertEquals(mockData, result);
    }
}


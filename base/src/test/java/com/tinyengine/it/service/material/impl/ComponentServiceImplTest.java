/**
 * Copyright (c) 2023 - present TinyEngine Authors.
 * Copyright (c) 2023 - present Huawei Cloud Computing Technologies Co., Ltd.
 *
 * Use of this source code is governed by an MIT-style license.
 *
 * THE OPEN SOURCE SOFTWARE IN THIS PRODUCT IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL,
 * BUT WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS FOR
 * A PARTICULAR PURPOSE. SEE THE APPLICABLE LICENSES FOR MORE DETAILS.
 *
 */

package com.tinyengine.it.service.material.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.utils.Utils;
import com.tinyengine.it.mapper.ComponentMapper;
import com.tinyengine.it.model.dto.BundleMaterial;
import com.tinyengine.it.model.dto.FileResult;
import com.tinyengine.it.model.dto.JsonFile;
import com.tinyengine.it.model.entity.Component;
import com.tinyengine.it.model.entity.MaterialComponent;
import com.tinyengine.it.model.entity.MaterialHistoryComponent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * test case
 *
 * @since 2024-10-29
 */
class ComponentServiceImplTest {
    @Mock
    private ComponentMapper componentMapper;
    @InjectMocks
    private ComponentServiceImpl componentServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllComponent() {
        Component component = new Component();
        when(componentMapper.queryAllComponent()).thenReturn(Arrays.<Component>asList(component));

        List<Component> result = componentServiceImpl.findAllComponent();
        Assertions.assertEquals(component, result.get(0));
    }

    @Test
    void testFindComponentById() {
        Component mockData = new Component();
        when(componentMapper.queryComponentById(1)).thenReturn(mockData);

        Component result = componentServiceImpl.findComponentById(1);
        Assertions.assertEquals(mockData, result);
    }

    @Test
    void testFindComponentByCondition() {
        Component component = new Component();
        Component param = new Component();
        when(componentMapper.queryComponentByCondition(param)).thenReturn(Arrays.asList(component));

        List<Component> result = componentServiceImpl.findComponentByCondition(param);
        Assertions.assertEquals(component, result.get(0));
    }

    @Test
    void testDeleteComponentById() {
        when(componentMapper.deleteComponentById(1)).thenReturn(2);

        Integer result = componentServiceImpl.deleteComponentById(1);
        Assertions.assertEquals(2, result);
    }

    @Test
    void testUpdateComponentById() {
        Component param = new Component();
        when(componentMapper.updateComponentById(param)).thenReturn(1);

        Integer result = componentServiceImpl.updateComponentById(param);
        Assertions.assertEquals(1, result);
    }

    @Test
    void testCreateComponent() {
        Component param = new Component();
        when(componentMapper.createComponent(param)).thenReturn(1);

        Integer result = componentServiceImpl.createComponent(param);
        Assertions.assertEquals(1, result);
    }

    @Test
    void testReadFileAndBulkCreate() {
        Component component = new Component();
        component.setId(1);
        List<Component> mockResult = Arrays.asList(component);

        when(componentMapper.queryComponentByCondition(any(Component.class))).thenReturn(mockResult);
        when(componentMapper.updateComponentById(any(Component.class))).thenReturn(Integer.valueOf(0));
        when(componentMapper.createComponent(any(Component.class))).thenReturn(Integer.valueOf(0));
        when(componentMapper.createMaterialComponent(any(MaterialComponent.class))).thenReturn(Integer.valueOf(0));
        when(componentMapper.createMaterialHistoryComponent(any(MaterialHistoryComponent.class)))
                .thenReturn(Integer.valueOf(0));

        MultipartFile file = mock(MultipartFile.class);
        JsonFile jsonFile = new JsonFile();
        HashMap<String, Object> fileContent = new HashMap<>();
        BundleMaterial bundleMaterial = new BundleMaterial();
        ArrayList<Map<String, Object>> components = new ArrayList<>();
        HashMap<String, Object> componentdata = new HashMap<>();
        componentdata.put("component", "name");
        components.add(componentdata);
        bundleMaterial.setComponents(components);
        bundleMaterial.setSnippets(new ArrayList<>());
        HashMap<Object, Object> material = new HashMap<>();
        material.put("framework", "Vue");
        material.put("materials", bundleMaterial);

        fileContent.put("data", material);
        jsonFile.setFileContent(fileContent);
        jsonFile.setFileName("fileName");
        Result<JsonFile> mockData = Result.success(jsonFile);
        try (MockedStatic<Utils> utils = Mockito.mockStatic(Utils.class)) {
            utils.when(() -> Utils.parseJsonFileStream(file)).thenReturn(mockData);
            Result<FileResult> result = componentServiceImpl.readFileAndBulkCreate(file);
            Assertions.assertEquals(1, result.getData().getUpdateNum());
        }
    }
}
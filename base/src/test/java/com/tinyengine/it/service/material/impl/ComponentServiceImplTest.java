package com.tinyengine.it.service.material.impl;

import static org.mockito.Mockito.when;

import com.tinyengine.it.mapper.ComponentMapper;
import com.tinyengine.it.model.entity.Component;

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
}

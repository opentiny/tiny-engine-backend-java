package com.tinyengine.it.service.material.impl;

import static org.mockito.Mockito.when;

import cn.hutool.core.util.ReflectUtil;

import com.tinyengine.it.mapper.ModelMapper;
import com.tinyengine.it.model.dto.ParametersDto;
import com.tinyengine.it.model.entity.Model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({
    "PMD.AtLeastOneConstructor",
    "PMD.LawOfDemeter",
    "PMD.LinguisticNaming",
    "PMD.LocalVariableCouldBeFinal"
})
class ModelServiceImplTest {

    @Mock private ModelMapper modelMapper;

    @InjectMocks private ModelServiceImpl modelServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectUtil.setFieldValue(modelServiceImpl, "baseMapper", modelMapper);
    }

    @Test
    void getTableByIdShouldIgnoreNullIsModel() {
        Model model = new Model();
        model.setNameEn("main_model");

        ParametersDto field = new ParametersDto();
        field.setProp("name");
        field.setType("String");
        field.setIsModel(null);

        List<ParametersDto> parameters = new ArrayList<>();
        parameters.add(field);
        model.setParameters(parameters);

        when(modelMapper.selectById(1)).thenReturn(model);

        String sql = modelServiceImpl.getTableById(1);

        Assertions.assertTrue(sql.contains("CREATE TABLE main_model"));
        Assertions.assertTrue(sql.contains("name VARCHAR(500)"));
    }
}

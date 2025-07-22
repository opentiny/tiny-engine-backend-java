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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tinyengine.it.common.enums.Enums;
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.common.log.SystemServiceLog;
import com.tinyengine.it.mapper.ModelMapper;
import com.tinyengine.it.model.dto.MethodDto;
import com.tinyengine.it.model.dto.RequestParameter;
import com.tinyengine.it.model.dto.ResponseParameter;
import com.tinyengine.it.model.entity.Model;
import com.tinyengine.it.service.material.ModelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class ModelServiceImpl extends ServiceImpl<ModelMapper, Model> implements ModelService {
    /**
     * 查询表t_model信息
     *
     * @param id
     * @return the Model
     */
    @Override
    @SystemServiceLog(description = "根据id查询model实现方法")
    public Model queryModelById(Integer id) {
        return this.baseMapper.selectById(id);
    }

    /**
     * 根据name查询表t_model信息
     *
     * @param name
     * @return the model list
     */
    @Override
    @SystemServiceLog(description = "根据名称查询model实现方法")
    public List<Model> getModelByName(String name) {
        QueryWrapper<Model> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", name);
        return  this.baseMapper.selectList(queryWrapper);
    }

    /**
     * 分页查询表t_model信息
     *
     * @return the list
     */
    @Override
    @SystemServiceLog(description = "分页查询model实现方法")
    public Page<Model> pageQuery(int currentPage, int pageSize, String name) {
        Page<Model> page = new Page<>(currentPage, pageSize);
        QueryWrapper<Model> queryWrapper = new QueryWrapper<>();
        if(name != null && !name.isEmpty()){
            queryWrapper.like("name", name);
        }
        page(page, queryWrapper);
        return page;
    }

    /**
     * 创建t_material
     *
     * @param model
     * @return the model
     * @ param the model
     */
    @Override
    @SystemServiceLog(description = "创建model实现方法")
    public Model createModel(Model model) {
        List<MethodDto> methodDtos = new ArrayList<>();
        methodDtos.add(getMethodDto(Enums.methodName.CREATED.getValue(), model));
        methodDtos.add(getMethodDto(Enums.methodName.UPDATE.getValue(), model));
        methodDtos.add(getMethodDto(Enums.methodName.QUERY.getValue(), model));
        methodDtos.add(getMethodDto(Enums.methodName.DELETE.getValue(), model));
        model.setMethod(methodDtos);
        int result = this.baseMapper.createModel(model);
        if (result != 1) {
            throw new ServiceException(ExceptionEnum.CM001.getResultCode(), ExceptionEnum.CM001.getResultCode());
        }
        return model;
    }

    /**
     * 删除t_model
     *
     * @param id
     * @return the Model
     * @ param the id
     */
    @Override
    @SystemServiceLog(description = "根据id删除model实现方法")
    public Model deleteModelById(Integer id) {
        Model model = this.baseMapper.selectById(id);
        int result = this.baseMapper.deleteById(id);
        if (result != 1) {
            throw new ServiceException(ExceptionEnum.CM001.getResultCode(), ExceptionEnum.CM001.getResultCode());
        }
        return model;
    }

    /**
     * 修改t_model
     *
     * @param model
     * @return the model
     * @ param the model
     */
    @Override
    @SystemServiceLog(description = "根据id修改model实现方法")
    public Model updateModelById(Model model) {
        List<MethodDto> methodDtos = new ArrayList<>();
        methodDtos.add(getMethodDto(Enums.methodName.CREATED.getValue(), model));
        methodDtos.add(getMethodDto(Enums.methodName.UPDATE.getValue(), model));
        methodDtos.add(getMethodDto(Enums.methodName.QUERY.getValue(), model));
        methodDtos.add(getMethodDto(Enums.methodName.DELETE.getValue(), model));
        model.setMethod(methodDtos);
        if (model.getId() == null) {
            throw new ServiceException(ExceptionEnum.CM002.getResultCode(), ExceptionEnum.CM002.getResultCode());
        }
        int result = this.baseMapper.updateModelById(model);
        if (result != 1) {
            throw new ServiceException(ExceptionEnum.CM001.getResultCode(), ExceptionEnum.CM001.getResultCode());
        }
        Model modelResult = this.baseMapper.selectById(model.getId());
        return modelResult;
    }

    private MethodDto getMethodDto(String name, Model model) {
        MethodDto methodDto = new MethodDto();
        methodDto.setName(name);
        RequestParameter requestParameter = new RequestParameter();
        requestParameter.setName("id");
        requestParameter.setType("Number");

        if (name != Enums.methodName.DELETE.getValue()) {
            requestParameter.setName(Enums.methodName.NAME.getValue());
            requestParameter.setType(Enums.methodName.TYPE.getValue());
            requestParameter.setChildren(model.getParameters());
        }

        ResponseParameter code = new ResponseParameter();
        code.setName("code");
        code.setType("Number");
        ResponseParameter message = new ResponseParameter();
        message.setName("message");
        message.setType("String");
        List<ResponseParameter> responseParameterList = new ArrayList<>();
        responseParameterList.add(code);
        responseParameterList.add(message);
        methodDto.setRequestParameters(Arrays.asList(requestParameter));
        methodDto.setResponseParameters(responseParameterList);
        return methodDto;
    }


}

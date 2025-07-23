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
     * @param nameCn
     * @return the model list
     */
    @Override
    @SystemServiceLog(description = "根据名称查询model实现方法")
    public List<Model> getModelByName(String nameCn) {
        QueryWrapper<Model> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name_cn", nameCn);
        return this.baseMapper.selectList(queryWrapper);
    }

    /**
     * 分页查询表t_model信息
     *
     * @return the list
     */
    @Override
    @SystemServiceLog(description = "分页查询model实现方法")
    public Page<Model> pageQuery(int currentPage, int pageSize, String nameCn, String nameEn) {
        Page<Model> page = new Page<>(currentPage, pageSize);
        QueryWrapper<Model> queryWrapper = new QueryWrapper<>();

        // 判断 nameCn 是否存在
        if (nameCn != null && !nameCn.isEmpty()) {
            queryWrapper.like("name_cn", nameCn);
        }

        // 判断 nameEn 是否存在
        if (nameEn != null && !nameEn.isEmpty()) {
            if (nameCn != null && !nameCn.isEmpty()) {
                queryWrapper.or().like("name_en", nameEn);
            } else {
                queryWrapper.like("name_en", nameEn);
            }
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
        List<ResponseParameter> responseParameterList = getResponseParameters(name);
        RequestParameter requestParameter = new RequestParameter();
        requestParameter.setProp(Enums.methodParam.ID.getValue());
        requestParameter.setType(Enums.paramType.NUMBER.getValue());
        List<RequestParameter> parameterList = new ArrayList<>();
        if (name.equals(Enums.methodName.QUERY.getValue())) {
            RequestParameter currentPage = new RequestParameter();
            currentPage.setProp(Enums.methodParam.CURRENTPAGE.getValue());
            currentPage.setType(Enums.paramType.NUMBER.getValue());
            RequestParameter pageSize = new RequestParameter();
            pageSize.setProp(Enums.methodParam.PAGESIZE.getValue());
            pageSize.setType(Enums.paramType.NUMBER.getValue());
            RequestParameter nameCn = new RequestParameter();
            nameCn.setProp(Enums.methodParam.NAMECN.getValue());
            nameCn.setType(Enums.paramType.STRING.getValue());
            RequestParameter nameEn = new RequestParameter();
            nameEn.setProp(Enums.methodParam.NAMEEN.getValue());
            nameEn.setType(Enums.paramType.STRING.getValue());
            parameterList.add(currentPage);
            parameterList.add(pageSize);
            parameterList.add(nameCn);
            parameterList.add(nameEn);

        }
        if (name != Enums.methodName.DELETE.getValue()) {
            requestParameter.setProp(Enums.methodParam.PARAMS.getValue());
            requestParameter.setType(Enums.paramType.OBJECT.getValue());
            requestParameter.setChildren(model.getParameters());
            parameterList.add(requestParameter);

            methodDto.setRequestParameters(parameterList);
            methodDto.setResponseParameters(responseParameterList);
            return methodDto;
        }

        parameterList.add(requestParameter);
        methodDto.setRequestParameters(parameterList);
        methodDto.setResponseParameters(responseParameterList);
        return methodDto;
    }

    private static List<ResponseParameter> getResponseParameters(String name) {
        ResponseParameter code = new ResponseParameter();
        code.setProp(Enums.methodParam.CODE.getValue());
        code.setType(Enums.paramType.NUMBER.getValue());
        ResponseParameter message = new ResponseParameter();
        message.setProp(Enums.methodParam.MESSAGE.getValue());
        message.setType(Enums.paramType.STRING.getValue());
        ResponseParameter data = new ResponseParameter();
        data.setProp(Enums.methodParam.DATA.getValue());
        data.setType(Enums.paramType.ARRAY.getValue());

        List<ResponseParameter> responseParameterList = new ArrayList<>();
        if (name.equals(Enums.methodName.QUERY.getValue())) {
            ResponseParameter total = new ResponseParameter();
            total.setProp(Enums.methodParam.TOTAL.getValue());
            total.setType(Enums.paramType.NUMBER.getValue());
            responseParameterList.add(total);
        }

        responseParameterList.add(code);
        responseParameterList.add(message);
        responseParameterList.add(data);
        return responseParameterList;
    }


}

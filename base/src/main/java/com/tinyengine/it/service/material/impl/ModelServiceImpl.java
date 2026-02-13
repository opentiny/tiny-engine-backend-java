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
import com.tinyengine.it.common.context.LoginUserContext;
import com.tinyengine.it.common.enums.Enums;
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.common.log.SystemServiceLog;
import com.tinyengine.it.common.utils.JsonUtils;
import com.tinyengine.it.dynamic.service.DynamicModelService;
import com.tinyengine.it.mapper.ModelMapper;
import com.tinyengine.it.model.dto.MethodDto;
import com.tinyengine.it.model.dto.ParametersDto;
import com.tinyengine.it.model.dto.RequestParameter;
import com.tinyengine.it.model.dto.ResponseParameter;
import com.tinyengine.it.model.entity.Model;
import com.tinyengine.it.service.material.ModelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ModelServiceImpl extends ServiceImpl<ModelMapper, Model> implements ModelService {

    @Autowired
    private DynamicModelService dynamicModelService;

    @Autowired
    private LoginUserContext loginUserContext;
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
     * 根据name查询表t_model信息
     *
     * @param nameEn
     * @return the model list
     */
    @Override
    @SystemServiceLog(description = "根据名称查询model实现方法")
    public List<Model> getModelByEnName(String nameEn) {
        QueryWrapper<Model> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name_en", nameEn);
        return this.baseMapper.selectList(queryWrapper);    }

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
        queryWrapper.eq("created_by", loginUserContext.getLoginUserId());
        queryWrapper.eq("tenant_id", loginUserContext.getTenantId());
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
    @Transactional
    public Model createModel(Model model) {
        // 验证模型唯一性
        QueryWrapper<Model> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name_en", model.getNameEn());
        if (this.baseMapper.selectCount(queryWrapper) > 0) {
            throw new ServiceException(ExceptionEnum.CM003.getResultCode(), "Model with the same name already exists");
        }
        List<MethodDto> methodDtos = new ArrayList<>();
        methodDtos.add(getMethodDto(Enums.methodName.CREATED.getValue(), Enums.methodName.INSERTAPI.getValue(), model));
        methodDtos.add(getMethodDto(Enums.methodName.UPDATE.getValue(), Enums.methodName.UPDATEAPI.getValue(), model));
        methodDtos.add(getMethodDto(Enums.methodName.QUERY.getValue(), Enums.methodName.QUERYAPI.getValue(), model));
        methodDtos.add(getMethodDto(Enums.methodName.DELETE.getValue(), Enums.methodName.DELETEAPI.getValue(), model));
        model.setMethod(methodDtos);
        model.setTenantId(loginUserContext.getTenantId());
        int result = this.baseMapper.createModel(model);
        if (result != 1) {
            throw new ServiceException(ExceptionEnum.CM001.getResultCode(), ExceptionEnum.CM001.getResultCode());
        }
        // 创建动态表
        dynamicModelService.createDynamicTable(model);
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
    @Transactional
    public Model deleteModelById(Integer id) {
        Model model = this.baseMapper.selectById(id);
        int result = this.baseMapper.deleteById(id);
        if (result != 1) {
            throw new ServiceException(ExceptionEnum.CM001.getResultCode(), ExceptionEnum.CM001.getResultCode());
        }
        try {
            dynamicModelService.dropDynamicTable(model);
        } catch (Exception e) {
            log.error("deleteModelById", e);
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
    @Transactional
    public Model updateModelById(Model model) {
        List<MethodDto> methodDtos = new ArrayList<>();
        methodDtos.add(getMethodDto(Enums.methodName.CREATED.getValue(), Enums.methodName.INSERTAPI.getValue(), model));
        methodDtos.add(getMethodDto(Enums.methodName.UPDATE.getValue(), Enums.methodName.UPDATEAPI.getValue(), model));
        methodDtos.add(getMethodDto(Enums.methodName.QUERY.getValue(), Enums.methodName.QUERYAPI.getValue(), model));
        methodDtos.add(getMethodDto(Enums.methodName.DELETE.getValue(), Enums.methodName.DELETEAPI.getValue(), model));
        model.setMethod(methodDtos);
        if (model.getId() == null) {
            throw new ServiceException(ExceptionEnum.CM002.getResultCode(), ExceptionEnum.CM002.getResultCode());
        }
        int result = this.baseMapper.updateModelById(model);
        if (result != 1) {
            throw new ServiceException(ExceptionEnum.CM001.getResultCode(), ExceptionEnum.CM001.getResultCode());
        }


        // 修改动态表
        try {
            dynamicModelService.modifyTableStructure(model);
        } catch (Exception e) {
            log.error("updateModelById", e);
            throw new ServiceException(ExceptionEnum.CM001.getResultCode(), ExceptionEnum.CM001.getResultCode());
        }
        Model modelResult = this.baseMapper.selectById(model.getId());
        return modelResult;
    }

    /**
     * 获取Model建表sql
     *
     * @param id
     * @return the String
     * @ param the id
     */
    @Override
    public String getTableById(Integer id) {
        Model model = this.baseMapper.selectById(id);
        StringBuilder sql = new StringBuilder(getTableByModle(model));
        List<?> rawList = model.getParameters();
        List<ParametersDto> fields = rawList.stream()
                .map(item -> JsonUtils.MAPPER.convertValue(item, ParametersDto.class))
                .collect(Collectors.toList());
        fields.forEach(item -> {
            if(item.getIsModel()) {
                Model result = this.baseMapper.selectById(item.getDefaultValue());
                sql.append(getTableByModle(result));
            }
        });
        return sql.toString();
    }

    /**
     * 获取所有模型的建表SQL语句
     * @return 拼接好的SQL语句字符串，每个表的SQL用分号分隔并换行
     * @throws IOException 如果JSON解析失败
     */
    @Override
    public String getAllTable() {
        // 查询所有模型
        List<Model> modelList = this.baseMapper.selectList(null);
        if (CollectionUtils.isEmpty(modelList)) {
            return "";
        }

        StringJoiner sqlJoiner = new StringJoiner(" ");

        modelList.stream()
                .map(this::getTableByModle)
                .forEach(sqlJoiner::add);

        return sqlJoiner.toString();
    }

    /**
     * 获取所有模型名称列表
     *
     * @return 模型名称列表
     */
    @Override
    public List<String> getAllModelName() {
        List<Model> modelList = this.baseMapper.selectList(null);
        if (!CollectionUtils.isEmpty(modelList)) {
            return modelList.stream()
                    .map(Model::getNameEn)
                    .collect(Collectors.toList());
        }
        return null;
    }

    private String getTableByModle(Model model) {
        List<?> rawList = model.getParameters();
        List<ParametersDto> fields = rawList.stream()
                .map(item -> JsonUtils.MAPPER.convertValue(item, ParametersDto.class))
                .collect(Collectors.toList());

        StringBuilder sql = new StringBuilder("CREATE TABLE " + model.getNameEn() + " (");

        for (int i = 0; i < fields.size(); i++) {
            ParametersDto field = fields.get(i);

            String prop = field.getProp();
            String type = field.getType();
            String defaultValue = field.getDefaultValue();

            // 根据字段类型映射为 SQL 数据类型
            String sqlType = mapJavaTypeToSQL(type);

            sql.append(prop).append(" ").append(sqlType);

            if (defaultValue != null && !defaultValue.isEmpty()) {
                sql.append(" DEFAULT ").append(defaultValue);
            }

            // 如果不是最后一个字段，添加逗号
            if (i != fields.size() - 1) {
                sql.append(", ");
            }
        }

        sql.append(");");
        return sql.toString();
    }

    private static String mapJavaTypeToSQL(String javaType) {
        if (javaType == null) {
            return "VARCHAR(255)"; // 默认处理
        }
        switch (javaType) {
            case "String":
                return "VARCHAR(500)";
            case "Number":
                return "INT";
            case "Boolean":
                return "BOOLEAN";
            case "Date":
                return "TIMESTAMP";
            case "Enum":
                return "LONGTEXT";
            default:
                return "LONGTEXT"; // 默认处理
        }
    }

    private MethodDto getMethodDto(String name, String nameEn, Model model) {
        MethodDto methodDto = new MethodDto();
        methodDto.setName(name);
        methodDto.setNameEn(nameEn);
        List<ResponseParameter> responseParameterList = getResponseParameters(name);
        RequestParameter requestParameter = new RequestParameter();
        requestParameter.setProp(Enums.methodParam.ID.getValue());
        requestParameter.setType(Enums.paramType.NUMBER.getValue());
        List<RequestParameter> parameterList = new ArrayList<>();
        RequestParameter requestNameEn = new RequestParameter();
        requestNameEn.setProp(Enums.methodParam.NAMEEN.getValue());
        requestNameEn.setType(Enums.paramType.STRING.getValue());
        parameterList.add(requestNameEn);
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
            parameterList.add(currentPage);
            parameterList.add(pageSize);
            parameterList.add(nameCn);

        }
        if( name.equals(Enums.methodName.UPDATE.getValue())) {
            RequestParameter requestParameterData = new RequestParameter();
            requestParameterData.setProp(Enums.methodParam.DATA.getValue());
            requestParameterData.setType(Enums.paramType.OBJECT.getValue());
            requestParameterData.setChildren(model.getParameters());
            parameterList.add(requestParameterData);
        }
        if (!name.equals(Enums.methodName.DELETE.getValue())) {
            RequestParameter requestParameterparams = new RequestParameter();
            requestParameterparams.setProp(Enums.methodParam.PARAMS.getValue());
            requestParameterparams.setType(Enums.paramType.OBJECT.getValue());
            requestParameterparams.setChildren(model.getParameters());
            parameterList.add(requestParameterparams);
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
        data.setType(Enums.paramType.ENUM.getValue());

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

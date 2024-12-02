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

package com.tinyengine.it.service.app.impl;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.common.log.SystemServiceLog;
import com.tinyengine.it.mapper.PageTemplateMapper;
import com.tinyengine.it.model.entity.PageTemplate;
import com.tinyengine.it.service.app.PageTemplateService;

import lombok.extern.slf4j.Slf4j;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Page template service.
 *
 * @since 2024-10-20
 */
@Service
@Slf4j
public class PageTemplateServiceImpl implements PageTemplateService {
    @Autowired
    private PageTemplateMapper pageTemplateMapper;

    /**
     * 查询表page_template所有数据
     *
     * @param name name
     * @param type name
     * @return PageTemplate
     * @throws ServiceException ServiceException
     */
    @Override
    @SystemServiceLog(description = "获取页面模版列表实现方法")
    public Result<List<PageTemplate>> queryAllPageTemplate(String name, String type) throws ServiceException {
        List<PageTemplate> pageTemplates = new ArrayList<>();
        if (name == null || name.isEmpty()) {
            pageTemplates = pageTemplateMapper.queryAllPageTemplate(type);
            return Result.success(pageTemplates);
        }
        pageTemplates = pageTemplateMapper.queryPageTemplateByName(name, type);
        return Result.success(pageTemplates);
    }

    /**
     * 根据主键id查询表page_template信息
     *
     * @param id id
     * @return query result
     * @throws ServiceException ServiceException
     */
    @Override
    @SystemServiceLog(description = "根据id获取页面模版详情实现方法")
    public Result<PageTemplate> queryPageTemplateById(@Param("id") Integer id) throws ServiceException {
        PageTemplate pageTemplate = pageTemplateMapper.queryPageTemplateById(id);
        return Result.success(pageTemplate);
    }

    /**
     * 根据条件查询表page_template数据
     *
     * @param pageTemplate pageTemplate
     * @return query result
     * @throws ServiceException ServiceException
     */
    @Override
    @SystemServiceLog(description = "获取页面模版条件查询实现方法")
    public List<PageTemplate> queryPageTemplateByCondition(PageTemplate pageTemplate) throws ServiceException {
        return pageTemplateMapper.queryPageTemplateByCondition(pageTemplate);
    }

    /**
     * 根据主键id列表删除表page_template数据
     *
     * @param ids id
     * @return execute success data number
     * @throws ServiceException ServiceException
     */
    @Override
    @SystemServiceLog(description = "批量删除页面模版实现方法")
    public Result<Integer> deletePageTemplateByIds(@Param("ids") List<Integer> ids) throws ServiceException {
        if (ids.isEmpty()) {
            return Result.failed(ExceptionEnum.CM002);
        }
        Integer result = pageTemplateMapper.deletePageTemplateByIds(ids);
        if (result != ids.size()) {
            return Result.failed(ExceptionEnum.CM001);
        }
        return Result.success(result);
    }

    /**
     * 根据主键id更新表page_template数据
     *
     * @param pageTemplate pageTemplate
     * @return execute success data number
     */
    @Override
    public Integer updatePageTemplateById(PageTemplate pageTemplate) {
        return pageTemplateMapper.updatePageTemplateById(pageTemplate);
    }

    /**
     * 新增表page_template数据
     *
     * @param pageTemplate pageTemplate
     * @return execute success data number
     * @throws ServiceException ServiceException
     */
    @Override
    @SystemServiceLog(description = "创建页面模版实现方法")
    public Result<PageTemplate> createPageTemplate(PageTemplate pageTemplate) throws ServiceException {
        PageTemplate queryPageTemplate = new PageTemplate();
        queryPageTemplate.setName(pageTemplate.getName());
        List<PageTemplate> pageTemplateResult = pageTemplateMapper.queryPageTemplateByCondition(queryPageTemplate);
        if (!pageTemplateResult.isEmpty()) {
            return Result.failed(ExceptionEnum.CM003);
        }
        int result = pageTemplateMapper.createPageTemplate(pageTemplate);
        if (result < 1) {
            return Result.failed(ExceptionEnum.CM001);
        }
        PageTemplate templateResult = pageTemplateMapper.queryPageTemplateById(pageTemplate.getId());
        return Result.success(templateResult);
    }
}

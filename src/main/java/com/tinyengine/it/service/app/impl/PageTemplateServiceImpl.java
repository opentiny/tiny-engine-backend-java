
package com.tinyengine.it.service.app.impl;

import com.tinyengine.it.mapper.PageTemplateMapper;
import com.tinyengine.it.model.entity.PageTemplate;
import com.tinyengine.it.service.app.PageTemplateService;

import lombok.extern.slf4j.Slf4j;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
     */
    @Override
    public List<PageTemplate> queryAllPageTemplate() {
        return pageTemplateMapper.queryAllPageTemplate();
    }

    /**
     * 根据主键id查询表page_template信息
     *
     * @param id id
     * @return query result
     */
    @Override
    public PageTemplate queryPageTemplateById(@Param("id") Integer id) {
        return pageTemplateMapper.queryPageTemplateById(id);
    }

    /**
     * 根据条件查询表page_template数据
     *
     * @param pageTemplate pageTemplate
     * @return query result
     */
    @Override
    public List<PageTemplate> queryPageTemplateByCondition(PageTemplate pageTemplate) {
        return pageTemplateMapper.queryPageTemplateByCondition(pageTemplate);
    }

    /**
     * 根据主键id删除表page_template数据
     *
     * @param id id
     * @return execute success data number
     */
    @Override
    public Integer deletePageTemplateById(@Param("id") Integer id) {
        return pageTemplateMapper.deletePageTemplateById(id);
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
     */
    @Override
    public Integer createPageTemplate(PageTemplate pageTemplate) {
        return pageTemplateMapper.createPageTemplate(pageTemplate);
    }
}

package com.tinyengine.it.service.app.impl;

import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.mapper.PageTemplateMapper;
import com.tinyengine.it.model.entity.PageTemplate;
import com.tinyengine.it.service.app.PageTemplateService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PageTemplateServiceImpl implements PageTemplateService {

    @Autowired
    private PageTemplateMapper pageTemplateMapper;

    /**
     * 查询表page_template所有数据
     */
    @Override
    public List<PageTemplate> queryAllPageTemplate() throws ServiceException {
        return pageTemplateMapper.queryAllPageTemplate();
    }

    /**
     * 根据主键id查询表page_template信息
     *
     * @param id
     */
    @Override
    public PageTemplate queryPageTemplateById(@Param("id") Integer id) throws ServiceException {
        return pageTemplateMapper.queryPageTemplateById(id);
    }

    /**
     * 根据条件查询表page_template数据
     *
     * @param pageTemplate
     */
    @Override
    public List<PageTemplate> queryPageTemplateByCondition(PageTemplate pageTemplate) throws ServiceException {
        return pageTemplateMapper.queryPageTemplateByCondition(pageTemplate);
    }

    /**
     * 根据主键id删除表page_template数据
     *
     * @param id
     */
    @Override
    public Integer deletePageTemplateById(@Param("id") Integer id) throws ServiceException {
        return pageTemplateMapper.deletePageTemplateById(id);
    }

    /**
     * 根据主键id更新表page_template数据
     *
     * @param pageTemplate
     */
    @Override
    public Integer updatePageTemplateById(PageTemplate pageTemplate) throws ServiceException {
        return pageTemplateMapper.updatePageTemplateById(pageTemplate);
    }

    /**
     * 新增表page_template数据
     *
     * @param pageTemplate
     */
    @Override
    public Integer createPageTemplate(PageTemplate pageTemplate) throws ServiceException {
        return pageTemplateMapper.createPageTemplate(pageTemplate);
    }
}

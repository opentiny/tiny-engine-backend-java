package com.tinyengine.it.service.app;

import com.tinyengine.it.model.entity.PageTemplate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PageTemplateService{

    /**
    *  查询表page_template所有信息
    */
    List<PageTemplate> findAllPageTemplate();

    /**
    *  根据主键id查询表page_template信息
    *  @param id
    */
    PageTemplate findPageTemplateById(@Param("id") Integer id);

    /**
    *  根据条件查询表page_template信息
    *  @param pageTemplate
    */
    List<PageTemplate> findPageTemplateByCondition(PageTemplate pageTemplate);

    /**
    *  根据主键id删除page_template数据
    *  @param id
    */
    Integer deletePageTemplateById(@Param("id") Integer id);

    /**
    *  根据主键id更新表page_template信息
    *  @param pageTemplate
    */
    Integer updatePageTemplateById(PageTemplate pageTemplate);

    /**
    *  新增表page_template数据
    *  @param pageTemplate
    */
    Integer createPageTemplate(PageTemplate pageTemplate);
}

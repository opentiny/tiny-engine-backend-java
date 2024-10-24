package com.tinyengine.it.service.app;

import com.tinyengine.it.model.entity.PageTemplate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface Page template service.
 */
public interface PageTemplateService {

    /**
     * 查询表page_template所有信息
     *
     * @return the list
     */
    List<PageTemplate> queryAllPageTemplate();

    /**
     * 根据主键id查询表page_template信息
     *
     * @param id the id
     * @return the page template
     */
    PageTemplate queryPageTemplateById(@Param("id") Integer id);

    /**
     * 根据条件查询表page_template信息
     *
     * @param pageTemplate the page template
     * @return the list
     */
    List<PageTemplate> queryPageTemplateByCondition(PageTemplate pageTemplate);

    /**
     * 根据主键id删除page_template数据
     *
     * @param id the id
     * @return the integer
     */
    Integer deletePageTemplateById(@Param("id") Integer id);

    /**
     * 根据主键id更新表page_template信息
     *
     * @param pageTemplate the page template
     * @return the integer
     */
    Integer updatePageTemplateById(PageTemplate pageTemplate);

    /**
     * 新增表page_template数据
     *
     * @param pageTemplate the page template
     * @return the integer
     */
    Integer createPageTemplate(PageTemplate pageTemplate);
}

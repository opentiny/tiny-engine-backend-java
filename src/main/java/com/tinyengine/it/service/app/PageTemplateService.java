package com.tinyengine.it.service.app;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.model.entity.PageTemplate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PageTemplateService {

    /**
     * 查询表page_template所有信息
     */
    Result<List<PageTemplate>> queryAllPageTemplate();

    /**
     * 根据主键id查询表page_template信息
     *
     * @param id
     */
    Result<PageTemplate> queryPageTemplateById(@Param("id") Integer id);

    /**
     * 根据条件查询表page_template信息
     *
     * @param pageTemplate
     */
    List<PageTemplate> queryPageTemplateByCondition(PageTemplate pageTemplate);

    /**
     * 根据主键id删除page_template数据
     *
     * @param ids
     */
    Result<Integer> deletePageTemplateByIds(@Param("ids") List<Integer> ids);

    /**
     * 根据主键id更新表page_template信息
     *
     * @param pageTemplate
     */
    Integer updatePageTemplateById(PageTemplate pageTemplate);

    /**
     * 新增表page_template数据
     *
     * @param pageTemplate
     */
    Result<PageTemplate> createPageTemplate(PageTemplate pageTemplate);
}

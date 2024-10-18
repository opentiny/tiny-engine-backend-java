package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.PageTemplate;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface PageTemplateMapper extends BaseMapper<PageTemplate> {

    /**
     * 查询表page_template所有信息
     */
    List<PageTemplate> findAllPageTemplate();

    /**
     * 根据主键id查询表page_template数据
     *
     * @param id
     */
    PageTemplate findPageTemplateById(@Param("id") Integer id);

    /**
     * 根据条件查询表page_template数据
     *
     * @param pageTemplate
     */
    List<PageTemplate> findPageTemplateByCondition(PageTemplate pageTemplate);

    /**
     * 根据主键id删除表page_template数据
     *
     * @param id
     */
    Integer deletePageTemplateById(@Param("id") Integer id);

    /**
     * 根据主键id更新表page_template数据
     *
     * @param pageTemplate
     */
    Integer updatePageTemplateById(PageTemplate pageTemplate);

    /**
     * 新增表page_template数据
     *
     * @param pageTemplate
     */
    Integer createPageTemplate(PageTemplate pageTemplate);
}
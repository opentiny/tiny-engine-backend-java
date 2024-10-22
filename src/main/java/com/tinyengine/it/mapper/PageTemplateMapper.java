package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.PageTemplate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PageTemplateMapper extends BaseMapper<PageTemplate> {

    /**
     * 查询表page_template所有信息
     */
    List<PageTemplate> queryAllPageTemplate();

    /**
     * 根据主键id查询表page_template数据
     *
     * @param id
     */
    PageTemplate queryPageTemplateById(@Param("id") Integer id);

    /**
     * 根据条件查询表page_template数据
     *
     * @param pageTemplate
     */
    List<PageTemplate> queryPageTemplateByCondition(PageTemplate pageTemplate);

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
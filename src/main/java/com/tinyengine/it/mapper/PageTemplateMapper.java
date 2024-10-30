package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.PageTemplate;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface Page template mapper.
 *
 * @since 2024-10-20
 */
public interface PageTemplateMapper extends BaseMapper<PageTemplate> {
    /**
     * 查询表page_template所有信息
     *
     * @return the list
     */
    List<PageTemplate> queryAllPageTemplate(@Param("type") String type);

    /**
     * 根据主键id查询表page_template数据
     *
     * @param id the id
     * @return the page template
     */
    PageTemplate queryPageTemplateById(@Param("id") Integer id);

    /**
     * 根据条件查询表page_template数据
     *
     * @param pageTemplate the page template
     * @return the list
     */
    List<PageTemplate> queryPageTemplateByCondition(PageTemplate pageTemplate);

    /**
     * 根据主键id删除表page_template数据
     *
     * @param ids id
     * @return the integer
     */
    Integer deletePageTemplateByIds(@Param("ids") List<Integer> ids);

    /**
     * 根据主键id更新表page_template数据
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

    /**
     * 模糊查询表page_template数据
     *
     * @param name the name
     * @param type the type
     * @return the list
     */
    List<PageTemplate> queryPageTemplateByName(String name, String type);

}
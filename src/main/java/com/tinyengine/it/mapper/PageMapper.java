package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.Page;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface PageMapper extends BaseMapper<Page> {

    /**
     * 查询表t_page所有信息
     */
    List<Page> findAllPage();

    /**
     * 根据主键id查询表t_page数据
     *
     * @param id
     */
    Page findPageById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_page数据
     *
     * @param page
     */
    List<Page> findPageByCondition(Page page);

    /**
     * 根据主键id删除表t_page数据
     *
     * @param id
     */
    Integer deletePageById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_page数据
     *
     * @param page
     */
    Integer updatePageById(Page page);

    /**
     * 新增表t_page数据
     *
     * @param page
     */
    Integer createPage(Page page);
}
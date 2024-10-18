package com.tinyengine.it.service.app;

import com.tinyengine.it.model.entity.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PageService{

    /**
    *  查询表t_page所有信息
    */
    List<Page> findAllPage();

    /**
    *  根据主键id查询表t_page信息
    *  @param id
    */
    Page findPageById(@Param("id") Integer id);

    /**
    *  根据条件查询表t_page信息
    *  @param page
    */
    List<Page> findPageByCondition(Page page);

    /**
    *  根据主键id删除t_page数据
    *  @param id
    */
    Integer deletePageById(@Param("id") Integer id);

    /**
    *  根据主键id更新表t_page信息
    *  @param page
    */
    Integer updatePageById(Page page);

    /**
    *  新增表t_page数据
    *  @param page
    */
    Integer createPage(Page page);
}

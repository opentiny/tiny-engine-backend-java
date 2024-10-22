package com.tinyengine.it.service.app;

import com.tinyengine.it.model.entity.PageHistory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PageHistoryService{

    /**
    *  查询表t_page_history所有信息
    */
    List<PageHistory> findAllPageHistory();

    /**
    *  根据主键id查询表t_page_history信息
    *  @param id
    */
    PageHistory findPageHistoryById(@Param("id") Integer id);

    /**
    *  根据条件查询表t_page_history信息
    *  @param pageHistory
    */
    List<PageHistory> findPageHistoryByCondition(PageHistory pageHistory);

    /**
    *  根据主键id删除t_page_history数据
    *  @param id
    */
    Integer deletePageHistoryById(@Param("id") Integer id);

    /**
    *  根据主键id更新表t_page_history信息
    *  @param pageHistory
    */
    Integer updatePageHistoryById(PageHistory pageHistory);

    /**
    *  新增表t_page_history数据
    *  @param pageHistory
    */
    Integer createPageHistory(PageHistory pageHistory);
}

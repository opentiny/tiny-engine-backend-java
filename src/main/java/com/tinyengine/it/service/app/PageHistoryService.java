package com.tinyengine.it.service.app;

import com.tinyengine.it.model.entity.PageHistory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface Page history service.
 *
 * @since 2024-10-20
 */
public interface PageHistoryService {
    /**
     * 查询表t_page_history所有信息
     *
     * @return the list
     */
    List<PageHistory> findAllPageHistory();

    /**
     * 根据主键id查询表t_page_history信息
     *
     * @param id the id
     * @return the page history
     */
    PageHistory findPageHistoryById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_page_history信息
     *
     * @param pageHistory the page history
     * @return the list
     */
    List<PageHistory> findPageHistoryByCondition(PageHistory pageHistory);

    /**
     * 根据主键id删除t_page_history数据
     *
     * @param id the id
     * @return the integer
     */
    Integer deletePageHistoryById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_page_history信息
     *
     * @param pageHistory the page history
     * @return the integer
     */
    Integer updatePageHistoryById(PageHistory pageHistory);

    /**
     * 新增表t_page_history数据
     *
     * @param pageHistory the page history
     * @return the integer
     */
    Integer createPageHistory(PageHistory pageHistory);
}

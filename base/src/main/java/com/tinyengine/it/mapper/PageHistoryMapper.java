package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.PageHistory;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface Page history mapper.
 *
 * @since 2024-10-20
 */
public interface PageHistoryMapper extends BaseMapper<PageHistory> {
    /**
     * 查询表t_page_history所有信息
     *
     * @return the list
     */
    List<PageHistory> queryAllPageHistory();

    /**
     * 根据主键id查询表t_page_history数据
     *
     * @param id the id
     * @return the page history
     */
    PageHistory queryPageHistoryById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_page_history数据
     *
     * @param pageHistory the page history
     * @return the list
     */
    List<PageHistory> queryPageHistoryByCondition(PageHistory pageHistory);

    /**
     * 根据主键id删除表t_page_history数据
     *
     * @param id the id
     * @return the integer
     */
    Integer deletePageHistoryById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_page_history数据
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

    /**
     * 新增表t_page_history数据
     *
     * @param name the page name
     * @return the List<PageHistory>
     */
    List<PageHistory> queryPageHistoryByName(String name);
}
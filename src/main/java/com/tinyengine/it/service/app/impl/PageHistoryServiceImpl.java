
package com.tinyengine.it.service.app.impl;

import com.tinyengine.it.mapper.PageHistoryMapper;
import com.tinyengine.it.model.entity.PageHistory;
import com.tinyengine.it.service.app.PageHistoryService;

import lombok.extern.slf4j.Slf4j;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Page history service.
 *
 * @since 2024-10-20
 */
@Service
@Slf4j
public class PageHistoryServiceImpl implements PageHistoryService {
    @Autowired
    private PageHistoryMapper pageHistoryMapper;

    /**
     * 查询表t_page_history所有数据
     *
     * @return PageHistory
     */
    @Override
    public List<PageHistory> findAllPageHistory() {
        return pageHistoryMapper.queryAllPageHistory();
    }

    /**
     * 根据主键id查询表t_page_history信息
     *
     * @param id id
     * @return query result
     */
    @Override
    public PageHistory findPageHistoryById(@Param("id") Integer id) {
        return pageHistoryMapper.queryPageHistoryById(id);
    }

    /**
     * 根据条件查询表t_page_history数据
     *
     * @param pageHistory pageHistory
     * @return query result
     */
    @Override
    public List<PageHistory> findPageHistoryByCondition(PageHistory pageHistory) {
        return pageHistoryMapper.queryPageHistoryByCondition(pageHistory);
    }

    /**
     * 根据主键id删除表t_page_history数据
     *
     * @param id id
     * @return execute success data number
     */
    @Override
    public Integer deletePageHistoryById(@Param("id") Integer id) {
        return pageHistoryMapper.deletePageHistoryById(id);
    }

    /**
     * 根据主键id更新表t_page_history数据
     *
     * @param pageHistory pageHistory
     * @return execute success data number
     */
    @Override
    public Integer updatePageHistoryById(PageHistory pageHistory) {
        return pageHistoryMapper.updatePageHistoryById(pageHistory);
    }

    /**
     * 新增表t_page_history数据
     *
     * @param pageHistory pageHistory
     * @return execute success data number
     */
    @Override
    public Integer createPageHistory(PageHistory pageHistory) {
        return pageHistoryMapper.createPageHistory(pageHistory);
    }
}

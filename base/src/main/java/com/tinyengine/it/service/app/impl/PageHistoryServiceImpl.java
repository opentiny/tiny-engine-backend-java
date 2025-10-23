/**
 * Copyright (c) 2023 - present TinyEngine Authors.
 * Copyright (c) 2023 - present Huawei Cloud Computing Technologies Co., Ltd.
 *
 * Use of this source code is governed by an MIT-style license.
 *
 * THE OPEN SOURCE SOFTWARE IN THIS PRODUCT IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL,
 * BUT WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS FOR
 * A PARTICULAR PURPOSE. SEE THE APPLICABLE LICENSES FOR MORE DETAILS.
 *
 */

package com.tinyengine.it.service.app.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tinyengine.it.common.base.PageQueryVo;
import com.tinyengine.it.common.log.SystemServiceLog;
import com.tinyengine.it.mapper.PageHistoryMapper;
import com.tinyengine.it.model.dto.PublishedPageVo;
import com.tinyengine.it.model.entity.PageHistory;
import com.tinyengine.it.service.app.PageHistoryService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * The type Page history service.
 *
 * @since 2024-10-20
 */
@Service
@Slf4j
public class PageHistoryServiceImpl extends ServiceImpl<PageHistoryMapper, PageHistory> implements PageHistoryService {
    private static final String DEFAULT_PAGE_HISTORY_VERSION = "0";

    /**
     * 查询表t_page_history所有数据
     *
     * @return PageHistory
     */
    @Override
    public List<PageHistory> findAllPageHistory() {
        return baseMapper.queryAllPageHistory();
    }

    /**
     * 根据主键id查询表t_page_history信息
     *
     * @param historyId id
     * @return query result
     */
    @Override
    public PageHistory findPageHistoryById(Integer historyId) {
        return baseMapper.queryPageHistoryById(historyId);
    }

    /**
     * 根据条件查询表t_page_history数据
     *
     * @param pageHistory pageHistory
     * @return query result
     */
    @Override
    public List<PageHistory> findPageHistoryByCondition(PageHistory pageHistory) {
        return baseMapper.queryPageHistoryByCondition(pageHistory);
    }

    /**
     * 根据主键id删除表t_page_history数据
     *
     * @param id id
     * @return execute success data number
     */
    @Override
    public Integer deletePageHistoryById(Integer id) {
        return baseMapper.deletePageHistoryById(id);
    }

    /**
     * 根据主键id更新表t_page_history数据
     *
     * @param pageHistory pageHistory
     * @return execute success data number
     */
    @Override
    public Integer updatePageHistoryById(PageHistory pageHistory) {
        return baseMapper.updatePageHistoryById(pageHistory);
    }

    /**
     * 新增表t_page_history数据
     *
     * @param pageHistory pageHistory
     * @return execute success data number
     */
    @Override
    @SystemServiceLog(description = "创建页面历史记录")
    public Integer createPageHistory(PageHistory pageHistory) {
        pageHistory.setIsPublished(true);
        return baseMapper.createPageHistory(pageHistory);
    }

    /**
     * 通过名称及appId查询表t_page_history数据
     *
     * @param app the app
     * @param name the page name
     * @return the List<PageHistory>
     */
    @Override
    public List<PageHistory> findPageHistoryByName(String name, Integer app) {
        return baseMapper.queryPageHistoryByName(name, app);
    }

    @Override
    public IPage<PublishedPageVo> findLatestPublishPage(PageQueryVo<PublishedPageVo> pageQueryVo) {
        PublishedPageVo queryData = pageQueryVo.getData();
        return baseMapper.findLatestPublishPage(pageQueryVo.getPage(), queryData);
    }

    /**
     * 查询页面历史的最大版本号
     *
     * @param app the app
     * @param name the name
     * @return 页面历史的最大版本号
     */
    @Override
    public String selectMaxVersionOfPageHistory(String name, Integer app) {
        List<PageHistory> pageHistories = baseMapper.queryPageHistoryByName(name, app);
        if (CollectionUtils.isEmpty(pageHistories)) {
            return DEFAULT_PAGE_HISTORY_VERSION;
        }
        PageHistory lastPageHistory = pageHistories.get(pageHistories.size() - 1);
        return lastPageHistory.getVersion();
    }
}

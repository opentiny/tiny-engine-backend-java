/**
 * Copyright (c) 2023 - present TinyEngine Authors.
 * Copyright (c) 2023 - present Huawei Cloud Computing Technologies Co., Ltd.
 * <p>
 * Use of this source code is governed by an MIT-style license.
 * <p>
 * THE OPEN SOURCE SOFTWARE IN THIS PRODUCT IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL,
 * BUT WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS FOR
 * A PARTICULAR PURPOSE. SEE THE APPLICABLE LICENSES FOR MORE DETAILS.
 */

package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tinyengine.it.model.dto.PageHistoryVo;
import com.tinyengine.it.model.dto.PublishedPageVo;
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
     * @param historyId the id
     * @return the page history
     */
    PageHistory queryPageHistoryById(@Param("historyId") Integer historyId);

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
     * @param app  the app
     * @param name the page name
     * @return the List<PageHistory>
     */
    List<PageHistory> queryPageHistoryByName(String name, Integer app);

    /**
     * 查询发布的页面记录
     *
     * @param pageHistoryVo the pageQueryVo
     * @return page history
     */
    IPage<PublishedPageVo> findLatestPublishPage(IPage<PublishedPageVo> iPage,
                                               @Param("pageHistory") PublishedPageVo pageHistoryVo);
}
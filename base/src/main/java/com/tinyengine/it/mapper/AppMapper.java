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

package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.App;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * The interface App mapper.
 *
 * @since 2024-10-20
 */
public interface AppMapper extends BaseMapper<App> {
    /**
     * 查询表t_app所有信息
     *
     * @return the list
     */
    List<App> queryAllApp(String tenantId);

    /**
     * 分页查询应用所有信息
     * @param offset the offset
     * @param pageSize the pageSize
     * @param name the name
     * @param industryId the industryId
     * @param sceneId the sceneId
     * @param framework the framework
     * @param orderBy the orderBy
     * @param createdBy the createdBy
     * @return the list
     */
    List<App> queryAllAppByPage(Integer pageSize, Integer offset, String name, Integer industryId,
        Integer sceneId, String framework, String orderBy, String createdBy, String tenantId);

    /**
     * 查询表t_app 应用总数
     *
     * @return the int
     */
    @Select("SELECT COUNT(id) FROM t_app WHERE tenant_id = #{tenantId} AND is_template IS NOT TRUE")
    int queryAppTotal(String tenantId);

    /**
     * 查询表t_app 模版总数
     *
     * @return the int
     */
    @Select("SELECT COUNT(id) FROM t_app WHERE tenant_id = #{tenantId} AND is_template = TRUE")
    int queryAppTemplateTotal(String tenantId);

    /**
     * 分页查询应用模版所有信息
     * @param offset the offset
     * @param pageSize the pageSize
     * @param name the name
     * @param industryId the industryId
     * @param sceneId the sceneId
     * @param framework the framework
     * @param orderBy the orderBy
     * @param createdBy the createdBy
     * @return the list
     */
    List<App> queryAllAppTemplate(Integer pageSize, Integer offset, String name, Integer industryId,
        Integer sceneId, String framework, String orderBy, String createdBy, String tenantId);

    /**
     * 根据主键id查询应用模版数据
     *
     * @param id the id
     * @return the app
     */
    App queryAppTemplateById(Integer id, String tenantId);

    /**
     * 根据主键id查询表t_app数据
     *
     * @param id the id
     * @return the app
     */
    App queryAppById(Integer id, String tenantId);

    /**
     * 根据条件查询表t_app数据
     *
     * @param app the app
     * @return the list
     */
    List<App> queryAppByCondition(App app);

    /**
     * 根据主键id删除表t_app数据
     *
     * @param id the id
     * @return the integer
     */
    Integer deleteAppById(@Param("id") Integer id, String tenantId);

    /**
     * 根据主键id更新表t_app数据
     *
     * @param app the app
     * @return the integer
     */
    Integer updateAppById(App app);

    /**
     * 新增表t_app数据
     *
     * @param app the app
     * @return the integer
     */
    Integer createApp(App app);
}
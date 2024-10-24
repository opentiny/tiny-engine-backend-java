package com.tinyengine.it.service.app;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.model.entity.AppExtension;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface App extension service.
 * @since 2024-10-20
 */
public interface AppExtensionService {

    /**
     * 查询表t_app_extension所有信息
     *
     * @return the result
     */
    Result<List<AppExtension>> findAllAppExtension();

    /**
     * 根据主键id查询表t_app_extension信息
     *
     * @param id the id
     * @return the app extension
     */
    AppExtension findAppExtensionById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_app_extension信息
     *
     * @param appExtension the app extension
     * @return the list
     */
    List<AppExtension> findAppExtensionByCondition(AppExtension appExtension);

    /**
     * 根据主键id删除t_app_extension数据
     *
     * @param id the id
     * @return the result
     */
    Result<AppExtension> deleteAppExtensionById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_app_extension信息
     *
     * @param appExtension the app extension
     * @return the result
     */
    Result<AppExtension> updateAppExtensionById(AppExtension appExtension);

    /**
     * 新增表t_app_extension数据
     *
     * @param appExtension the app extension
     * @return the result
     */
    Result<AppExtension> createAppExtension(AppExtension appExtension);
}

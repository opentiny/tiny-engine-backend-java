package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.AppExtension;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface App extension mapper.
 */
public interface AppExtensionMapper extends BaseMapper<AppExtension> {

    /**
     * 查询表t_app_extension所有信息
     *
     * @return the list
     */
    List<AppExtension> queryAllAppExtension();

    /**
     * 根据主键id查询表t_app_extension数据
     *
     * @param id the id
     * @return the app extension
     */
    AppExtension queryAppExtensionById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_app_extension数据
     *
     * @param appExtension the app extension
     * @return the list
     */
    List<AppExtension> queryAppExtensionByCondition(AppExtension appExtension);

    /**
     * 根据主键id删除表t_app_extension数据
     *
     * @param id the id
     * @return the integer
     */
    Integer deleteAppExtensionById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_app_extension数据
     *
     * @param appExtension the app extension
     * @return the integer
     */
    Integer updateAppExtensionById(AppExtension appExtension);

    /**
     * 新增表t_app_extension数据
     *
     * @param appExtension the app extension
     * @return the integer
     */
    Integer createAppExtension(AppExtension appExtension);
}
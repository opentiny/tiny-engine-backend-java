package com.tinyengine.it.service.app;

import com.tinyengine.it.model.entity.AppExtensions;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppExtensionsService{

    /**
    *  查询表app_extensions所有信息
    */
    List<AppExtensions> findAllAppExtensions();

    /**
    *  根据主键id查询表app_extensions信息
    *  @param id
    */
    AppExtensions findAppExtensionsById(@Param("id") Integer id);

    /**
    *  根据条件查询表app_extensions信息
    *  @param appExtensions
    */
    List<AppExtensions> findAppExtensionsByCondition(AppExtensions appExtensions);

    /**
    *  根据主键id删除app_extensions数据
    *  @param id
    */
    Integer deleteAppExtensionsById(@Param("id") Integer id);

    /**
    *  根据主键id更新表app_extensions信息
    *  @param appExtensions
    */
    Integer updateAppExtensionsById(AppExtensions appExtensions);

    /**
    *  新增表app_extensions数据
    *  @param appExtensions
    */
    Integer createAppExtensions(AppExtensions appExtensions);
}

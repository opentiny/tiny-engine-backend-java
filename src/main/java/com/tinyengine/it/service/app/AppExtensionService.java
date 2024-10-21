package com.tinyengine.it.service.app;

import com.tinyengine.it.model.dto.Result;
import com.tinyengine.it.model.entity.AppExtension;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppExtensionService{

    /**
    *  查询表t_app_extension所有信息
    */
    Result<List<AppExtension>> findAllAppExtension();

    /**
    *  根据主键id查询表t_app_extension信息
    *  @param id
    */
    AppExtension findAppExtensionById(@Param("id") Integer id);

    /**
    *  根据条件查询表t_app_extension信息
    *  @param appExtension
    */
    List<AppExtension> findAppExtensionByCondition(AppExtension appExtension);

    /**
    *  根据主键id删除t_app_extension数据
    *  @param id
    */
    Result<AppExtension> deleteAppExtensionById(@Param("id") Integer id);

    /**
    *  根据主键id更新表t_app_extension信息
    *  @param appExtension
    */
    Result<AppExtension> updateAppExtensionById(AppExtension appExtension);

    /**
    *  新增表t_app_extension数据
    *  @param appExtension
    */
    Result<AppExtension> createAppExtension(AppExtension appExtension);
}

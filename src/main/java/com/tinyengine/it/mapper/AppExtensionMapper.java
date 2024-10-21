package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.AppExtension;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AppExtensionMapper extends BaseMapper<AppExtension> {

    /**
    *  查询表t_app_extension所有信息
    */
    List<AppExtension> queryAllAppExtension();

    /**
    * 根据主键id查询表t_app_extension数据
    * @param id
    */
    AppExtension queryAppExtensionById(@Param("id") Integer id);
    /**
    *  根据条件查询表t_app_extension数据
    *  @param appExtension
    */
    List<AppExtension> queryAppExtensionByCondition(AppExtension appExtension);

    /**
    *  根据主键id删除表t_app_extension数据
    *  @param id
    */
    Integer deleteAppExtensionById(@Param("id") Integer id);

    /**
    *  根据主键id更新表t_app_extension数据
    *  @param appExtension
    */
    Integer updateAppExtensionById(AppExtension appExtension);

    /**
    *  新增表t_app_extension数据
    *  @param appExtension
    */
    Integer createAppExtension(AppExtension appExtension);
}
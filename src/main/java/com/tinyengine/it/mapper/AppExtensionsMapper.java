package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.AppExtensions;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AppExtensionsMapper extends BaseMapper<AppExtensions> {

    /**
     * 查询表app_extensions所有信息
     */
    List<AppExtensions> findAllAppExtensions();

    /**
     * 根据主键id查询表app_extensions数据
     *
     * @param id
     */
    AppExtensions findAppExtensionsById(@Param("id") Integer id);

    /**
     * 根据条件查询表app_extensions数据
     *
     * @param appExtensions
     */
    List<AppExtensions> findAppExtensionsByCondition(AppExtensions appExtensions);

    /**
     * 根据主键id删除表app_extensions数据
     *
     * @param id
     */
    Integer deleteAppExtensionsById(@Param("id") Integer id);

    /**
     * 根据主键id更新表app_extensions数据
     *
     * @param appExtensions
     */
    Integer updateAppExtensionsById(AppExtensions appExtensions);

    /**
     * 新增表app_extensions数据
     *
     * @param appExtensions
     */
    Integer createAppExtensions(AppExtensions appExtensions);


    /**
     * 根据appId、name、category三个字段查询app_extensions数据
     *
     * @param appId
     * @param name
     * @param category
     * @return
     */
    @Select("select * from app_extensions " +
            "where app=#{appId} and name=#{name} and category=#{category}")
    AppExtensions findAppExtensionByAppIdNameCategory(int appId, String name, String category);
}
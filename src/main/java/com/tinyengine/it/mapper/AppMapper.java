package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.App;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppMapper extends BaseMapper<App> {

    /**
     * 查询表t_app所有信息
     */
    List<App> queryAllApp();

    /**
     * 根据主键id查询表t_app数据
     *
     * @param id
     */
    App queryAppById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_app数据
     *
     * @param app
     */
    List<App> queryAppByCondition(App app);

    /**
     * 根据主键id删除表t_app数据
     *
     * @param id
     */
    Integer deleteAppById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_app数据
     *
     * @param app
     */
    Integer updateAppById(App app);

    /**
     * 新增表t_app数据
     *
     * @param app
     */
    Integer createApp(App app);
}
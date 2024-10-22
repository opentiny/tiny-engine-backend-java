package com.tinyengine.it.service.app;

import com.tinyengine.it.model.dto.Result;
import com.tinyengine.it.model.entity.App;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppService {

    /**
     * 查询表t_app所有信息
     */
    List<App> queryAllApp();

    /**
     * 根据主键id查询表t_app信息
     *
     * @param id
     */
    Result<App> queryAppById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_app信息
     *
     * @param app
     */
    List<App> queryAppByCondition(App app);

    /**
     * 根据主键id删除t_app数据
     *
     * @param id
     */
    Result<App> deleteAppById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_app信息
     *
     * @param app
     */
    Result<App> updateAppById(App app);

    /**
     * 新增表t_app数据
     *
     * @param app
     */
    Result<App> createApp(App app);
}

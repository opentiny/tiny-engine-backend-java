package com.tinyengine.it.service.impl.app;

import com.tinyengine.it.config.SystemServiceLog;
import com.tinyengine.it.exception.ExceptionEnum;
import com.tinyengine.it.mapper.PlatformMapper;
import com.tinyengine.it.model.dto.Result;
import com.tinyengine.it.model.entity.App;
import com.tinyengine.it.mapper.AppMapper;
import com.tinyengine.it.model.entity.Platform;
import com.tinyengine.it.service.app.AppService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.ibatis.annotations.Param;
import com.tinyengine.it.exception.ServiceException;

import java.util.List;
import java.util.Map;

@Service
public class AppServiceImpl implements AppService {

    @Autowired
    AppMapper appMapper;
    @Autowired
    PlatformMapper platformMapper;

    /**
     * 查询表t_app所有数据
     */
    @Override
    public List<App> queryAllApp() throws ServiceException {
        return appMapper.queryAllApp();
    }

    /**
     * 根据主键id查询表t_app信息
     *
     * @param id
     */
    @Override
    @SystemServiceLog(description = "通过id查询应用实现方法")
    public Result<App> queryAppById(@Param("id") Integer id) throws ServiceException {
        App app = appMapper.queryAppById(id);
        if(app == null ){
            return Result.failed(ExceptionEnum.CM009);
        }
        return Result.success(app);
    }

    /**
     * 根据条件查询表t_app数据
     *
     * @param app
     */
    @Override
    public List<App> queryAppByCondition(App app) throws ServiceException {
        return appMapper.queryAppByCondition(app);
    }

    /**
     * 根据主键id删除表t_app数据
     *
     * @param id
     */
    @Override
    @SystemServiceLog(description = "应用删除实现方法")
    public Result<App> deleteAppById(@Param("id") Integer id) throws ServiceException {
        App app = appMapper.queryAppById(id);
        int result = appMapper.deleteAppById(id);
        if(result < 1){
            return Result.failed(ExceptionEnum.CM009);
        }
        return Result.success(app);
    }

    /**
     * 根据主键id更新表t_app数据
     *
     * @param app
     */
    @Override
    public Result<App> updateAppById(App app) throws ServiceException {
        // 如果更新extend_config字段，从platform获取数据，继承非route部分
        if(!app.getExtendConfig().isEmpty()){
            App appResult = appMapper.queryAppById(app.getId());
            Platform platform =platformMapper.queryPlatformById(appResult.getPlatformId());
            Map<String,Object> appExtendConfig = platform.getAppExtendConfig();
            appExtendConfig.remove("route");
            app.getExtendConfig().putAll(appExtendConfig);
        }
        int result = appMapper.updateAppById(app);
        if(result < 1){
            return Result.failed(ExceptionEnum.CM001);
        }
        app = appMapper.queryAppById(app.getId());
        return Result.success(app);
    }

    /**
     * 新增表t_app数据
     *
     * @param app
     */
    @Override
    public Result<App> createApp(App app) throws ServiceException {

        List<App> appResult = appMapper.queryAppByCondition(app);
        if(!appResult.isEmpty()){
            return Result.failed(ExceptionEnum.CM003);
        }
        int result = appMapper.createApp(app);
        if(result < 1) {
            return Result.failed(ExceptionEnum.CM001);
        }
        return Result.success(app);
    }
}

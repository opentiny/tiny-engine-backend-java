package com.tinyengine.it.service.impl.app;

import com.tinyengine.it.model.entity.App;
import com.tinyengine.it.mapper.AppMapper;
import com.tinyengine.it.service.app.AppService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.ibatis.annotations.Param;
import com.tinyengine.it.exception.ServiceException;

import java.util.List;

@Service
public class AppServiceImpl implements AppService {

    @Autowired
    private AppMapper appMapper;

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
    public App queryAppById(@Param("id") Integer id) throws ServiceException {
        return appMapper.queryAppById(id);
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
    public Integer deleteAppById(@Param("id") Integer id) throws ServiceException {
        return appMapper.deleteAppById(id);
    }

    /**
     * 根据主键id更新表t_app数据
     *
     * @param app
     */
    @Override
    public Integer updateAppById(App app) throws ServiceException {
        return appMapper.updateAppById(app);
    }

    /**
     * 新增表t_app数据
     *
     * @param app
     */
    @Override
    public Integer createApp(App app) throws ServiceException {
        return appMapper.createApp(app);
    }
}

package com.tinyengine.it.service.impl.app;

import com.tinyengine.it.exception.ServiceException;
import com.tinyengine.it.mapper.AppExtensionsMapper;
import com.tinyengine.it.model.entity.AppExtensions;
import com.tinyengine.it.service.app.AppExtensionsService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppExtensionsServiceImpl implements AppExtensionsService {

    @Autowired
    AppExtensionsMapper appExtensionsMapper;

    /**
     * 查询表app_extensions所有数据
     */
    @Override
    public List<AppExtensions> findAllAppExtensions() throws ServiceException {
        return appExtensionsMapper.findAllAppExtensions();
    }

    /**
     * 根据主键id查询表app_extensions信息
     *
     * @param id
     */
    @Override
    public AppExtensions findAppExtensionsById(@Param("id") Integer id) throws ServiceException {
        return appExtensionsMapper.findAppExtensionsById(id);
    }

    /**
     * 根据条件查询表app_extensions数据
     *
     * @param appExtensions
     */
    @Override
    public List<AppExtensions> findAppExtensionsByCondition(AppExtensions appExtensions) throws ServiceException {
        return appExtensionsMapper.findAppExtensionsByCondition(appExtensions);
    }

    /**
     * 根据主键id删除表app_extensions数据
     *
     * @param id
     */
    @Override
    public Integer deleteAppExtensionsById(@Param("id") Integer id) throws ServiceException {
        return appExtensionsMapper.deleteAppExtensionsById(id);
    }

    /**
     * 根据主键id更新表app_extensions数据
     *
     * @param appExtensions
     */
    @Override
    public Integer updateAppExtensionsById(AppExtensions appExtensions) throws ServiceException {
        return appExtensionsMapper.updateAppExtensionsById(appExtensions);
    }

    /**
     * 新增表app_extensions数据
     *
     * @param appExtensions
     */
    @Override
    public Integer createAppExtensions(AppExtensions appExtensions) throws ServiceException {
        return appExtensionsMapper.createAppExtensions(appExtensions);
    }
}

package com.tinyengine.it.service.impl.app;

import com.tinyengine.it.config.Enums;
import com.tinyengine.it.config.SystemServiceLog;
import com.tinyengine.it.exception.ExceptionEnum;
import com.tinyengine.it.model.dto.Result;
import com.tinyengine.it.model.entity.AppExtension;
import com.tinyengine.it.mapper.AppExtensionMapper;
import com.tinyengine.it.service.app.AppExtensionService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.ibatis.annotations.Param;
import com.tinyengine.it.exception.ServiceException;

import java.util.List;

@Service
public class AppExtensionServiceImpl implements AppExtensionService {

    @Autowired
    private AppExtensionMapper appExtensionMapper;

    /**
    *  查询表t_app_extension所有数据
    */
    @Override
    @SystemServiceLog(description = "应用扩展列表查询实现方法")
    public Result<List<AppExtension>> findAllAppExtension() throws ServiceException {
        List<AppExtension> appExtensionList = appExtensionMapper.queryAllAppExtension();
        return Result.success(appExtensionList);
    }

    /**
    *  根据主键id查询表t_app_extension信息
    *  @param id
    */
    @Override
    @SystemServiceLog(description = "应用扩展id查询实现方法")
    public AppExtension findAppExtensionById(@Param("id") Integer id) throws ServiceException {
        return appExtensionMapper.queryAppExtensionById(id);
    }

    /**
    *  根据条件查询表t_app_extension数据
    *  @param appExtension
    */
    @Override
    @SystemServiceLog(description = "应用扩展条件查询实现方法")
    public List<AppExtension> findAppExtensionByCondition(AppExtension appExtension) throws ServiceException {
        return appExtensionMapper.queryAppExtensionByCondition(appExtension);
    }

    /**
    *  根据主键id删除表t_app_extension数据
    *  @param id
    */
    @Override
    @SystemServiceLog(description = "应用扩展删除实现方法")
    public Result<AppExtension> deleteAppExtensionById(@Param("id") Integer id) throws ServiceException {
        AppExtension appExtension = appExtensionMapper.queryAppExtensionById(id);
        if(appExtension != null) {
            appExtensionMapper.deleteAppExtensionById(id);
            return Result.success(appExtension);
        }
        return Result.failed(ExceptionEnum.CM009);
    }

    /**
    *  根据主键id更新表t_app_extension数据
    *  @param appExtension
    */
    @Override
    @SystemServiceLog(description = "应用扩展修改实现方法")
    public Result<AppExtension> updateAppExtensionById(AppExtension appExtension) throws ServiceException {
        int result = appExtensionMapper.updateAppExtensionById(appExtension);
        if(result == 1){
            appExtension = appExtensionMapper.queryAppExtensionById(appExtension.getId());
            return Result.success(appExtension);
        }
        return Result.failed(ExceptionEnum.CM001);
    }

    /**
    *  新增表t_app_extension数据
    *  @param appExtension
    */
    @Override
    @SystemServiceLog(description = "应用扩展创建实现方法")
    public Result<AppExtension> createAppExtension(AppExtension appExtension) throws ServiceException {
        int result = appExtensionMapper.createAppExtension(appExtension);
        if(result == 1){
            return Result.success(appExtension);
        }
        return Result.failed(ExceptionEnum.CM001);
    }
}

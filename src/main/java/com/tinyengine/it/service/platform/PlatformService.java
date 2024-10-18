package com.tinyengine.it.service.platform;

import com.tinyengine.it.model.entity.Platform;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PlatformService{

    /**
    *  查询表t_platform所有信息
    */
    List<Platform> findAllPlatform();

    /**
    *  根据主键id查询表t_platform信息
    *  @param id
    */
    Platform findPlatformById(@Param("id") Integer id);

    /**
    *  根据条件查询表t_platform信息
    *  @param platform
    */
    List<Platform> findPlatformByCondition(Platform platform);

    /**
    *  根据主键id删除t_platform数据
    *  @param id
    */
    Integer deletePlatformById(@Param("id") Integer id);

    /**
    *  根据主键id更新表t_platform信息
    *  @param platform
    */
    Integer updatePlatformById(Platform platform);

    /**
    *  新增表t_platform数据
    *  @param platform
    */
    Integer createPlatform(Platform platform);
}

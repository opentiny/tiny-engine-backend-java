package com.tinyengine.it.service.platform.impl;

import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.mapper.PlatformMapper;
import com.tinyengine.it.model.entity.Platform;
import com.tinyengine.it.service.platform.PlatformService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Platform service.
 */
@Service
public class PlatformServiceImpl implements PlatformService {

    @Autowired
    private PlatformMapper platformMapper;

    /**
     * 查询表t_platform所有数据
     */
    @Override
    public List<Platform> queryAllPlatform() throws ServiceException {
        return platformMapper.queryAllPlatform();
    }

    /**
     * 根据主键id查询表t_platform信息
     *
     * @param id
     */
    @Override
    public Platform queryPlatformById(@Param("id") Integer id) throws ServiceException {
        return platformMapper.queryPlatformById(id);
    }

    /**
     * 根据条件查询表t_platform数据
     *
     * @param platform
     */
    @Override
    public List<Platform> queryPlatformByCondition(Platform platform) throws ServiceException {
        return platformMapper.queryPlatformByCondition(platform);
    }

    /**
     * 根据主键id删除表t_platform数据
     *
     * @param id
     */
    @Override
    public Integer deletePlatformById(@Param("id") Integer id) throws ServiceException {
        return platformMapper.deletePlatformById(id);
    }

    /**
     * 根据主键id更新表t_platform数据
     *
     * @param platform
     */
    @Override
    public Integer updatePlatformById(Platform platform) throws ServiceException {
        return platformMapper.updatePlatformById(platform);
    }

    /**
     * 新增表t_platform数据
     *
     * @param platform
     */
    @Override
    public Integer createPlatform(Platform platform) throws ServiceException {
        return platformMapper.createPlatform(platform);
    }
}

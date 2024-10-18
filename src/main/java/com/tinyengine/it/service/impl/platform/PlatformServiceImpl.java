package com.tinyengine.it.service.impl.platform;

import com.tinyengine.it.model.entity.Platform;
import com.tinyengine.it.mapper.PlatformMapper;
import com.tinyengine.it.service.platform.PlatformService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.ibatis.annotations.Param;
import com.tinyengine.it.exception.ServiceException;

import java.util.List;

@Service
public class PlatformServiceImpl implements PlatformService {

    @Autowired
    private PlatformMapper platformMapper;

    /**
    *  查询表t_platform所有数据
    */
    @Override
    public List<Platform> findAllPlatform() throws ServiceException {
        return platformMapper.findAllPlatform();
    }

    /**
    *  根据主键id查询表t_platform信息
    *  @param id
    */
    @Override
    public Platform findPlatformById(@Param("id") Integer id) throws ServiceException {
        return platformMapper.findPlatformById(id);
    }

    /**
    *  根据条件查询表t_platform数据
    *  @param platform
    */
    @Override
    public List<Platform> findPlatformByCondition(Platform platform) throws ServiceException {
        return platformMapper.findPlatformByCondition(platform);
    }

    /**
    *  根据主键id删除表t_platform数据
    *  @param id
    */
    @Override
    public Integer deletePlatformById(@Param("id") Integer id) throws ServiceException {
        return platformMapper.deletePlatformById(id);
    }

    /**
    *  根据主键id更新表t_platform数据
    *  @param platform
    */
    @Override
    public Integer updatePlatformById(Platform platform) throws ServiceException {
        return platformMapper.updatePlatformById(platform);
    }

    /**
    *  新增表t_platform数据
    *  @param platform
    */
    @Override
    public Integer createPlatform(Platform platform) throws ServiceException {
        return platformMapper.createPlatform(platform);
    }
}

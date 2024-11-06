package com.tinyengine.it.service.platform.impl;

import com.tinyengine.it.mapper.PlatformMapper;
import com.tinyengine.it.model.entity.Platform;
import com.tinyengine.it.service.platform.PlatformService;

import lombok.extern.slf4j.Slf4j;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Platform service.
 *
 * @since 2024-10-20
 */
@Service
@Slf4j
public class PlatformServiceImpl implements PlatformService {
    @Autowired
    private PlatformMapper platformMapper;

    /**
     * 查询表t_platform所有数据
     *
     * @return Platform
     */
    @Override
    public List<Platform> queryAllPlatform() {
        return platformMapper.queryAllPlatform();
    }

    /**
     * 根据主键id查询表t_platform信息
     *
     * @param id id
     * @return query result
     */
    @Override
    public Platform queryPlatformById(@Param("id") Integer id) {
        return platformMapper.queryPlatformById(id);
    }

    /**
     * 根据条件查询表t_platform数据
     *
     * @param platform platform
     * @return query result
     */
    @Override
    public List<Platform> queryPlatformByCondition(Platform platform) {
        return platformMapper.queryPlatformByCondition(platform);
    }

    /**
     * 根据主键id删除表t_platform数据
     *
     * @param id id
     * @return execute success data number
     */
    @Override
    public Integer deletePlatformById(@Param("id") Integer id) {
        return platformMapper.deletePlatformById(id);
    }

    /**
     * 根据主键id更新表t_platform数据
     *
     * @param platform platform
     * @return execute success data number
     */
    @Override
    public Integer updatePlatformById(Platform platform) {
        return platformMapper.updatePlatformById(platform);
    }

    /**
     * 新增表t_platform数据
     *
     * @param platform platform
     * @return execute success data number
     */
    @Override
    public Integer createPlatform(Platform platform) {
        return platformMapper.createPlatform(platform);
    }
}

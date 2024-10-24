package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.Platform;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface Platform mapper.
 *
 * @since 2024-10-20
 */
public interface PlatformMapper extends BaseMapper<Platform> {

    /**
     * 查询表t_platform所有信息
     *
     * @return the list
     */
    List<Platform> queryAllPlatform();

    /**
     * 根据主键id查询表t_platform数据
     *
     * @param id the id
     * @return the platform
     */
    Platform queryPlatformById(Integer id);

    /**
     * 根据条件查询表t_platform数据
     *
     * @param platform the platform
     * @return the list
     */
    List<Platform> queryPlatformByCondition(Platform platform);

    /**
     * 根据主键id删除表t_platform数据
     *
     * @param id the id
     * @return the integer
     */
    Integer deletePlatformById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_platform数据
     *
     * @param platform the platform
     * @return the integer
     */
    Integer updatePlatformById(Platform platform);

    /**
     * 新增表t_platform数据
     *
     * @param platform the platform
     * @return the integer
     */
    Integer createPlatform(Platform platform);
}
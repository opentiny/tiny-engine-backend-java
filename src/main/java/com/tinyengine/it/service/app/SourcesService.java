package com.tinyengine.it.service.app;

import com.tinyengine.it.model.entity.Sources;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SourcesService {

    /**
     * 根据主键id查询表sources信息
     *
     * @param id
     */
    Sources findSourcesById(@Param("id") Integer id);

    /**
     * 根据条件查询表sources信息
     *
     * @param sources
     */
    List<Sources> findSourcesByCondition(Sources sources);

    /**
     * 根据主键id删除sources数据
     *
     * @param id
     */
    Integer deleteSourcesById(@Param("id") Integer id);

    /**
     * 根据主键id更新表sources信息
     *
     * @param sources
     */
    Integer updateSourcesById(Sources sources);

    /**
     * 新增表sources数据
     *
     * @param sources
     */
    Integer createSources(Sources sources);
}

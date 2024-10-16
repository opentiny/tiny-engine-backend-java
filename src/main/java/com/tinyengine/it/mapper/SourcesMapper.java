package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.Sources;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SourcesMapper extends BaseMapper<Sources> {

    /**
     * 查询表sources所有信息
     */
    List<Sources> findAllSources();

    /**
     * 根据主键id查询表sources数据
     *
     * @param id
     */
    Sources findSourcesById(@Param("id") Integer id);

    /**
     * 根据条件查询表sources数据
     *
     * @param sources
     */
    List<Sources> findSourcesByCondition(Sources sources);

    /**
     * 根据主键id删除表sources数据
     *
     * @param id
     */
    Integer deleteSourcesById(@Param("id") Integer id);

    /**
     * 根据主键id更新表sources数据
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
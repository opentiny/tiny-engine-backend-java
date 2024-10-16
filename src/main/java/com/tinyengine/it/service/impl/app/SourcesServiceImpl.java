package com.tinyengine.it.service.impl.app;

import com.tinyengine.it.exception.ServiceException;
import com.tinyengine.it.mapper.SourcesMapper;
import com.tinyengine.it.model.entity.Sources;
import com.tinyengine.it.service.app.SourcesService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SourcesServiceImpl implements SourcesService {

    @Autowired
    SourcesMapper sourcesMapper;


    /**
     * 根据主键id查询表sources信息
     *
     * @param id
     */
    @Override
    public Sources findSourcesById(@Param("id") Integer id) throws ServiceException {
        return sourcesMapper.findSourcesById(id);
    }

    /**
     * 根据条件查询表sources数据
     *
     * @param sources
     */
    @Override
    public List<Sources> findSourcesByCondition(Sources sources) throws ServiceException {
        return sourcesMapper.findSourcesByCondition(sources);
    }

    /**
     * 根据主键id删除表sources数据
     *
     * @param id
     */
    @Override
    public Integer deleteSourcesById(@Param("id") Integer id) throws ServiceException {
        return sourcesMapper.deleteSourcesById(id);
    }

    /**
     * 根据主键id更新表sources数据
     *
     * @param sources
     */
    @Override
    public Integer updateSourcesById(Sources sources) throws ServiceException {
        return sourcesMapper.updateSourcesById(sources);
    }

    /**
     * 新增表sources数据
     *
     * @param sources
     */
    @Override
    public Integer createSources(Sources sources) throws ServiceException {
        return sourcesMapper.createSources(sources);
    }
}

package com.tinyengine.it.service.impl.app;

import com.tinyengine.it.exception.ServiceException;
import com.tinyengine.it.mapper.PagesHistoriesMapper;
import com.tinyengine.it.model.entity.PagesHistories;
import com.tinyengine.it.service.app.PagesHistoriesService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PagesHistoriesServiceImpl implements PagesHistoriesService {

    @Autowired
    PagesHistoriesMapper pagesHistoriesMapper;


    /**
     * 根据主键id查询表pages_histories信息
     *
     * @param id
     */
    @Override
    public PagesHistories findPagesHistoriesById(@Param("id") Integer id) throws ServiceException {
        return pagesHistoriesMapper.findPagesHistoriesById(id);
    }

    /**
     * 根据条件查询表pages_histories数据
     *
     * @param pagesHistories
     */
    @Override
    public List<PagesHistories> findPagesHistoriesByCondition(PagesHistories pagesHistories) throws ServiceException {
        return pagesHistoriesMapper.findPagesHistoriesByCondition(pagesHistories);
    }

    /**
     * 根据主键id删除表pages_histories数据
     *
     * @param id
     */
    @Override
    public Integer deletePagesHistoriesById(@Param("id") Integer id) throws ServiceException {
        return pagesHistoriesMapper.deletePagesHistoriesById(id);
    }


    /**
     * 新增表pages_histories数据
     *
     * @param pagesHistories
     */
    @Override
    public Integer createPagesHistories(PagesHistories pagesHistories) throws ServiceException {
        return pagesHistoriesMapper.createPagesHistories(pagesHistories);
    }
}

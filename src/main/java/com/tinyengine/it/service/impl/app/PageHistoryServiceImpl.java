package com.tinyengine.it.service.impl.app;

import com.tinyengine.it.model.entity.PageHistory;
import com.tinyengine.it.mapper.PageHistoryMapper;
import com.tinyengine.it.service.app.PageHistoryService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.ibatis.annotations.Param;
import com.tinyengine.it.exception.ServiceException;

import java.util.List;

@Service
public class PageHistoryServiceImpl implements PageHistoryService {

    @Autowired
    private PageHistoryMapper pageHistoryMapper;

    /**
    *  查询表t_page_history所有数据
    */
    @Override
    public List<PageHistory> findAllPageHistory() throws ServiceException {
        return pageHistoryMapper.queryAllPageHistory();
    }

    /**
    *  根据主键id查询表t_page_history信息
    *  @param id
    */
    @Override
    public PageHistory findPageHistoryById(@Param("id") Integer id) throws ServiceException {
        return pageHistoryMapper.queryPageHistoryById(id);
    }

    /**
    *  根据条件查询表t_page_history数据
    *  @param pageHistory
    */
    @Override
    public List<PageHistory> findPageHistoryByCondition(PageHistory pageHistory) throws ServiceException {
        return pageHistoryMapper.queryPageHistoryByCondition(pageHistory);
    }

    /**
    *  根据主键id删除表t_page_history数据
    *  @param id
    */
    @Override
    public Integer deletePageHistoryById(@Param("id") Integer id) throws ServiceException {
        return pageHistoryMapper.deletePageHistoryById(id);
    }

    /**
    *  根据主键id更新表t_page_history数据
    *  @param pageHistory
    */
    @Override
    public Integer updatePageHistoryById(PageHistory pageHistory) throws ServiceException {
        return pageHistoryMapper.updatePageHistoryById(pageHistory);
    }

    /**
    *  新增表t_page_history数据
    *  @param pageHistory
    */
    @Override
    public Integer createPageHistory(PageHistory pageHistory) throws ServiceException {
        return pageHistoryMapper.createPageHistory(pageHistory);
    }
}

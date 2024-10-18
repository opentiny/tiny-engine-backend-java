package com.tinyengine.it.service.impl.app;

import com.tinyengine.it.model.entity.Page;
import com.tinyengine.it.mapper.PageMapper;
import com.tinyengine.it.service.app.PageService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.ibatis.annotations.Param;
import com.tinyengine.it.exception.ServiceException;

import java.util.List;

@Service
public class PageServiceImpl implements PageService {

    @Autowired
    private PageMapper pageMapper;

    /**
    *  查询表t_page所有数据
    */
    @Override
    public List<Page> findAllPage() throws ServiceException {
        return pageMapper.findAllPage();
    }

    /**
    *  根据主键id查询表t_page信息
    *  @param id
    */
    @Override
    public Page findPageById(@Param("id") Integer id) throws ServiceException {
        return pageMapper.findPageById(id);
    }

    /**
    *  根据条件查询表t_page数据
    *  @param page
    */
    @Override
    public List<Page> findPageByCondition(Page page) throws ServiceException {
        return pageMapper.findPageByCondition(page);
    }

    /**
    *  根据主键id删除表t_page数据
    *  @param id
    */
    @Override
    public Integer deletePageById(@Param("id") Integer id) throws ServiceException {
        return pageMapper.deletePageById(id);
    }

    /**
    *  根据主键id更新表t_page数据
    *  @param page
    */
    @Override
    public Integer updatePageById(Page page) throws ServiceException {
        return pageMapper.updatePageById(page);
    }

    /**
    *  新增表t_page数据
    *  @param page
    */
    @Override
    public Integer createPage(Page page) throws ServiceException {
        return pageMapper.createPage(page);
    }
}

package com.tinyengine.it.service.impl;

import com.tinyengine.it.model.entity.Component;
import com.tinyengine.it.mapper.ComponentMapper;
import com.tinyengine.it.service.material.ComponentService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.ibatis.annotations.Param;
import com.tinyengine.it.exception.ServiceException;

import java.util.List;

@Service
public class ComponentServiceImpl implements ComponentService {

    @Autowired
    private ComponentMapper componentMapper;

    /**
    *  查询表t_component所有数据
    */
    @Override
    public List<Component> findAllComponent() throws ServiceException {
        return componentMapper.findAllComponent();
    }

    /**
    *  根据主键id查询表t_component信息
    *  @param id
    */
    @Override
    public Component findComponentById(@Param("id") Integer id) throws ServiceException {
        return componentMapper.findComponentById(id);
    }

    /**
    *  根据条件查询表t_component数据
    *  @param component
    */
    @Override
    public List<Component> findComponentByCondition(Component component) throws ServiceException {
        return componentMapper.findComponentByCondition(component);
    }

    /**
    *  根据主键id删除表t_component数据
    *  @param id
    */
    @Override
    public Integer deleteComponentById(@Param("id") Integer id) throws ServiceException {
        return componentMapper.deleteComponentById(id);
    }

    /**
    *  根据主键id更新表t_component数据
    *  @param component
    */
    @Override
    public Integer updateComponentById(Component component) throws ServiceException {
        return componentMapper.updateComponentById(component);
    }

    /**
    *  新增表t_component数据
    *  @param component
    */
    @Override
    public Integer createComponent(Component component) throws ServiceException {
        return componentMapper.createComponent(component);
    }
}

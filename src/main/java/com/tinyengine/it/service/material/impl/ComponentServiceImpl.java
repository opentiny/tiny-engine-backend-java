package com.tinyengine.it.service.material.impl;

import com.tinyengine.it.mapper.ComponentMapper;
import com.tinyengine.it.model.entity.Component;
import com.tinyengine.it.service.material.ComponentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Component service.
 *
 * @since 2024-10-20
 */
@Service
@Slf4j
public class ComponentServiceImpl implements ComponentService {
    @Autowired
    private ComponentMapper componentMapper;

    /**
     * 查询表t_component所有数据
     */
    @Override
    public List<Component> findAllComponent() {
        return componentMapper.queryAllComponent();
    }

    /**
     * 根据主键id查询表t_component信息
     *
     * @param id id
     * @return query result
     */
    @Override
    public Component findComponentById(@Param("id") Integer id) {
        return componentMapper.queryComponentById(id);
    }

    /**
     * 根据条件查询表t_component数据
     *
     * @param component component
     * @return query result
     */
    @Override
    public List<Component> findComponentByCondition(Component component) {
        return componentMapper.queryComponentByCondition(component);
    }

    /**
     * 根据主键id删除表t_component数据
     *
     * @param id id
     * @return execute success data number
     */
    @Override
    public Integer deleteComponentById(@Param("id") Integer id) {
        return componentMapper.deleteComponentById(id);
    }

    /**
     * 根据主键id更新表t_component数据
     *
     * @param component component
     * @return execute success data number
     */
    @Override
    public Integer updateComponentById(Component component) {
        return componentMapper.updateComponentById(component);
    }

    /**
     * 新增表t_component数据
     *
     * @param component component
     * @return execute success data number
     */
    @Override
    public Integer createComponent(Component component) {
        return componentMapper.createComponent(component);
    }
}

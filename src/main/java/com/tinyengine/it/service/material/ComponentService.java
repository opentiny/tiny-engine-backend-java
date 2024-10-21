package com.tinyengine.it.service.material;

import com.tinyengine.it.model.entity.Component;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ComponentService{

    /**
    *  查询表t_component所有信息
    */
    List<Component> findAllComponent();

    /**
    *  根据主键id查询表t_component信息
    *  @param id
    */
    Component findComponentById(@Param("id") Integer id);

    /**
    *  根据条件查询表t_component信息
    *  @param component
    */
    List<Component> findComponentByCondition(Component component);

    /**
    *  根据主键id删除t_component数据
    *  @param id
    */
    Integer deleteComponentById(@Param("id") Integer id);

    /**
    *  根据主键id更新表t_component信息
    *  @param component
    */
    Integer updateComponentById(Component component);

    /**
    *  新增表t_component数据
    *  @param component
    */
    Integer createComponent(Component component);
}

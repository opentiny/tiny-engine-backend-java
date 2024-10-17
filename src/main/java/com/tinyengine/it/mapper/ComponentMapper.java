package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.Component;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ComponentMapper extends BaseMapper<Component> {

    /**
    *  查询表t_component所有信息
    */
    List<Component> findAllComponent();

    /**
    * 根据主键id查询表t_component数据
    * @param id
    */
    Component findComponentById(@Param("id") Integer id);
    /**
    *  根据条件查询表t_component数据
    *  @param component
    */
    List<Component> findComponentByCondition(Component component);

    /**
    *  根据主键id删除表t_component数据
    *  @param id
    */
    Integer deleteComponentById(@Param("id") Integer id);

    /**
    *  根据主键id更新表t_component数据
    *  @param component
    */
    Integer updateComponentById(Component component);

    /**
    *  新增表t_component数据
    *  @param component
    */
    Integer createComponent(Component component);
}
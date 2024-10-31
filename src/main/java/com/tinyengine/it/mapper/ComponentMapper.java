package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.Component;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface Component mapper.
 *
 * @since 2024-10-20
 */
public interface ComponentMapper extends BaseMapper<Component> {
    /**
     * 查询表t_component所有信息
     *
     * @return the list
     */
    List<Component> queryAllComponent();

    /**
     * 根据主键id查询表t_component数据
     *
     * @param id the id
     * @return the component
     */
    Component queryComponentById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_component数据
     *
     * @param component the component
     * @return the list
     */
    List<Component> queryComponentByCondition(Component component);

    /**
     * 根据主键id删除表t_component数据
     *
     * @param id the id
     * @return the integer
     */
    Integer deleteComponentById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_component数据
     *
     * @param component the component
     * @return the integer
     */
    Integer updateComponentById(Component component);

    /**
     * 新增表t_component数据
     *
     * @param component the component
     * @return the integer
     */
    Integer createComponent(Component component);
}
package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.UserComponents;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserComponentsMapper extends BaseMapper<UserComponents> {

    /**
     * 查询表user_components所有信息
     */
    List<UserComponents> findAllUserComponents();

    /**
     * 根据主键id查询表user_components数据
     *
     * @param id
     */
    UserComponents findUserComponentsById(@Param("id") Integer id);

    /**
     * 根据条件查询表user_components数据
     *
     * @param userComponents
     */
    List<UserComponents> findUserComponentsByCondition(UserComponents userComponents);

    /**
     * 根据主键id删除表user_components数据
     *
     * @param id
     */
    Integer deleteUserComponentsById(@Param("id") Integer id);

    /**
     * 根据主键id更新表user_components数据
     *
     * @param userComponents
     */
    Integer updateUserComponentsById(UserComponents userComponents);

    /**
     * 新增表user_components数据
     *
     * @param userComponents
     */
    Integer createUserComponents(UserComponents userComponents);
}
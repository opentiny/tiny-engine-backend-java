package com.tinyengine.it.service.impl.material;

import com.tinyengine.it.exception.ServiceException;
import com.tinyengine.it.mapper.UserComponentsMapper;
import com.tinyengine.it.model.entity.UserComponents;
import com.tinyengine.it.service.material.UserComponentsService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserComponentsServiceImpl implements UserComponentsService {

    @Autowired
    UserComponentsMapper userComponentsMapper;

    /**
     * 查询表user_components所有数据
     */
    @Override
    public List<UserComponents> findAllUserComponents() throws ServiceException {
        return userComponentsMapper.findAllUserComponents();
    }

    /**
     * 根据主键id查询表user_components信息
     *
     * @param id
     */
    @Override
    public UserComponents findUserComponentsById(@Param("id") Integer id) throws ServiceException {
        return userComponentsMapper.findUserComponentsById(id);
    }

    /**
     * 根据条件查询表user_components数据
     *
     * @param userComponents
     */
    @Override
    public List<UserComponents> findUserComponentsByCondition(UserComponents userComponents) throws ServiceException {
        return userComponentsMapper.findUserComponentsByCondition(userComponents);
    }

    /**
     * 根据主键id删除表user_components数据
     *
     * @param id
     */
    @Override
    public Integer deleteUserComponentsById(@Param("id") Integer id) throws ServiceException {
        return userComponentsMapper.deleteUserComponentsById(id);
    }

    /**
     * 根据主键id更新表user_components数据
     *
     * @param userComponents
     */
    @Override
    public Integer updateUserComponentsById(UserComponents userComponents) throws ServiceException {
        return userComponentsMapper.updateUserComponentsById(userComponents);
    }

    /**
     * 新增表user_components数据
     *
     * @param userComponents
     */
    @Override
    public Integer createUserComponents(UserComponents userComponents) throws ServiceException {
        return userComponentsMapper.createUserComponents(userComponents);
    }
}

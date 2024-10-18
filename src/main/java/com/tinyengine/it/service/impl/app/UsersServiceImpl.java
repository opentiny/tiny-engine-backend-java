package com.tinyengine.it.service.impl.app;

import com.tinyengine.it.model.entity.Users;
import com.tinyengine.it.mapper.UsersMapper;
import com.tinyengine.it.service.app.UsersService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.ibatis.annotations.Param;
import com.tinyengine.it.exception.ServiceException;

import java.util.List;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersMapper usersMapper;

    /**
    *  查询表t_users所有数据
    */
    @Override
    public List<Users> findAllUsers() throws ServiceException {
        return usersMapper.findAllUsers();
    }

    /**
    *  根据主键id查询表t_users信息
    *  @param id
    */
    @Override
    public Users findUsersById(@Param("id") Integer id) throws ServiceException {
        return usersMapper.findUsersById(id);
    }

    /**
    *  根据条件查询表t_users数据
    *  @param users
    */
    @Override
    public List<Users> findUsersByCondition(Users users) throws ServiceException {
        return usersMapper.findUsersByCondition(users);
    }

    /**
    *  根据主键id删除表t_users数据
    *  @param id
    */
    @Override
    public Integer deleteUsersById(@Param("id") Integer id) throws ServiceException {
        return usersMapper.deleteUsersById(id);
    }

    /**
    *  根据主键id更新表t_users数据
    *  @param users
    */
    @Override
    public Integer updateUsersById(Users users) throws ServiceException {
        return usersMapper.updateUsersById(users);
    }

    /**
    *  新增表t_users数据
    *  @param users
    */
    @Override
    public Integer createUsers(Users users) throws ServiceException {
        return usersMapper.createUsers(users);
    }
}

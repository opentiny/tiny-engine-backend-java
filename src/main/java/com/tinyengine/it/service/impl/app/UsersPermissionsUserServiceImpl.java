package com.tinyengine.it.service.impl.app;

import com.tinyengine.it.exception.ServiceException;
import com.tinyengine.it.mapper.UsersPermissionsUserMapper;
import com.tinyengine.it.model.entity.UsersPermissionsUser;
import com.tinyengine.it.service.app.UsersPermissionsUserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersPermissionsUserServiceImpl implements UsersPermissionsUserService {

    @Autowired
    UsersPermissionsUserMapper usersPermissionsUserMapper;

    /**
     * 查询表users-permissions_user所有数据
     */
    @Override
    public List<UsersPermissionsUser> findAllUsersPermissionsUser() throws ServiceException {
        return usersPermissionsUserMapper.findAllUsersPermissionsUser();
    }

    /**
     * 根据主键id查询表users-permissions_user信息
     *
     * @param id
     */
    @Override
    public UsersPermissionsUser findUsersPermissionsUserById(@Param("id") Integer id) throws ServiceException {
        return usersPermissionsUserMapper.findUsersPermissionsUserById(id);
    }

    /**
     * 根据条件查询表users-permissions_user数据
     *
     * @param usersPermissionsUser
     */
    @Override
    public List<UsersPermissionsUser> findUsersPermissionsUserByCondition(UsersPermissionsUser usersPermissionsUser) throws ServiceException {
        return usersPermissionsUserMapper.findUsersPermissionsUserByCondition(usersPermissionsUser);
    }

    /**
     * 根据主键id删除表users-permissions_user数据
     *
     * @param id
     */
    @Override
    public Integer deleteUsersPermissionsUserById(@Param("id") Integer id) throws ServiceException {
        return usersPermissionsUserMapper.deleteUsersPermissionsUserById(id);
    }

    /**
     * 根据主键id更新表users-permissions_user数据
     *
     * @param usersPermissionsUser
     */
    @Override
    public Integer updateUsersPermissionsUserById(UsersPermissionsUser usersPermissionsUser) throws ServiceException {
        return usersPermissionsUserMapper.updateUsersPermissionsUserById(usersPermissionsUser);
    }

    /**
     * 新增表users-permissions_user数据
     *
     * @param usersPermissionsUser
     */
    @Override
    public Integer createUsersPermissionsUser(UsersPermissionsUser usersPermissionsUser) throws ServiceException {
        return usersPermissionsUserMapper.createUsersPermissionsUser(usersPermissionsUser);
    }

    /**
     * @param ids
     * @return
     */
    @Override
    public List<UsersPermissionsUser> selectUsersByIds(List<String> ids) {
        return usersPermissionsUserMapper.selectUsersByIds(ids);
    }
}

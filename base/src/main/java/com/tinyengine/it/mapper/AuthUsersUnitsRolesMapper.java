package com.tinyengine.it.mapper;

import com.tinyengine.it.model.entity.AuthUsersUnitsRoles;
import com.tinyengine.it.model.entity.Resource;
import com.tinyengine.it.model.entity.AuthUsersUnitsRoles;
import com.tinyengine.it.model.entity.Tenant;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AuthUsersUnitsRolesMapper {
    /**
     * 查询表r_auth_users_units_roles所有信息
     *
     * @return the list
     */
    List<AuthUsersUnitsRoles> queryAllAuthUsersUnitsRoles();

    /**
     * 查询表r_auth_users_units_roles所有信息
     *
     * @param userId the userId
     * @return the list
     */
    @Select("SELECT DISTINCT tt.* FROM t_tenant tt " +
            "INNER JOIN r_auth_users_units_roles raur ON tt.id = raur.tenant_id " +
            "WHERE raur.user_id = #{userId}")
    List<Tenant> queryAllTenantByUserId(Integer userId);

    /**
     * 根据主键id查询表r_auth_users_units_roles数据
     *
     * @param id the id
     * @return the page template
     */
    AuthUsersUnitsRoles queryAuthUsersUnitsRolesById(@Param("id") Integer id);

    /**
     * 根据条件查询表r_auth_users_units_roles数据
     *
     * @param authUsersUnitsRoles the page template
     * @return the list
     */
    List<AuthUsersUnitsRoles> queryAuthUsersUnitsRolesByCondition(AuthUsersUnitsRoles authUsersUnitsRoles);

    /**
     * 根据主键id删除表r_auth_users_units_roles数据
     *
     * @param id id
     * @return the integer
     */
    Integer deleteAuthUsersUnitsRolesById(Integer id);

    /**
     * 根据主键id更新表r_auth_users_units_roles数据
     *
     * @param authUsersUnitsRoles the page template
     * @return the integer
     */
    Integer updateAuthUsersUnitsRolesById(AuthUsersUnitsRoles authUsersUnitsRoles);

    /**
     * 新增表r_auth_users_units_roles数据
     *
     * @param authUsersUnitsRoles the page template
     * @return the integer
     */
    Integer createAuthUsersUnitsRoles(AuthUsersUnitsRoles authUsersUnitsRoles);
}

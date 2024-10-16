package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.Tenants;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TenantsMapper extends BaseMapper<Tenants> {

    /**
     * 查询表tenants所有信息
     */
    List<Tenants> findAllTenants();

    /**
     * 根据主键id查询表tenants数据
     *
     * @param id
     */
    Tenants findTenantsById(@Param("id") Integer id);

    /**
     * 根据条件查询表tenants数据
     *
     * @param tenants
     */
    List<Tenants> findTenantsByCondition(Tenants tenants);

    /**
     * 根据主键id删除表tenants数据
     *
     * @param id
     */
    Integer deleteTenantsById(@Param("id") Integer id);

    /**
     * 根据主键id更新表tenants数据
     *
     * @param tenants
     */
    Integer updateTenantsById(Tenants tenants);

    /**
     * 新增表tenants数据
     *
     * @param tenants
     */
    Integer createTenants(Tenants tenants);
}
package com.tinyengine.it.service.app;

import com.tinyengine.it.model.entity.Datasource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DatasourceService{

    /**
    *  查询表t_datasource所有信息
    */
    List<Datasource> findAllDatasource();

    /**
    *  根据主键id查询表t_datasource信息
    *  @param id
    */
    Datasource findDatasourceById(@Param("id") Integer id);

    /**
    *  根据条件查询表t_datasource信息
    *  @param datasource
    */
    List<Datasource> findDatasourceByCondition(Datasource datasource);

    /**
    *  根据主键id删除t_datasource数据
    *  @param id
    */
    Integer deleteDatasourceById(@Param("id") Integer id);

    /**
    *  根据主键id更新表t_datasource信息
    *  @param datasource
    */
    Integer updateDatasourceById(Datasource datasource);

    /**
    *  新增表t_datasource数据
    *  @param datasource
    */
    Integer createDatasource(Datasource datasource);
}

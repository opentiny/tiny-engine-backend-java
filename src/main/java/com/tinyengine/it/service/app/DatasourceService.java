package com.tinyengine.it.service.app;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.model.entity.Datasource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface Datasource service.
 *
 * @since 2024-10-20
 */
public interface DatasourceService {
    /**
     * 根据主键id查询表t_datasource信息
     *
     * @param id the id
     * @return the datasource
     */
    Datasource queryDatasourceById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_datasource信息
     *
     * @param datasource the datasource
     * @return the list
     */
    List<Datasource> queryDatasourceByCondition(Datasource datasource);

    /**
     * 根据主键id删除t_datasource数据
     *
     * @param id the id
     * @return the result
     */
    Result<Datasource> deleteDatasourceById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_datasource信息
     *
     * @param datasource the datasource
     * @return the result
     */
    Result<Datasource> updateDatasourceById(Datasource datasource);

    /**
     * 新增表t_datasource数据
     *
     * @param datasource the datasource
     * @return the result
     * @throws Exception the exception
     */
    Result<Datasource> createDatasource(Datasource datasource) throws Exception;
}

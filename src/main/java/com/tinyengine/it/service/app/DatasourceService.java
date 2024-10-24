package com.tinyengine.it.service.app;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.model.entity.Datasource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DatasourceService {

    /**
     * 根据主键id查询表t_datasource信息
     *
     * @param id
     */
    Datasource queryDatasourceById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_datasource信息
     *
     * @param datasource
     */
    List<Datasource> queryDatasourceByCondition(Datasource datasource);

    /**
     * 根据主键id删除t_datasource数据
     *
     * @param id
     */
    Result<Datasource> deleteDatasourceById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_datasource信息
     *
     * @param datasource
     */
    Result<Datasource> updateDatasourceById(Datasource datasource);

    /**
     * 新增表t_datasource数据
     *
     * @param datasource
     */
    Result<Datasource> createDatasource(Datasource datasource) throws Exception;
}

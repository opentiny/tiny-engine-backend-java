package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.Datasource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DatasourceMapper extends BaseMapper<Datasource> {

    /**
     * 查询表t_datasource所有信息
     */
    List<Datasource> queryAllDatasource();

    /**
     * 根据主键id查询表t_datasource数据
     *
     * @param id
     */
    Datasource queryDatasourceById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_datasource数据
     *
     * @param datasource
     */
    List<Datasource> queryDatasourceByCondition(Datasource datasource);

    /**
     * 根据主键id删除表t_datasource数据
     *
     * @param id
     */
    Integer deleteDatasourceById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_datasource数据
     *
     * @param datasource
     */
    Integer updateDatasourceById(Datasource datasource);

    /**
     * 新增表t_datasource数据
     *
     * @param datasource
     */
    Integer createDatasource(Datasource datasource);
}
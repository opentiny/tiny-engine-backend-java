package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.Datasource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface Datasource mapper.
 */
public interface DatasourceMapper extends BaseMapper<Datasource> {

    /**
     * 查询表t_datasource所有信息
     *
     * @return the list
     */
    List<Datasource> queryAllDatasource();

    /**
     * 根据主键id查询表t_datasource数据
     *
     * @param id the id
     * @return the datasource
     */
    Datasource queryDatasourceById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_datasource数据
     *
     * @param datasource the datasource
     * @return the list
     */
    List<Datasource> queryDatasourceByCondition(Datasource datasource);

    /**
     * 根据主键id删除表t_datasource数据
     *
     * @param id the id
     * @return the integer
     */
    Integer deleteDatasourceById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_datasource数据
     *
     * @param datasource the datasource
     * @return the integer
     */
    Integer updateDatasourceById(Datasource datasource);

    /**
     * 新增表t_datasource数据
     *
     * @param datasource the datasource
     * @return the integer
     */
    Integer createDatasource(Datasource datasource);
}
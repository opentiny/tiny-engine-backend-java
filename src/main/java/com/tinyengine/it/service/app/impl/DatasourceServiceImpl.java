package com.tinyengine.it.service.app.impl;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.mapper.DatasourceMapper;
import com.tinyengine.it.model.entity.Datasource;
import com.tinyengine.it.service.app.DatasourceService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatasourceServiceImpl implements DatasourceService {

    @Autowired
    private DatasourceMapper datasourceMapper;


    /**
     * 根据主键id查询表t_datasource信息
     *
     * @param id
     */
    @Override
    public Datasource queryDatasourceById(@Param("id") Integer id) throws ServiceException {
        return datasourceMapper.queryDatasourceById(id);
    }

    /**
     * 根据条件查询表t_datasource数据
     *
     * @param datasource
     */
    @Override
    public List<Datasource> queryDatasourceByCondition(Datasource datasource) throws ServiceException {
        return datasourceMapper.queryDatasourceByCondition(datasource);
    }

    /**
     * 根据主键id删除表t_datasource数据
     *
     * @param id
     */
    @Override
    public Result<Datasource> deleteDatasourceById(@Param("id") Integer id) throws ServiceException {
        Datasource sources = queryDatasourceById(id);
        if (sources != null) {
            datasourceMapper.deleteDatasourceById(id);
            return Result.success(sources);
        }
        return Result.failed(ExceptionEnum.CM009);
    }

    /**
     * 根据主键id更新表t_datasource数据
     *
     * @param datasource
     */
    @Override
    public Result<Datasource> updateDatasourceById(Datasource datasource) throws ServiceException {
        int res = datasourceMapper.updateDatasourceById(datasource);
        if (res == 1) {
            datasource = queryDatasourceById(datasource.getId());
            return Result.success(datasource);
        }
        return Result.failed(ExceptionEnum.CM001);
    }

    /**
     * 新增表t_datasource数据
     *
     * @param datasource
     */
    @Override
    public Result<Datasource> createDatasource(Datasource datasource) throws Exception {
        Integer appId = datasource.getApp();
        String name = datasource.getName();
        if (appId != 0 && String.valueOf(appId).matches("^[0-9]+$") && !name.isEmpty()) {
            int res = datasourceMapper.createDatasource(datasource);
            if (res == 1) {
                int id = datasource.getId();
                datasource = queryDatasourceById(id);
                return Result.success(datasource);
            } else {
                Result.failed(ExceptionEnum.CM001);
            }
        }
        throw new Exception("The request body is missing some parameters");

    }
}

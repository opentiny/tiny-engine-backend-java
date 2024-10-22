package com.tinyengine.it.service.impl.app;

import com.tinyengine.it.exception.ExceptionEnum;
import com.tinyengine.it.model.dto.Result;
import com.tinyengine.it.model.entity.Datasource;
import com.tinyengine.it.mapper.DatasourceMapper;
import com.tinyengine.it.service.app.DatasourceService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.ibatis.annotations.Param;
import com.tinyengine.it.exception.ServiceException;

import java.util.List;

@Service
public class DatasourceServiceImpl implements DatasourceService {

    @Autowired
    private DatasourceMapper datasourceMapper;

    /**
     * 查询表t_datasource所有数据
     */
    @Override
    public List<Datasource> queryAllDatasource() throws ServiceException {
        return datasourceMapper.queryAllDatasource();
    }

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
        Datasource sources = datasourceMapper.queryDatasourceById(id);
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
            datasource = datasourceMapper.queryDatasourceById(datasource.getId());
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
        long appId = datasource.getAppId();
        String name = datasource.getName();
        if (appId != 0 && String.valueOf(appId).matches("^[0-9]+$") && !name.isEmpty()) {
            int res = datasourceMapper.createDatasource(datasource);
            if (res == 1) {
                int id = datasource.getId();
                datasource = datasourceMapper.queryDatasourceById(id);
                return Result.success(datasource);
            } else {
                Result.failed(ExceptionEnum.CM001);
            }
        }
        throw new Exception("The request body is missing some parameters");

    }
}

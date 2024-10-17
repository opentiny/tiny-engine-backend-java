package com.tinyengine.it.service.impl.app;

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
    *  查询表t_datasource所有数据
    */
    @Override
    public List<Datasource> findAllDatasource() throws ServiceException {
        return datasourceMapper.findAllDatasource();
    }

    /**
    *  根据主键id查询表t_datasource信息
    *  @param id
    */
    @Override
    public Datasource findDatasourceById(@Param("id") Integer id) throws ServiceException {
        return datasourceMapper.findDatasourceById(id);
    }

    /**
    *  根据条件查询表t_datasource数据
    *  @param datasource
    */
    @Override
    public List<Datasource> findDatasourceByCondition(Datasource datasource) throws ServiceException {
        return datasourceMapper.findDatasourceByCondition(datasource);
    }

    /**
    *  根据主键id删除表t_datasource数据
    *  @param id
    */
    @Override
    public Integer deleteDatasourceById(@Param("id") Integer id) throws ServiceException {
        return datasourceMapper.deleteDatasourceById(id);
    }

    /**
    *  根据主键id更新表t_datasource数据
    *  @param datasource
    */
    @Override
    public Integer updateDatasourceById(Datasource datasource) throws ServiceException {
        return datasourceMapper.updateDatasourceById(datasource);
    }

    /**
    *  新增表t_datasource数据
    *  @param datasource
    */
    @Override
    public Integer createDatasource(Datasource datasource) throws ServiceException {
        return datasourceMapper.createDatasource(datasource);
    }
}

package com.tinyengine.it.service.impl.app;

import com.tinyengine.it.exception.ServiceException;
import com.tinyengine.it.mapper.MaterialHistoriesMapper;
import com.tinyengine.it.model.entity.MaterialHistories;
import com.tinyengine.it.service.app.MaterialHistoriesService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialHistoriesServiceImpl implements MaterialHistoriesService {

    @Autowired
    MaterialHistoriesMapper materialHistoriesMapper;

    /**
     * 查询表material_histories所有数据
     */
    @Override
    public List<MaterialHistories> findAllMaterialHistories() throws ServiceException {
        return materialHistoriesMapper.findAllMaterialHistories();
    }

    /**
     * 根据主键id查询表material_histories信息
     *
     * @param id
     */
    @Override
    public MaterialHistories findMaterialHistoriesById(@Param("id") Integer id) throws ServiceException {
        return materialHistoriesMapper.findMaterialHistoriesById(id);
    }


    /**
     * 根据主键id删除表material_histories数据
     *
     * @param id
     */
    @Override
    public Integer deleteMaterialHistoriesById(@Param("id") Integer id) throws ServiceException {
        return materialHistoriesMapper.deleteMaterialHistoriesById(id);
    }

    /**
     * 根据主键id更新表material_histories数据
     *
     * @param materialHistories
     */
    @Override
    public Integer updateMaterialHistoriesById(MaterialHistories materialHistories) throws ServiceException {
        return materialHistoriesMapper.updateMaterialHistoriesById(materialHistories);
    }

    /**
     * 新增表material_histories数据
     *
     * @param materialHistories
     */
    @Override
    public Integer createMaterialHistories(MaterialHistories materialHistories) throws ServiceException {
        return materialHistoriesMapper.createMaterialHistories(materialHistories);
    }
}

package com.tinyengine.it.service.impl.material;

import com.tinyengine.it.model.entity.MaterialHistory;
import com.tinyengine.it.mapper.MaterialHistoryMapper;
import com.tinyengine.it.service.material.MaterialHistoryService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.ibatis.annotations.Param;
import com.tinyengine.it.exception.ServiceException;

import java.util.List;

@Service
public class MaterialHistoryServiceImpl implements MaterialHistoryService {

    @Autowired
    private MaterialHistoryMapper materialHistoryMapper;

    /**
    *  查询表t_material_history所有数据
    */
    @Override
    public List<MaterialHistory> findAllMaterialHistory() throws ServiceException {
        return materialHistoryMapper.findAllMaterialHistory();
    }

    /**
    *  根据主键id查询表t_material_history信息
    *  @param id
    */
    @Override
    public MaterialHistory findMaterialHistoryById(@Param("id") Integer id) throws ServiceException {
        return materialHistoryMapper.findMaterialHistoryById(id);
    }

    /**
    *  根据条件查询表t_material_history数据
    *  @param materialHistory
    */
    @Override
    public List<MaterialHistory> findMaterialHistoryByCondition(MaterialHistory materialHistory) throws ServiceException {
        return materialHistoryMapper.findMaterialHistoryByCondition(materialHistory);
    }

    /**
    *  根据主键id删除表t_material_history数据
    *  @param id
    */
    @Override
    public Integer deleteMaterialHistoryById(@Param("id") Integer id) throws ServiceException {
        return materialHistoryMapper.deleteMaterialHistoryById(id);
    }

    /**
    *  根据主键id更新表t_material_history数据
    *  @param materialHistory
    */
    @Override
    public Integer updateMaterialHistoryById(MaterialHistory materialHistory) throws ServiceException {
        return materialHistoryMapper.updateMaterialHistoryById(materialHistory);
    }

    /**
    *  新增表t_material_history数据
    *  @param materialHistory
    */
    @Override
    public Integer createMaterialHistory(MaterialHistory materialHistory) throws ServiceException {
        return materialHistoryMapper.createMaterialHistory(materialHistory);
    }
}

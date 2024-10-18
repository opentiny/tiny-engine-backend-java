package com.tinyengine.it.service.impl.material;

import com.tinyengine.it.model.entity.MaterialHistoryComponent;
import com.tinyengine.it.mapper.MaterialHistoryComponentMapper;
import com.tinyengine.it.service.material.MaterialHistoryComponentService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.ibatis.annotations.Param;
import com.tinyengine.it.exception.ServiceException;

import java.util.List;

@Service
public class MaterialHistoryComponentServiceImpl implements MaterialHistoryComponentService {

    @Autowired
    private MaterialHistoryComponentMapper materialHistoryComponentMapper;

    /**
    *  查询表r_material_history_component所有数据
    */
    @Override
    public List<MaterialHistoryComponent> findAllMaterialHistoryComponent() throws ServiceException {
        return materialHistoryComponentMapper.findAllMaterialHistoryComponent();
    }

    /**
    *  根据主键id查询表r_material_history_component信息
    *  @param id
    */
    @Override
    public MaterialHistoryComponent findMaterialHistoryComponentById(@Param("id") Integer id) throws ServiceException {
        return materialHistoryComponentMapper.findMaterialHistoryComponentById(id);
    }

    /**
    *  根据条件查询表r_material_history_component数据
    *  @param materialHistoryComponent
    */
    @Override
    public List<MaterialHistoryComponent> findMaterialHistoryComponentByCondition(MaterialHistoryComponent materialHistoryComponent) throws ServiceException {
        return materialHistoryComponentMapper.findMaterialHistoryComponentByCondition(materialHistoryComponent);
    }

    /**
    *  根据主键id删除表r_material_history_component数据
    *  @param id
    */
    @Override
    public Integer deleteMaterialHistoryComponentById(@Param("id") Integer id) throws ServiceException {
        return materialHistoryComponentMapper.deleteMaterialHistoryComponentById(id);
    }

    /**
    *  根据主键id更新表r_material_history_component数据
    *  @param materialHistoryComponent
    */
    @Override
    public Integer updateMaterialHistoryComponentById(MaterialHistoryComponent materialHistoryComponent) throws ServiceException {
        return materialHistoryComponentMapper.updateMaterialHistoryComponentById(materialHistoryComponent);
    }

    /**
    *  新增表r_material_history_component数据
    *  @param materialHistoryComponent
    */
    @Override
    public Integer createMaterialHistoryComponent(MaterialHistoryComponent materialHistoryComponent) throws ServiceException {
        return materialHistoryComponentMapper.createMaterialHistoryComponent(materialHistoryComponent);
    }
}

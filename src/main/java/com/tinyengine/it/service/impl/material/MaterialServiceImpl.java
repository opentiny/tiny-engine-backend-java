package com.tinyengine.it.service.impl.material;

import com.tinyengine.it.model.entity.Material;
import com.tinyengine.it.mapper.MaterialMapper;
import com.tinyengine.it.service.material.MaterialService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.ibatis.annotations.Param;
import com.tinyengine.it.exception.ServiceException;

import java.util.List;

@Service
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    private MaterialMapper materialMapper;

    /**
    *  查询表t_material所有数据
    */
    @Override
    public List<Material> findAllMaterial() throws ServiceException {
        return materialMapper.findAllMaterial();
    }

    /**
    *  根据主键id查询表t_material信息
    *  @param id
    */
    @Override
    public Material findMaterialById(@Param("id") Integer id) throws ServiceException {
        return materialMapper.findMaterialById(id);
    }

    /**
    *  根据条件查询表t_material数据
    *  @param material
    */
    @Override
    public List<Material> findMaterialByCondition(Material material) throws ServiceException {
        return materialMapper.findMaterialByCondition(material);
    }

    /**
    *  根据主键id删除表t_material数据
    *  @param id
    */
    @Override
    public Integer deleteMaterialById(@Param("id") Integer id) throws ServiceException {
        return materialMapper.deleteMaterialById(id);
    }

    /**
    *  根据主键id更新表t_material数据
    *  @param material
    */
    @Override
    public Integer updateMaterialById(Material material) throws ServiceException {
        return materialMapper.updateMaterialById(material);
    }

    /**
    *  新增表t_material数据
    *  @param material
    */
    @Override
    public Integer createMaterial(Material material) throws ServiceException {
        return materialMapper.createMaterial(material);
    }
}

package com.tinyengine.it.service.material.impl;

import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.mapper.MaterialMapper;
import com.tinyengine.it.model.entity.Material;
import com.tinyengine.it.service.material.MaterialService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Material service.
 */
@Service
public class MaterialServiceImpl implements MaterialService {

    @Autowired
    private MaterialMapper materialMapper;

    /**
     * 查询表t_material所有数据
     */
    @Override
    public List<Material> queryAllMaterial() throws ServiceException {
        return materialMapper.queryAllMaterial();
    }

    /**
     * 根据主键id查询表t_material信息
     *
     * @param id
     */
    @Override
    public Material queryMaterialById(@Param("id") Integer id) throws ServiceException {
        return materialMapper.queryMaterialById(id);
    }

    /**
     * 根据条件查询表t_material数据
     *
     * @param material
     */
    @Override
    public List<Material> queryMaterialByCondition(Material material) throws ServiceException {
        return materialMapper.queryMaterialByCondition(material);
    }

    /**
     * 根据主键id删除表t_material数据
     *
     * @param id
     */
    @Override
    public Integer deleteMaterialById(@Param("id") Integer id) throws ServiceException {
        return materialMapper.deleteMaterialById(id);
    }

    /**
     * 根据主键id更新表t_material数据
     *
     * @param material
     */
    @Override
    public Integer updateMaterialById(Material material) throws ServiceException {
        return materialMapper.updateMaterialById(material);
    }

    /**
     * 新增表t_material数据
     *
     * @param material
     */
    @Override
    public Integer createMaterial(Material material) throws ServiceException {
        return materialMapper.createMaterial(material);
    }
}

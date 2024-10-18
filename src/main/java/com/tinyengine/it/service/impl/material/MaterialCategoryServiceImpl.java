package com.tinyengine.it.service.impl.material;

import com.tinyengine.it.model.entity.MaterialCategory;
import com.tinyengine.it.mapper.MaterialCategoryMapper;
import com.tinyengine.it.service.material.MaterialCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.ibatis.annotations.Param;
import com.tinyengine.it.exception.ServiceException;

import java.util.List;

@Service
public class MaterialCategoryServiceImpl implements MaterialCategoryService {

    @Autowired
    private MaterialCategoryMapper materialCategoryMapper;

    /**
    *  查询表r_material_category所有数据
    */
    @Override
    public List<MaterialCategory> findAllMaterialCategory() throws ServiceException {
        return materialCategoryMapper.findAllMaterialCategory();
    }

    /**
    *  根据主键id查询表r_material_category信息
    *  @param id
    */
    @Override
    public MaterialCategory findMaterialCategoryById(@Param("id") Integer id) throws ServiceException {
        return materialCategoryMapper.findMaterialCategoryById(id);
    }

    /**
    *  根据条件查询表r_material_category数据
    *  @param materialCategory
    */
    @Override
    public List<MaterialCategory> findMaterialCategoryByCondition(MaterialCategory materialCategory) throws ServiceException {
        return materialCategoryMapper.findMaterialCategoryByCondition(materialCategory);
    }

    /**
    *  根据主键id删除表r_material_category数据
    *  @param id
    */
    @Override
    public Integer deleteMaterialCategoryById(@Param("id") Integer id) throws ServiceException {
        return materialCategoryMapper.deleteMaterialCategoryById(id);
    }

    /**
    *  根据主键id更新表r_material_category数据
    *  @param materialCategory
    */
    @Override
    public Integer updateMaterialCategoryById(MaterialCategory materialCategory) throws ServiceException {
        return materialCategoryMapper.updateMaterialCategoryById(materialCategory);
    }

    /**
    *  新增表r_material_category数据
    *  @param materialCategory
    */
    @Override
    public Integer createMaterialCategory(MaterialCategory materialCategory) throws ServiceException {
        return materialCategoryMapper.createMaterialCategory(materialCategory);
    }
}

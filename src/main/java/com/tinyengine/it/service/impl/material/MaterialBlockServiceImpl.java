package com.tinyengine.it.service.impl.material;

import com.tinyengine.it.model.entity.MaterialBlock;
import com.tinyengine.it.mapper.MaterialBlockMapper;
import com.tinyengine.it.service.material.MaterialBlockService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.ibatis.annotations.Param;
import com.tinyengine.it.exception.ServiceException;

import java.util.List;

@Service
public class MaterialBlockServiceImpl implements MaterialBlockService {

    @Autowired
    private MaterialBlockMapper materialBlockMapper;

    /**
    *  查询表r_material_block所有数据
    */
    @Override
    public List<MaterialBlock> findAllMaterialBlock() throws ServiceException {
        return materialBlockMapper.findAllMaterialBlock();
    }

    /**
    *  根据主键id查询表r_material_block信息
    *  @param id
    */
    @Override
    public MaterialBlock findMaterialBlockById(@Param("id") Integer id) throws ServiceException {
        return materialBlockMapper.findMaterialBlockById(id);
    }

    /**
    *  根据条件查询表r_material_block数据
    *  @param materialBlock
    */
    @Override
    public List<MaterialBlock> findMaterialBlockByCondition(MaterialBlock materialBlock) throws ServiceException {
        return materialBlockMapper.findMaterialBlockByCondition(materialBlock);
    }

    /**
    *  根据主键id删除表r_material_block数据
    *  @param id
    */
    @Override
    public Integer deleteMaterialBlockById(@Param("id") Integer id) throws ServiceException {
        return materialBlockMapper.deleteMaterialBlockById(id);
    }

    /**
    *  根据主键id更新表r_material_block数据
    *  @param materialBlock
    */
    @Override
    public Integer updateMaterialBlockById(MaterialBlock materialBlock) throws ServiceException {
        return materialBlockMapper.updateMaterialBlockById(materialBlock);
    }

    /**
    *  新增表r_material_block数据
    *  @param materialBlock
    */
    @Override
    public Integer createMaterialBlock(MaterialBlock materialBlock) throws ServiceException {
        return materialBlockMapper.createMaterialBlock(materialBlock);
    }
}

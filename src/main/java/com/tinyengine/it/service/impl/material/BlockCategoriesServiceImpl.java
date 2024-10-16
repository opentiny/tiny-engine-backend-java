package com.tinyengine.it.service.impl.material;

import com.tinyengine.it.exception.ServiceException;
import com.tinyengine.it.mapper.BlockCategoriesMapper;
import com.tinyengine.it.model.entity.BlockCategories;
import com.tinyengine.it.service.material.BlockCategoriesService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlockCategoriesServiceImpl implements BlockCategoriesService {

    @Autowired
    BlockCategoriesMapper blockCategoriesMapper;


    /**
     * 根据主键id查询表block_categories信息
     *
     * @param id
     */
    @Override
    public BlockCategories findBlockCategoriesById(@Param("id") Integer id) throws ServiceException {
        return blockCategoriesMapper.findBlockCategoriesById(id);
    }

    /**
     * 根据条件查询表block_categories数据
     *
     * @param blockCategories
     */
    @Override
    public List<BlockCategories> findBlockCategoriesByCondition(BlockCategories blockCategories) throws ServiceException {
        return blockCategoriesMapper.findBlockCategoriesByCondition(blockCategories);
    }

    /**
     * 根据主键id删除表block_categories数据
     *
     * @param id
     */
    @Override
    public Integer deleteBlockCategoriesById(@Param("id") Integer id) throws ServiceException {
        return blockCategoriesMapper.deleteBlockCategoriesById(id);
    }

    /**
     * 根据主键id更新表block_categories数据
     *
     * @param blockCategories
     */
    @Override
    public Integer updateBlockCategoriesById(BlockCategories blockCategories) throws ServiceException {
        return blockCategoriesMapper.updateBlockCategoriesById(blockCategories);
    }

    /**
     * 新增表block_categories数据
     *
     * @param blockCategories
     */
    @Override
    public Integer createBlockCategories(BlockCategories blockCategories) throws ServiceException {
        return blockCategoriesMapper.createBlockCategories(blockCategories);
    }
}

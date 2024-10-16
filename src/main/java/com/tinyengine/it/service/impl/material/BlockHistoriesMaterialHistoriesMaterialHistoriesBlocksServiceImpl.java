package com.tinyengine.it.service.impl.material;

import com.tinyengine.it.exception.ServiceException;
import com.tinyengine.it.mapper.BlockHistoriesMaterialHistoriesMaterialHistoriesBlocksMapper;
import com.tinyengine.it.model.entity.BlockHistoriesMaterialHistoriesMaterialHistoriesBlocks;
import com.tinyengine.it.service.material.BlockHistoriesMaterialHistoriesMaterialHistoriesBlocksService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlockHistoriesMaterialHistoriesMaterialHistoriesBlocksServiceImpl implements BlockHistoriesMaterialHistoriesMaterialHistoriesBlocksService {

    @Autowired
    BlockHistoriesMaterialHistoriesMaterialHistoriesBlocksMapper blockHistoriesMaterialHistoriesMaterialHistoriesBlocksMapper;

    /**
     * 查询表block_histories_material_histories__material_histories_blocks所有数据
     */
    @Override
    public List<BlockHistoriesMaterialHistoriesMaterialHistoriesBlocks> findAllBlockHistoriesMaterialHistoriesMaterialHistoriesBlocks() throws ServiceException {
        return blockHistoriesMaterialHistoriesMaterialHistoriesBlocksMapper.findAllBlockHistoriesMaterialHistoriesMaterialHistoriesBlocks();
    }

    /**
     * 根据主键id查询表block_histories_material_histories__material_histories_blocks信息
     *
     * @param id
     */
    @Override
    public BlockHistoriesMaterialHistoriesMaterialHistoriesBlocks findBlockHistoriesMaterialHistoriesMaterialHistoriesBlocksById(@Param("id") Integer id) throws ServiceException {
        return blockHistoriesMaterialHistoriesMaterialHistoriesBlocksMapper.findBlockHistoriesMaterialHistoriesMaterialHistoriesBlocksById(id);
    }


    /**
     * 根据主键id删除表block_histories_material_histories__material_histories_blocks数据
     *
     * @param id
     */
    @Override
    public Integer deleteBlockHistoriesMaterialHistoriesMaterialHistoriesBlocksById(@Param("id") Integer id) throws ServiceException {
        return blockHistoriesMaterialHistoriesMaterialHistoriesBlocksMapper.deleteBlockHistoriesMaterialHistoriesMaterialHistoriesBlocksById(id);
    }

    /**
     * 根据主键id更新表block_histories_material_histories__material_histories_blocks数据
     *
     * @param blockHistoriesMaterialHistoriesMaterialHistoriesBlocks
     */
    @Override
    public Integer updateBlockHistoriesMaterialHistoriesMaterialHistoriesBlocksById(BlockHistoriesMaterialHistoriesMaterialHistoriesBlocks blockHistoriesMaterialHistoriesMaterialHistoriesBlocks) throws ServiceException {
        return blockHistoriesMaterialHistoriesMaterialHistoriesBlocksMapper.updateBlockHistoriesMaterialHistoriesMaterialHistoriesBlocksById(blockHistoriesMaterialHistoriesMaterialHistoriesBlocks);
    }

    /**
     * 新增表block_histories_material_histories__material_histories_blocks数据
     *
     * @param blockHistoriesMaterialHistoriesMaterialHistoriesBlocks
     */
    @Override
    public Integer createBlockHistoriesMaterialHistoriesMaterialHistoriesBlocks(BlockHistoriesMaterialHistoriesMaterialHistoriesBlocks blockHistoriesMaterialHistoriesMaterialHistoriesBlocks) throws ServiceException {
        return blockHistoriesMaterialHistoriesMaterialHistoriesBlocksMapper.createBlockHistoriesMaterialHistoriesMaterialHistoriesBlocks(blockHistoriesMaterialHistoriesMaterialHistoriesBlocks);
    }
}

package com.tinyengine.it.service.impl.material;

import com.tinyengine.it.exception.ServiceException;
import com.tinyengine.it.mapper.BlocksCarriersRelationsMapper;
import com.tinyengine.it.model.entity.BlocksCarriersRelations;
import com.tinyengine.it.service.material.BlocksCarriersRelationsService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlocksCarriersRelationsServiceImpl implements BlocksCarriersRelationsService {

    @Autowired
    BlocksCarriersRelationsMapper blocksCarriersRelationsMapper;

    /**
     * 查询表blocks_carriers_relations所有数据
     */
    @Override
    public List<BlocksCarriersRelations> findAllBlocksCarriersRelations() throws ServiceException {
        return blocksCarriersRelationsMapper.findAllBlocksCarriersRelations();
    }

    /**
     * 根据主键id查询表blocks_carriers_relations信息
     *
     * @param id
     */
    @Override
    public BlocksCarriersRelations findBlocksCarriersRelationsById(@Param("id") Integer id) throws ServiceException {
        return blocksCarriersRelationsMapper.findBlocksCarriersRelationsById(id);
    }


    /**
     * 根据主键id删除表blocks_carriers_relations数据
     *
     * @param id
     */
    @Override
    public Integer deleteBlocksCarriersRelationsById(@Param("id") Integer id) throws ServiceException {
        return blocksCarriersRelationsMapper.deleteBlocksCarriersRelationsById(id);
    }

    /**
     * 根据主键id更新表blocks_carriers_relations数据
     *
     * @param blocksCarriersRelations
     */
    @Override
    public Integer updateBlocksCarriersRelationsById(BlocksCarriersRelations blocksCarriersRelations) throws ServiceException {
        return blocksCarriersRelationsMapper.updateBlocksCarriersRelationsById(blocksCarriersRelations);
    }

    /**
     * 新增表blocks_carriers_relations数据
     *
     * @param blocksCarriersRelations
     */
    @Override
    public Integer createBlocksCarriersRelations(BlocksCarriersRelations blocksCarriersRelations) throws ServiceException {
        return blocksCarriersRelationsMapper.createBlocksCarriersRelations(blocksCarriersRelations);
    }
}

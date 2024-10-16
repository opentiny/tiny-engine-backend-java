package com.tinyengine.it.service.impl.material;


import com.tinyengine.it.exception.ServiceException;
import com.tinyengine.it.mapper.BlockGroupsMapper;
import com.tinyengine.it.mapper.I18nEntriesMapper;
import com.tinyengine.it.model.dto.BlockGroupsDto;
import com.tinyengine.it.model.dto.I18nEntriesDto;
import com.tinyengine.it.model.entity.BlockGroups;
import com.tinyengine.it.model.entity.Blocks;
import com.tinyengine.it.service.material.BlockGroupsService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BlockGroupsServiceImpl implements BlockGroupsService {

    @Autowired
    BlockGroupsMapper blockGroupsMapper;
    @Autowired
    I18nEntriesMapper i18nEntriesMapper;


    /**
     * 根据主键id查询表block_groups信息
     *
     * @param id
     */
    @Override
    public BlockGroupsDto findBlockGroupsById(@Param("id") Integer id) throws ServiceException {
        return blockGroupsMapper.findBlockGroupsById(id);
    }

    /**
     * 根据条件查询表block_groups数据
     *
     * @param blockGroups
     */
    @Override
    public List<BlockGroupsDto> findBlockGroupsByCondition(BlockGroups blockGroups) throws ServiceException {
        return blockGroupsMapper.findBlockGroupsByCondition(blockGroups);
    }

    /**
     * 根据主键id删除表block_groups数据
     *
     * @param id
     */
    @Override
    public Integer deleteBlockGroupsById(@Param("id") Integer id) throws ServiceException {
        return blockGroupsMapper.deleteBlockGroupsById(id);
    }

    /**
     * 根据主键id更新表block_groups数据
     *
     * @param blockGroups
     */
    @Override
    public Integer updateBlockGroupsById(BlockGroups blockGroups) throws ServiceException {
        return blockGroupsMapper.updateBlockGroupsById(blockGroups);
    }

    /**
     * 新增表block_groups数据
     *
     * @param blockGroups
     */
    @Override
    public Integer createBlockGroups(BlockGroups blockGroups) throws ServiceException {
        return blockGroupsMapper.createBlockGroups(blockGroups);
    }


    /**
     * 获取应用下的区块分组关联的区块国际化
     *
     * @param appId
     * @return
     */
    public Map<String, Object> getBlockGroupI18nEntries(int appId) {
        // 获取应用下的区块组
        List<BlockGroupsDto> blockGroupsList = blockGroupsMapper.findGroupByAppId(appId);

        List<Integer> blockIds = new ArrayList<>();
        // 从区块组中获取区块id
        blockGroupsList.forEach(group -> {
            List<Integer> ids = Optional.ofNullable(group.getBlocks())
                    .orElse(Collections.emptyList())
                    .stream().map(Blocks::getId)
                    .collect(Collectors.toList());
            blockIds.addAll(ids);
        });

        List<I18nEntriesDto> entriesList = getBlocksI18nEntries(blockIds);
        Map<String, Object> result = new HashMap<>();
        result.put("blockIds", blockIds);
        result.put("entries", entriesList);
        return result;

    }

    public List<I18nEntriesDto> getBlocksI18nEntries(List<Integer> blockIds) {
        if (blockIds.isEmpty()) {
            return new ArrayList<>();
        }

        return i18nEntriesMapper.findI18nEntriesByHostsandHostType(blockIds, "block");
    }

}

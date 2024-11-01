package com.tinyengine.it.service.material;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.model.dto.BlockDto;
import com.tinyengine.it.model.entity.Block;

import com.tinyengine.it.model.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * The interface Block service.
 *
 * @since 2024-10-20
 */
public interface BlockService {
    /**
     * 查询表t_block所有信息
     *
     * @return the list
     */
    List<Block> queryAllBlock();

    /**
     * 根据主键id查询表t_block信息
     *
     * @param id the id
     * @return the block
     */
    Block queryBlockById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_block信息
     *
     * @param block the block
     * @return the list
     */
    List<Block> queryBlockByCondition(Block block);

    /**
     * 根据主键id删除t_block数据
     *
     * @param id the id
     * @return the integer
     */
    Integer deleteBlockById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_block信息
     *
     * @param blockDto the block dto
     * @return the BlockDto
     */
    BlockDto updateBlockById(BlockDto blockDto);

    /**
     * 新增表t_block数据
     *
     * @param blockDto the block dto
     * @return the result
     */
    Result<BlockDto> createBlock(BlockDto blockDto);

    /**
     * 区块分页查询
     *
     * @param request request
     * @return the ipage
     */
    IPage<Block> findBlocksByPagetionList(Map<String, String> request);

    /**
     * 查找表中所有tags
     *
     * @return the list
     */
    List<String> allTags();


    /**
     * 根据条件进行分页查询
     *
     * @param request request
     * @return the ipage
     */
    IPage<Block> findBlocksByConditionPagetion(Map<String, String> request);

    /**
     * 获取用户
     *
     * @param blocksList the block list
     * @return the list
     */
    List<User> getUsers(List<Block> blocksList);


    /**
     * 获取区块
     *
     * @param map the map
     * @return the list
     */
    Result<List<Block>> listNew(Map<String, String> map);
}

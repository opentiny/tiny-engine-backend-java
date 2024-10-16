package com.tinyengine.it.service.material;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tinyengine.it.model.dto.BlockDto;
import com.tinyengine.it.model.entity.Blocks;
import com.tinyengine.it.model.entity.UsersPermissionsUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BlocksService {

    /**
     * 根据主键id查询表blocks信息
     *
     * @param id
     */
    Blocks findBlocksById(@Param("id") Integer id);

    /**
     * 根据条件查询表blocks信息
     *
     * @param blocks
     */
    List<Blocks> findBlocksByCondition(Blocks blocks);

    /**
     * 根据主键id删除blocks数据
     *
     * @param id
     */
    Integer deleteBlocksById(@Param("id") Integer id);

    /**
     * 根据主键id更新表blocks信息
     *
     * @param blockDto
     */
    BlockDto updateBlocksById(BlockDto blockDto);

    /**
     * 新增表blocks数据
     *
     * @param blocks
     */
    Integer createBlocks(Blocks blocks);

    /**
     * 查找表中所有tags
     *
     * @return
     */
    List<String> allTags();

    /**
     * 获取区块
     *
     * @param categoryId
     * @param appId
     * @return
     */
    List<Blocks> listNew(int categoryId, int appId);

    /**
     * 根据条件进行分页查询
     *
     * @param request
     * @return
     */
    IPage<Blocks> findBlocksByConditionPagetion(Map<String, String> request);

    IPage<Blocks> findBlocksByPagetionList(Map<String, String> request);

    /**
     * 获取用户
     *
     * @param blocksList
     * @return
     */
    List<UsersPermissionsUser> getUsers(List<Blocks> blocksList);

    /**
     * 调用DSL转换方法生成代码
     *
     * @param blocks
     * @return 转换后的代码
     * @throws Exception
     */
    List<Blocks> translate(Blocks blocks) throws Exception;


    List<BlockDto> getNotInGroupBlocks(Map<String, String> map);

}

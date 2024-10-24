package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.Page;
import org.apache.ibatis.annotations.*;

import java.util.List;
public interface PageMapper extends BaseMapper<Page> {

    /**
     * 查询表pages所有信息
     */
    List<Page> findAllPages();

    /**
     * 根据主键id查询表pages数据
     *
     * @param id
     */
    Page queryPageById(Integer id);

    /**
     * 根据条件查询表pages数据
     *
     * @param page
     */

    List<Page> queryPageByCondition(Page page);

    /**
     * 根据主键id删除表pages数据
     *
     * @param id
     */
    Integer deletePageById(@Param("id") Integer id);

    /**
     * 根据主键id更新表pages数据
     *
     * @param page
     */
    Integer updatePageById(Page page);

    /**
     * 新增表pages数据
     *
     * @param page
     */
    Integer createPage(Page page);

    /**
     * 通过app查pages
     *
     * @param appId
     */

    List<Page> queryPageByApp(Integer appId);
}
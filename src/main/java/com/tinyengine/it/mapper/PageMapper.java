package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface Page mapper.
 * @since 2024-10-20
 */
public interface PageMapper extends BaseMapper<Page> {

    /**
     * 查询表pages所有信息
     *
     * @return the list
     */
    List<Page> queryAllPage();

    /**
     * 根据主键id查询表pages数据
     *
     * @param id the id
     * @return the page
     */
    Page queryPageById(Integer id);

    /**
     * 根据条件查询表pages数据
     *
     * @param page the page
     * @return the list
     */
    List<Page> queryPageByCondition(Page page);

    /**
     * 根据主键id删除表pages数据
     *
     * @param id the id
     * @return the integer
     */
    Integer deletePageById(@Param("id") Integer id);

    /**
     * 根据主键id更新表pages数据
     *
     * @param page the page
     * @return the integer
     */
    Integer updatePageById(Page page);

    /**
     * 新增表pages数据
     *
     * @param page the page
     * @return the integer
     */
    Integer createPage(Page page);

    /**
     * 通过app查pages
     *
     * @param appId the app id
     * @return the list
     */
    List<Page> queryPageByApp(Integer appId);
}
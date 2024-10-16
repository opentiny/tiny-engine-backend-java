package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.PagesHistories;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PagesHistoriesMapper extends BaseMapper<PagesHistories> {

    /**
     * 查询表pages_histories所有信息
     */
    List<PagesHistories> findAllPagesHistories();

    /**
     * 根据主键id查询表pages_histories数据
     *
     * @param id
     */
    PagesHistories findPagesHistoriesById(@Param("id") Integer id);

    /**
     * 根据条件查询表pages_histories数据
     *
     * @param pagesHistories
     */
    List<PagesHistories> findPagesHistoriesByCondition(PagesHistories pagesHistories);

    /**
     * 根据主键id删除表pages_histories数据
     *
     * @param id
     */
    Integer deletePagesHistoriesById(@Param("id") Integer id);

    /**
     * 根据主键id更新表pages_histories数据
     *
     * @param pagesHistories
     */
    Integer updatePagesHistoriesById(PagesHistories pagesHistories);

    /**
     * 新增表pages_histories数据
     *
     * @param pagesHistories
     */
    Integer createPagesHistories(PagesHistories pagesHistories);
}
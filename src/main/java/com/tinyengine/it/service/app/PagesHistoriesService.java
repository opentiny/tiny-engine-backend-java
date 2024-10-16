package com.tinyengine.it.service.app;

import com.tinyengine.it.model.entity.PagesHistories;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PagesHistoriesService {

    /**
     * 根据主键id查询表pages_histories信息
     *
     * @param id
     */
    PagesHistories findPagesHistoriesById(@Param("id") Integer id);

    /**
     * 根据条件查询表pages_histories信息
     *
     * @param pagesHistories
     */
    List<PagesHistories> findPagesHistoriesByCondition(PagesHistories pagesHistories);

    /**
     * 根据主键id删除pages_histories数据
     *
     * @param id
     */
    Integer deletePagesHistoriesById(@Param("id") Integer id);


    /**
     * 新增表pages_histories数据
     *
     * @param pagesHistories
     */
    Integer createPagesHistories(PagesHistories pagesHistories);
}

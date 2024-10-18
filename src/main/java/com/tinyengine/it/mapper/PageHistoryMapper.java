package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.PageHistory;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PageHistoryMapper extends BaseMapper<PageHistory> {

    /**
    *  查询表t_page_history所有信息
    */
    List<PageHistory> queryAllPageHistory();

    /**
    * 根据主键id查询表t_page_history数据
    * @param id
    */
    PageHistory queryPageHistoryById(@Param("id") Integer id);
    /**
    *  根据条件查询表t_page_history数据
    *  @param pageHistory
    */
    List<PageHistory> queryPageHistoryByCondition(PageHistory pageHistory);

    /**
    *  根据主键id删除表t_page_history数据
    *  @param id
    */
    Integer deletePageHistoryById(@Param("id") Integer id);

    /**
    *  根据主键id更新表t_page_history数据
    *  @param pageHistory
    */
    Integer updatePageHistoryById(PageHistory pageHistory);

    /**
    *  新增表t_page_history数据
    *  @param pageHistory
    */
    Integer createPageHistory(PageHistory pageHistory);
}
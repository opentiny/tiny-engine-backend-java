package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.SourceTpls;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SourceTplsMapper extends BaseMapper<SourceTpls> {

    /**
     * 查询表source_tpls所有信息
     */
    List<SourceTpls> findAllSourceTpls();

    /**
     * 根据主键id查询表source_tpls数据
     *
     * @param id
     */
    SourceTpls findSourceTplsById(@Param("id") Integer id);

    /**
     * 根据条件查询表source_tpls数据
     *
     * @param sourceTpls
     */
    List<SourceTpls> findSourceTplsByCondition(SourceTpls sourceTpls);

    /**
     * 根据主键id删除表source_tpls数据
     *
     * @param id
     */
    Integer deleteSourceTplsById(@Param("id") Integer id);

    /**
     * 根据主键id更新表source_tpls数据
     *
     * @param sourceTpls
     */
    Integer updateSourceTplsById(SourceTpls sourceTpls);

    /**
     * 新增表source_tpls数据
     *
     * @param sourceTpls
     */
    Integer createSourceTpls(SourceTpls sourceTpls);
}
package com.tinyengine.it.service.app;

import com.tinyengine.it.model.entity.SourceTpls;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SourceTplsService {

    /**
     * 查询表source_tpls所有信息
     */
    List<SourceTpls> findAllSourceTpls();

    /**
     * 根据主键id查询表source_tpls信息
     *
     * @param id
     */
    SourceTpls findSourceTplsById(@Param("id") Integer id);


    /**
     * 根据主键id删除source_tpls数据
     *
     * @param id
     */
    Integer deleteSourceTplsById(@Param("id") Integer id);

    /**
     * 根据主键id更新表source_tpls信息
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

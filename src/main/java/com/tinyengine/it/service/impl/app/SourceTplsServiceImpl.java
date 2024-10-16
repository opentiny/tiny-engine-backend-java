package com.tinyengine.it.service.impl.app;

import com.tinyengine.it.exception.ServiceException;
import com.tinyengine.it.mapper.SourceTplsMapper;
import com.tinyengine.it.model.entity.SourceTpls;
import com.tinyengine.it.service.app.SourceTplsService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SourceTplsServiceImpl implements SourceTplsService {

    @Autowired
    SourceTplsMapper sourceTplsMapper;

    /**
     * 查询表source_tpls所有数据
     */
    @Override
    public List<SourceTpls> findAllSourceTpls() throws ServiceException {
        return sourceTplsMapper.findAllSourceTpls();
    }

    /**
     * 根据主键id查询表source_tpls信息
     *
     * @param id
     */
    @Override
    public SourceTpls findSourceTplsById(@Param("id") Integer id) throws ServiceException {
        return sourceTplsMapper.findSourceTplsById(id);
    }


    /**
     * 根据主键id删除表source_tpls数据
     *
     * @param id
     */
    @Override
    public Integer deleteSourceTplsById(@Param("id") Integer id) throws ServiceException {
        return sourceTplsMapper.deleteSourceTplsById(id);
    }

    /**
     * 根据主键id更新表source_tpls数据
     *
     * @param sourceTpls
     */
    @Override
    public Integer updateSourceTplsById(SourceTpls sourceTpls) throws ServiceException {
        return sourceTplsMapper.updateSourceTplsById(sourceTpls);
    }

    /**
     * 新增表source_tpls数据
     *
     * @param sourceTpls
     */
    @Override
    public Integer createSourceTpls(SourceTpls sourceTpls) throws ServiceException {
        return sourceTplsMapper.createSourceTpls(sourceTpls);
    }
}

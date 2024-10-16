package com.tinyengine.it.service.impl.app;

import com.tinyengine.it.exception.ServiceException;
import com.tinyengine.it.mapper.I18nLangsMapper;
import com.tinyengine.it.model.entity.I18nLangs;
import com.tinyengine.it.service.app.I18nLangsService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class I18nLangsServiceImpl implements I18nLangsService {

    @Autowired
    I18nLangsMapper i18nLangsMapper;

    /**
     * 查询表i18n_langs所有数据
     */
    @Override
    public List<I18nLangs> findAllI18nLangs() throws ServiceException {
        return i18nLangsMapper.findAllI18nLangs();
    }

    /**
     * 根据主键id查询表i18n_langs信息
     *
     * @param id
     */
    @Override
    public I18nLangs findI18nLangsById(@Param("id") Integer id) throws ServiceException {
        return i18nLangsMapper.findI18nLangsById(id);
    }


    /**
     * 根据主键id删除表i18n_langs数据
     *
     * @param id
     */
    @Override
    public Integer deleteI18nLangsById(@Param("id") Integer id) throws ServiceException {
        return i18nLangsMapper.deleteI18nLangsById(id);
    }

    /**
     * 根据主键id更新表i18n_langs数据
     *
     * @param i18nLangs
     */
    @Override
    public Integer updateI18nLangsById(I18nLangs i18nLangs) throws ServiceException {
        return i18nLangsMapper.updateI18nLangsById(i18nLangs);
    }

    /**
     * 新增表i18n_langs数据
     *
     * @param i18nLangs
     */
    @Override
    public Integer createI18nLangs(I18nLangs i18nLangs) throws ServiceException {
        return i18nLangsMapper.createI18nLangs(i18nLangs);
    }
}

package com.tinyengine.it.service.app.impl;

import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.mapper.I18nLangMapper;
import com.tinyengine.it.model.entity.I18nLang;
import com.tinyengine.it.service.app.I18nLangService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class I18nLangServiceImpl implements I18nLangService {

    @Autowired
    private I18nLangMapper i18nLangMapper;

    /**
     * 查询表t_i18n_lang所有数据
     */
    @Override
    public List<I18nLang> queryAllI18nLang() throws ServiceException {
        return i18nLangMapper.queryAllI18nLang();
    }

    /**
     * 根据主键id查询表t_i18n_lang信息
     *
     * @param id
     */
    @Override
    public I18nLang queryI18nLangById(@Param("id") Integer id) throws ServiceException {
        return i18nLangMapper.queryI18nLangById(id);
    }

    /**
     * 根据条件查询表t_i18n_lang数据
     *
     * @param i18nLang
     */
    @Override
    public List<I18nLang> queryI18nLangByCondition(I18nLang i18nLang) throws ServiceException {
        return i18nLangMapper.queryI18nLangByCondition(i18nLang);
    }

    /**
     * 根据主键id删除表t_i18n_lang数据
     *
     * @param id
     */
    @Override
    public Integer deleteI18nLangById(@Param("id") Integer id) throws ServiceException {
        return i18nLangMapper.deleteI18nLangById(id);
    }

    /**
     * 根据主键id更新表t_i18n_lang数据
     *
     * @param i18nLang
     */
    @Override
    public Integer updateI18nLangById(I18nLang i18nLang) throws ServiceException {
        return i18nLangMapper.updateI18nLangById(i18nLang);
    }

    /**
     * 新增表t_i18n_lang数据
     *
     * @param i18nLang
     */
    @Override
    public Integer createI18nLang(I18nLang i18nLang) throws ServiceException {
        return i18nLangMapper.createI18nLang(i18nLang);
    }
}

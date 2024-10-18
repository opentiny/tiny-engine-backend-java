package com.tinyengine.it.service.app;

import com.tinyengine.it.model.entity.I18nLang;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface I18nLangService {

    /**
     * 查询表t_i18n_lang所有信息
     */
    List<I18nLang> queryAllI18nLang();

    /**
     * 根据主键id查询表t_i18n_lang信息
     *
     * @param id
     */
    I18nLang queryI18nLangById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_i18n_lang信息
     *
     * @param i18nLang
     */
    List<I18nLang> queryI18nLangByCondition(I18nLang i18nLang);

    /**
     * 根据主键id删除t_i18n_lang数据
     *
     * @param id
     */
    Integer deleteI18nLangById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_i18n_lang信息
     *
     * @param i18nLang
     */
    Integer updateI18nLangById(I18nLang i18nLang);

    /**
     * 新增表t_i18n_lang数据
     *
     * @param i18nLang
     */
    Integer createI18nLang(I18nLang i18nLang);
}

package com.tinyengine.it.service.app;

import com.tinyengine.it.model.entity.I18nLang;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface 18 n lang service.
 *
 * @since 2024-10-20
 */
public interface I18nLangService {
    /**
     * 查询表t_i18n_lang所有信息
     *
     * @return the list
     */
    List<I18nLang> queryAllI18nLang();

    /**
     * 根据主键id查询表t_i18n_lang信息
     *
     * @param id the id
     * @return the 18 n lang
     */
    I18nLang queryI18nLangById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_i18n_lang信息
     *
     * @param i18nLang the 18 n lang
     * @return the list
     */
    List<I18nLang> queryI18nLangByCondition(I18nLang i18nLang);

    /**
     * 根据主键id删除t_i18n_lang数据
     *
     * @param id the id
     * @return the integer
     */
    Integer deleteI18nLangById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_i18n_lang信息
     *
     * @param i18nLang the 18 n lang
     * @return the integer
     */
    Integer updateI18nLangById(I18nLang i18nLang);

    /**
     * 新增表t_i18n_lang数据
     *
     * @param i18nLang the 18 n lang
     * @return the integer
     */
    Integer createI18nLang(I18nLang i18nLang);
}

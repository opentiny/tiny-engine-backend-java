package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.I18nLang;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface I18nLangMapper extends BaseMapper<I18nLang> {

    /**
     * 查询表t_i18n_lang所有信息
     */
    List<I18nLang> findAllI18nLang();

    /**
     * 根据主键id查询表t_i18n_lang数据
     *
     * @param id
     */
    I18nLang findI18nLangById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_i18n_lang数据
     *
     * @param i18nLang
     */
    List<I18nLang> findI18nLangByCondition(I18nLang i18nLang);

    /**
     * 根据主键id删除表t_i18n_lang数据
     *
     * @param id
     */
    Integer deleteI18nLangById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_i18n_lang数据
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
package com.tinyengine.it.service.app;

import com.tinyengine.it.model.entity.I18nLangs;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface I18nLangsService {

    /**
     * 查询表i18n_langs所有信息
     */
    List<I18nLangs> findAllI18nLangs();

    /**
     * 根据主键id查询表i18n_langs信息
     *
     * @param id
     */
    I18nLangs findI18nLangsById(@Param("id") Integer id);


    /**
     * 根据主键id删除i18n_langs数据
     *
     * @param id
     */
    Integer deleteI18nLangsById(@Param("id") Integer id);

    /**
     * 根据主键id更新表i18n_langs信息
     *
     * @param i18nLangs
     */
    Integer updateI18nLangsById(I18nLangs i18nLangs);

    /**
     * 新增表i18n_langs数据
     *
     * @param i18nLangs
     */
    Integer createI18nLangs(I18nLangs i18nLangs);
}

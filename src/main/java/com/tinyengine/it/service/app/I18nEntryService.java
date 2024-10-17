package com.tinyengine.it.service.app;

import com.tinyengine.it.model.entity.I18nEntry;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface I18nEntryService{

    /**
    *  查询表t_i18n_entry所有信息
    */
    List<I18nEntry> findAllI18nEntry();

    /**
    *  根据主键id查询表t_i18n_entry信息
    *  @param id
    */
    I18nEntry findI18nEntryById(@Param("id") Integer id);

    /**
    *  根据条件查询表t_i18n_entry信息
    *  @param i18nEntry
    */
    List<I18nEntry> findI18nEntryByCondition(I18nEntry i18nEntry);

    /**
    *  根据主键id删除t_i18n_entry数据
    *  @param id
    */
    Integer deleteI18nEntryById(@Param("id") Integer id);

    /**
    *  根据主键id更新表t_i18n_entry信息
    *  @param i18nEntry
    */
    Integer updateI18nEntryById(I18nEntry i18nEntry);

    /**
    *  新增表t_i18n_entry数据
    *  @param i18nEntry
    */
    Integer createI18nEntry(I18nEntry i18nEntry);
}

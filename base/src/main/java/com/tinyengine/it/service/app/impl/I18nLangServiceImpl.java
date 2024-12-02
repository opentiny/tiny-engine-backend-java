/**
 * Copyright (c) 2023 - present TinyEngine Authors.
 * Copyright (c) 2023 - present Huawei Cloud Computing Technologies Co., Ltd.
 *
 * Use of this source code is governed by an MIT-style license.
 *
 * THE OPEN SOURCE SOFTWARE IN THIS PRODUCT IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL,
 * BUT WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS FOR
 * A PARTICULAR PURPOSE. SEE THE APPLICABLE LICENSES FOR MORE DETAILS.
 *
 */

package com.tinyengine.it.service.app.impl;

import com.tinyengine.it.mapper.I18nLangMapper;
import com.tinyengine.it.model.entity.I18nLang;
import com.tinyengine.it.service.app.I18nLangService;

import lombok.extern.slf4j.Slf4j;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type 18 n lang service.
 *
 * @since 2024-10-20
 */
@Service
@Slf4j
public class I18nLangServiceImpl implements I18nLangService {
    @Autowired
    private I18nLangMapper i18nLangMapper;

    /**
     * 查询表t_i18n_lang所有数据
     *
     * @return I18nLang
     */
    @Override
    public List<I18nLang> queryAllI18nLang() {
        return i18nLangMapper.queryAllI18nLang();
    }

    /**
     * 根据主键id查询表t_i18n_lang信息
     *
     * @param id id
     * @return query result
     */
    @Override
    public I18nLang queryI18nLangById(@Param("id") Integer id) {
        return i18nLangMapper.queryI18nLangById(id);
    }

    /**
     * 根据条件查询表t_i18n_lang数据
     *
     * @param i18nLang i18nLang
     * @return query result
     */
    @Override
    public List<I18nLang> queryI18nLangByCondition(I18nLang i18nLang) {
        return i18nLangMapper.queryI18nLangByCondition(i18nLang);
    }

    /**
     * 根据主键id删除表t_i18n_lang数据
     *
     * @param id id
     * @return execute success data number
     */
    @Override
    public Integer deleteI18nLangById(@Param("id") Integer id) {
        return i18nLangMapper.deleteI18nLangById(id);
    }

    /**
     * 根据主键id更新表t_i18n_lang数据
     *
     * @param i18nLang i18nLang
     * @return execute success data number
     */
    @Override
    public Integer updateI18nLangById(I18nLang i18nLang) {
        return i18nLangMapper.updateI18nLangById(i18nLang);
    }

    /**
     * 新增表t_i18n_lang数据
     *
     * @param i18nLang i18nLang
     * @return execute success data number
     */
    @Override
    public Integer createI18nLang(I18nLang i18nLang) {
        return i18nLangMapper.createI18nLang(i18nLang);
    }
}

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

package com.tinyengine.it.service.app;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.model.dto.DeleteI18nEntry;
import com.tinyengine.it.model.dto.FileResult;
import com.tinyengine.it.model.dto.I18nEntryDto;
import com.tinyengine.it.model.dto.I18nEntryListResult;
import com.tinyengine.it.model.dto.OperateI18nBatchEntries;
import com.tinyengine.it.model.dto.OperateI18nEntries;
import com.tinyengine.it.model.dto.SchemaI18n;
import com.tinyengine.it.model.entity.I18nEntry;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * The interface 18 n entry service.
 *
 * @since 2024-10-20
 */
public interface I18nEntryService {
    /**
     * 查询表t_i18n_entry所有信息
     *
     * @return the 18 n entry list result
     */
    I18nEntryListResult findAllI18nEntry();

    /**
     * 根据主键id查询表t_i18n_entry信息
     *
     * @param id the id
     * @return the 18 n entry
     */
    I18nEntryDto findI18nEntryById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_i18n_entry信息
     *
     * @param i18nEntry the 18 n entry
     * @return the list
     */
    List<I18nEntryDto> findI18nEntryByCondition(I18nEntry i18nEntry);

    /**
     * 根据主键id更新表t_i18n_entry信息
     *
     * @param i18nEntry the 18 n entry
     * @return the integer
     */
    Integer updateI18nEntryById(I18nEntry i18nEntry);

    /**
     * 格式化词条列表
     *
     * @param i18nEntriesList the 18 n entries list
     * @return map
     */
    SchemaI18n formatEntriesList(List<I18nEntryDto> i18nEntriesList);

    /**
     * 创建多词条国际化
     *
     * @param operateI18nEntries the operate i 18 n entries
     * @return list
     */
    List<I18nEntry> create(OperateI18nEntries operateI18nEntries);

    /**
     * 批量创建
     *
     * @param operateI18nBatchEntries the operate i 18 n batch entries
     * @return list
     */
    List<I18nEntry> bulkCreate(OperateI18nBatchEntries operateI18nBatchEntries);

    /**
     * 批量更新
     *
     * @param operateI18nEntries the operate i 18 n entries
     * @return list
     */
    List<I18nEntry> bulkUpdate(OperateI18nEntries operateI18nEntries);

    /**
     * Delete i 18 n entries by host and host type and key list.
     *
     * @param deleteI18nEntry the deleteI18nEntry
     * @return the list
     */
    List<I18nEntryDto> deleteI18nEntriesByHostAndHostTypeAndKey(DeleteI18nEntry deleteI18nEntry);

    /**
     * 上传单个文件
     *
     * @param file the file
     * @param host the host
     * @return the result
     * @throws Exception the exception
     */
    Result<FileResult> readSingleFileAndBulkCreate(MultipartFile file, int host) throws Exception;

    /**
     * 批量上传词条数据
     *
     * @param lang the lang
     * @param file the file
     * @param host the host
     * @return the result
     * @throws Exception the exception
     */
    Result<FileResult> readFilesAndbulkCreate(String lang, MultipartFile file, int host) throws Exception;
}

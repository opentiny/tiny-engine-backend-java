
package com.tinyengine.it.service.app;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.model.dto.*;
import com.tinyengine.it.model.entity.I18nEntry;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

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
    Result<I18nFileResult> readSingleFileAndBulkCreate(MultipartFile file, int host) throws Exception;

    /**
     * 批量上传词条数据
     *
     * @param lang the lang
     * @param file the file
     * @param host the host
     * @return the result
     * @throws Exception the exception
     */
    Result<I18nFileResult> readFilesAndbulkCreate(String lang, MultipartFile file, int host) throws Exception;
}

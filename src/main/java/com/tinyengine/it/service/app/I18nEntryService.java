package com.tinyengine.it.service.app;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.model.dto.*;
import com.tinyengine.it.model.entity.I18nEntry;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface I18nEntryService {

    /**
     * 查询表t_i18n_entry所有信息
     */
    I18nEntryListResult findAllI18nEntry();

    /**
     * 根据主键id查询表t_i18n_entry信息
     *
     * @param id
     */
    I18nEntryDto findI18nEntryById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_i18n_entry信息
     *
     * @param i18nEntry
     */
    List<I18nEntryDto> findI18nEntryByCondition(I18nEntry i18nEntry);


    /**
     * 根据主键id更新表t_i18n_entry信息
     *
     * @param i18nEntry
     */
    Integer updateI18nEntryById(I18nEntry i18nEntry);


    /**
     * 格式化词条列表
     *
     * @param i18nEntriesList
     * @return
     */
    Map<String, Map<String, String>> formatEntriesList(List<I18nEntryDto> i18nEntriesList);


    /**
     * 创建多词条国际化
     *
     * @param operateI18nEntries
     * @return
     */
    List<I18nEntry> Create(OperateI18nEntries operateI18nEntries);

    /**
     * 批量创建
     *
     * @param operateI18nBatchEntries
     * @return
     */
    List<I18nEntry> bulkCreate(OperateI18nBatchEntries operateI18nBatchEntries);


    /**
     * 批量更新
     *
     * @param operateI18nEntries
     * @return
     */
    List<I18nEntry> bulkUpdate(OperateI18nEntries operateI18nEntries);

    List<I18nEntryDto> deleteI18nEntriesByHostAndHostTypeAndKey(DeleteI18nEntry deleteI18nEntry);

    /**
     * 上传单个文件
     */
    Result<Map<String, Object>> readSingleFileAndBulkCreate(String lang, MultipartFile file, int host) throws Exception;

    /**
     * 批量上传词条数据
     *
     * @param file
     * @param host
     */
    Result<Map<String, Object>> readFilesAndbulkCreate(String lang, MultipartFile file, int host) throws Exception;
}

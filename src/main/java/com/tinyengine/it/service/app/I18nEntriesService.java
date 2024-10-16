package com.tinyengine.it.service.app;

import com.tinyengine.it.model.dto.*;
import com.tinyengine.it.model.entity.I18nEntries;
import com.tinyengine.it.model.entity.I18nLangs;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface I18nEntriesService {

    /**
     * 查询表i18n_entries所有信息
     */
    I18nEntriesListResult findAllI18nEntries();

    /**
     * 根据主键id查询表i18n_entries信息
     *
     * @param id
     */
    I18nEntries findI18nEntriesById(@Param("id") Integer id);

    /**
     * 根据条件查询表i18n_entries信息
     *
     * @param i18nEntries
     */
    List<I18nEntries> findI18nEntriesByCondition(I18nEntries i18nEntries);

    /**
     * 根据主键id删除i18n_entries数据
     *
     * @param id
     */
    Integer deleteI18nEntriesById(@Param("id") Integer id);

    /**
     * 根据主键id更新表i18n_entries信息
     *
     * @param i18nEntries
     */
    Integer updateI18nEntriesById(I18nEntries i18nEntries);

    /**
     * 新增表i18n_entries数据
     *
     * @param i18nEntries
     */
    Integer createI18nEntries(I18nEntries i18nEntries);

    Map<String, Map<String, String>> formatEntriesList(List<I18nEntriesDto> i18nEntriesList);

    List<I18nLangs> getHostLangs();

    List<I18nEntries> Create(OperateI18nEntries operateI18nEntries);

    List<I18nEntries> bulkCreate(OperateI18nBatchEntries operateI18nBatchEntries);

    List<I18nEntries> bulkUpdate(OperateI18nEntries operateI18nEntries);

    List<I18nEntries> deleteI18nEntriesByHostAndHostTypeAndKey(String host, String hostType, List<String> keys);

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

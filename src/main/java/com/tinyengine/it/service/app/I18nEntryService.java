package com.tinyengine.it.service.app;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.model.dto.I18nEntryDto;
import com.tinyengine.it.model.dto.I18nEntryListResult;
import com.tinyengine.it.model.dto.OperateI18nBatchEntries;
import com.tinyengine.it.model.dto.OperateI18nEntries;
import com.tinyengine.it.model.entity.I18nEntry;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * The interface 18 n entry service.
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
    I18nEntry findI18nEntryById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_i18n_entry信息
     *
     * @param i18nEntry the 18 n entry
     * @return the list
     */
    List<I18nEntry> findI18nEntryByCondition(I18nEntry i18nEntry);

    //    /**
    //     * 根据主键id删除t_i18n_entry数据
    //     *
    //     * @param id
    //     */
    //    Integer deleteI18nEntryById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_i18n_entry信息
     *
     * @param i18nEntry the 18 n entry
     * @return the integer
     */
    Integer updateI18nEntryById(I18nEntry i18nEntry);

    //    /**
    //     * 新增表t_i18n_entry数据
    //     *
    //     * @param i18nEntry
    //     */
    //    Integer createI18nEntry(I18nEntry i18nEntry);

    /**
     * 格式化词条列表
     *
     * @param i18nEntriesList the 18 n entries list
     * @return map
     */
    Map<String, Map<String, String>> formatEntriesList(List<I18nEntryDto> i18nEntriesList);

    /**
     * 创建多词条国际化
     *
     * @param operateI18nEntries the operate i 18 n entries
     * @return list
     */
    List<I18nEntry> Create(OperateI18nEntries operateI18nEntries);

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
     * @param host     the host
     * @param hostType the host type
     * @param keys     the keys
     * @return the list
     */
    List<I18nEntry> deleteI18nEntriesByHostAndHostTypeAndKey(String host, String hostType, List<String> keys);

    /**
     * 上传单个文件
     *
     * @param lang the lang
     * @param file the file
     * @param host the host
     * @return the result
     * @throws Exception the exception
     */
    Result<Map<String, Object>> readSingleFileAndBulkCreate(String lang, MultipartFile file, int host) throws Exception;

    /**
     * 批量上传词条数据
     *
     * @param lang the lang
     * @param file the file
     * @param host the host
     * @return the result
     * @throws Exception the exception
     */
    Result<Map<String, Object>> readFilesAndbulkCreate(String lang, MultipartFile file, int host) throws Exception;
}

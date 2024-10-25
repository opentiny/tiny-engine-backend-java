package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.dto.I18nEntryDto;
import com.tinyengine.it.model.entity.I18nEntry;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface I18nEntryMapper extends BaseMapper<I18nEntry> {

    /**
     * 查询表t_i18n_entry所有信息
     */
    List<I18nEntryDto> queryAllI18nEntry();

    /**
     * 根据主键id查询表t_i18n_entry数据
     *
     * @param id
     */
    I18nEntryDto queryI18nEntryById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_i18n_entry数据
     *
     * @param i18nEntry
     */
    List<I18nEntryDto> queryI18nEntryByCondition(I18nEntry i18nEntry);

    /**
     * 根据主键id删除表t_i18n_entry数据
     *
     * @param id
     */
    Integer deleteI18nEntryById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_i18n_entry数据
     *
     * @param i18nEntry
     */
    Integer updateI18nEntryById(I18nEntry i18nEntry);

    /**
     * 新增表t_i18n_entry数据
     *
     * @param i18nEntry
     */
    Integer createI18nEntry(I18nEntry i18nEntry);

    /**
     * 查询所有词条及关联语言信息
     *
     * @return
     */


    @Update("UPDATE t_i18n_entry  SET content = #{content}   WHERE  (host_id = #{hostId} AND host_type = #{hostType} AND `key` = #{key} AND lang_id = #{langId})")
    Integer updateByEntry(String content, Integer hostId, String hostType, String key, Integer langId);

    /**
     * 根据key、lang查询i18n_entries数据
     *
     * @param entriesKey
     * @param lang
     * @return
     */
    I18nEntryDto findI18nEntriesByKeyAndLang(@Param("key") String entriesKey, @Param("langId") int lang);


    List<I18nEntryDto> findI18nEntriesByHostandHostType(Integer hostId, String hostType);

    /**
     * 通过区块ids和hostType查询国际化词条信息
     *
     * @param blockIds
     * @param hostType
     * @return
     */


}
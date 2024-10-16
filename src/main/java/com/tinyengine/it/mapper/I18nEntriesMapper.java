package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.dto.I18nEntriesDto;
import com.tinyengine.it.model.entity.I18nEntries;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface I18nEntriesMapper extends BaseMapper<I18nEntries> {

    /**
     * 查询表i18n_entries所有信息
     */
    List<I18nEntries> findAllI18nEntries();

    /**
     * 根据主键id查询表i18n_entries数据
     *
     * @param id
     */
    I18nEntries findI18nEntriesById(@Param("id") Integer id);

    /**
     * 根据条件查询表i18n_entries数据
     *
     * @param i18nEntries
     */
    List<I18nEntries> findI18nEntriesByCondition(I18nEntries i18nEntries);

    /**
     * 根据主键id删除表i18n_entries数据
     *
     * @param id
     */
    Integer deleteI18nEntriesById(@Param("id") Integer id);

    /**
     * 根据主键id更新表i18n_entries数据
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

    /**
     * 根据key、lang查询i18n_entries数据
     *
     * @param entriesKey
     * @param lang
     * @return
     */
    I18nEntries findI18nEntriesByKeyAndLang(@Param("key") String entriesKey, @Param("langId") int lang);

    @Results({
            @Result(column = "lang", property = "langId"),
            @Result(column = "lang", property = "lang",
                    one = @One(select = "com.tinyengine.it.mapper.I18nLangsMapper.findI18nLangsById"))})
    @Select("select * from i18n_entries where host = #{host} and host_type = #{hostType}")
    List<I18nEntriesDto> findI18nEntriesByHostandHostType(Integer host, String hostType);

    /**
     * 查询所有词条及关联语言信息
     *
     * @return
     */
    @Results({
            @Result(column = "lang", property = "langId"),
            @Result(column = "lang", property = "lang",
                    one = @One(select = "com.tinyengine.it.mapper.I18nLangsMapper.findI18nLangsById"))})
    @Select("select * from i18n_entries")
    List<I18nEntriesDto> findAllI18();

    /**
     * 通过区块ids和hostType查询国际化词条信息
     *
     * @param blockIds
     * @param hostType
     * @return
     */

    @Select({
            "<script>",
            "SELECT * FROM i18n_entries",
            "WHERE host IN",
            "<foreach item='item' index='index' collection='blockIds' open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "AND host_type = #{hostType}",
            "</script>"
    })
    List<I18nEntriesDto> findI18nEntriesByHostsandHostType(@Param("blockIds") List<Integer> blockIds, @Param("hostType") String hostType);

    @Update("UPDATE i18n_entries  SET content = #{content}   WHERE  (host = #{host} AND host_type = #{hostType} AND `key` = #{key} AND lang = #{lang})")
    Integer updateByEntries(String content, Integer host, String hostType, String key, Integer lang);
}
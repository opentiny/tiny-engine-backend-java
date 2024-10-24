package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.dto.I18nEntryDto;
import com.tinyengine.it.model.entity.I18nEntry;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * The interface 18 n entry mapper.
 */
public interface I18nEntryMapper extends BaseMapper<I18nEntry> {

    /**
     * 查询表t_i18n_entry所有信息
     *
     * @return the list
     */
    List<I18nEntry> queryAllI18nEntry();

    /**
     * 根据主键id查询表t_i18n_entry数据
     *
     * @param id the id
     * @return the 18 n entry
     */
    I18nEntry queryI18nEntryById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_i18n_entry数据
     *
     * @param i18nEntry the 18 n entry
     * @return the list
     */
    List<I18nEntry> queryI18nEntryByCondition(I18nEntry i18nEntry);

    /**
     * 根据主键id删除表t_i18n_entry数据
     *
     * @param id the id
     * @return the integer
     */
    Integer deleteI18nEntryById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_i18n_entry数据
     *
     * @param i18nEntry the 18 n entry
     * @return the integer
     */
    Integer updateI18nEntryById(I18nEntry i18nEntry);

    /**
     * 新增表t_i18n_entry数据
     *
     * @param i18nEntry the 18 n entry
     * @return the integer
     */
    Integer createI18nEntry(I18nEntry i18nEntry);

    /**
     * 查询所有词条及关联语言信息
     *
     * @return list
     */
    @Results({@Result(column = "lang", property = "langId"), @Result(column = "lang", property = "lang",
        one = @One(select = "com.tinyengine.it.mapper.I18nLangMapper.queryI18nLangById"))})
    @Select("select * from t_i18n_entry")
    List<I18nEntryDto> findAllI18();

    /**
     * Update by entry integer.
     *
     * @param content  the content
     * @param hostId   the host id
     * @param hostType the host type
     * @param key      the key
     * @param langId   the lang id
     * @return the integer
     */
    @Update(
        "UPDATE t_i18n_entry  SET content = #{content}   WHERE  (host_id = #{hostId} AND host_type = #{hostType} AND `key` = #{key} AND lang_id = #{langId})")
    Integer updateByEntry(String content, Integer hostId, String hostType, String key, Integer langId);

    /**
     * 根据key、lang查询i18n_entries数据
     *
     * @param entriesKey the entries key
     * @param lang       the lang
     * @return 18 n entry
     */
    I18nEntry findI18nEntriesByKeyAndLang(@Param("key") String entriesKey, @Param("langId") int lang);

    /**
     * Find i 18 n entries by hostand host type list.
     *
     * @param hostId   the host id
     * @param hostType the host type
     * @return the list
     */
    @Results({@Result(column = "lang", property = "langId"), @Result(column = "lang", property = "lang",
        one = @One(select = "com.tinyengine.it.mapper.I18nLangMapper.queryI18nLangById"))})
    @Select("select * from t_i18n_entry where host_id = #{hostId} and host_type = #{hostType}")
    List<I18nEntryDto> findI18nEntriesByHostandHostType(Integer hostId, String hostType);

    /**
     * 通过区块ids和hostType查询国际化词条信息
     *
     * @param blockIds the block ids
     * @param hostType the host type
     * @return list
     */
    @Select({"<script>", "SELECT * FROM t_i18n_entry", "WHERE host_id IN",
        "<foreach item='item' index='index' collection='blockIds' open='(' separator=',' close=')'>", "#{item}",
        "</foreach>", "AND host_type = #{hostType}", "</script>"})
    List<I18nEntryDto> findI18nEntriesByHostsandHostType(@Param("blockIds") List<Integer> blockIds,
        @Param("hostType") String hostType);

}
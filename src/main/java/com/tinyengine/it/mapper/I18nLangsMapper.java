package com.tinyengine.it.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tinyengine.it.model.entity.I18nLangs;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface I18nLangsMapper extends BaseMapper<I18nLangs> {

    /**
     * 查询表i18n_langs所有信息
     */
    List<I18nLangs> findAllI18nLangs();

    /**
     * 根据主键id查询表i18n_langs数据
     *
     * @param id
     */
    I18nLangs findI18nLangsById(@Param("id") Integer id);

    /**
     * 根据条件查询表i18n_langs数据
     *
     * @param i18nLangs
     */
    List<I18nLangs> findI18nLangsByCondition(I18nLangs i18nLangs);

    /**
     * 根据主键id删除表i18n_langs数据
     *
     * @param id
     */
    Integer deleteI18nLangsById(@Param("id") Integer id);

    /**
     * 根据主键id更新表i18n_langs数据
     *
     * @param i18nLangs
     */
    Integer updateI18nLangsById(I18nLangs i18nLangs);

    /**
     * 新增表i18n_langs数据
     *
     * @param i18nLangs
     */
    Integer createI18nLangs(I18nLangs i18nLangs);

    /**
     * 根据区块id查i18n_lang信息
     *
     * @param blockId
     * @return
     */
    @Select("select * from i18n_langs il " +
            "left join blocks_i_18_n_langs__i_18_n_langs_blocks bi18nli18nlb on il.id = `i18n-lang_id` " +
            "where bi18nli18nlb.block_id = #{blockId}")
    List<I18nLangs> findI18nLangByBlockId(Integer blockId);
}
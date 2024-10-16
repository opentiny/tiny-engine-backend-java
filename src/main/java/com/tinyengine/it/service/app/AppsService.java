package com.tinyengine.it.service.app;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tinyengine.it.model.dto.AppComplexInfoDto;
import com.tinyengine.it.model.dto.AppsDto;
import com.tinyengine.it.model.dto.I18nEntriesDto;
import com.tinyengine.it.model.dto.PreviewDto;
import com.tinyengine.it.model.entity.Apps;
import com.tinyengine.it.model.entity.I18nEntries;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AppsService {

    /**
     * 查询表apps所有信息
     */
    List<Apps> findAllApps();

    /**
     * 根据主键id查询表apps信息
     *
     * @param id
     */
    Apps findAppsById(@Param("id") Integer id);

    /**
     * 根据条件查询表apps信息
     *
     * @param apps
     */
    List<Apps> findAppsByCondition(Apps apps);

    /**
     * 根据主键id删除apps数据
     *
     * @param id
     */
    Integer deleteAppsById(@Param("id") Integer id);

    /**
     * 根据主键id更新表apps信息
     *
     * @param apps
     */
    Integer updateAppsById(Apps apps);

    /**
     * 新增表apps数据
     *
     * @param apps
     */
    Integer createApps(Apps apps);

    /**
     * 检验hash
     *
     * @param appId
     */
    AppComplexInfoDto calculateHashValue(Integer appId);

    /**
     * 序列化国际化词条
     *
     * @param i18nEntries 国际化词条标准请求返回数据
     * @param userdIn     国际化词条从属单元 （应用或区块）
     * @param id          应用id或区块id
     */
    Map<String, Map<String, String>> formatI18nEntrites(List<I18nEntriesDto> i18nEntries, Integer userdIn, Integer id);

    /**
     * 获取预览元数据
     *
     * @param id 应用id
     */
    PreviewDto getAppPreviewMetaData(Integer id);


    /**
     * 关联应用信息
     *
     * @param map
     */
    List<AppsDto> associateBlocksInApps(Map<String, Object> map);


    /**
     * 获取应用下的全部国际化词条
     *
     * @param appId
     * @return
     */
    Map<String, Object> getI18n(int appId);


    /**
     * 序列化国际化词条
     *
     * @param i18nEntries 国际化词条标准请求返回数据
     * @param userdIn     国际化词条从属单元 （应用或区块）
     * @param ids         区块id集合
     * @return
     */
    Map<String, Map<String, String>> formatI18nEntritesByBlockIds(List<I18nEntriesDto> i18nEntries, Integer userdIn, List<Integer> ids);
}

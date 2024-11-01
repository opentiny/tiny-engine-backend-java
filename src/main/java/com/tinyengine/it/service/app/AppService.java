package com.tinyengine.it.service.app;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.model.dto.I18nEntryDto;
import com.tinyengine.it.model.dto.PreviewDto;
import com.tinyengine.it.model.dto.SchemaI18n;
import com.tinyengine.it.model.entity.App;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface App service.
 *
 * @since 2024-10-20
 */
public interface AppService {
    /**
     * 查询表t_app所有信息
     *
     * @return the list
     */
    List<App> queryAllApp();

    /**
     * 根据主键id查询表t_app信息
     *
     * @param id the id
     * @return the result
     */
    Result<App> queryAppById(@Param("id") Integer id);

    /**
     * 根据条件查询表t_app信息
     *
     * @param app the app
     * @return the list
     */
    List<App> queryAppByCondition(App app);

    /**
     * 根据主键id删除t_app数据
     *
     * @param id the id
     * @return the result
     */
    Result<App> deleteAppById(@Param("id") Integer id);

    /**
     * 根据主键id更新表t_app信息
     *
     * @param app the app
     * @return the result
     */
    Result<App> updateAppById(App app);

    /**
     * 新增表t_app数据
     *
     * @param app the app
     * @return the result
     */
    Result<App> createApp(App app);

    /**
     * 序列化国际化词条
     *
     * @param i18nEntries 国际化词条标准请求返回数据
     * @param userdIn     国际化词条从属单元 （应用或区块）
     * @param id          应用id或区块id
     * @return the map
     */
    SchemaI18n formatI18nEntrites(List<I18nEntryDto> i18nEntries, Integer userdIn, Integer id);

    /**
     * 获取预览元数据
     *
     * @param id 应用id
     * @return the app preview meta data
     */
    PreviewDto getAppPreviewMetaData(Integer id);
}

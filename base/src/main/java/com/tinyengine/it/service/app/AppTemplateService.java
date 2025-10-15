package com.tinyengine.it.service.app;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.model.entity.App;

import java.util.List;

public interface AppTemplateService {
    /**
     * 查询表应用模版所有信息
     *
     * @return the list
     */
    List<App> queryAllAppTemplate();

    /**
     * 根据主键id查询应用模版信息
     *
     * @param id the id
     * @return the result
     */
    Result<App> queryAppTemplateById(Integer id);

    /**
     * 通过模版应用创建应用
     *
     * @param app the app
     * @return the result
     */
    Result<App> createAppByTemplate(App app);

}

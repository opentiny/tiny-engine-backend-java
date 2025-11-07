/**
 * Copyright (c) 2023 - present TinyEngine Authors.
 * Copyright (c) 2023 - present Huawei Cloud Computing Technologies Co., Ltd.
 *
 * Use of this source code is governed by an MIT-style license.
 *
 * THE OPEN SOURCE SOFTWARE IN THIS PRODUCT IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL,
 * BUT WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS FOR
 * A PARTICULAR PURPOSE. SEE THE APPLICABLE LICENSES FOR MORE DETAILS.
 *
 */

package com.tinyengine.it.service.app;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.model.dto.AppDto;
import com.tinyengine.it.model.entity.App;

import java.util.List;

public interface AppTemplateService {
    /**
     * 分页查询应用模版所有信息
     * @param currentPage the currentPage
     * @param  pageSize the pageSize
     * @param  orderBy the orderBy
     * @param app the app
     * @return the AppDto
     */
    AppDto queryAllAppTemplate(Integer currentPage, Integer pageSize, String orderBy, App app);

    /**
     * 根据主键id查询应用模版信息
     *
     * @param id the id
     * @return the result
     */
    Result<App> queryAppTemplateById(Integer id);

    /**
     * 创建应用模版
     *
     * @param app the app
     * @return the App
     */
    App createAppByTemplate(App app);

}

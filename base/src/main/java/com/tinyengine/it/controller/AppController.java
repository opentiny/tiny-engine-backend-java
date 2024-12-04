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

package com.tinyengine.it.controller;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.log.SystemControllerLog;
import com.tinyengine.it.mapper.AppMapper;
import com.tinyengine.it.model.entity.App;
import com.tinyengine.it.service.app.AppService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

/**
 * 应用api
 *
 * @since 2024-10-20
 */
@Validated
@RestController
@RequestMapping("/app-center/api")
public class AppController {
    /**
     * The App service.
     */
    @Autowired
    private AppService appService;

    /**
     * The App mapper.
     */
    @Autowired
    private AppMapper appMapper;

    /**
     * 查询表App信息
     *
     * @return App信息 all app
     */
    @GetMapping("/apps/list")
    public Result<List<App>> getAllApp() {
        List<App> appList = appService.queryAllApp();
        return Result.success(appList);
    }

    /**
     * 根据id查询表App信息
     *
     * @param id the id
     * @return App信息 app by id
     */
    @GetMapping("/apps/{id}")
    public Result<App> getAppById(@PathVariable Integer id) {
        return appService.queryAppById(id);
    }

    /**
     * 创建App
     *
     * @param app the app
     * @return App信息 result
     */
    @PostMapping("/apps/create")
    public Result<App> createApp(@Valid @RequestBody App app) {
        return appService.createApp(app);
    }

    /**
     * 修改App信息
     *
     * @param id  the id
     * @param app the app
     * @return App信息 result
     */
    @Operation(summary = "修改单个App信息", description = "修改单个App信息", parameters = {
            @Parameter(name = "id", description = "appId"),
            @Parameter(name = "App", description = "入参对象")}, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = App.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "修改单个App信息")
    @PostMapping("/apps/update/{id}")
    public Result<App> updateApp(@PathVariable Integer id, @RequestBody App app) {
        app.setId(id);
        return appService.updateAppById(app);
    }

    /**
     * 删除App信息
     *
     * @param id the id
     * @return app信息 result
     */
    @GetMapping("/apps/delete/{id}")
    public Result<App> deleteApp(@PathVariable Integer id) {
        return appService.deleteAppById(id);
    }

    /**
     * 获取应用信息详情
     *
     * @param id the id
     * @return the result
     */
    @Operation(summary = "获取应用信息详情", description = "获取应用信息详情", parameters = {
            @Parameter(name = "id", description = "appId")}, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = App.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "获取应用信息详情")
    @GetMapping("/apps/detail/{id}")
    public Result<App> detail(@PathVariable Integer id) {
        return appService.queryAppById(id);
    }

    /**
     * 修改应用对应的国际化语种关联
     *
     * @param id    the id
     * @param param the param
     * @return the result
     */
    @Operation(summary = "修改应用对应的国际化语种关联", description = "修改应用对应的国际化语种关联", parameters = {
            @Parameter(name = "id", description = "appId"),
            @Parameter(name = "param", description = "入参对象")}, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = App.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "修改应用对应的国际化语种关联")
    @PostMapping("/apps/i18n/{id}")
    public Result<App> updateI18n(@PathVariable Integer id, @RequestBody Map<String, Object> param) {
        if (param.get("i18n_langs") == null) {
            return Result.failed("i18n_langs 是必须的");
        }
        // 判断参数i18n_langs是不是数组且是数字
        boolean isAreNumbers = Arrays.stream((int[]) param.get("i18n_langs")).allMatch(Integer.class::isInstance);
        if (!isAreNumbers) {
            return Result.failed("i18n_langs[0] should be a number");
        }
        // needTODO 对于传参进行修改逻辑存疑
        return Result.failed("修改应用对应的国际化语种关联接口逻辑存疑");
    }
}

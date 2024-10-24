package com.tinyengine.it.controller;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.config.log.SystemControllerLog;
import com.tinyengine.it.model.entity.AppExtension;
import com.tinyengine.it.service.app.AppExtensionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * app的桥接或工具
 * </p>
 *
 * @author lu -yg
 * @since 2024 -10-18
 */
@Validated
@RestController
@CrossOrigin
@RequestMapping("/app-center/api")
public class AppExtensionController {
    /**
     * The App extension service.
     */
    @Autowired
    AppExtensionService AppExtensionService;

    /**
     * 获取应用的桥接源或工具类列表
     *
     * @param appId    应用ID
     * @param category 分类
     * @return 返回值 all app extension
     */
    @Operation(summary = "获取应用的桥接源或工具类列表", description = "获取应用的桥接源或工具类列表",
        parameters = {@Parameter(name = "appId", description = "appId"),
            @Parameter(name = "category", description = "category")}, responses = {
        @ApiResponse(responseCode = "200", description = "返回信息",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppExtension.class))),
        @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "获取应用的桥接源或工具类列表")
    @GetMapping("/apps/extension/list")
    public Result<List<AppExtension>> getAllAppExtension(@RequestParam(value = "app") String appId,
        @RequestParam String category) {
        AppExtension AppExtension = new AppExtension();
        AppExtension.setApp(Integer.valueOf(appId));
        AppExtension.setCategory(category);
        List<AppExtension> AppExtensionList = AppExtensionService.findAppExtensionByCondition(AppExtension);
        return Result.success(AppExtensionList);
    }

    /**
     * 获取单个应用的桥接源或工具类列表
     *
     * @param map the map
     * @return app extension by id
     */
    @Operation(summary = "获取单个应用的桥接源或工具类列表", description = "获取单个应用的桥接源或工具类列表",
        parameters = {@Parameter(name = "map", description = "入参对象")}, responses = {
        @ApiResponse(responseCode = "200", description = "返回信息",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppExtension.class))),
        @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "获取单个应用的桥接源或工具类列表")
    @GetMapping("/apps/extension")
    public Result<AppExtension> getAppExtensionById(@RequestParam Map<String, String> map) {
        AppExtension appExtension = new AppExtension();
        appExtension.setApp(Integer.parseInt(map.get("app")));
        appExtension.setName(map.get("name"));
        appExtension.setCategory(map.get("category"));
        appExtension = AppExtensionService.findAppExtensionByCondition(appExtension).get(0);
        return Result.success(appExtension);
    }

    /**
     * 新建桥接或工具
     *
     * @param AppExtension the app extension
     * @return result
     */
    @Operation(summary = "新建桥接或工具", description = "新建桥接或工具",
        parameters = {@Parameter(name = "AppExtension", description = "入参对象")}, responses = {
        @ApiResponse(responseCode = "200", description = "返回信息",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppExtension.class))),
        @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "新建桥接或工具")
    @PostMapping("/apps/extension/create")
    public Result<AppExtension> createAppExtension(@Valid @RequestBody AppExtension AppExtension) {
        return AppExtensionService.createAppExtension(AppExtension);
    }

    /**
     * 修改桥接或工具
     *
     * @param AppExtension the app extension
     * @return result
     */
    @Operation(summary = "修改桥接或工具", description = "修改桥接或工具",
        parameters = {@Parameter(name = "AppExtension", description = "入参对象")}, responses = {
        @ApiResponse(responseCode = "200", description = "返回信息",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppExtension.class))),
        @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "修改桥接或工具")
    @PostMapping("/apps/extension/update")
    public Result<AppExtension> updateAppExtension(@RequestBody AppExtension AppExtension) {
        return AppExtensionService.updateAppExtensionById(AppExtension);
    }

    /**
     * 删除单个桥接或工具
     *
     * @param id the id
     * @return result
     * @throws ServiceException the service exception
     */
    @Operation(summary = "删除单个桥接或工具", description = "删除单个桥接或工具",
        parameters = {@Parameter(name = "id", description = "AppExtensionId")}, responses = {
        @ApiResponse(responseCode = "200", description = "返回信息",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppExtension.class))),
        @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "删除单个桥接或工具")
    @GetMapping("/apps/extension/delete")
    public Result<AppExtension> deleteAppExtension(@RequestParam Integer id) throws ServiceException {
        return AppExtensionService.deleteAppExtensionById(id);
    }
}

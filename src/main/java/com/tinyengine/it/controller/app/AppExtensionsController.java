package com.tinyengine.it.controller.app;


import com.tinyengine.it.config.SystemControllerLog;
import com.tinyengine.it.exception.ServiceException;
import com.tinyengine.it.model.dto.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * app的桥接或工具
 * </p>
 *
 * @author zhangjuncao
 * @since 2024-04-17
 */
@Validated
@RestController
@CrossOrigin
@RequestMapping("/app-center/api")
public class AppExtensionsController {
    @Autowired
    AppExtensionsService appExtensionsService;
    @Autowired
    AppExtensionsMapper appExtensionsMapper;

    /**
     * 获取应用的桥接源或工具类列表
     *
     * @param appId    应用ID
     * @param category 分类
     * @return 返回值
     */

    @Operation(summary = "获取应用的桥接源或工具类列表",
            description = "获取应用的桥接源或工具类列表",
            parameters = {
                    @Parameter(name = "appId", description = "appId"),
                    @Parameter(name = "category", description = "category")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppExtensions.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "获取应用的桥接源或工具类列表")
    @GetMapping("/apps/extension/list")
    public Result<List<AppExtensions>> getAllAppExtensions(@RequestParam(value = "app") String appId, @RequestParam String category) {
        AppExtensions appExtensions = new AppExtensions();
        appExtensions.setApp(Integer.valueOf(appId));
        appExtensions.setCategory(category);
        List<AppExtensions> AppExtensionsList = new ArrayList<AppExtensions>();
        AppExtensionsList = appExtensionsService.findAppExtensionsByCondition(appExtensions);
        return Result.success(AppExtensionsList);
    }

    /**
     * 获取单个应用的桥接源或工具类列表
     *
     * @param map
     * @return
     */

    @Operation(summary = "获取单个应用的桥接源或工具类列表",
            description = "获取单个应用的桥接源或工具类列表",
            parameters = {
                    @Parameter(name = "map", description = "入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppExtensions.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "获取单个应用的桥接源或工具类列表")
    @GetMapping("/apps/extension")
    public Result<AppExtensions> getAppExtensionsById(@RequestParam Map<String, String> map) {
        int appId = Integer.parseInt(map.get("app"));
        String name = map.get("name");
        String category = map.get("category");

        AppExtensions appExtensions = appExtensionsMapper.findAppExtensionByAppIdNameCategory(appId, name, category);
        return Result.success(appExtensions);
    }

    /**
     * 新建桥接或工具
     *
     * @param appExtensions
     * @return
     */

    @Operation(summary = "新建桥接或工具",
            description = "新建桥接或工具",
            parameters = {
                    @Parameter(name = "appExtensions", description = "入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppExtensions.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "新建桥接或工具")
    @PostMapping("/apps/extension/create")
    public Result<AppExtensions> createAppExtensions(@Valid @RequestBody AppExtensions appExtensions) {
        appExtensionsService.createAppExtensions(appExtensions);
        int id = appExtensions.getId();
        appExtensions = appExtensionsService.findAppExtensionsById(id);
        return Result.success(appExtensions);
    }

    /**
     * 修改桥接或工具
     *
     * @param appExtensions
     * @return
     */

    @Operation(summary = "修改桥接或工具",
            description = "修改桥接或工具",
            parameters = {
                    @Parameter(name = "appExtensions", description = "入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppExtensions.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "修改桥接或工具")
    @PostMapping("/apps/extension/update")
    public Result<AppExtensions> updateAppExtensions(@RequestBody AppExtensions appExtensions) {
        appExtensionsService.updateAppExtensionsById(appExtensions);
        appExtensions = appExtensionsService.findAppExtensionsById(appExtensions.getId());
        return Result.success(appExtensions);
    }

    /**
     * 删除单个桥接或工具
     *
     * @param id
     * @return
     * @throws ServiceException
     */

    @Operation(summary = "删除单个桥接或工具",
            description = "删除单个桥接或工具",
            parameters = {
                    @Parameter(name = "id", description = "appExtensionsId")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AppExtensions.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "删除单个桥接或工具")
    @GetMapping("/apps/extension/delete")
    public Result<AppExtensions> deleteAppExtensions(@RequestParam Integer id) throws ServiceException {
        AppExtensions appExtensions = appExtensionsService.findAppExtensionsById(id);
        appExtensionsService.deleteAppExtensionsById(id);
        return Result.success(appExtensions);
    }
}

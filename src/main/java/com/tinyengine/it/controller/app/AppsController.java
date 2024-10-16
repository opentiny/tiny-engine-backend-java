package com.tinyengine.it.controller.app;


import com.tinyengine.it.config.SystemControllerLog;
import com.tinyengine.it.mapper.AppsMapper;
import com.tinyengine.it.model.dto.AppsDto;
import com.tinyengine.it.model.dto.Result;
import com.tinyengine.it.model.entity.Apps;
import com.tinyengine.it.service.app.AppsService;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 应用api
 */
@Validated
@RestController
@CrossOrigin
@RequestMapping("/app-center/api")
public class AppsController {
    @Autowired
    AppsService appsService;
    @Autowired
    AppsMapper appsMapper;

    /**
     * 查询表apps信息
     *
     * @param
     * @return apps信息
     */
    @GetMapping("/apps/list")
    public Result<List<Apps>> getAllApps() {
        List<Apps> AppsList = new ArrayList<Apps>();
        AppsList = appsService.findAllApps();
        return Result.success(AppsList);
    }

    /**
     * 根据id查询表apps信息
     *
     * @param id
     * @return apps信息
     */
    @GetMapping("/apps/{id}")
    public Result<Apps> getAppsById(@PathVariable Integer id) {
        Apps apps = appsService.findAppsById(id);
        return Result.success(apps);
    }

    /**
     * 创建apps
     *
     * @param apps
     * @return apps信息
     */
    @PostMapping("/apps/create")
    public Result<Apps> createApps(@Valid @RequestBody Apps apps) {
        appsService.createApps(apps);
        int id = apps.getId();
        apps = appsService.findAppsById(id);
        return Result.success(apps);
    }

    /**
     * 修改apps信息
     *
     * @param apps
     * @return apps信息
     */

    @Operation(summary = "修改单个apps信息",
            description = "修改单个apps信息",
            parameters = {
                    @Parameter(name = "id", description = "appId"),
                    @Parameter(name = "apps", description = "入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Apps.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "修改单个App信息")
    @PostMapping("/apps/update/{id}")
    public Result<AppsDto> updateApps(@PathVariable Integer id, @RequestBody Apps apps) {
        apps.setId(id);
        appsService.updateAppsById(apps);
        AppsDto appResult = appsMapper.findAppsAndBlockGroupAndBlockCateById(id);
        return Result.success(appResult);
    }

    /**
     * 删除apps信息
     *
     * @param id
     * @return app信息
     */
    @GetMapping("/apps/delete/{id}")
    public Result<Apps> deleteApps(@PathVariable Integer id) {
        Apps apps = appsService.findAppsById(id);
        appsService.deleteAppsById(id);
        return Result.success(apps);
    }

    /**
     * 获取应用信息详情
     *
     * @param id
     */

    @Operation(summary = "获取应用信息详情",
            description = "获取应用信息详情",
            parameters = {
                    @Parameter(name = "id", description = "appId")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Apps.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "获取应用信息详情")
    @GetMapping("/apps/detail/{id}")
    public Result<AppsDto> detail(@PathVariable Integer id) {
        AppsDto appResult = appsMapper.findAppsAndBlockGroupAndBlockCateById(id);
        return Result.success(appResult);
    }


    /**
     * 关联应用信息，主要是apps下block-histories
     */

    @Operation(summary = "关联应用信息",
            description = "关联应用信息",
            parameters = {
                    @Parameter(name = "map", description = "入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Apps.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "关联应用信息")
    @GetMapping("/apps/associate")
    public Result<List<AppsDto>> associate(@RequestParam Map<String, Object> map) {

        List<AppsDto> appsList = appsService.associateBlocksInApps(map);
        return Result.success(appsList);
    }


    /**
     * 获取应用下的全部国际化词条
     *
     * @param id
     */

    @Operation(summary = "获取应用下的全部国际化词条",
            description = "获取应用下的全部国际化词条",
            parameters = {
                    @Parameter(name = "id", description = "appId")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Apps.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "获取应用下的全部国际化词条")
    @GetMapping("/apps/i18n/{id}")
    public Result<Map<String, Object>> getI18n(@PathVariable Integer id) {
        Map<String, Object> result = appsService.getI18n(id);
        return Result.success(result);

    }


    /**
     * 修改应用对应的国际化语种关联
     *
     * @param id
     */

    @Operation(summary = "修改应用对应的国际化语种关联",
            description = "修改应用对应的国际化语种关联",
            parameters = {
                    @Parameter(name = "id", description = "appId"),
                    @Parameter(name = "param", description = "入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Apps.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "修改应用对应的国际化语种关联")
    @PostMapping("/apps/i18n/{id}")
    public Result<Apps> updateI18n(@PathVariable Integer id, @RequestBody Map<String, Object> param) {
        if (param.get("i18n_langs") == null) {
            return Result.failed("i18n_langs 是必须的");
        }
        // 判断参数i18n_langs是不是数组且是数字
        boolean allAreNumbers = Arrays.stream((int[]) param.get("i18n_langs"))
                .allMatch(Integer.class::isInstance);
        if (!allAreNumbers) {
            return Result.failed("i18n_langs[0] should be a number");
        }
        // todo 对于传参进行修改逻辑存疑
        return Result.failed("修改应用对应的国际化语种关联接口逻辑存疑");
    }

}

package com.tinyengine.it.controller.app;


import com.tinyengine.it.config.SystemControllerLog;
import com.tinyengine.it.exception.ExceptionEnum;
import com.tinyengine.it.exception.ServiceException;
import com.tinyengine.it.model.dto.*;
import com.tinyengine.it.model.entity.I18nEntries;
import com.tinyengine.it.service.app.I18nEntriesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 国际化词条
 * </p>
 *
 * @author zhangjuncao
 * @since 2024-04-16
 */
@Validated
@RestController
@CrossOrigin
@RequestMapping("/app-center/api")

public class I18nEntriesController {
    @Autowired
    I18nEntriesService i18nEntriesService;

    /**
     * @return 获取国际化词条列表
     */

    @Operation(summary = "获取国际化词条列表",
            description = "获取国际化词条列表",
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema())),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "获取国际化词条列表")
    @GetMapping("/i18n/entries")
    public Result<I18nEntriesListResult> getAllI18nEntries() {
        I18nEntriesListResult i18nEntriesList = i18nEntriesService.findAllI18nEntries();
        return Result.success(i18nEntriesList);
    }

    /**
     * 获取国际化语言的详情。
     *
     * @param id 国际化语言id
     * @return 国际化语言详情
     */
    @Operation(summary = "获取国际化语言的详情",
            description = "获取国际化语言的详情",
            parameters = {
                    @Parameter(name = "id", description = "I18nEntries主键id")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = I18nEntries.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "获取国际化语言的详情")
    @GetMapping("/i18n/entries/{id}")
    public Result<I18nEntries> getI18nEntriesById(@Valid @PathVariable Integer id) {
        I18nEntries i18nEntries = i18nEntriesService.findI18nEntriesById(id);
        return Result.success(i18nEntries);
    }

    /**
     * 创建国际化多语言词条
     *
     * @param operateI18nEntries
     * @return
     */
    @Operation(summary = "创建国际化多语言词条",
            description = "创建国际化多语言词条",
            parameters = {
                    @Parameter(name = "OperateI18nEntries", description = "入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = I18nEntries.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "创建国际化多语言词条")
    @PostMapping("/i18n/entries/create")
    public Result<List<I18nEntries>> createI18nEntries(@Valid @RequestBody OperateI18nEntries operateI18nEntries) {
        List<I18nEntries> i18nEntriesList = new ArrayList<>();
        i18nEntriesList = i18nEntriesService.Create(operateI18nEntries);
        return Result.success(i18nEntriesList);
    }

    /**
     * 批量创建国际化多语言词条
     *
     * @param operateI18nBatchEntries
     */
    @Operation(summary = "批量创建国际化多语言词条",
            description = "批量创建国际化多语言词条",
            parameters = {
                    @Parameter(name = "operateI18nBatchEntries", description = "入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = I18nEntries.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "批量创建国际化多语言词条")
    @PostMapping("/i18n/entries/batch/create")
    public Result<List<I18nEntries>> batchCreateEntries(@Valid @RequestBody OperateI18nBatchEntries operateI18nBatchEntries) {
        // map中有host、host_type、entries
        List<I18nEntries> i18nEntriesList = i18nEntriesService.bulkCreate(operateI18nBatchEntries);
        return Result.success(i18nEntriesList);
    }

    @Operation(summary = "修改国际化单语言词条",
            description = "修改国际化单语言词条",
            parameters = {
                    @Parameter(name = "id", description = "I18nEntries主键id"),
                    @Parameter(name = "i18nEntries", description = "入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = I18nEntries.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "修改国际化单语言词条")
    @PostMapping("/i18n/entries/update/{id}")
    public Result<I18nEntries> updateI18nEntries(@PathVariable Integer id, @RequestBody I18nEntries i18nEntries) {
        i18nEntries.setId(id);
        i18nEntriesService.updateI18nEntriesById(i18nEntries);
        i18nEntries = i18nEntriesService.findI18nEntriesById(id);
        return Result.success(i18nEntries);
    }

    /**
     * 修改国际化多语言词条
     *
     * @param operateI18nEntries
     * @return 修改成功信息
     */
    @Operation(summary = "修改国际化多语言词条",
            description = "修改国际化多语言词条",
            parameters = {
                    @Parameter(name = "operateI18nEntries", description = "入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = I18nEntries.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "修改国际化多语言词条")
    @PostMapping("/i18n/entries/update")
    public Result<List<I18nEntries>> updateEntry(@Valid @RequestBody OperateI18nEntries operateI18nEntries) {
        // bulkUpdate
        List<I18nEntries> i18nEntriesList = new ArrayList<>();
        i18nEntriesList = i18nEntriesService.bulkUpdate(operateI18nEntries);
        return Result.success(i18nEntriesList);
    }

    /**
     * 删除多语言词条
     *
     * @param iDeleteI18nEntry
     * @return
     * @throws ServiceException
     */
    @Operation(summary = "删除多语言词条",
            description = "删除多语言词条",
            parameters = {
                    @Parameter(name = "iDeleteI18nEntry", description = "入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = I18nEntries.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "删除多语言词条")
    @PostMapping("/i18n/entries/bulk/delete")
    public Result<List<I18nEntries>> deleteI18nEntries(@RequestBody DeleteI18nEntry iDeleteI18nEntry) throws ServiceException {
        String host = iDeleteI18nEntry.getHost();
        String hostType = iDeleteI18nEntry.getHost_type();
        List<String> keys = iDeleteI18nEntry.getKey_in();

        List<I18nEntries> i18nEntriesList = i18nEntriesService.deleteI18nEntriesByHostAndHostTypeAndKey(host, hostType, keys);
        return Result.success(i18nEntriesList);
    }


    /**
     * 应用下上传单文件处理国际化词条
     *
     * @param id
     * @param filesMap
     * @return
     * @throws Exception
     */
    @Operation(summary = "应用下上传单文件处理国际化词条",
            description = "应用下上传单文件处理国际化词条",
            parameters = {
                    @Parameter(name = "id", description = "appId"),
                    @Parameter(name = "filesMap", description = "文件参数对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema())),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "应用下上传单文件处理国际化词条")
    @PostMapping("/apps/{id}/i18n/entries/update")
    public Result<Map<String, Object>> updateI18nSingleFile(@PathVariable Integer id, @RequestParam Map<String, MultipartFile> filesMap) throws Exception {
        Result<Map<String, Object>> result = new Result<>();
        // 处理上传的文件
        for (Map.Entry<String, MultipartFile> entry : filesMap.entrySet()) {
            String key = entry.getKey(); // 获取动态的参数名
            MultipartFile file = entry.getValue(); // 获取对应的文件

            if (file.isEmpty()) {
                return Result.failed(ExceptionEnum.CM307);
            }

            // 返回插入和更新的条数
            result = i18nEntriesService.readSingleFileAndBulkCreate(key, file, id);

        }

        return result;
    }


    /**
     * 应用下批量上传国际化词条文件
     *
     * @param id
     * @param filesMap
     * @return
     * @throws Exception
     */
    @Operation(summary = "应用下批量上传国际化词条文件",
            description = "应用下批量上传国际化词条文件",
            parameters = {
                    @Parameter(name = "id", description = "appId"),
                    @Parameter(name = "filesMap", description = "文件参数对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema())),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "应用下批量上传国际化词条文件")
    @PostMapping("/apps/{id}/i18n/entries/multiUpdate")
    public Result<Map<String, Object>> updateI18nMultiFile(@PathVariable Integer id, @RequestParam Map<String, MultipartFile> filesMap) throws Exception {
        Result<Map<String, Object>> result = new Result<>();
        // 处理上传的文件
        for (Map.Entry<String, MultipartFile> entry : filesMap.entrySet()) {
            String key = entry.getKey(); // 获取动态的参数名
            MultipartFile file = entry.getValue(); // 获取对应的文件

            if (file.isEmpty()) {
                return Result.failed(ExceptionEnum.CM307);
            }

            // 返回插入和更新的条数
            result = i18nEntriesService.readFilesAndbulkCreate(key, file, id);


        }
        return result;

    }

}

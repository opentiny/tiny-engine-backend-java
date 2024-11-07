package com.tinyengine.it.controller;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.common.log.SystemControllerLog;
import com.tinyengine.it.model.dto.DeleteI18nEntry;
import com.tinyengine.it.model.dto.I18nEntryDto;
import com.tinyengine.it.model.dto.I18nEntryListResult;
import com.tinyengine.it.model.dto.I18nFileResult;
import com.tinyengine.it.model.dto.OperateI18nBatchEntries;
import com.tinyengine.it.model.dto.OperateI18nEntries;
import com.tinyengine.it.model.entity.I18nEntry;
import com.tinyengine.it.service.app.I18nEntryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

/**
 * <p>
 * 国际化词条
 * </p>
 *
 * @author zhangjuncao
 * @since 2024-10-20
 */
@Validated
@RestController
@CrossOrigin
@RequestMapping("/app-center/api")
public class I18nEntryController {
    /**
     * The 18 n entry service.
     */
    @Autowired
    private I18nEntryService i18nEntryService;

    /**
     * Gets all i 18 n entries.
     *
     * @return 获取国际化词条列表 all i 18 n entries
     */
    @Operation(summary = "获取国际化词条列表", description = "获取国际化词条列表", responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json", schema = @Schema())),
            @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "获取国际化词条列表")
    @GetMapping("/i18n/entries")
    public Result<I18nEntryListResult> getAllI18nEntries() {
        I18nEntryListResult i18nEntriesList = i18nEntryService.findAllI18nEntry();
        return Result.success(i18nEntriesList);
    }

    /**
     * 获取国际化语言的详情。
     *
     * @param id 国际化语言id
     * @return 国际化语言详情 i 18 n entries by id
     */
    @Operation(summary = "获取国际化语言的详情", description = "获取国际化语言的详情", parameters = {
            @Parameter(name = "id", description = "I18nEntries主键id")}, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = I18nEntry.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "获取国际化语言的详情")
    @GetMapping("/i18n/entries/{id}")
    public Result<I18nEntryDto> getI18nEntriesById(@Valid @PathVariable Integer id) {
        I18nEntryDto i18nEntry = i18nEntryService.findI18nEntryById(id);
        return Result.success(i18nEntry);
    }

    /**
     * 创建国际化多语言词条
     *
     * @param operateI18nEntries the operate i 18 n entries
     * @return result
     */
    @Operation(summary = "创建国际化多语言词条", description = "创建国际化多语言词条", parameters = {
            @Parameter(name = "OperateI18nEntries", description = "入参对象")}, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = I18nEntry.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "创建国际化多语言词条")
    @PostMapping("/i18n/entries/create")
    public Result<List<I18nEntry>> createI18nEntries(@Valid @RequestBody OperateI18nEntries operateI18nEntries) {
        List<I18nEntry> i18nEntriesList = new ArrayList<>();
        i18nEntriesList = i18nEntryService.create(operateI18nEntries);
        return Result.success(i18nEntriesList);
    }

    /**
     * 批量创建国际化多语言词条
     *
     * @param operateI18nBatchEntries the operate i 18 n batch entries
     * @return the result
     */
    @Operation(summary = "批量创建国际化多语言词条", description = "批量创建国际化多语言词条", parameters = {
            @Parameter(name = "operateI18nBatchEntries", description = "入参对象")}, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = I18nEntry.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "批量创建国际化多语言词条")
    @PostMapping("/i18n/entries/batch/create")
    public Result<List<I18nEntry>> batchCreateEntries(
            @Valid @RequestBody OperateI18nBatchEntries operateI18nBatchEntries) {
        // map中有host、host_type、entries
        List<I18nEntry> i18nEntriesList = i18nEntryService.bulkCreate(operateI18nBatchEntries);
        return Result.success(i18nEntriesList);
    }

    /**
     * Update i 18 n entries result.
     *
     * @param id          the id
     * @param i18nEntries the 18 n entries
     * @return the result
     */
    @Operation(summary = "修改国际化单语言词条", description = "修改国际化单语言词条", parameters = {
            @Parameter(name = "id", description = "I18nEntries主键id"),
            @Parameter(name = "i18nEntries", description = "入参对象")}, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = I18nEntry.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "修改国际化单语言词条")
    @PostMapping("/i18n/entries/update/{id}")
    public Result<I18nEntryDto> updateI18nEntries(@PathVariable Integer id, @RequestBody I18nEntry i18nEntries) {
        i18nEntries.setId(id);
        i18nEntryService.updateI18nEntryById(i18nEntries);
        I18nEntryDto i18nEntryDto = i18nEntryService.findI18nEntryById(id);
        return Result.success(i18nEntryDto);
    }

    /**
     * 修改国际化多语言词条
     *
     * @param operateI18nEntries the operate i 18 n entries
     * @return 修改成功信息 result
     */
    @Operation(summary = "修改国际化多语言词条", description = "修改国际化多语言词条", parameters = {
            @Parameter(name = "operateI18nEntries", description = "入参对象")}, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = I18nEntry.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "修改国际化多语言词条")
    @PostMapping("/i18n/entries/update")
    public Result<List<I18nEntry>> updateEntry(@Valid @RequestBody OperateI18nEntries operateI18nEntries) {
        // bulkUpdate
        List<I18nEntry> i18nEntriesList = new ArrayList<>();
        i18nEntriesList = i18nEntryService.bulkUpdate(operateI18nEntries);
        return Result.success(i18nEntriesList);
    }

    /**
     * 删除多语言词条
     *
     * @param deleteI18nEntry the delete i 18 n entry
     * @return result
     * @throws ServiceException the service exception
     */
    @Operation(summary = "删除多语言词条", description = "删除多语言词条", parameters = {
            @Parameter(name = "iDeleteI18nEntry", description = "入参对象")}, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = I18nEntry.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "删除多语言词条")
    @PostMapping("/i18n/entries/bulk/delete")
    public Result<List<I18nEntryDto>> deleteI18nEntries(@RequestBody DeleteI18nEntry deleteI18nEntry)
            throws ServiceException {
        List<I18nEntryDto> i18nEntriesList = i18nEntryService
                .deleteI18nEntriesByHostAndHostTypeAndKey(deleteI18nEntry);
        return Result.success(i18nEntriesList);
    }

    /**
     * 应用下上传单文件处理国际化词条
     *
     * @param id       the id
     * @param filesMap the files map
     * @return result
     * @throws Exception the exception
     */
    @Operation(summary = "应用下上传单文件处理国际化词条", description = "应用下上传单文件处理国际化词条", parameters = {
            @Parameter(name = "id", description = "appId"),
            @Parameter(name = "filesMap", description = "文件参数对象")}, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json", schema = @Schema())),
            @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "应用下上传单文件处理国际化词条")
    @PostMapping("/apps/{id}/i18n/entries/update")
    public Result<I18nFileResult> updateI18nSingleFile(
            @PathVariable Integer id,
            @RequestParam Map<String, MultipartFile> filesMap) throws Exception {
        Result<I18nFileResult> result = new Result<>();

        for (Map.Entry<String, MultipartFile> entry : filesMap.entrySet()) {
            // 获取对应的文件
            MultipartFile file = entry.getValue();

            if (file.isEmpty()) {
                return Result.failed(ExceptionEnum.CM307);
            }
            // 返回插入和更新的条数
            result = i18nEntryService.readSingleFileAndBulkCreate(file, id);
        }
        return result;
    }

    /**
     * 应用下批量上传国际化词条文件
     *
     * @param id       id
     * @param filesMap filesMap
     * @return the result
     * @throws Exception exception
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
    public Result<I18nFileResult> updateI18nMultiFile(
            @PathVariable Integer id,
            @RequestParam Map<String, MultipartFile> filesMap) throws Exception {
        Result<I18nFileResult> result = new Result<>();
        // 处理上传的文件
        for (Map.Entry<String, MultipartFile> entry : filesMap.entrySet()) {
            String key = entry.getKey(); // 获取动态的参数名
            MultipartFile file = entry.getValue(); // 获取对应的文件

            if (file.isEmpty()) {
                return Result.failed(ExceptionEnum.CM307);
            }
            // 返回插入和更新的条数
            result = i18nEntryService.readFilesAndbulkCreate(key, file, id);
        }
        return result;
    }
}

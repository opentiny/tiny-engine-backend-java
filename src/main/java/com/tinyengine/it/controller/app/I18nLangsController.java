package com.tinyengine.it.controller.app;


import com.tinyengine.it.config.SystemControllerLog;
import com.tinyengine.it.exception.ServiceException;
import com.tinyengine.it.model.dto.Result;
import com.tinyengine.it.model.entity.I18nLangs;
import com.tinyengine.it.service.app.I18nLangsService;
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

/**
 * <p>
 * 国际化语言
 * </p>
 *
 * @author zhangjuncao
 * @since 2024-04-14
 */
@Validated
@RestController
@CrossOrigin
@RequestMapping("/app-center/api")
public class I18nLangsController {
    @Autowired
    I18nLangsService i18nLangsService;

    /**
     * 获取国际化语言列表
     *
     * @return 获取国际化语言列表
     */
    @Operation(summary = "获取国际化语言列表",
            description = "获取国际化语言列表",
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = I18nLangs.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "获取国际化语言列表")
    @GetMapping("/i18n/langs")
    public Result<List<I18nLangs>> getAllI18nLangs() {
        List<I18nLangs> I18nLangsList = new ArrayList<I18nLangs>();
        I18nLangsList = i18nLangsService.findAllI18nLangs();
        return Result.success(I18nLangsList);
    }

    /**
     * 获取国际化语言的详情
     *
     * @param id 国际化语言id
     * @return 获取国际化语言的详情成功的消息
     */
    @Operation(summary = "获取国际化语言的详情",
            description = "获取国际化语言的详情",
            parameters = {
                    @Parameter(name = "id", description = "I18nLangs主键id")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = I18nLangs.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "获取国际化语言的详情")
    @GetMapping("/i18n/langs/{id}")
    public Result<I18nLangs> getI18nLangsById(@Valid @PathVariable Integer id) {
        I18nLangs i18nLangs = i18nLangsService.findI18nLangsById(id);
        return Result.success(i18nLangs);
    }

    /**
     * 创建国际化语言
     *
     * @return 返回创建国际化语言信息成功的消息
     */
    @Operation(summary = "创建国际化语言",
            description = "创建国际化语言",
            parameters = {
                    @Parameter(name = "i18nLangs", description = "入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = I18nLangs.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "创建国际化语言")
    @PostMapping("/i18n/langs/create")
    public Result<I18nLangs> createI18nLangs(@Valid @RequestBody I18nLangs i18nLangs) {
        i18nLangsService.createI18nLangs(i18nLangs);
        return Result.success(i18nLangs);
    }


    /**
     * 修改国际化语言
     *
     * @return 返回更改国际化语言信息成功的消息
     */
    @Operation(summary = "修改国际化语言",
            description = "修改国际化语言",
            parameters = {
                    @Parameter(name = "id", description = "国际化语言主键id"),
                    @Parameter(name = "i18nLangs", description = "入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = I18nLangs.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "修改国际化语言")
    @PostMapping("/i18n/langs/update/{id}")
    public Result<I18nLangs> updateI18nLangs(@PathVariable Integer id, @RequestBody I18nLangs i18nLangs) {
        i18nLangs.setId(id);
        i18nLangsService.updateI18nLangsById(i18nLangs);
        i18nLangs = i18nLangsService.findI18nLangsById(id);
        return Result.success(i18nLangs);
    }

    /**
     * 删除国际化语言
     *
     * @param id 国际化语言ID
     * @return 删除成功的消息
     * @throws ServiceException 如果国际化不存在，则抛出该异常
     */
    @Operation(summary = "删除国际化语言",
            description = "删除国际化语言",
            parameters = {
                    @Parameter(name = "id", description = "国际化语言主键id")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = I18nLangs.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "删除国际化语言")
    @GetMapping("/i18n/langs/delete/{id}")
    public Result<I18nLangs> deleteI18nLangs(@Valid @PathVariable Integer id) throws ServiceException {
        I18nLangs i18nLangs = i18nLangsService.findI18nLangsById(id);
        i18nLangsService.deleteI18nLangsById(id);
        return Result.success(i18nLangs);
    }
}

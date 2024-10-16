package com.tinyengine.it.controller.app;


import com.tinyengine.it.config.SystemControllerLog;
import com.tinyengine.it.model.dto.Result;
import com.tinyengine.it.model.entity.PagesHistories;
import com.tinyengine.it.service.app.PagesHistoriesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

/**
 * <p>
 * 页面历史记录管理
 * </p>
 *
 * @author zhangjuncao
 * @since 2024-05-23
 */
@Validated
@RestController
@CrossOrigin
@RequestMapping("/app-center/api")
public class PagesHistoriesController {
    @Autowired
    PagesHistoriesService pagesHistoriesService;


    /**
     * 获取页面历史记录列表
     *
     * @param page
     * @return
     */
    @Operation(summary = "获取页面历史记录列表",
            description = "获取页面历史记录列表",
            parameters = {
                    @Parameter(name = "page", description = "page页面主键id")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PagesHistories.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "获取页面历史记录列表")
    @GetMapping("/pages/histories")
    public Result<List<PagesHistories>> getAllPagesHistories(@RequestParam Integer page) {
        PagesHistories pagesHistories = new PagesHistories();
        pagesHistories.setPage(Long.valueOf(page));
        List<PagesHistories> PagesHistoriesList = pagesHistoriesService.findPagesHistoriesByCondition(pagesHistories);
        return Result.success(PagesHistoriesList);
    }

    /**
     * 获取页面历史记录明细
     *
     * @param id
     * @return
     */
    @Operation(summary = "获取页面历史记录明细",
            description = "获取页面历史记录明细",
            parameters = {
                    @Parameter(name = "id", description = "页面历史主键id")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PagesHistories.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "获取页面历史记录明细")
    @GetMapping("/pages/histories/{id}")
    public Result<PagesHistories> getPagesHistoriesById(@PathVariable Integer id) {
        PagesHistories pagesHistories = pagesHistoriesService.findPagesHistoriesById(id);
        return Result.success(pagesHistories);
    }

    /**
     * 创建页面历史记录
     *
     * @param pagesHistories
     * @return
     */
    @Operation(summary = "创建页面历史记录",
            description = "创建页面历史记录",
            parameters = {
                    @Parameter(name = "pagesHistories", description = "入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PagesHistories.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "创建页面历史记录")
    @PostMapping("/pages/histories/create")
    public Result<PagesHistories> createPagesHistories(@Valid @RequestBody PagesHistories pagesHistories) {
        if (pagesHistories.getPage() != null && Pattern.matches("^[0-9]+$", pagesHistories.getPage().toString()) && pagesHistories.getPageContent() != null) {
            pagesHistories.setTime(LocalDateTime.now());
            pagesHistoriesService.createPagesHistories(pagesHistories);
            int id = pagesHistories.getId();
            pagesHistories = pagesHistoriesService.findPagesHistoriesById(id);

        } else {
            return Result.failed("The request body is missing some parameters");
        }

        return Result.success(pagesHistories);
    }


    /**
     * 删除页面历史记录
     *
     * @param id
     * @return
     */
    @Operation(summary = "删除页面历史记录",
            description = "删除页面历史记录",
            parameters = {
                    @Parameter(name = "id", description = "页面历史主键id")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PagesHistories.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "删除页面历史记录")
    @GetMapping("/pages/histories/delete/{id}")
    public Result<PagesHistories> deletePagesHistories(@PathVariable Integer id) {
        PagesHistories pagesHistories = pagesHistoriesService.findPagesHistoriesById(id);
        pagesHistoriesService.deletePagesHistoriesById(id);
        return Result.success(pagesHistories);
    }
}

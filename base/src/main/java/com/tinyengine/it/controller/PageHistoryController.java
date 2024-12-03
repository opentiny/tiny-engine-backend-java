package com.tinyengine.it.controller;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.log.SystemControllerLog;
import com.tinyengine.it.model.entity.PageHistory;
import com.tinyengine.it.service.app.PageHistoryService;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.regex.Pattern;

import javax.validation.Valid;

/**
 * <p>
 * 页面历史记录管理
 * </p>
 *
 * @author lu -yg
 * @since 2024-10-24
 */
@Validated
@RestController
@RequestMapping("/app-center/api")
public class PageHistoryController {
    /**
     * The Page history service.
     */
    @Autowired
    private PageHistoryService pageHistoryService;

    /**
     * 获取页面历史记录列表
     *
     * @param page the page
     * @return all page history
     */
    @Operation(summary = "获取页面历史记录列表", description = "获取页面历史记录列表", parameters = {
            @Parameter(name = "page", description = "page页面主键id")}, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PageHistory.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "获取页面历史记录列表")
    @GetMapping("/pages/histories")
    public Result<List<PageHistory>> getAllPageHistory(@RequestParam Integer page) {
        PageHistory pageHistory = new PageHistory();
        pageHistory.setPage(page);
        return Result.success(pageHistoryService.findPageHistoryByCondition(pageHistory));
    }

    /**
     * 获取页面历史记录明细
     *
     * @param id the id
     * @return page history by id
     */
    @Operation(summary = "获取页面历史记录明细", description = "获取页面历史记录明细", parameters = {
            @Parameter(name = "id", description = "页面历史主键id")}, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PageHistory.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "获取页面历史记录明细")
    @GetMapping("/pages/histories/{id}")
    public Result<PageHistory> getPageHistoryById(@PathVariable Integer id) {
        PageHistory pageHistory = pageHistoryService.findPageHistoryById(id);
        return Result.success(pageHistory);
    }

    /**
     * 创建页面历史记录
     *
     * @param pageHistory the page history
     * @return result
     */
    @Operation(summary = "创建页面历史记录", description = "创建页面历史记录", parameters = {
            @Parameter(name = "pageHistory", description = "入参对象")}, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PageHistory.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "创建页面历史记录")
    @PostMapping("/pages/history/create")
    public Result<PageHistory> createPageHistory(@Valid @RequestBody PageHistory pageHistory) {
        PageHistory result;
        if (pageHistory.getPage() != null && Pattern.matches("^[0-9]+$", pageHistory.getPage().toString())
                && pageHistory.getPageContent() != null) {
            pageHistoryService.createPageHistory(pageHistory);
            int id = pageHistory.getId();
            result = pageHistoryService.findPageHistoryById(id);
        } else {
            return Result.failed("The request body is missing some parameters");
        }
        return Result.success(result);
    }

    /**
     * 删除页面历史记录
     *
     * @param id the id
     * @return result
     */
    @Operation(summary = "删除页面历史记录", description = "删除页面历史记录", parameters = {
            @Parameter(name = "id", description = "页面历史主键id")}, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PageHistory.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "删除页面历史记录")
    @GetMapping("/pages/histories/delete/{id}")
    public Result<PageHistory> deletePageHistory(@PathVariable Integer id) {
        PageHistory pageHistory = pageHistoryService.findPageHistoryById(id);
        pageHistoryService.deletePageHistoryById(id);
        return Result.success(pageHistory);
    }

    /**
     * 删除页面历史记录
     *
     * @param name the name
     * @return result
     */
    @Operation(summary = "根据名称查询页面历史记录", description = "根据名称查询页面历史记录", parameters = {
            @Parameter(name = "name", description = "页面名称")}, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PageHistory.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "根据名称查询页面历史记录")
    @GetMapping("/pages/histories/find")
    public Result<List<PageHistory>> findPageHistory(@RequestParam("name") String name, @RequestParam Integer app) {
        List<PageHistory> pageHistoryList = pageHistoryService.findPageHistoryByName(name, app);
        return Result.success(pageHistoryList);
    }
}

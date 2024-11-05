package com.tinyengine.it.controller;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.log.SystemControllerLog;
import com.tinyengine.it.model.dto.PreviewDto;
import com.tinyengine.it.model.dto.PreviewParam;
import com.tinyengine.it.model.entity.Page;
import com.tinyengine.it.service.app.PageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.validation.Valid;

/**
 * <p>
 * 页面管理
 * </p>
 *
 * @author zhangjuncao
 * @since 2024-04-17
 */
@Validated
@RestController
@CrossOrigin
@RequestMapping("/app-center/api")
public class PageController {
    /**
     * The Page service.
     */
    @Autowired
    private PageService pageService;

    /**
     * 获取页面列表
     *
     * @param aid the aid
     * @return allpage
     */
    @Operation(summary = "获取页面列表", description = "获取页面列表", parameters = {
            @Parameter(name = "aid", description = "appId")}, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "获取页面列表")
    @GetMapping("/pages/list/{aid}")
    public Result<List<Page>> getAllPage(@PathVariable Integer aid) {
        List<Page> pageList = pageService.queryAllPage(aid);
        return Result.success(pageList);
    }

    /**
     * 获取页面明细
     *
     * @param id the id
     * @return by id
     * @throws Exception the exception
     */
    @Operation(summary = "获取页面明细", description = "获取页面明细", parameters = {
            @Parameter(name = "id", description = "page主键id")}, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "获取页面明细")
    @GetMapping("/pages/detail/{id}")
    public Result<Page> getPageById(@PathVariable Integer id) throws Exception {
        Page page = pageService.queryPageById(id);
        return Result.success(page);
    }

    /**
     * 创建页面
     *
     * @param page the page
     * @return result
     * @throws Exception the exception
     */
    @Operation(summary = "创建页面", description = "创建页面", parameters = {
            @Parameter(name = "page", description = "入参对象")}, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "创建页面")
    @PostMapping("/pages/create")
    public Result<Page> createPage(@Valid @RequestBody Page page) throws Exception {
        if (page.getIsPage()) {
            // 创建页面
            return pageService.createPage(page);
        } else {
            // 创建文件夹
            return pageService.createFolder(page);
        }
    }

    /**
     * 修改页面
     *
     * @param page the page
     * @return result
     * @throws Exception the exception
     */
    @Operation(summary = "修改页面", description = "修改页面",
            parameters = {@Parameter(name = "id", description = "页面主键id"),
                    @Parameter(name = "param", description = "入参对象")}, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "修改页面")
    @PostMapping("/pages/update/{id}")
    public Result<Page> updatePage(@RequestBody Page page) throws Exception {
        page.setLastUpdatedTime(null);
        page.setCreatedTime(null);
        page.setLastUpdatedBy(null);
        if (page.getIsPage()) {
            // 更新页面
            return pageService.updatePage(page);
        } else {
            // 更新文件夹
            return pageService.update(page);
        }
    }

    /**
     * 删除页面
     *
     * @param id the id
     * @return result
     * @throws Exception the exception
     */
    @Operation(summary = "删除页面", description = "删除页面", parameters = {
            @Parameter(name = "id", description = "页面主键id")}, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "删除页面")
    @GetMapping("/pages/delete/{id}")
    public Result<Page> deletePage(@PathVariable Integer id) throws Exception {
        return pageService.delPage(id);
    }

    /**
     * GET /api/preview/metadata 获取预览元数据
     *
     * @param previewParam the preview param
     * @return PreviewDto result
     */
    @Operation(summary = "查询页面预览元数据", description = "查询页面预览元数据信息并返回", parameters = {
            @Parameter(name = "PreviewParam", description = "入参对象")}, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json", schema = @Schema())),
            @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "查询页面预览元数据")
    @GetMapping("/preview/metadata")
    public Result<PreviewDto> previewData(@ModelAttribute PreviewParam previewParam) {
        PreviewDto previewDto = pageService.getPreviewMetaData(previewParam);
        return Result.success(previewDto);
    }
}

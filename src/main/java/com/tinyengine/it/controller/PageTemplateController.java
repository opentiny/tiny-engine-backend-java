package com.tinyengine.it.controller;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.config.log.SystemControllerLog;
import com.tinyengine.it.model.entity.App;
import com.tinyengine.it.model.entity.PageTemplate;
import com.tinyengine.it.service.app.PageTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 页面模版
 * </p>
 *
 * @author lu-yg
 * @since 2024-10-25
 */
@Validated
@RestController
@CrossOrigin
@RequestMapping("/material-center/api")
public class PageTemplateController {
    @Autowired
    PageTemplateService pageTemplateService;

    /**
     * 创建pageTemplate
     *
     * @param pageTemplate
     * @return pageTemplate信息
     */
    @Operation(summary = "创建页面模版",
            description = "创建页面模版",
            parameters = {
                    @Parameter(name = "pageTemplate", description = "PageTemplate")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PageTemplate.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "创建页面模版")
    @PostMapping("/page_template/create")
    public Result<PageTemplate> createPageTemplate(@Valid @RequestBody PageTemplate pageTemplate) {

        return pageTemplateService.createPageTemplate(pageTemplate);
    }

    /**
     * 批量删除pageTemplate
     *
     * @param id
     * @return pageTemplate信息
     */
    @Operation(summary = "批量删除页面模版",
            description = "批量删除页面模版",
            parameters = {
                    @Parameter(name = "id", description = "ids")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PageTemplate.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "批量删除页面模版")
    @PostMapping("/template_basic/bulk/delete")
    public Result<Integer> deletePageTemplate(@RequestBody List<Integer> id) {
        return pageTemplateService.deletePageTemplateByIds(id);
    }

    @Operation(summary = "获取页面模版信息详情",
            description = "获取页面模版信息详情",
            parameters = {
                    @Parameter(name = "id", description = "id")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PageTemplate.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "获取页面模版信息详情")
    @GetMapping("/template_basic/detail/{id}")
    public Result<PageTemplate> detail(@PathVariable Integer id) {
        return pageTemplateService.queryPageTemplateById(id);
    }

    @Operation(summary = "获取页面模版信息列表",
            description = "获取页面模版信息列表",
            parameters = {
                    @Parameter(name = "", description = "")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PageTemplate.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "获取页面模版信息列表")
    @GetMapping("/template_basic/list")
    public Result<List<PageTemplate>> findAllPageTemplate() {
        return pageTemplateService.queryAllPageTemplate();
    }
}

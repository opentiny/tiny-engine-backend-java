package com.tinyengine.it.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.config.log.SystemControllerLog;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    PageService pageService;


    /**
     * 获取页面列表
     *
     * @return
     */
    @Operation(summary = "获取页面列表",
            description = "获取页面列表",
            parameters = {
                    @Parameter(name = "aid", description = "appId")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "获取页面列表")
    @GetMapping("/pages/list/{aid}")
    public Result<List<Page>> getAllpage(@PathVariable Integer aid) {
        List<Page> pageList = pageService.queryAllPage(aid);
        return Result.success(pageList);
    }

    /**
     * 获取页面明细
     *
     * @param id
     * @return
     */
    @Operation(summary = "获取页面明细",
            description = "获取页面明细",
            parameters = {
                    @Parameter(name = "id", description = "page主键id")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "获取页面明细")
    @GetMapping("/pages/detail/{id}")
    public Result<Page> getpageById(@PathVariable Integer id) throws Exception {
        Page page = pageService.queryPageById(id);
        return Result.success(page);
    }

    /**
     * 创建页面
     *
     * @param page
     * @return
     */
    @Operation(summary = "创建页面",
            description = "创建页面",
            parameters = {
                    @Parameter(name = "page", description = "入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "创建页面")
    @PostMapping("/pages/create")
    public Result<Page> createpage(@Valid @RequestBody Page page) {

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
     * @param id
     * @param param
     * @return
     * @throws Exception
     */
    @Operation(summary = "修改页面",
            description = "修改页面",
            parameters = {
                    @Parameter(name = "id", description = "页面主键id"),
                    @Parameter(name = "param", description = "入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "修改页面")
    @PostMapping("/pages/update/{id}")
    public Result<Page> updatepage(@PathVariable Integer id, @RequestBody Map<String, Object> param) throws Exception {
        // 由于前端传过来的参数中createBy和occupier是个对象，想把param中的所有属性值对应的给赋值到page中做以下处理
        Map<String, Object> targetMap = new HashMap<>(param);
        targetMap.remove("createdBy");
        targetMap.remove("occupier");
        targetMap.remove("createdTime");
        targetMap.remove("lastUpdatedTime");
        targetMap.remove("occupierBy");
        targetMap.remove("trueFolder");
        String occupierId = (String) param.get("occupierId");
        ObjectMapper objectMapper = new ObjectMapper();
        Page page = objectMapper.convertValue(targetMap, Page.class);
        page.setOccupierBy(occupierId);
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
     * @param id
     * @return
     * @throws ServiceException
     */
    @Operation(summary = "删除页面",
            description = "删除页面",
            parameters = {
                    @Parameter(name = "id", description = "页面主键id")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "删除页面")
    @GetMapping("/pages/delete/{id}")
    public Result<Page> deletepage(@PathVariable Integer id) throws Exception {

        return pageService.delPage(id);
    }

    /**
     * GET /api/preview/metadata
     * 获取预览元数据
     *
     * @param previewParam
     * @return PreviewDto
     */
    @Operation(summary = "查询页面预览元数据",
            description = "查询页面预览元数据信息并返回",
            parameters = {
                    @Parameter(name = "PreviewParam", description = "入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema())),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "查询页面预览元数据")
    @GetMapping("/preview/metadata")
    public Result<PreviewDto> previewData(@ModelAttribute PreviewParam previewParam) {
        PreviewDto previewDto = pageService.getPreviewMetaData(previewParam);
        return Result.success(previewDto);
    }

}

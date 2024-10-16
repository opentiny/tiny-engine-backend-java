package com.tinyengine.it.controller.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinyengine.it.config.SystemControllerLog;
import com.tinyengine.it.exception.ServiceException;
import com.tinyengine.it.mapper.PagesMapper;
import com.tinyengine.it.model.dto.*;
import com.tinyengine.it.model.entity.Pages;
import com.tinyengine.it.service.app.PagesService;
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
public class PagesController {
    @Autowired
    PagesService pagesService;
    @Autowired
    PagesMapper pagesMapper;


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
                                    schema = @Schema(implementation = Pages.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "获取页面列表")
    @GetMapping("/pages/list/{aid}")
    public Result<List<PageDto>> getAllPages(@PathVariable Integer aid) {
        List<PageDto> PagesList = pagesMapper.findPagesByApp(aid);
        return Result.success(PagesList);
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
                                    schema = @Schema(implementation = Pages.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "获取页面明细")
    @GetMapping("/pages/detail/{id}")
    public Result<PageDto> getPagesById(@PathVariable Integer id) throws Exception {
        PageDto pages = pagesService.getPageById(id);
        return Result.success(pages);
    }

    /**
     * 创建页面
     *
     * @param pages
     * @return
     */
    @Operation(summary = "创建页面",
            description = "创建页面",
            parameters = {
                    @Parameter(name = "pages", description = "入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Pages.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "创建页面")
    @PostMapping("/pages/create")
    public Result<PageDto> createPages(@Valid @RequestBody Pages pages) {
        PageDto pageResult = new PageDto();
        if (pages.getIsPage()) {
            // 创建页面
            pageResult = pagesService.createPage(pages);

        } else {
            // 创建文件夹
            pageResult = pagesService.createFolder(pages);

        }

        return Result.success(pageResult);
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
                                    schema = @Schema(implementation = Pages.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "修改页面")
    @PostMapping("/pages/update/{id}")
    public Result<PageDto> updatePages(@PathVariable Integer id, @RequestBody Map<String, Object> param) throws Exception {
        // 由于前端传过来的参数中createBy和occupier是个对象，想把param中的所有属性值对应的给赋值到Pages中做以下处理
        Map<String, Object> targetMap = new HashMap<>(param);
        targetMap.remove("createdBy");
        targetMap.remove("occupier");
        targetMap.remove("created_at");
        targetMap.remove("updated_at");
        targetMap.remove("occupierId");
        targetMap.remove("trueFolder");
        int occupier = (int) param.get("occupierId");
        ObjectMapper objectMapper = new ObjectMapper();
        Pages pages = objectMapper.convertValue(targetMap, Pages.class);
        pages.setOccupier(occupier);
        if (pages.getIsPage()) {
            // 更新页面
            return pagesService.updatePage(pages);

        } else {
            // 更新文件夹
            return pagesService.update(pages);

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
                                    schema = @Schema(implementation = Pages.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "删除页面")
    @GetMapping("/pages/delete/{id}")
    public Result<PageDto> deletePages(@PathVariable Integer id) throws Exception {

        return pagesService.delPage(id);
    }

    /**
     * 查询页面schemaCode
     * 新版本页面预览不用该接口
     *
     * @param schemaCodeParam
     * @return schemaCode
     */
    @Operation(summary = "查询页面schemaCode",
            description = "根据app和pageInfo查询表schemaCode信息并返回",
            parameters = {
                    @Parameter(name = "schemaCodeParam", description = "入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema())),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "查询页面schemaCode")
    @GetMapping("/schema2code")
    public Result<List<Map<String, Object>>> schema2code(@ModelAttribute SchemaCodeParam schemaCodeParam) {
        List<Map<String, Object>> code = pagesService.schema2Code(schemaCodeParam);
        return Result.success(code);
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
        PreviewDto previewDto = pagesService.getPreviewMetaData(previewParam);
        return Result.success(previewDto);
    }

    /**
     * GET /api/code
     * 获取页面或区块代码
     *
     * @param dslCodeParam
     * @return List<Map < String, Object>>
     */
    @Operation(summary = "获取页面或区块代码",
            description = "获取页面或区块代码",
            parameters = {
                    @Parameter(name = "DslCodeParam", description = "入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema())),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "获取页面或区块代码")
    @GetMapping("/code")
    public Result<List<Map<String, Object>>> dslCode(@ModelAttribute DslCodeParam dslCodeParam) {
        List<Map<String, Object>> code = pagesService.dslCode(dslCodeParam);
        return Result.success(code);

    }
}

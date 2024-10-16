package com.tinyengine.it.controller.app;


import com.tinyengine.it.config.SystemControllerLog;
import com.tinyengine.it.model.dto.Result;
import com.tinyengine.it.model.entity.Sources;
import com.tinyengine.it.service.app.SourcesService;
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
 * 前端控制器
 * </p>
 *
 * @author zhangjuncao
 * @since 2024-07-23
 */
@Validated
@RestController
@CrossOrigin
@RequestMapping("/app-center/api")
public class SourcesController {
    @Autowired
    SourcesService sourcesService;

    /**
     * 获取数据源列表
     *
     * @return
     */
    @Operation(summary = "获取数据源列表",
            description = "获取数据源列表",
            parameters = {
                    @Parameter(name = "aid", description = "appId")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Sources.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "获取数据源列表")
    @GetMapping("/sources/list/{aid}")
    public Result<List<Sources>> getAllSources(@PathVariable Integer aid) {
        Sources sources = new Sources();
        sources.setApp(Long.valueOf(aid.longValue()));
        List<Sources> SourcesList = sourcesService.findSourcesByCondition(sources);
        return Result.success(SourcesList);
    }

    /**
     * 获取某条数据源
     *
     * @param id
     * @return
     */
    @Operation(summary = "获取某条数据源",
            description = "获取某条数据源",
            parameters = {
                    @Parameter(name = "id", description = "数据源主键id")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Sources.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "获取某条数据源")
    @GetMapping("/sources/detail/{id}")
    public Result<Sources> getSourcesById(@PathVariable Integer id) {
        Sources sources = sourcesService.findSourcesById(id);
        return Result.success(sources);
    }

    /**
     * 创建数据源
     *
     * @param sources
     * @return
     */
    @Operation(summary = "创建数据源",
            description = "创建数据源",
            parameters = {
                    @Parameter(name = "sources", description = "入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Sources.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "创建数据源")
    @PostMapping("/sources/create")
    public Result<Sources> createSources(@Valid @RequestBody Sources sources) throws Exception {
        long appId = sources.getApp();
        String name = sources.getName();
        if (appId != 0 && String.valueOf(appId).matches("^[0-9]+$") && !name.isEmpty()) {
            sourcesService.createSources(sources);
        } else {
            throw new Exception("The request body is missing some parameters");
        }

        int id = sources.getId();
        sources = sourcesService.findSourcesById(id);
        return Result.success(sources);
    }

    /**
     * 修改数据源某条数据
     *
     * @param id
     * @param sources
     * @return
     */
    @Operation(summary = "修改数据源某条数据",
            description = "修改数据源某条数据",
            parameters = {
                    @Parameter(name = "id", description = "数据源主键id"),
                    @Parameter(name = "sources", description = "入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Sources.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "修改数据源某条数据")
    @PostMapping("/sources/update/{id}")
    public Result<Sources> updateSources(@PathVariable Integer id, @RequestBody Sources sources) {
        sources.setId(id);
        sourcesService.updateSourcesById(sources);
        sources = sourcesService.findSourcesById(id);
        return Result.success(sources);
    }

    /**
     * 删除数据源某条数据
     *
     * @param id
     * @return
     */
    @Operation(summary = "删除数据源某条数据",
            description = "删除数据源某条数据",
            parameters = {
                    @Parameter(name = "id", description = "数据源主键id")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Sources.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "删除数据源某条数据")
    @GetMapping("/sources/delete/{id}")
    public Result<Sources> deleteSources(@PathVariable Integer id) {
        Sources sources = sourcesService.findSourcesById(id);
        sourcesService.deleteSourcesById(id);
        return Result.success(sources);
    }
}

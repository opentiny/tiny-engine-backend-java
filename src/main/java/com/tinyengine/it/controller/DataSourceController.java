package com.tinyengine.it.controller;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.config.log.SystemControllerLog;
import com.tinyengine.it.model.entity.Datasource;
import com.tinyengine.it.service.app.DatasourceService;

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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.validation.Valid;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhangjuncao
 * @since 2024-10-20
 */
@Validated
@RestController
@CrossOrigin
@RequestMapping("/app-center/api")
public class DataSourceController {
    /**
     * The Datasource service.
     */
    @Autowired
    private DatasourceService datasourceService;

    /**
     * 获取数据源列表
     *
     * @param aid the aid
     * @return all sources
     */
    @Operation(summary = "获取数据源列表", description = "获取数据源列表", parameters = {
            @Parameter(name = "aid", description = "appId")}, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Datasource.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "获取数据源列表")
    @GetMapping("/sources/list/{aid}")
    public Result<List<Datasource>> getAllSources(@PathVariable Integer aid) {
        Datasource sources = new Datasource();
        sources.setApp(aid);
        return Result.success(datasourceService.queryDatasourceByCondition(sources));
    }

    /**
     * 获取某条数据源
     *
     * @param id the id
     * @return sources by id
     */
    @Operation(summary = "获取某条数据源", description = "获取某条数据源", parameters = {
            @Parameter(name = "id", description = "数据源主键id")}, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Datasource.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "获取某条数据源")
    @GetMapping("/sources/detail/{id}")
    public Result<Datasource> getSourcesById(@PathVariable Integer id) {
        Datasource sources = datasourceService.queryDatasourceById(id);
        return Result.success(sources);
    }

    /**
     * 创建数据源
     *
     * @param sources the sources
     * @return result
     */
    @Operation(summary = "创建数据源", description = "创建数据源", parameters = {
            @Parameter(name = "sources", description = "入参对象")}, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Datasource.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "创建数据源")
    @PostMapping("/sources/create")
    public Result<Datasource> createSources(@Valid @RequestBody Datasource sources) {
        return datasourceService.createDatasource(sources);
    }

    /**
     * 修改数据源某条数据
     *
     * @param id      the id
     * @param sources the sources
     * @return result
     */
    @Operation(summary = "修改数据源某条数据", description = "修改数据源某条数据", parameters = {
            @Parameter(name = "id", description = "数据源主键id"),
            @Parameter(name = "sources", description = "入参对象")}, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Datasource.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "修改数据源某条数据")
    @PostMapping("/sources/update/{id}")
    public Result<Datasource> updateSources(@PathVariable Integer id, @RequestBody Datasource sources) {
        sources.setId(id);
        return datasourceService.updateDatasourceById(sources);
    }

    /**
     * 删除数据源某条数据
     *
     * @param id the id
     * @return result
     */
    @Operation(summary = "删除数据源某条数据", description = "删除数据源某条数据", parameters = {
            @Parameter(name = "id", description = "数据源主键id")}, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Datasource.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "删除数据源某条数据")
    @GetMapping("/sources/delete/{id}")
    public Result<Datasource> deleteSources(@PathVariable Integer id) {
        return datasourceService.deleteDatasourceById(id);
    }
}

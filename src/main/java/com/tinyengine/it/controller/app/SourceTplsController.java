package com.tinyengine.it.controller.app;


import com.tinyengine.it.config.SystemControllerLog;
import com.tinyengine.it.model.dto.Result;
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
public class SourceTplsController {
    @Autowired
    SourceTplsService sourceTplsService;

    /**
     * 获取数据源模版
     *
     * @return
     */
    @Operation(summary = "获取数据源模版",
            description = "获取数据源模版",
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SourceTpls.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "获取数据源模版")
    @GetMapping("/source_tpl")
    public Result<List<SourceTpls>> getAllSourceTpls() {
        List<SourceTpls> SourceTplsList = new ArrayList<SourceTpls>();
        SourceTplsList = sourceTplsService.findAllSourceTpls();
        return Result.success(SourceTplsList);
    }


    /**
     * 创建数据源模版
     *
     * @param sourceTpls
     * @return
     */
    @Operation(summary = "创建数据源模版",
            description = "创建数据源模版",
            parameters = {
                    @Parameter(name = "sourceTpls", description = "入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SourceTpls.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "创建数据源模版")
    @PostMapping("/source_tpl/create")
    public Result<SourceTpls> createSourceTpls(@Valid @RequestBody SourceTpls sourceTpls) {
        sourceTplsService.createSourceTpls(sourceTpls);
        int id = sourceTpls.getId();
        sourceTpls = sourceTplsService.findSourceTplsById(id);
        return Result.success(sourceTpls);
    }


    /**
     * 修改数据源模版
     *
     * @param id
     * @param sourceTpls
     * @return
     */
    @Operation(summary = "修改数据源模版",
            description = "修改数据源模版",
            parameters = {
                    @Parameter(name = "id", description = "数据源模版主键id"),
                    @Parameter(name = "sourceTpls", description = "入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SourceTpls.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "修改数据源模版")
    @PostMapping("/source_tpl/update/{id}")
    public Result<SourceTpls> updateSourceTpls(@PathVariable Integer id, @RequestBody SourceTpls sourceTpls) {
        sourceTpls.setId(id);
        sourceTplsService.updateSourceTplsById(sourceTpls);
        sourceTpls = sourceTplsService.findSourceTplsById(id);
        return Result.success(sourceTpls);
    }


    /**
     * 删除数据源模版
     *
     * @param id
     * @return
     */
    @Operation(summary = "删除数据源模版",
            description = "删除数据源模版",
            parameters = {
                    @Parameter(name = "id", description = "数据源模版主键id")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = SourceTpls.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "删除数据源模版")
    @GetMapping("/source_tpl/delete/{id}")
    public Result<SourceTpls> deleteSourceTpls(@PathVariable Integer id) {
        SourceTpls sourceTpls = sourceTplsService.findSourceTplsById(id);
        sourceTplsService.deleteSourceTplsById(id);
        return Result.success(sourceTpls);
    }
}

package com.tinyengine.it.controller.app.v1;

import com.tinyengine.it.config.SystemControllerLog;
import com.tinyengine.it.exception.ServiceException;
import com.tinyengine.it.model.dto.Result;
import com.tinyengine.it.service.app.v1.AppsV1Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 应用api v1版本
 */
@Validated
@RestController
@RequestMapping("/app-center/v1/api")
public class AppsV1Controller {
    @Autowired
    private AppsV1Service appsV1Service;

    /**
     * 查询app的schema信息
     *
     * @param
     * @return app的schema信息
     */
    @Operation(summary = "查询app schema详情",
            description = "根据id查询app schema信息并返回",
            parameters = {
                    @Parameter(name = "id", description = "appId")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Apps.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "获取app schema api")
    @GetMapping("/apps/schema/{id}")
    public Result<Map<String, Object>> getSchema(@PathVariable Integer id) throws ServiceException {
        Map<String, Object> schema = appsV1Service.appSchema(id);
        return Result.success(schema);
    }
}

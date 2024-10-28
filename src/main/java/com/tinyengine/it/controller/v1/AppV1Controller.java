package com.tinyengine.it.controller.v1;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.exception.ServiceException;
import com.tinyengine.it.config.log.SystemControllerLog;
import com.tinyengine.it.model.dto.SchemaDto;
import com.tinyengine.it.model.entity.App;
import com.tinyengine.it.service.app.v1.AppV1Service;
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
 *
 * @since 2024-10-20
 */
@Validated
@RestController
@RequestMapping("/app-center/v1/api")
public class AppV1Controller {
    @Autowired
    private AppV1Service appV1Service;

    /**
     * 查询app的schema信息
     *
     * @param id the id
     * @return app的schema信息 schema
     * @throws ServiceException the service exception
     */
    @Operation(summary = "查询app schema详情", description = "根据id查询app schema信息并返回",
        parameters = {@Parameter(name = "id", description = "appId")}, responses = {
        @ApiResponse(responseCode = "200", description = "返回信息",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = App.class))),
        @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "获取app schema api")
    @GetMapping("/apps/schema/{id}")
    public Result<SchemaDto> getSchema(@PathVariable Integer id) {
        SchemaDto schema = appV1Service.appSchema(id);
        return Result.success(schema);
    }
}

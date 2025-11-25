package com.tinyengine.it.controller;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.common.log.SystemControllerLog;
import com.tinyengine.it.model.entity.Tenant;
import com.tinyengine.it.service.platform.TenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/platform-center/api")
@Tag(name = "tenant")
public class TenantController {
    /**
     * The tenant service.
     */
    @Autowired
    private TenantService tenantService;

    /**
     * 获取组织列表
     *
     * @return 返回值 all tenant
     */
    @Operation(summary = "获取组织列表", description = "获取组织列表", responses = {
        @ApiResponse(responseCode = "200", description = "返回信息",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = Tenant.class))), 
        @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "获取组织列表")
    @GetMapping("/tenant/list")
    public Result<List<Tenant>> getAllTenant() {
        
        return Result.success(tenantService.findAllTenant());
    }

    /**
     * 新建组织
     *
     * @param tenant the tenant
     * @return tenant
     */
    @Operation(summary = "新建组织", description = "新建组织",
            parameters = {
                    @Parameter(name = "tenant", description = "入参对象")
            }, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Tenant.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "新建组织")
    @PostMapping("/tenant/create")
    public Result<Tenant> createTenant(@Valid @RequestBody Tenant tenant) {
        int result = tenantService.createTenant(tenant);
        return Result.success(tenantService.findTenantById(result));
    }

    /**
     * 修改组织
     *
     * @param tenant the tenant
     * @return Tenant
     */
    @Operation(summary = "修改组织", description = "修改组织",
        parameters = {
            @Parameter(name = "Tenant", description = "入参对象")
        }, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Tenant.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "修改组织")
    @PostMapping("/tenant/update")
    public Result<Tenant> updateTenant(@RequestBody Tenant tenant) {
        if (tenant.getId() == null || tenant.getId().isEmpty()) {
            return Result.failed(ExceptionEnum.CM002);
        }
        int result = tenantService.updateTenantById(tenant);
        if (result != 1) {
            return Result.failed(ExceptionEnum.CM002);
        }
        return Result.success(tenantService.findTenantById(Integer.valueOf(tenant.getId())));
    }

    /**
     * 删除单个组织
     *
     * @param id the id
     * @return result
     */
    @Operation(summary = "删除单个组织", description = "删除单个组织",
        parameters = {
            @Parameter(name = "id", description = "id")
        }, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Tenant.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "删除单个组织")
    @DeleteMapping("/tenant/delete")
    public Result<Tenant> deleteTenant(@RequestParam Integer id) {
        Tenant tenant = tenantService.findTenantById(id);
        int result = tenantService.deleteTenantById(id);
        if (result != 1) {
            return Result.failed(ExceptionEnum.CM009);
        }
        return Result.success(tenant);
    }
}

package com.tinyengine.it.controller;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.log.SystemControllerLog;
import com.tinyengine.it.model.entity.App;
import com.tinyengine.it.model.entity.BusinessCategory;
import com.tinyengine.it.service.material.BusinessCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 业务场景分类
 *
 * @since 2025-10-20
 */
@Validated
@RestController
@RequestMapping("/material-center/api")
@Tag(name = "业务场景分类")
public class BusinessCategoryController {
    /**
     * The business category service.
     */
    @Autowired
    BusinessCategoryService businessCategoryService;

    /**
     * 查询业务场景分类
     *
     * @return Business category 信息 all business category
     */
    @Operation(summary = "查询所有业务场景分类", description = "查询所有业务场景分类",
        responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = App.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "查询所有业务场景分类")
    @GetMapping("/business-category/list")
    public Result<List<BusinessCategory>> getAllBusinessCategory() {
        List<BusinessCategory> result = businessCategoryService.queryAllBusinessCategory();
        return Result.success(result);
    }

    /**
     * 通过分组查询业务场景分类
     * @param group the group
     * @return Business category 信息 all business category
     */
    @Operation(summary = "查询所有业务场景分类", description = "查询所有业务场景分类",
        parameters = {
            @Parameter(name = "group", description = "分组")
        }, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = App.class))),
            @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "查询所有业务场景分类")
    @GetMapping("/business-category/find")
    public Result<List<BusinessCategory>> getBusinessCategoryByGroup(@RequestParam String group) {
        BusinessCategory businessCategory = new BusinessCategory();
        businessCategory.setBusinessGroup(group);
        List<BusinessCategory> result = businessCategoryService.queryBusinessCategoryByCondition(businessCategory);
        return Result.success(result);
    }
}

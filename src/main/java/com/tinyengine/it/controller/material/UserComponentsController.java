package com.tinyengine.it.controller.material;


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
import java.util.List;

/**
 * <p>
 * 组件
 * </p>
 *
 * @author lyg
 * @since 2024-08-19
 */
@Validated
@RestController
@RequestMapping("/material-center/api")
public class UserComponentsController {
    @Autowired
    UserComponentsService userComponentsService;

    /**
     * 获取组件列表
     *
     * @param
     * @return
     */
    @Operation(summary = "获取组件列表",
            description = "查询表user_components信息并返回",

            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Blocks.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @GetMapping("/component/list")
    public Result<List<UserComponents>> getAllUserComponents() {
        List<UserComponents> UserComponentsList = userComponentsService.findAllUserComponents();
        return Result.success(UserComponentsList);
    }

    /**
     * 通过id获取组件
     *
     * @param id
     * @return
     */
    @Operation(summary = "通过id获取组件详情",
            description = "通过id获取组件信息并返回",
            parameters = {
                    @Parameter(name = "id", description = "组件Id")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Blocks.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @GetMapping("/component/find/{id}")
    public Result<UserComponents> getUserComponentsById(@PathVariable Integer id) {
        UserComponents userComponents = userComponentsService.findUserComponentsById(id);
        return Result.success(userComponents);
    }

    /**
     * 创建组件
     *
     * @param userComponents
     * @return
     */
    @Operation(summary = "创建组件",
            description = "创建组件",
            parameters = {
                    @Parameter(name = "userComponents", description = "组件")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Blocks.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @PostMapping("/component/create")
    public Result<UserComponents> createUserComponents(@Valid @RequestBody UserComponents userComponents) {
        userComponentsService.createUserComponents(userComponents);
        int id = userComponents.getId();
        userComponents = userComponentsService.findUserComponentsById(id);
        return Result.success(userComponents);
    }

    /**
     * 修改组件
     *
     * @param userComponents
     * @return
     */
    @Operation(summary = "修改组件",
            description = "修改组件",
            parameters = {
                    @Parameter(name = "userComponents", description = "组件")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Blocks.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @PutMapping("/component/update/{id}")
    public Result<UserComponents> updateUserComponents(@PathVariable Integer id, @RequestBody UserComponents userComponents) {
        userComponents.setId(id);
        userComponentsService.updateUserComponentsById(userComponents);
        userComponents = userComponentsService.findUserComponentsById(id);
        return Result.success(userComponents);
    }

    /**
     * 通过id删除组件
     *
     * @param id
     * @return
     */
    @Operation(summary = "删除组件",
            description = "通过id删除组件",
            parameters = {
                    @Parameter(name = "id", description = "组件id")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Blocks.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @DeleteMapping("/component/delete/{id}")
    public Result<UserComponents> deleteUserComponents(@PathVariable Integer id) {
        UserComponents userComponents = userComponentsService.findUserComponentsById(id);
        userComponentsService.deleteUserComponentsById(id);
        return Result.success(userComponents);
    }
}

package com.tinyengine.it.controller.material;


import com.tinyengine.it.config.SystemControllerLog;
import com.tinyengine.it.exception.ExceptionEnum;
import com.tinyengine.it.model.dto.BlockCategoriesDto;
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
import java.util.Map;

/**
 * <p>
 * 区块分类
 * </p>
 *
 * @author zhangjuncao
 * @since 2024-05-23
 */
@Validated
@RestController
@CrossOrigin
@RequestMapping("/material-center/api")
public class BlockCategoriesController {
    @Autowired
    BlockCategoriesService blockCategoriesService;
    @Autowired
    AppsService appsService;
    @Autowired
    BlocksService blocksService;
    @Autowired
    BlockCategoriesMapper blockCategoriesMapper;

    @Operation(summary = "获取区块列表",
            description = "获取区块列表",
            parameters = {
                    @Parameter(name = "map", description = "入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Blocks.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "获取区块列表")
    @GetMapping("/blocks")
    public Result<List<Blocks>> getAllBlockCategories(@Valid @RequestParam Map<String, String> map) {
        int categoryId = 0;
        int appId = 0;
        if (map.get("categoryId") != null) {
            categoryId = Integer.parseInt(map.get("categoryId"));
        }
        if (map.get("appId") != null) {
            appId = Integer.parseInt(map.get("appId"));
        }
        Apps apps = appsService.findAppsById(appId);
        if (categoryId != 0) {
            if (!apps.getId().equals(appId)) {
                return Result.failed(ExceptionEnum.CM206);
            }
        }
        // listNew
        List<Blocks> blocksList = blocksService.listNew(categoryId, appId);
        return Result.success(blocksList);
    }


    /**
     * 获取区块分类列表
     *
     * @param appId
     * @return
     */
    @Operation(summary = "获取区块分类列表",
            description = "获取区块分类列表",
            parameters = {
                    @Parameter(name = "appId", description = "appId")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema())),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "获取区块分类列表")
    @GetMapping("/block-categories")
    public Result<List<BlockCategoriesDto>> list(@RequestParam Integer appId) {
        List<BlockCategoriesDto> blockCategoriesResult = blockCategoriesMapper.findBlockCateRelationAppByAppId(appId);
        return Result.success(blockCategoriesResult);
    }

    /**
     * 创建区块分类
     *
     * @param blockCategories
     * @return
     */
    @Operation(summary = "创建区块分类",
            description = "创建区块分类",
            parameters = {
                    @Parameter(name = "blockCategories", description = "入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema())),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "创建区块分类")
    @PostMapping("/block-categories")
    public Result<BlockCategoriesDto> createBlockCategories(@Valid @RequestBody BlockCategories blockCategories) {
        List<BlockCategories> blockCategoriesList = blockCategoriesService.findBlockCategoriesByCondition(blockCategories);
        if (blockCategoriesList.isEmpty()) {
            blockCategoriesMapper.createBlockCategories(blockCategories);
        } else {
            return Result.failed(ExceptionEnum.CM003);
        }
        // 页面返回数据显示
        BlockCategoriesDto blockCategoriesResult = blockCategoriesMapper.getBlockCategoriesById(blockCategories.getId());
        return Result.success(blockCategoriesResult);
    }

    /**
     * 修改区块分类
     *
     * @param id
     * @param blockCategories
     * @return
     */
    @Operation(summary = "修改区块分类",
            description = "修改区块分类",
            parameters = {
                    @Parameter(name = "id", description = "分类主键id"),
                    @Parameter(name = "blockCategories", description = "入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema())),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "修改区块分类")
    @PutMapping("/block-categories/{id}")
    public Result<BlockCategoriesDto> updateBlockCategories(@PathVariable Integer id, @RequestBody BlockCategories blockCategories) {
        blockCategories.setId(id);
        blockCategoriesService.updateBlockCategoriesById(blockCategories);
        // 页面返回数据显示
        BlockCategoriesDto blockCategoriesResult = blockCategoriesMapper.getBlockCategoriesById(blockCategories.getId());
        return Result.success(blockCategoriesResult);
    }

    /**
     * 删除区块分类
     *
     * @param id
     * @return
     */
    @Operation(summary = "删除区块分类",
            description = "删除区块分类",
            parameters = {
                    @Parameter(name = "id", description = "分类主键id")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema())),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "删除区块分类")
    @DeleteMapping("/block-categories/{id}")
    public Result<BlockCategoriesDto> deleteBlockCategories(@PathVariable Integer id) {
        BlockCategories blockCategories = blockCategoriesService.findBlockCategoriesById(id);
        if (blockCategories == null) {
            return Result.failed(ExceptionEnum.CM009);
        }
        // 页面返回数据显示
        BlockCategoriesDto blockCategoriesResult = blockCategoriesMapper.getBlockCategoriesById(blockCategories.getId());
        blockCategoriesService.deleteBlockCategoriesById(id);

        return Result.success(blockCategoriesResult);
    }
}

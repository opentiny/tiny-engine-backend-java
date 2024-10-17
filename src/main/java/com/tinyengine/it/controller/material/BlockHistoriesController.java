package com.tinyengine.it.controller.material;


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
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhangjuncao
 * @since 2024-06-10
 */
@Validated
@RestController
@RequestMapping("/material-center/api")
public class BlockHistoriesController {
    @Autowired
    BlockHistoriesService blockHistoriesService;

    /**
     * 查找区块历史记录
     *
     * @param blockId
     * @param version
     * @return
     */
    @Operation(summary = "查找区块历史记录",
            description = "查找区块历史记录",
            parameters = {
                    @Parameter(name = "blockId", description = "区块id"),
                    @Parameter(name = "version", description = "版本")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BlockHistories.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "查找区块历史记录")
    @GetMapping("/block-history")
    public Result<List<BlockHistories>> find(@RequestParam(value = "block_id") Integer blockId, @RequestParam String version) {
        BlockHistories blockHistories = new BlockHistories();
        blockHistories.setBlockId(blockId);
        blockHistories.setVersion(version);
        List<BlockHistories> blockHistoriesList = blockHistoriesService.findBlockHistoriesByCondition(blockHistories);
        return Result.success(blockHistoriesList);
    }

    /**
     * 创建区块历史记录
     *
     * @param blockHistories
     * @return
     */
    @Operation(summary = "创建区块历史记录",
            description = "创建区块历史记录",
            parameters = {
                    @Parameter(name = "blockHistories", description = "入参对象")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BlockHistories.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "创建区块历史记录")
    @PostMapping("/block-history/create")
    public Result<BlockHistories> createBlockHistories(@Valid @RequestBody BlockHistories blockHistories) {
        blockHistoriesService.createBlockHistories(blockHistories);
        int id = blockHistories.getId();
        blockHistories = blockHistoriesService.findBlockHistoriesById(id);
        return Result.success(blockHistories);
    }

    /**
     * 删除区块历史记录
     *
     * @param id
     * @return
     */
    @Operation(summary = "删除区块历史记录",
            description = "删除区块历史记录",
            parameters = {
                    @Parameter(name = "id", description = "区块历史id")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BlockHistories.class))),
                    @ApiResponse(responseCode = "400", description = "请求失败")}
    )
    @SystemControllerLog(description = "删除区块历史记录")
    @GetMapping("/block-history/delete/{id}")
    public Result<BlockHistories> deleteBlockHistories(@PathVariable Integer id) {
        BlockHistories blockHistories = blockHistoriesService.findBlockHistoriesById(id);
        blockHistoriesService.deleteBlockHistoriesById(id);
        return Result.success(blockHistories);
    }
}

package com.tinyengine.it.controller;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.log.SystemControllerLog;
import com.tinyengine.it.rag.VectorStorageService;
import com.tinyengine.it.rag.entity.VectorDocument;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The type vector storage controller.
 *
 * @since 2025-9-25
 */
@Validated
@RestController
@RequestMapping("/app-center/api")
@Tag(name = "VectorStorage")
public class VectorStorageController {

    /**
     * file storage
     *
     * @param filePath the filePath
     * @return ai回答信息 result
     */
    @Operation(summary = "文件向量存储", description = "文件向量存储",
            parameters = {
                    @Parameter(name = "filePath", description = "入参对象")
            }, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json", schema = @Schema())),
            @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "文件向量存储")
    @PostMapping("/vector-storage/create")
    public Result<VectorDocument> create(@RequestBody List<String> filePath) {
       VectorDocument vectorDocument = VectorStorageService.initializeKnowledgeBase(filePath);
       return Result.success(vectorDocument);
    }
}

/**
 * Copyright (c) 2023 - present TinyEngine Authors.
 * Copyright (c) 2023 - present Huawei Cloud Computing Technologies Co., Ltd.
 *
 * Use of this source code is governed by an MIT-style license.
 *
 * THE OPEN SOURCE SOFTWARE IN THIS PRODUCT IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL,
 * BUT WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS FOR
 * A PARTICULAR PURPOSE. SEE THE APPLICABLE LICENSES FOR MORE DETAILS.
 *
 */

package com.tinyengine.it.controller;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.log.SystemControllerLog;
import com.tinyengine.it.rag.entity.BatchDeleteResult;
import com.tinyengine.it.rag.entity.DeleteResult;
import com.tinyengine.it.rag.entity.EmbeddingMatchDto;
import com.tinyengine.it.rag.entity.SearchRequest;
import com.tinyengine.it.rag.service.StorageService;
import com.tinyengine.it.rag.entity.VectorDocument;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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

     @Autowired
     private StorageService vectorStorageService;

    /**
     * file storage
     *
     * @param filePath the filePath
     * @return result
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
       VectorDocument vectorDocument = vectorStorageService.initializeKnowledgeBase(filePath);
       return Result.success(vectorDocument);
    }

    /**
     * file storage in collection
     *
     * @param documentPaths the documentPaths
     * @param collection the collection
     * @return result
     */
    @Operation(summary = "存储文档到指定集合", description = "存储文档到指定集合",
        parameters = {
            @Parameter(name = "documentPaths", description = "文件路径集合"),
            @Parameter(name = "collection", description = "集合名称")
        }, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json", schema = @Schema())),
            @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "存储文档到指定集合")
    @PostMapping("/vector-storage/collection/{collection}")
    public Result<VectorDocument> createInCollection(
            @PathVariable String collection,
            @RequestBody List<String> documentPaths) {
        VectorDocument result = vectorStorageService.initializeKnowledgeBase(documentPaths, null, collection);
        return Result.success(result);
    }

    /**
     * search in collection
     *
     * @param searchDto the searchDto
     * @return result
     */
    @Operation(summary = "在指定集合中搜索", description = "在指定集合中搜索",
        parameters = {
            @Parameter(name = "searchDto", description = "搜索请求参数体"),
        }, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json", schema = @Schema())),
            @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "在指定集合中搜索")
    @PostMapping("/vector-storage/search")
    public Result<List<EmbeddingMatchDto>> searchInCollection(@RequestBody SearchRequest searchDto) {
            List<EmbeddingMatchDto> results = vectorStorageService.search(searchDto);
            return Result.success(results);
    }

    /**
     * search in all collection
     *
     * @param searchDto the searchDto
     * @return result
     */
    @Operation(summary = "跨集合搜索", description = "跨集合搜索",
        parameters = {
            @Parameter(name = "searchDto", description = "搜索请求参数体"),
        }, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json", schema = @Schema())),
            @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "跨集合搜索")
    @PostMapping("/vector-storage/all-collections")
    public Result<Map<String, List<EmbeddingMatchDto>>> searchAllCollections(
        @RequestBody SearchRequest searchDto) {
        Map<String, List<EmbeddingMatchDto>> results =
            vectorStorageService.searchAcrossCollections(searchDto);
        return Result.success(results);
    }

    /**
     * get all collections
     *
     * @return result
     */
    @Operation(summary = "获取集合列表", description = "获取集合列表",
        responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json", schema = @Schema())),
            @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "获取集合列表")
    @GetMapping("/vector-storage/collections")
    public Result<List<String>> getAllCollections() {
        List<String> collections = vectorStorageService.getSupportedCollections();
        return Result.success(collections);
    }

    /**
     * auto add folder to knowledge base
     * @return result
     */
    @Operation(summary = "自动添加文件夹文件到知识库", description = "自动添加文件夹文件到知识库",
       responses = {
           @ApiResponse(responseCode = "200", description = "返回信息",
               content = @Content(mediaType = "application/json", schema = @Schema())),
           @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "自动添加文件夹文件到知识库")
    @GetMapping("/vector-storage/auto")
    public Result<VectorDocument> autoAddFolderToKnowledgeBase() {
        VectorDocument vectorDocument = vectorStorageService.autoAddFolderToKnowledgeBase();
        return Result.success(vectorDocument);
    }

    /**
     * file delete by file path
     *
     * @param filePath the filePath
     * @return result
     */
    @Operation(summary = "通过路径和集合名称删除知识库文档", description = "通过路径删除知识库文档",
        parameters = {
            @Parameter(name = "filePath", description = "文档路径"),
            @Parameter(name = "collection", description = "集合名称"),
        }, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json", schema = @Schema())),
            @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "通过路径和集合名称删除知识库文档")
    @DeleteMapping("/vector-storage/delete")
    public Result<DeleteResult> deleteByFilePath(@RequestParam String filePath, @RequestParam String collection) {
        DeleteResult result = vectorStorageService.deleteByFilePath(filePath, collection);
        return Result.success(result);
    }

    /**
     * file delete by file path list
     *
     * @param filePaths the filePaths
     * @return result
     */
    @Operation(summary = "通过路径和集合名称批量删除知识库文档", description = "通过路径和集合名称批量删除知识库文档",
        parameters = {
            @Parameter(name = "filePaths", description = "文档路径集合"),
            @Parameter(name = "collection", description = "集合名称"),
        }, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                content = @Content(mediaType = "application/json", schema = @Schema())),
            @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "通过路径和集合名称批量删除知识库文档")
    @DeleteMapping("/vector-storage/batch/{collection}")
    public Result<BatchDeleteResult> deleteMultipleFiles(@PathVariable String collection,
        @RequestBody @NotEmpty List<@NotEmpty String> filePaths) {
        BatchDeleteResult result = vectorStorageService.deleteMultipleFiles(filePaths, collection);
        return Result.success(result);
    }

    /**
     * get stored files
     *
     * @return result
     */
    @Operation(summary = "获取知识库文档集合及文档路径", description = "获取知识库文档集合及文档路径",
        responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                content = @Content(mediaType = "application/json", schema = @Schema())),
            @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "获取知识库文档集合及文档路径")
    @GetMapping("/vector-storage/files")
    public Result<Map<String, List<String>>> getStoredFiles() {
        Map<String, List<String>> files = vectorStorageService.getAllCollectionDocuments();
        return Result.success(files);
    }
}

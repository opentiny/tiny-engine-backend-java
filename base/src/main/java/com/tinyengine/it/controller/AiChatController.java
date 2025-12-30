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
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.common.log.SystemControllerLog;
import com.tinyengine.it.model.dto.AiToken;
import com.tinyengine.it.model.dto.ChatRequest;

import com.tinyengine.it.rag.entity.EmbeddingMatchDto;
import com.tinyengine.it.rag.entity.SearchRequest;
import com.tinyengine.it.rag.service.StorageService;
import com.tinyengine.it.service.app.v1.AiChatV1Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;

/**
 * The type Ai chat controller.
 *
 * @since 2024-10-20
 */
@Validated
@RestController
@RequestMapping("/app-center/api")
@Tag(name = "AIChat")
public class AiChatController {
    /**
     * The Ai chat v1 service.
     */
    @Autowired
    private AiChatV1Service aiChatV1Service;

    @Autowired
    private StorageService vectorStorageService;

    /**
     * AI api
     *
     * @param request the AI param
     * @return ai回答信息 result
     */
    @Operation(summary = "获取ai回答信息", description = "获取ai回答信息",
        parameters = {
            @Parameter(name = "ChatRequest", description = "入参对象")
        }, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                content = @Content(mediaType = "application/json", schema = @Schema())),
            @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "AI chat")
    @PostMapping("/ai/chat")
    public ResponseEntity<?> aiChat(@RequestBody ChatRequest request,
        @RequestHeader(value = "Authorization", required = false) String authorization) throws Exception {

        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.replace("Bearer ", "");
            request.setApiKey(token);
        }

        Object response = aiChatV1Service.chatCompletion(request);

        if (request.isStream()) {
            return ResponseEntity.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .header("Cache-Control", "no-cache")
                .header("X-Accel-Buffering", "no") // 禁用Nginx缓冲
                .body((StreamingResponseBody) response);
        } else {
            return ResponseEntity.ok(response);
        }

    }


    /**
     * AI api v1
     *
     * @param request the AI param
     * @return ai回答信息 result
     */
    @Operation(summary = "获取ai回答信息", description = "获取ai回答信息",
        parameters = {
            @Parameter(name = "ChatRequest", description = "入参对象")
        }, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                content = @Content(mediaType = "application/json", schema = @Schema())),
            @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "AI completions")
    @PostMapping("/chat/completions")
    public ResponseEntity<?> completions(@RequestBody ChatRequest request,
        @RequestHeader(value = "Authorization", required = false) String authorization) throws Exception {
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.replace("Bearer ", "");
            request.setApiKey(token);
        }

        Object response = aiChatV1Service.chatCompletion(request);

        if (request.isStream()) {
            return ResponseEntity.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .header("Cache-Control", "no-cache")
                .header("X-Accel-Buffering", "no") // 禁用Nginx缓冲
                .body((StreamingResponseBody) response);
        } else {
            return ResponseEntity.ok(response);
        }
    }
    /**
     * get token
     *
     * @param request the request
     * @return ai回答信息 result
     */
    @Operation(summary = "获取加密key信息", description = "获取加密key信息",
        parameters = {
            @Parameter(name = "request", description = "入参对象")
        }, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                content = @Content(mediaType = "application/json", schema = @Schema())),
            @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "get token")
    @PostMapping("/encrypt-key")
    public Result<AiToken> getToken(@RequestBody ChatRequest request) throws Exception {
        String apiKey = request.getApiKey();
        if (apiKey == null || apiKey.isEmpty()) {
            return Result.failed(ExceptionEnum.CM320);
        }
        String token = aiChatV1Service.getToken(apiKey);
        return Result.success(new AiToken(token));
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
    @SystemControllerLog(description = "AI search in collection")
    @PostMapping("/ai/search")
    public Result<List<EmbeddingMatchDto>> searchInCollection(@RequestBody SearchRequest searchDto) {
        List<EmbeddingMatchDto> results = vectorStorageService.search(searchDto);
        return Result.success(results);
    }
}

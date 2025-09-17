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
import com.tinyengine.it.model.dto.ChatRequest;
import com.tinyengine.it.model.dto.NodeDto;

import com.tinyengine.it.service.app.v1.AiChatV1Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @SystemControllerLog(description = "AI api")
    @PostMapping("/ai/chat")
    public ResponseEntity<?> aiChat(@RequestBody ChatRequest request) {
        try {
            Object response = aiChatV1Service.chatCompletion(request);

            if (request.isStream()) {
                return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_EVENT_STREAM)
                    .body((StreamingResponseBody) response);
            } else {
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
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
    @SystemControllerLog(description = "AI api v1")
    @PostMapping("/chat/completions")
    public ResponseEntity<?> chat(@RequestBody ChatRequest request,
        @RequestHeader("Authorization") String authorization) {
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.replace("Bearer ", "");
            request.setApiKey(token);
        }
        try {
            Object response = aiChatV1Service.chatCompletion(request);

            if (request.isStream()) {
                return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_EVENT_STREAM)
                    .body((StreamingResponseBody) response);
            } else {
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
        }
    }

    /**
     * AI search api
     *
     * @param content the AI search param
     * @return ai回答信息 result
     */
    @Operation(summary = "搜索知识库", description = "搜索知识库",
            parameters = {
                    @Parameter(name = "content", description = "入参对象")
            }, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json", schema = @Schema())),
            @ApiResponse(responseCode = "400", description = "请求失败")
    })
    @SystemControllerLog(description = "AI serarch api")
    @PostMapping("/ai/search")
    public Result<List<NodeDto>> search(@RequestBody String content) throws Exception {
         return aiChatV1Service.chatSearch(content);
    }
}

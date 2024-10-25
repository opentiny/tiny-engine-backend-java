
package com.tinyengine.it.controller;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.config.log.SystemControllerLog;
import com.tinyengine.it.model.dto.AiParam;
import com.tinyengine.it.service.app.AiChatService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * The type Ai chat controller.
 *
 * @since 2024-10-20
 */
@Validated
@RestController
@CrossOrigin
@RequestMapping("/app-center/api")
public class AiChatController {
    /**
     * The Ai chat service.
     */
    @Autowired
    private AiChatService aiChatService;

    /**
     * AI api
     *
     * @param aiParam the AI param
     * @return ai回答信息 result
     */
    @Operation(summary = "获取ai回答信息", description = "获取ai回答信息", parameters = {
            @Parameter(name = "AiParam", description = "入参对象")}, responses = {
                    @ApiResponse(responseCode = "200", description = "返回信息", content = @Content(mediaType = "application/json", schema = @Schema())),
                    @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "AI api")
    @PostMapping("/ai/chat")
    public Result<Map<String, Object>> aiChat(@RequestBody AiParam aiParam) {
        return aiChatService.getAnswerFromAi(aiParam);
    }
}

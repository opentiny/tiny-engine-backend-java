package com.tinyengine.it.controller;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.exception.ExceptionEnum;
import com.tinyengine.it.common.log.SystemControllerLog;
import com.tinyengine.it.model.dto.FileResult;
import com.tinyengine.it.service.material.ComponentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 组件api
 *
 * @since 2024-11-13
 */
@Validated
@RestController
@CrossOrigin
@RequestMapping("/material-center/api")
public class ComponentController {

    @Autowired
    private ComponentService componentService;

    /**
     * 上传bunled.json文件处理自定义组件
     *
     * @param filesMap the files map
     * @return result
     */
    @Operation(summary = "上传bunled.json文件处理自定义组件", description = "上传bunled.json文件处理自定义组件", parameters = {
            @Parameter(name = "filesMap", description = "文件参数对象")}, responses = {
            @ApiResponse(responseCode = "200", description = "返回信息",
                    content = @Content(mediaType = "application/json", schema = @Schema())),
            @ApiResponse(responseCode = "400", description = "请求失败")})
    @SystemControllerLog(description = "上传bunled.json文件处理自定义组件")
    @PostMapping("/component/custom/create")
    public Result<FileResult> createCustComponent(@RequestParam Map<String, MultipartFile> filesMap) {
        Result<FileResult> result = new Result<>();

        for (Map.Entry<String, MultipartFile> entry : filesMap.entrySet()) {
            // 获取对应的文件
            MultipartFile file = entry.getValue();

            if (file.isEmpty()) {
                return Result.failed(ExceptionEnum.CM307);
            }
            // 返回插入和更新的条数
            result = componentService.readFileAndBulkCreate(file);
        }
        return result;
    }
}

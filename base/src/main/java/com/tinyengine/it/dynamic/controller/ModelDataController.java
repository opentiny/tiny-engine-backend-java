package com.tinyengine.it.dynamic.controller;

import com.tinyengine.it.common.base.Result;
import com.tinyengine.it.common.log.SystemControllerLog;
import com.tinyengine.it.dynamic.dto.DynamicDelete;
import com.tinyengine.it.dynamic.dto.DynamicInsert;
import com.tinyengine.it.dynamic.dto.DynamicQuery;
import com.tinyengine.it.dynamic.dto.DynamicUpdate;
import com.tinyengine.it.dynamic.service.DynamicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Validated
@Slf4j
@RestController
@RequestMapping("/platform-center/api")
@Tag(name = "模型数据")
public class ModelDataController {
	@Autowired
	private DynamicService dynamicService;

	/**
	 * 模型数据查询
	 *
	 * @return 返回值 all
	 */
	@Operation(summary = "模型数据查询", description = "模型数据查询", responses = {
		@ApiResponse(responseCode = "200", description = "返回信息",
			content = @Content(mediaType = "application/json",schema = @Schema(implementation = Map.class))),
		@ApiResponse(responseCode = "400", description = "请求失败")
	})
	@SystemControllerLog(description = "模型数据查询")
	@PostMapping("/model-data/queryApi")
	public Result<Map<String, Object>> query(@RequestBody @Valid DynamicQuery dto) {
		try {
	        return Result.success(dynamicService.queryWithPage(dto));
        } catch (Exception e) {
			log.error("Query failed for table: {}", dto.getNameEn(), e);
			return Result.failed("Query operation failed");
        }

	}

	/**
	 * 新增模型数据
	 *
	 * @return 返回值 map
	 */
	@Operation(summary = "新增模型数据", description = "新增模型数据", responses = {
		@ApiResponse(responseCode = "200", description = "返回信息",
			content = @Content(mediaType = "application/json",schema = @Schema(implementation = Map.class))),
		@ApiResponse(responseCode = "400", description = "请求失败")
	})
	@SystemControllerLog(description = "新增模型数据")
	@PostMapping("/model-data/insertApi")
	public Result<Map<String, Object> > insert(@RequestBody @Valid DynamicInsert dto) {
		try {
			return Result.success(dynamicService.insert(dto));
		} catch (Exception e) {
			log.error("insert failed for table: {}", dto.getNameEn(), e);
			return Result.failed("insert operation failed:"+e.getMessage());
		}

	}

	/**
	 * 更新模型数据
	 *
	 * @return 返回值 map
	 */
	@Operation(summary = "更新模型数据", description = "更新模型数据", responses = {
		@ApiResponse(responseCode = "200", description = "返回信息",
			content = @Content(mediaType = "application/json",schema = @Schema(implementation = Map.class))),
		@ApiResponse(responseCode = "400", description = "请求失败")
	})
	@SystemControllerLog(description = "更新模型数据")
	@PostMapping("/model-data/updateApi")
	public Result<Map<String, Object> > update(@RequestBody @Valid DynamicUpdate dto) {
		try {
			return Result.success(dynamicService.update(dto));
		} catch (Exception e) {
			log.error("updateApi failed for table: {}", dto.getNameEn(), e);
			return Result.failed("update operation failed:"+e.getMessage());
		}

	}
	/**
	 * 刪除模型数据
	 *
	 * @return 返回值 map
	 */
	@Operation(summary = "刪除模型数据", description = "刪除模型数据", responses = {
		@ApiResponse(responseCode = "200", description = "返回信息",
			content = @Content(mediaType = "application/json",schema = @Schema(implementation = Map.class))),
		@ApiResponse(responseCode = "400", description = "请求失败")
	})
	@SystemControllerLog(description = "刪除模型数据")
	@PostMapping("/model-data/deleteApi")
	public Result<Map<String, Object> > delete(@RequestBody @Valid DynamicDelete dto) {
		try {
			return Result.success(dynamicService.delete(dto));
		} catch (Exception e) {
			log.error("deleteApi failed for table: {}", dto.getNameEn(), e);
			return Result.failed("delete operation failed:"+e.getMessage());
		}
	}
}

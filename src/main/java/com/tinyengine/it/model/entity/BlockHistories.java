package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinyengine.it.model.dto.BlockVersionDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author zhangjuncao
 * @since 2024-06-10
 */
@Getter
@Setter
@TableName("block_histories")
@Schema(name = "BlockHistories对象", description = "返回对象")
public class BlockHistories implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "id", description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(name = "message", description = "历史记录描述消息")
    private String message;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @Schema(name = "content", description = "区块内容")
    private Map<String, Object> content;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @Schema(name = "assets", description = "区块构建资源")
    private Map<String, Object> assets;

    @JsonProperty("build_info")
    @Schema(name = "buildInfo", description = "构建信息")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> buildInfo;

    @JsonProperty("created_at")
    @Schema(name = "createdAt", description = "创建时间")
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    @Schema(name = "updatedAt", description = "更新时间")
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @Schema(name = "screenshot", description = "截图")
    private String screenshot;

    @Schema(name = "path", description = "区块路径")
    private String path;

    @Schema(name = "label", description = "区块编码")
    private String label;

    @Schema(name = "description", description = "区块描述")
    private String description;

    @Schema(name = "mode", description = "模式：vscode")
    private String mode;

    @JsonProperty("block_id")
    @Schema(name = "blockId", description = "区块id")
    @TableField("block_id")
    private Integer blockId;

    @Schema(name = "version", description = "版本")
    private String version;

    @JsonProperty("npm_name")
    @Schema(name = "npmName", description = "npm包名")
    @TableField("npm_name")
    private String npmName;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @Schema(name = "i18n", description = "国际化")
    private Map<String, Map<String, String>> i18n;

    @Schema(name = "createdApp", description = "创建区块appId")
    @JsonProperty("created_app")
    private Integer createdApp;

    @Schema(name = "content_blocks", description = "*暂不清楚*")
    @JsonProperty("content_blocks")
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<BlockVersionDto> contentBlocks;

    @Schema(name = "createdBy", type = "Integer", description = "创建人")
    @TableField(value = "createdBy", fill = FieldFill.INSERT)
    private Integer createdBy;

    @Schema(name = "updatedBy", type = "Integer", description = "更新人")
    @TableField(value = "updatedBy", fill = FieldFill.INSERT)
    private Integer updatedBy;
}
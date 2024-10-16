package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author zhangjuncao
 * @since 2024-05-23
 */
@Getter
@Setter
@TableName("pages_histories")
@Schema(name = "PagesHistories对象", description = "返回对象")
public class PagesHistories implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(name = "id", description = "页面历史id")
    private Integer id;

    @Schema(name = "message", description = "信息")
    private String message;

    @Schema(name = "page", description = "页面")
    private Long page;

    @Schema(name = "time", description = "时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime time;

    @Schema(name = "pageContent", description = "页面内容")
    @TableField(typeHandler = JacksonTypeHandler.class)
    @JsonProperty("page_content")
    private Map<String, Object> pageContent;

    @TableField(fill = FieldFill.INSERT)
    @JsonProperty("created_at")
    @Schema(name = "created_at", type = "LocalDateTime", description = "创建时间")
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonProperty("updated_at")
    @Schema(name = "updated_at", type = "LocalDateTime", description = "更新时间")
    private LocalDateTime updatedAt;

    @JsonProperty("is_body")
    @Schema(name = "isBody", description = "根元素是否是body")
    private Boolean isBody;


    @Schema(name = "parentId", description = "父文件夹id")
    private Long parentId;

    @Schema(name = "route", description = "路径")
    private String route;

    @JsonProperty("is_home")
    @Schema(name = "isHome", description = "是否是主页")
    private Boolean isHome;

    @Schema(name = "name", description = "页面名称")
    private String name;

    @Schema(name = "group", description = "分组")
    private String group;

    @Schema(name = "contentBlocks", description = "区块内容")
    @TableField(value = "content_blocks", typeHandler = JacksonTypeHandler.class)
    @JsonProperty("content_blocks")
    private Map<String, Object> contentBlocks;

    @Schema(name = "createdBy", description = "创建人")
    @TableField(value = "createdBy", fill = FieldFill.INSERT)
    private Integer createdBy;

    @Schema(name = "updatedBy", description = "更新人")
    @TableField(value = "updatedBy", fill = FieldFill.INSERT)
    private Integer updatedBy;


}

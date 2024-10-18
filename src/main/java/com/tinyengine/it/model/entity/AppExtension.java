package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tinyengine.it.utils.MapTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 应用扩展
 * </p>
 *
 * @author lu-yg
 * @since 2024-10-17
 */
@Getter
@Setter
@TableName("t_app_extension")
@Schema(name = "AppExtension", description = "应用扩展")
public class AppExtension implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(name= "id", description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(name= "name", description = "名称")
    private String name;

    @Schema(name= "type", description = "类型：npm, function")
    private String type;

    @Schema(name= "content", description = "内容")
    @TableField(typeHandler = MapTypeHandler.class)
    private String content;

    @Schema(name= "app", description = "关联app表Id")
    private Integer app;

    @Schema(name= "category", description = "分类：utils,bridge")
    private String category;

    @TableField(fill = FieldFill.INSERT)
    @Schema(name= "createdBy", description = "创建人")
    private String createdBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(name= "lastUpdatedBy", description = "最后修改人")
    private String lastUpdatedBy;

    @TableField(fill = FieldFill.INSERT)
    @Schema(name= "createdTime", description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(name= "lastUpdatedTime", description = "更新时间")
    private LocalDateTime lastUpdatedTime;

    @Schema(name= "tenantId", description = "租户id")
    private String tenantId;

    @Schema(name= "siteId", description = "站点id")
    private String siteId;

}

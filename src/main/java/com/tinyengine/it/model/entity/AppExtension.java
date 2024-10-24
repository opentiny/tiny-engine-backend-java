package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tinyengine.it.common.base.BaseEntity;
import com.tinyengine.it.config.handler.MapTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

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
public class AppExtension extends BaseEntity {
    @Schema(name = "name", description = "名称")
    private String name;

    @Schema(name = "type", description = "类型：npm, function")
    private String type;

    @Schema(name = "content", description = "内容")
    @TableField(typeHandler = MapTypeHandler.class)
    private Map<String,Object> content;

    @Schema(name = "app", description = "关联app表Id")
    private Integer app;

    @Schema(name = "category", description = "分类：utils,bridge")
    private String category;
}

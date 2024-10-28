
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
 * 页面模板表
 * </p>
 *
 * @author zhangjuncao
 * @since 2024-10-17
 */
@Getter
@Setter
@TableName("page_template")
@Schema(name = "PageTemplate", description = "页面模板表")
public class PageTemplate extends BaseEntity {
    @Schema(name = "name", description = "名称")
    private String name;

    @Schema(name = "pageContent", description = "模板页面内容，存储页面内容，数据源等")
    @TableField(typeHandler = MapTypeHandler.class)
    private Map<String, Object> pageContent;

    @Schema(name = "framework", description = "技术栈")
    private String framework;

    @Schema(name = "published", description = "是否发布：1是，0否")
    private Integer published;

    @Schema(name = "public", description = "公开状态：0,1,2")
    private Integer publicStatus;

    @Schema(name = "type", description = "模版类型")
    private String type;

    @Schema(name = "status", description = "模板状态")
    private String status;

    @Schema(name = "isPreset", description = "*设计预留字段*")
    private Integer isPreset;

    @Schema(name = "imageUrl", description = "模板截图")
    private String imageUrl;

    @Schema(name = "description", description = "描述")
    private String description;

    @Schema(name = "platformId", description = "设计器ID")
    private Integer platformId;
}

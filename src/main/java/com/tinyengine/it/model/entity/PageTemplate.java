package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

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
public class PageTemplate implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(name = "id", description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(name = "name", description = "名称")
    private String name;

    @Schema(name = "pageContent", description = "模板页面内容，存储页面内容，数据源等")
    private String pageContent;

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

    @Schema(name = "tplImage", description = "模板截图")
    private String tplImage;

    @Schema(name = "description", description = "描述")
    private String description;

    @Schema(name = "platformId", description = "设计器ID")
    private Integer platformId;

    @Schema(name = "createdBy", description = "创建人")
    private String createdBy;

    @Schema(name = "lastUpdatedBy", description = "最后修改人")
    private String lastUpdatedBy;

    @Schema(name = "createdTime", description = "创建时间")
    private LocalDateTime createdTime;

    @Schema(name = "lastUpdatedTime", description = "更新时间")
    private LocalDateTime lastUpdatedTime;

    @Schema(name = "tenantId", description = "租户ID")
    private String tenantId;

    @Schema(name = "siteId", description = "站点ID")
    private String siteId;

}

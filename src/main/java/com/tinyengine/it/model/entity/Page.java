package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinyengine.it.model.dto.BlockVersionDto;
import com.tinyengine.it.utils.ListTypeHandler;
import com.tinyengine.it.utils.MapTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 页面表
 * </p>
 *
 * @author zhangjuncao
 * @since 2024-10-17
 */
@Getter
@Setter
@TableName("t_page")
@Schema(name = "Page", description = "页面表")
public class Page implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(name= "id", description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(name= "name", description = "名称")
    private String name;

    @Schema(name= "app", description = "关联appId")
    private Long app;

    @Schema(name= "route", description = "访问路由")
    private String route;

    @Schema(name= "pageCotent", description = "页面内容")
    @TableField(typeHandler = MapTypeHandler.class)
    private Map<String, Object> pageContent;

    @Schema(name= "isBody", description = "根元素是否是body")
    private Boolean isBody;

    @Schema(name= "parentId", description = "父文件夹id")
    private String parentId;

    @Schema(name= "group", description = "分组")
    private String group;

    @Schema(name= "depth", description = "页面/文件夹深度，更改层级时服务端校验用（校验可有可无）")
    private Integer depth;

    @Schema(name= "isPage", description = "是否为页面：分为页面和文件夹")
    private Boolean isPage;

    @Schema(name= "occupierBy", description = "当前检出者id")
    private String occupierBy;

    @Schema(name= "isDefault", description = "是否是默认页面")
    private Boolean isDefault;

    @Schema(name= "contentBlocks", description = "*设计预留字段*")
    @TableField(typeHandler = ListTypeHandler.class)
    private List<BlockVersionDto> contentBlocks;

    @Schema(name= "latestVersion", description = "当前历史记录表最新版本")
    private String latestVersion;

    @Schema(name= "latestHistoryId", description = "当前历史记录表ID")
    private Integer latestHistoryId;

    @Schema(name= "createdBy", description = "创建人")
    @TableField(value = "createdBy", fill = FieldFill.INSERT)
    private String createdBy;

    @Schema(name= "lastUpdatedBy", description = "最后修改人")
    @TableField(value = "lastUpdatedBy", fill = FieldFill.INSERT_UPDATE)
    private String lastUpdatedBy;

    @Schema(name= "createdTime", description = "创建时间")
    @TableField(value = "createdTime", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    @Schema(name= "lastUpdatedTime", description = "更新时间")
    @TableField(value = "lastUpdatedTime", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime lastUpdatedTime;

    @Schema(name= "tenantId", description = "租户ID")
    private String tenantId;

    @Schema(name= "siteId", description = "站点ID")
    private String siteId;

    @Schema(name = "occupier", description = "当前检出者")
    private User occupier;

    @TableField(exist = false)
    private Boolean isHome;

    @TableField(exist = false)
    private Map<String, List<String>> assets;

    @TableField(exist = false)
    private String message;
}

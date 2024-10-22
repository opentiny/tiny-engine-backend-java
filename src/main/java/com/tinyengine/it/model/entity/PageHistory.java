package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.tinyengine.it.model.dto.BlockVersionDto;
import com.tinyengine.it.utils.ListTypeHandler;
import com.tinyengine.it.utils.MapTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 页面历史
 * </p>
 *
 * @author lu-yg
 * @since 2024-10-17
 */
@Getter
@Setter
@TableName("t_page_history")
@Schema(name = "PageHistory", description = "页面历史")
public class PageHistory implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(name= "refId", description = "关联主表id")
    private Integer refId;

    @Schema(name= "name", description = "名称")
    private String name;

    @Schema(name= "appId", description = "关联app表Id")
    private Integer appId;

    @Schema(name= "route", description = "页面路由")
    private String route;

    @Schema(name= "pageCotent", description = "页面内容")
    @TableField(typeHandler = MapTypeHandler.class)
    private Map<String, Object> pageContent;

    @Schema(name= "isBody", description = "根元素是否是body")
    private Boolean isBody;

    @Schema(name= "parentId", description = "父文件夹id")
    private Long parentId;

    private String group;

    @Schema(name= "depth", description = "*页面/文件夹深度，更改层级时服务端校验用（校验可有可无）*")
    private Integer depth;

    @Schema(name= "isPage", description = "是否为页面：分为页面和文件夹")
    private Boolean isPage;

    @Schema(name= "isDefault", description = "是否是默认页面")
    private Boolean isDefault;

    @Schema(name= "message", description = "信息")
    private String message;

    @Schema(name= "isHome", description = "是否为主页")
    private Boolean isHome;

    @Schema(name= "contentBlocks", description = "*设计预留字段*")
    @TableField(typeHandler = ListTypeHandler.class)
    private List<BlockVersionDto> contentBlocks;

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

    @Schema(name= "tenantId", description = "租户id")
    private String tenantId;

    @Schema(name= "siteId", description = "站点id")
    private String siteId;

}

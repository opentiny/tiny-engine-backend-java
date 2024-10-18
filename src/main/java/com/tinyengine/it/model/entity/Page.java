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

    @Schema(name= "appId", description = "关联appId")
    private Long appId;

    @Schema(name= "route", description = "访问路由")
    private String route;

    @Schema(name= "pageCotent", description = "页面内容")
    private String pageCotent;

    @Schema(name= "isBody", description = "根元素是否是body")
    private Boolean isBody;

    @Schema(name= "parentId", description = "父文件夹id")
    private Integer parentId;

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
    private String contentBlocks;

    @Schema(name= "latestVersion", description = "当前历史记录表最新版本")
    private String latestVersion;

    @Schema(name= "latestHistoryId", description = "当前历史记录表ID")
    private Integer latestHistoryId;

    @Schema(name= "createdBy", description = "创建人")
    private String createdBy;

    @Schema(name= "lastUpdatedBy", description = "最后修改人")
    private String lastUpdatedBy;

    @Schema(name= "createdTime", description = "创建时间")
    private LocalDateTime createdTime;

    @Schema(name= "lastUpdatedTime", description = "更新时间")
    private LocalDateTime lastUpdatedTime;

    @Schema(name= "tenantId", description = "租户ID")
    private String tenantId;

    @Schema(name= "siteId", description = "站点ID")
    private String siteId;

}

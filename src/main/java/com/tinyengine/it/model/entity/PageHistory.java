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

    @Schema(name= "pageContent", description = "页面内容")
    private String pageContent;

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

    private String contentBlocks;

    @Schema(name= "createdBy", description = "创建人")
    private String createdBy;

    @Schema(name= "lastUpdatedBy", description = "最后修改人")
    private String lastUpdatedBy;

    @Schema(name= "createdTime", description = "创建时间")
    private LocalDateTime createdTime;

    @Schema(name= "lastUpdatedTime", description = "更新时间")
    private LocalDateTime lastUpdatedTime;

    @Schema(name= "tenantId", description = "租户id")
    private String tenantId;

    @Schema(name= "siteId", description = "站点id")
    private String siteId;

}

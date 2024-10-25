package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinyengine.it.common.base.HistoryEntity;
import com.tinyengine.it.config.handler.ListTypeHandler;
import com.tinyengine.it.config.handler.MapTypeHandler;
import com.tinyengine.it.model.dto.BlockVersionDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 页面历史
 * </p>
 *
 * @author lu -yg
 * @since 2024-10-17
 */
@Getter
@Setter
@TableName("t_page_history")
@Schema(name = "PageHistory", description = "页面历史")
public class PageHistory extends HistoryEntity {
    @Schema(name = "name", description = "名称")
    private String name;

    @Schema(name = "page", description = "关联page表Id")
    private Integer page;
    @Schema(name = "app", description = "关联app表Id")
    private Integer app;

    @Schema(name = "route", description = "页面路由")
    private String route;

    @Schema(name = "page_content", description = "页面内容")
    @JsonProperty("page_content")
    @TableField(typeHandler = MapTypeHandler.class)
    private Map<String, Object> pageContent;

    @Schema(name = "isBody", description = "根元素是否是body")
    private Boolean isBody;

    @Schema(name = "parentId", description = "父文件夹id")
    private String parentId;

    private String group;

    @Schema(name = "depth", description = "*页面/文件夹深度，更改层级时服务端校验用（校验可有可无）*")
    private Integer depth;

    @Schema(name = "isPage", description = "是否为页面：分为页面和文件夹")
    private Boolean isPage;

    @Schema(name = "isDefault", description = "是否是默认页面")
    private Boolean isDefault;

    @Schema(name = "message", description = "信息")
    private String message;

    @Schema(name = "isHome", description = "是否为主页")
    private Boolean isHome;

    @Schema(name = "contentBlocks", description = "*设计预留字段*")
    @TableField(typeHandler = ListTypeHandler.class)
    private List<BlockVersionDto> contentBlocks;
}

package com.tinyengine.it.model.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinyengine.it.utils.ListTypeHandler;
import com.tinyengine.it.utils.MapTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class PageDto {
    @Schema(name = "id", description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(name = "name", description = "名称")
    private String name;

    @Schema(name = "app", description = "关联appId")
    private Long app;

    @Schema(name = "route", description = "路径")
    private String route;

    @Schema(name = "pageContent", description = "页面内容")
    @JsonProperty("page_content")
    @TableField(typeHandler = MapTypeHandler.class)
    private Map<String, Object> pageContent;

    @TableField(fill = FieldFill.INSERT)
    @JsonProperty("created_at")
    @Schema(name = "created_at", type = "LocalDateTime", description = "创建时间")
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonProperty("updated_at")
    @Schema(name = "updated_at", type = "LocalDateTime", description = "更新时间")
    private LocalDateTime updatedAt;

    @Schema(name = "createdBy", description = "创建人id")
    private Integer createdBy;

    @Schema(name = "updatedBy", description = "更新人id")
    private Integer updatedBy;

    @Schema(name = "tenant", description = "所属组织id")
    private Integer tenant;

    @Schema(name = "isBody", description = "根元素是否是body")
    private Boolean isBody;

    @Schema(name = "parentId", description = "父文件夹id")
    private String parentId;

    @Schema(name = "group", description = "分组")
    private String group;

    @Schema(name = "depth", description = "*页面/文件夹深度，更改层级时服务端校验用（校验可有可无）*")
    private Integer depth;

    @Schema(name = "isPage", description = "是否为页面：分为页面和文件夹")
    private Boolean isPage;
    @JsonProperty("occupierId")
    private Integer occupierId;

    @JsonProperty("occupier")
    @Schema(name = "occupier", description = "当前检出者id")
    private UsersPermissionsUser occupier;

    @Schema(name = "isDefault", description = "是否是默认页面")
    private Boolean isDefault;

    @Schema(name = "content_blocks", description = "*区块及版本*")
    @JsonProperty("content_blocks")
    @TableField(typeHandler = ListTypeHandler.class)
    private List<BlockVersionDto> contentBlocks;

    @TableField(exist = false)
    private Boolean isHome;

    @TableField(exist = false)
    private Map<String, List<String>> assets;

    @TableField(exist = false)
    private String message;
}

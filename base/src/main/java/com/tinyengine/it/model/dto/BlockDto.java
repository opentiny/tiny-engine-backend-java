package com.tinyengine.it.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinyengine.it.common.base.BaseEntity;
import com.tinyengine.it.common.handler.ListTypeHandler;
import com.tinyengine.it.model.entity.BlockHistory;
import com.tinyengine.it.model.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 区块dto
 * </p>
 *
 * @author zhangjuncao
 * @since 2024-10-17
 */
@Data
public class BlockDto extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @Schema(name = "label", description = "区块显示名称，严格大小写格式")
    private String label;

    @Schema(name = "name", description = "区块名称")
    @JsonProperty("name_cn")
    private String name;

    @Schema(name = "framework", description = "技术栈")
    private String framework;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @Schema(name = "assets", description = "构建资源")
    private Map<String, Object> assets;

    @JsonProperty("last_build_info")
    @TableField(typeHandler = JacksonTypeHandler.class)
    @Schema(name = "lastBuildInfo", description = "最新一次构建信息")
    private Map<String, Object> lastBuildInfo;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @Schema(name = "content", description = "区块内容")
    private Map<String, Object> content;

    @Schema(name = "description", description = "描述")
    private String description;

    @Schema(name = "tags", description = "标签")
    @TableField(typeHandler = ListTypeHandler.class)
    private List<String> tags;

    @Schema(name = "latestHistoryId", description = "当前历史记录表ID")
    @JsonProperty("latest_history_id")
    private Integer latestHistoryId;

    @Schema(name = "screenshot", description = "截屏")
    private String screenshot;

    @Schema(name = "path", description = "区块路径")
    private String path;

    @Schema(name = "latestVersion", description = "当前历史记录表最新版本")
    @JsonProperty("latest_version")
    private String latestVersion;

    @Schema(name = "occupierId", description = "当前锁定人id")
    private String occupierId;

    @Schema(name = "isOfficial", description = "是否是官方")
    @JsonProperty("is_official")
    private Boolean isOfficial;

    @Schema(name = "isDefault", description = "是否是默认")
    @JsonProperty("is_default")
    private Boolean isDefault;

    @Schema(name = "tinyReserved", description = "是否是tiny专有")
    @JsonProperty("tiny_reserved")
    private Boolean tinyReserved;

    @Schema(name = "npmName", description = "npm包名")
    @JsonProperty("npm_name")
    private String npmName;

    @Schema(name = "public", description = "公开状态：0,1,2")
    @JsonProperty("public")
    private Integer publicStatus;
    
    @Schema(name = "i18n", description = "国际化内容")
    private String i18n;

    @Schema(name = "platformId", description = "设计器ID")
    @JsonProperty("platform_id")
    private Integer platformId;

    @Schema(name = "appId", description = "创建区块时所在appId")
    @JsonProperty("created_app")
    private Integer appId;

    @Schema(name = "contentBlocks", description = "*设计预留字段用途*")
    @JsonProperty("content_blocks")
    private String contentBlocks;

    @Schema(name = "blockGroupId", description = "区块分组id,关联t_block_group表id")
    @JsonProperty("block_group_id")
    private Integer blockGroupId;

    @JsonProperty("occupier")
    @Schema(name = "occupierBy", description = "当前锁定人")
    private User occupier;

    @TableField(exist = false)
    @JsonProperty("public_scope_tenants")
    private List<Object> publicScopeTenants = new ArrayList<>();

    @TableField(exist = false)
    @Schema(name = "groups", type = " List<BlockGroup>", description = "区块分组")
    private List<Object> groups = new ArrayList<>();

    @TableField(exist = false)
    @Schema(name = "histories", type = " List<BlockHistory>", description = "区块历史")
    private List<BlockHistory> histories = new ArrayList<>();

    @JsonProperty("public")
    public Integer getPublic() {
        return this.publicStatus;
    }
}

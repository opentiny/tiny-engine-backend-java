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
 * 区块表
 * </p>
 *
 * @author zhangjuncao
 * @since 2024-10-17
 */
@Getter
@Setter
@TableName("t_block")
@Schema(name = "Block", description = "区块表")
public class Block implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(name = "id", description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(name = "label", description = "区块显示名称，严格大小写格式")
    private String label;

    @Schema(name = "name", description = "区块名称")
    private String name;

    @Schema(name = "framework", description = "技术栈")
    private String framework;

    @Schema(name = "content", description = "区块内容")
    private String content;

    @Schema(name = "assets", description = "构建资源")
    private String assets;

    @Schema(name = "lastBuildInfo", description = "最新一次构建信息")
    private String lastBuildInfo;

    @Schema(name = "description", description = "描述")
    private String description;

    @Schema(name = "tags", description = "标签")
    private String tags;

    @Schema(name = "latestVersion", description = "当前历史记录表最新版本")
    private String latestVersion;

    @Schema(name = "latestHistoryId", description = "当前历史记录表ID")
    private Integer latestHistoryId;

    @Schema(name = "screenshot", description = "截屏")
    private String screenshot;

    @Schema(name = "path", description = "区块路径")
    private String path;

    @Schema(name = "occupierBy", description = "当前锁定人id")
    private String occupierBy;

    @Schema(name = "isOfficial", description = "是否是官方")
    private Boolean isOfficial;

    @Schema(name = "public", description = "公开状态：0,1,2")
    private Integer publicStatus;

    @Schema(name = "isDefault", description = "是否是默认")
    private Boolean isDefault;

    @Schema(name = "tinyReserved", description = "是否是tiny专有")
    private Boolean tinyReserved;

    @Schema(name = "npmName", description = "npm包名")
    private String npmName;

    @Schema(name = "i18n", description = "国际化内容")
    private String i18n;

    @Schema(name = "platformId", description = "设计器ID")
    private Integer platformId;

    @Schema(name = "appId", description = "创建区块时所在appId")
    private Integer appId;

    @Schema(name = "contentBlocks", description = "*设计预留字段用途*")
    private String contentBlocks;

    @Schema(name = "blockGroupId", description = "区块分组id,关联t_block_group表id")
    private Integer blockGroupId;

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

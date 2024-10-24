package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinyengine.it.common.base.BaseEntity;
import com.tinyengine.it.config.handler.MapTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * <p>
 * 物料包表
 * </p>
 *
 * @author zhangjuncao
 * @since 2024-10-17
 */
@Getter
@Setter
@TableName("t_material")
@Schema(name = "Material", description = "物料包表")
public class Material extends BaseEntity {
    @Schema(name = "name", description = "物料包名称")
    private String name;

    @Schema(name = "npmName", description = "npm包名")
    private String npmName;

    @Schema(name = "framework", description = "技术栈")
    private String framework;

    @Schema(name = "assetsUrl", description = "资源地址")
    @TableField(typeHandler = MapTypeHandler.class)
    private Map<String, Object> assetsUrl;

    @Schema(name = "imageUrl", description = "封面图地址")
    private String imageUrl;

    @Schema(name = "published", description = "是否发布：1是，0否")
    private Boolean published;

    @Schema(name = "latestVersion", description = "当前历史记录表最新版本")
    private String latestVersion;

    @Schema(name = "latestHistoryId", description = "当前历史记录表ID")
    private Integer latestHistoryId;

    @Schema(name = "public", description = "公开状态：0,1,2")
    @JsonProperty("public")
    private Integer publicStatus;

    @Schema(name = "lastBuildInfo", description = "最新一次构建信息")
    @TableField(typeHandler = MapTypeHandler.class)
    private Map<String, Object> lastBuildInfo;

    @Schema(name = "description", description = "描述")
    private String description;

    @Schema(name = "isOfficial", description = "是否是官方")
    private Boolean isOfficial;

    @Schema(name = "isDefault", description = "是否默认")
    private Boolean isDefault;

    @Schema(name = "tinyReserved", description = "是否是tiny专有")
    private Boolean tinyReserved;

    @Schema(name = "componentLibraryId", description = "*设计预留字段*")
    private Integer componentLibraryId;

    @Schema(name = "materialCategoryId", description = "物料包业务类型")
    private Integer materialCategoryId;

    @Schema(name = "materialSize", description = "物料包大小")
    private Integer materialSize;

    @Schema(name = "tgzUrl", description = "物料包存储地址")
    private String tgzUrl;

    @Schema(name = "unzipTgzRootPathUrl", description = "物料包存储根路径")
    private String unzipTgzRootPathUrl;

    @Schema(name = "unzipTgzFiles", description = "物料包存储文件")
    private String unzipTgzFiles;
}

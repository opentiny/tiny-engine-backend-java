package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinyengine.it.common.base.BaseEntity;
import com.tinyengine.it.common.utils.MapTypeHandler;
import com.tinyengine.it.model.dto.BlockVersionDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 区块历史表
 * </p>
 *
 * @author lu-yg
 * @since 2024-10-17
 */
@Getter
@Setter
@TableName("t_block_history")
@Schema(name = "BlockHistory", description = "区块历史表")
public class BlockHistory extends BaseEntity {
    @Schema(name = "id", description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @Schema(name = "message", description = "历史记录描述消息")
    private String message;


    @Schema(name = "label", description = "区块编码")
    private String label;

    @Schema(name = "name", description = "区块名称")
    private String name;

    @Schema(name = "framework", description = "技术栈")
    private String framework;

    @TableField(typeHandler = MapTypeHandler.class)
    @Schema(name = "content", description = "区块内容")
    private Map<String, Object> content;

    @Schema(name = "assets", description = "区块构建资源")
    @TableField(typeHandler = MapTypeHandler.class)
    private Map<String, Object> assets;

    @Schema(name = "buildInfo", description = "构建信息")
    @TableField(typeHandler = MapTypeHandler.class)
    private Map<String, Object> buildInfo;

    @Schema(name = "screenshot", description = "截图")
    private String screenshot;

    @Schema(name = "path", description = "区块路径")
    private String path;

    @Schema(name = "description", description = "区块描述")
    private String description;

    @Schema(name = "tags", description = "标签")
    private String tags;

    @Schema(name = "isOfficial", description = "是否是官方")
    private Boolean isOfficial;

    @JsonProperty("public")
    @Schema(name = "public", description = "公开状态：0，1，2")
    private Integer isPublic;

    @Schema(name = "isDefault", description = "是否是默认")
    private Boolean isDefault;

    @Schema(name = "tinyReserved", description = "是否是tiny专有")
    private Boolean tinyReserved;

    @Schema(name = "mode", description = "模式：vscode")
    private String mode;

    @Schema(name = "platformId", description = "区块id")
    private Integer platformId;

    @Schema(name = "appId", description = "创建区块appId")
    private Integer appId;

    @Schema(name = "npmName", description = "npm包名")
    private String npmName;

    @Schema(name = "i18n", description = "国际化")
    @TableField(typeHandler = MapTypeHandler.class)
    private Map<String, Map<String, String>> i18n;

    @Schema(name = "blockGroupId", description = "区块分组关联Id")
    private Integer blockGroupId;

    @Schema(name = "contentBlocks", description = "*暂不清楚*")
    @TableField(typeHandler = MapTypeHandler.class)
    private List<BlockVersionDto> contentBlocks;


}

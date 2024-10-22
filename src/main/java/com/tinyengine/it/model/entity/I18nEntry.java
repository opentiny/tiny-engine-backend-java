package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 国际化语言配置表
 * </p>
 *
 * @author lu-yg
 * @since 2024-10-17
 */
@Getter
@Setter
@TableName("t_i18n_entry")
@Schema(name = "I18nEntry", description = "国际化语言配置表")
public class I18nEntry implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(name = "id", description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(name = "key", description = "国际化词条key")
    private String key;

    @Schema(name = "content", description = "词条内容")
    private String content;

    @Schema(name = "hostId", description = "关联的hostid： appId或blockId")
    @JsonProperty("host_id")
    private Integer hostId;

    @Schema(name = "hostType", description = "app或者block")
    @JsonProperty("host_type")
    private String hostType;

    @Schema(name = "langId", description = "关联语言id")
    @JsonProperty("lang_id")
    private Integer langId;

    @Schema(name = "createdBy", description = "创建人")
    @JsonProperty("created_by")
    private String createdBy;

    @Schema(name = "lastUpdatedBy", description = "最后修改人")
    @JsonProperty("last_updated_by")
    private String lastUpdatedBy;

    @Schema(name = "createdTime", description = "创建时间")
    @JsonProperty("created_time")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    @Schema(name = "lastUpdatedTime", description = "更新时间")
    @JsonProperty("last_updated_time")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime lastUpdatedTime;

    @Schema(name = "tenantId", description = "租户id")
    @JsonProperty("tenant_id")
    private String tenantId;

    @Schema(name = "siteId", description = "站点id")
    @JsonProperty("site_id")
    private String siteId;

}

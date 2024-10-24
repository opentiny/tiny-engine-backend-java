package com.tinyengine.it.model.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinyengine.it.model.entity.I18nLang;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author zhangjuncao
 * @since 2024 -10-21
 */
@Data
@Schema(name = "I18nEntries对象", description = "返回对象")
public class I18nEntryDto {
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

    @Schema(name = "lang", description = "更新人")
    private I18nLang lang;

}

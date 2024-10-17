package com.tinyengine.it.model.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author zhangjuncao
 * @since 2024-04-16
 */
@Data
@Schema(name = "I18nEntries对象", description = "返回对象")
public class I18nEntriesDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "id", description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(name = "key", description = "国际化词条key")
    private String key;

    @Schema(name = "content", description = "词条内容")
    private String content;

    @Schema(name = "host", description = "关联的hostid： appId或blockId")
    private Integer host;

    @JsonProperty("host_type")
    @Schema(name = "host_type", description = "app或者block")
    private String hostType;

    @JsonProperty("langId")
    @Schema(name = "langId", description = "I18lang表id")
    private Integer langId;

    @JsonProperty("created_at")
    @Schema(name = "created_at", description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    @Schema(name = "updated_at", description = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @Schema(name = "createdBy", description = "创建人")
    @TableField(value = "createdBy", fill = FieldFill.INSERT)
    private Integer createdBy;

    @Schema(name = "updatedBy", description = "更新人")
    @TableField(value = "updatedBy", fill = FieldFill.INSERT)
    private Integer updatedBy;

    @Schema(name = "lang", description = "更新人")
    private I18nLangs lang;

}

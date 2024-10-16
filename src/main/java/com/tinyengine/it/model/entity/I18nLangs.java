package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author zhangjuncao
 * @since 2024-04-28
 */
@Getter
@Setter
@TableName("i18n_langs")
@Schema(name = "I18nLangs对象", description = "返回对象")
public class I18nLangs implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "id", description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(name = "lang", description = "语言代码")
    private String lang;

    @Schema(name = "label", description = "语言")
    private String label;

    @TableField(fill = FieldFill.INSERT)
    @JsonProperty("created_at")
    @Schema(name = "created_at", type = "LocalDateTime", description = "创建时间")
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonProperty("updated_at")
    @Schema(name = "updated_at", type = "LocalDateTime", description = "更新时间")
    private LocalDateTime updatedAt;

    @Schema(name = "createdBy", description = "*创建人*")
    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private Integer createdBy;

    @Schema(name = "updatedBy", description = "*更新人*")
    @TableField(value = "updated_by", fill = FieldFill.INSERT)
    private Integer updatedBy;

    public I18nLangs(String lang, String label) {
        this.lang = lang;
        this.label = label;
    }
}

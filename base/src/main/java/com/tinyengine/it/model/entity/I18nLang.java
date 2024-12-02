/**
 * Copyright (c) 2023 - present TinyEngine Authors.
 * Copyright (c) 2023 - present Huawei Cloud Computing Technologies Co., Ltd.
 *
 * Use of this source code is governed by an MIT-style license.
 *
 * THE OPEN SOURCE SOFTWARE IN THIS PRODUCT IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL,
 * BUT WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS FOR
 * A PARTICULAR PURPOSE. SEE THE APPLICABLE LICENSES FOR MORE DETAILS.
 *
 */

package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * <p>
 * 国际化语言表
 * </p>
 *
 * @author zhangjuncao
 * @since 2024-10-17
 */
@Getter
@Setter
@TableName("t_i18n_lang")
@Schema(name = "I18nLang", description = "国际化语言表")
public class I18nLang {
    @Schema(name = "id", description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(name = "lang", description = "语言代码")
    private String lang;

    @Schema(name = "label", description = "语言")
    private String label;

    @TableField(fill = FieldFill.INSERT)
    @Schema(name = "createdBy", description = "创建人")
    private String createdBy;

    @TableField(fill = FieldFill.INSERT)
    @Schema(name = "createdTime", description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(name = "lastUpdatedTime", description = "更新时间")
    private LocalDateTime lastUpdatedTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(name = "lastUpdatedBy", description = "最后修改人")
    private String lastUpdatedBy;

    /**
     * Instantiates a new 18 n lang.
     *
     * @param lang  the lang
     * @param label the label
     */
    public I18nLang(String lang, String label) {
        this.lang = lang;
        this.label = label;
    }
}

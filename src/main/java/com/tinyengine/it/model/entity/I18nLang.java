package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tinyengine.it.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

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
public class I18nLang extends BaseEntity {
    @Schema(name = "lang", description = "语言代码")
    private String lang;

    @Schema(name = "label", description = "语言")
    private String label;

    public I18nLang(String lang, String label) {
        this.lang = lang;
        this.label = label;
    }
}

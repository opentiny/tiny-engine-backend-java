package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinyengine.it.common.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 国际化语言配置表
 * </p>
 *
 * @author lu -yg
 * @since 2024-10-17
 */
@Getter
@Setter
@TableName("t_i18n_entry")
@Schema(name = "I18nEntry", description = "国际化语言配置表")
public class I18nEntry extends BaseEntity {
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
}

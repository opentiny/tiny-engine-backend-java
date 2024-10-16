package com.tinyengine.it.model.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinyengine.it.model.entity.*;
import com.tinyengine.it.utils.ListTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class BlockDto {
    private Integer id;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> content;

    private String label;

    private String framework;

    @TableField(value = "public")
    @JsonProperty("public")
    private Integer isPublic;

    @TableField(fill = FieldFill.INSERT)
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> assets;

    @TableField(value = "createdBy", fill = FieldFill.INSERT)
    private Integer createdBy;


    @TableField(value = "updatedBy", fill = FieldFill.INSERT)
    private Integer updatedBy;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @JsonProperty("last_build_info")
    private Map<String, Object> lastBuildInfo;

    private String description;
    @TableField(typeHandler = ListTypeHandler.class)
    private List<String> tags = new ArrayList<>();

    @JsonProperty("current_history")
    private Integer currentHistory;

    private String screenshot;

    private String path;

    @JsonProperty("occupierId")
    private Integer occupierId;

    @JsonProperty("occupier")
    @Schema(name = "occupier", description = "当前锁定人")
    private UsersPermissionsUser occupier;

    @TableField(value = "isOfficial")
    private Boolean isOfficial;

    @TableField(value = "isDefault")
    private Boolean isDefault;

    @JsonProperty("tiny_reserved")
    private Boolean tinyReserved;

    private Integer author;

    @JsonProperty("name_cn")
    private String nameCn;

    @JsonProperty("npm_name")
    private String npmName;

    @JsonProperty("created_app")
    private Integer createdApp;

    @JsonProperty("content_blocks")
    private String contentBlocks;

    @TableField(exist = false)
    private List<Object> public_scope_tenants = new ArrayList<>();

    @TableField(exist = false)
    @Schema(name = "groups", type = " List<BlockGroups>", description = "区块分组")
    List<BlockGroups> groups = new ArrayList<>();

    @TableField(exist = false)
    @Schema(name = "categories", type = " List<BlockCategories>", description = "区块分类")
    List<Object> categories = new ArrayList<>();

    @TableField(exist = false)
    @Schema(name = "histories", type = " List<histories>", description = "区块历史")
    List<BlockHistories> histories = new ArrayList<>();

    @TableField(exist = false)
    @Schema(name = "i18n_langs", type = " List<I18nLangs>", description = "国际化语言")
    List<I18nLangs> i18n_langs = new ArrayList<>();

    @JsonProperty("public")
    public Integer getPublic() {
        return this.isPublic;
    }
}

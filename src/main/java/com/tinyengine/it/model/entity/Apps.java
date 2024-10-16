package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinyengine.it.utils.ListTypeHandler;
import com.tinyengine.it.utils.MapTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author lyg
 * @since 2024-04-28
 */
@Getter
@Setter
@Schema(name = "Apps对象", description = "返回对象")
@TableName(value = "apps")
public class Apps implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "id", description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(name = "name", description = "应用名称")
    private String name;

    @JsonProperty("app_website")
    @Schema(name = "appWebsite", description = "暂不清楚")
    private String appWebsite;

    @Schema(name = "platform", description = "关联设计器id")
    private Integer platform;

    @JsonProperty("obs_url")
    @Schema(name = "obsUrl", description = "上传至obs的地址")
    private String obsUrl;

    @JsonProperty("created_at")
    @Schema(name = "createdAt", description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    @Schema(name = "updated_at", description = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @Schema(name = "state", description = "状态")
    private Integer state;

    @Schema(name = "published", description = "是否发布")
    private Boolean published;

    @Schema(name = "createdBy", description = "创建人")
    @TableField(value = "createdBy", fill = FieldFill.INSERT)
    private Integer createdBy;

    @Schema(name = "updatedBy", description = "更新人")
    @TableField(value = "updatedBy", fill = FieldFill.INSERT)
    private Integer updatedBy;

    @Schema(name = "tenant", description = "所属组织")
    private Integer tenant;

    @JsonProperty("home_page")
    @Schema(name = "homePage", description = "主页面id")
    private Long homePage;

    @Schema(name = "css", description = "css")
    private String css;

    @TableField(typeHandler = JacksonTypeHandler.class)
    @Schema(name = "config", description = "config")
    private Map<String, Object> config;

    @JsonProperty("git_group")
    @Schema(name = "gitGroup", description = "git仓库分组")
    private String gitGroup;

    @JsonProperty("project_name")
    @Schema(name = "projectName", description = "git仓库名称")
    private String projectName;

    @Schema(name = "constants", description = "constants")
    private String constants;

    @JsonProperty("data_handler")
    @TableField(typeHandler = MapTypeHandler.class)
    @Schema(name = "dataHandler", description = "数据源的拦截器")
    private Map<String, Object> dataHandler;

    @Schema(name = "description", description = "描述")
    private String description;

    @Schema(name = "latest", description = "应用最新历史记录id")
    private Integer latest;

    @JsonProperty("platform_history")
    @Schema(name = "platformHistory", description = "设计器历史")
    private Integer platformHistory;

    @JsonProperty("editor_url")
    @Schema(name = "editorUrl", description = "设计器地址")
    private String editorUrl;

    @Schema(name = "branch", description = "默认提交分支")
    private String branch;

    @JsonProperty("visit_url")
    @Schema(name = "visitUrl", description = "访问地址")
    private String visitUrl;

    @JsonProperty("is_demo")
    @Schema(name = "isDemo", description = "是否是demo应用")
    private Boolean isDemo;

    @JsonProperty("image_url")
    @Schema(name = "imageUrl", description = "封面图地址")
    private String imageUrl;

    @JsonProperty("is_default")
    @Schema(name = "isDefault", description = "是否是默认应用")
    private Boolean isDefault;

    @JsonProperty("templateType")
    @Schema(name = "templateType", description = "应用模板类型")
    private String templateType;

    @JsonProperty("set_template_time")
    @Schema(name = "setTemplateTime", description = "设置模板时间")
    private LocalDateTime setTemplateTime;

    @JsonProperty("set_template_by")
    @Schema(name = "setTemplateBy", description = "设置模板人id")
    private Integer setTemplateBy;

    @JsonProperty("set_default_by")
    @Schema(name = "setDefaultBy", description = "设置为默认应用人id")
    private Integer setDefaultBy;

    @Schema(name = "framework", description = "应用框架")
    private String framework;

    @JsonProperty("global_state")
    @TableField(typeHandler = ListTypeHandler.class)
    @Schema(name = "globalState", description = "应用全局状态")
    private List<Map<String, Object>> globalState;

    @JsonProperty("default_lang")
    @Schema(name = "defaultLang", description = "默认语言")
    private String defaultLang;

    @JsonProperty("extend_config")
    @TableField(typeHandler = MapTypeHandler.class)
    @Schema(name = "extendConfig", description = "应用扩展config")
    private Map<String, Object> extendConfig;

    @JsonProperty("assets_url")
    @Schema(name = "assetsUrl", description = "应用资源url")
    private String assetsUrl;

    @JsonProperty("data_hash")
    @Schema(name = "dataHash", description = "应用内容哈希值")
    private String dataHash;

    @JsonProperty("can_associate")
    @Schema(name = "canAssociate", description = "暂未使用")
    private Boolean canAssociate;

    @JsonProperty("data_source_global")
    @TableField(typeHandler = MapTypeHandler.class)
    @Schema(name = "dataSourceGlobal", description = "数据源全局配置")
    private Map<String, Object> dataSourceGlobal;

    @JsonProperty("tpl-groups")
    @Schema(name = "tplGroups", description = "模板分组")
    private String tplGroups;
}

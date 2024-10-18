package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 应用表
 * </p>
 *
 * @author zhangjuncao
 * @since 2024-10-17
 */
@Getter
@Setter
@TableName("t_app")
@Schema(name = "App", description = "应用表")
public class App implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(name= "id", description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(name= "name", description = "应用名称")
    private String name;

    @Schema(name= "appWebsite", description = "*设计预留字段*")
    private String appWebsite;

    @Schema(name= "platformId", description = "设计器id")
    private Integer platformId;

    @Schema(name= "platformHistoryId", description = "关联设计器的历史版本ID")
    private String platformHistoryId;

    @Schema(name= "publishUrl", description = "应用静态资源托管地址URL")
    private String publishUrl;

    @Schema(name= "editorUrl", description = "设计器地址")
    private String editorUrl;

    @Schema(name= "visitUrl", description = "访问地址")
    private String visitUrl;

    @Schema(name= "imageUrl", description = "封面图地址")
    private String imageUrl;

    @Schema(name= "assetsUrl", description = "应用资源url")
    private String assetsUrl;

    @Schema(name= "state", description = "应用状态：1可用，0不可用")
    private Integer state;

    @Schema(name= "published", description = "是否发布：1是，0否")
    private Boolean published;

    @Schema(name= "homePageId", description = "主页面id，关联page表的id")
    private Integer homePageId;

    @Schema(name= "css", description = "*设计预留字段*")
    private String css;

    @Schema(name= "config", description = "*设计预留字段*")
    private String config;

    @Schema(name= "constants", description = "*设计预留字段*")
    private String constants;

    @Schema(name= "dataHandler", description = "数据源的拦截器")
    private String dataHandler;

    @Schema(name= "description", description = "描述")
    private String description;

    @Schema(name= "latest", description = "应用最新历史记录id")
    private String latest;

    @Schema(name= "gitGroup", description = "git仓库分组")
    private String gitGroup;

    @Schema(name= "projectName", description = "git仓库名称")
    private String projectName;

    @Schema(name= "branch", description = "默认提交分支")
    private String branch;

    @Schema(name= "isDemo", description = "是否是demo应用")
    private String isDemo;

    @Schema(name= "isDefault", description = "是否是默认应用")
    private String isDefault;

    @Schema(name= "templateType", description = "应用模板类型")
    private String templateType;

    @Schema(name= "setTemplateTime", description = "设置模板时间")
    private LocalDateTime setTemplateTime;

    @Schema(name= "setTemplateBy", description = "设置模板人id")
    private String setTemplateBy;

    @Schema(name= "setDefaultBy", description = "设置为默认应用人id")
    private String setDefaultBy;

    @Schema(name= "framework", description = "应用框架")
    private String framework;

    @Schema(name= "globalState", description = "应用全局状态")
    private String globalState;

    @Schema(name= "defaultLang", description = "默认语言")
    private String defaultLang;

    @Schema(name= "extendConfig", description = "应用扩展config")
    private String extendConfig;

    @Schema(name= "dataHash", description = "应用内容哈希值")
    private String dataHash;

    @Schema(name= "canAssociate", description = "*设计预留字段*")
    private String canAssociate;

    @Schema(name= "dataSourceGlobal", description = "数据源全局配置")
    private String dataSourceGlobal;

    @Schema(name= "createdBy", description = "创建人")
    private String createdBy;

    @Schema(name= "lastUpdatedBy", description = "最后修改人")
    private String lastUpdatedBy;

    @Schema(name= "createdTime", description = "创建时间")
    private LocalDateTime createdTime;

    @Schema(name= "lastUpdatedTime", description = "更新时间")
    private LocalDateTime lastUpdatedTime;

    @Schema(name= "tenantId", description = "租户ID")
    private String tenantId;

    @Schema(name= "siteId", description = "站点ID")
    private String siteId;

}

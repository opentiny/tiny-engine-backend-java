package com.tinyengine.it.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tinyengine.it.common.base.BaseEntity;
import com.tinyengine.it.config.handler.ListTypeHandler;
import com.tinyengine.it.config.handler.MapTypeHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设计器表
 * </p>
 *
 * @author zhangjuncao
 * @since 2024-10-17
 */
@Getter
@Setter
@TableName("t_platform")
@Schema(name = "Platform", description = "设计器表")
public class Platform extends BaseEntity {
    @Schema(name = "name", description = "名称")
    private String name;

    @Schema(name = "published", description = "是否发布：1是，0否")
    private Boolean published;

    @Schema(name = "lastBuildInfo", description = "最后构建信息")
    @TableField(typeHandler = MapTypeHandler.class)
    private Map<String, Object> lastBuildInfo;

    @Schema(name = "description", description = "描述")
    private String description;

    @Schema(name = "latestVersion", description = "当前历史记录表最新版本")
    private String latestVersion;

    @Schema(name = "latestHistoryId", description = "当前历史记录表ID")
    private Integer latestHistoryId;

    @Schema(name = "materialHistoryId", description = "关联物料包历史ID")
    private Integer materialHistoryId;

    @Schema(name = "imageUrl", description = "设计器截图地址")
    private String imageUrl;

    @Schema(name = "sortPlugins", description = "插件集合")
    @TableField(typeHandler = ListTypeHandler.class)
    private List<Integer> sortPlugins;

    @Schema(name = "sortToolbar", description = "工具集合")
    @TableField(typeHandler = ListTypeHandler.class)
    private List<Integer> sortToolbar;

    @Schema(name = "isDefault", description = "是否默认编辑器：1是，0否")
    private Boolean isDefault;

    @Schema(name = "prettierOpts", description = "*设计预留字段*")
    @TableField(typeHandler = MapTypeHandler.class)
    private String prettierOpts;

    @Schema(name = "setDefaultBy", description = "设置默认编辑器的人")
    private String setDefaultBy;

    @Schema(name = "appExtendConfig", description = "应用扩展配置")
    @TableField(typeHandler = MapTypeHandler.class)
    private Map<String, Object> appExtendConfig;

    @Schema(name = "dataHash", description = "设计器数据Hash，验证数据一致性")
    private String dataHash;

    @Schema(name = "businessCategoryId", description = "业务类型")
    private Integer businessCategoryId;

    @Schema(name = "themeId", description = "生态扩展使用，关联主题")
    private Integer themeId;

    @Schema(name = "platformUrl", description = "设计器静态资源托管地址URL")
    private String platformUrl;

    @Schema(name = "vscodeUrl", description = "*设计预留字段*")
    private String vscodeUrl;
}

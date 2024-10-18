DROP TABLE IF EXISTS `t_platform_history`;
CREATE TABLE `t_platform_history`
(
    `id`                  int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `ref_id`              int(10) NOT NULL COMMENT '关联表id',
    `name`                varchar(255) NOT NULL COMMENT '名称',
    `publish_url`         varchar(255) NOT NULL COMMENT '设计器静态资源托管地址',
    `vscode_url`          varchar(255) COMMENT '预留字段',
    `version`             varchar(255) NOT NULL COMMENT '版本',
    `description`         varchar(255) NOT NULL COMMENT '描述',
    `material_history_id` int(10) NOT NULL COMMENT '关联表物料历史id',
    `sub_count`           int(10) NOT NULL COMMENT '预留字段',
    `material_pkg_name`   varchar(255) COMMENT '物料包名',
    `material_version`    varchar(255) COMMENT '物料包版本',
    `created_by`          varchar(255) NOT NULL COMMENT '创建人',
    `last_updated_by`     varchar(255) NOT NULL COMMENT '最后修改人',
    `created_time`        timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `last_updated_time`   timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant_id`           varchar(255) NOT NULL COMMENT '租户id',
    `site_id`             varchar(255) COMMENT '站点id',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `u_idx_platform_history`(`ref_id`, `version`) USING BTREE
) ENGINE = InnoDB COMMENT '设计器历史表';

DROP TABLE IF EXISTS `t_app_extension`;
CREATE TABLE `t_app_extension`
(
    `id`                int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `name`              varchar(255) NOT NULL COMMENT '名称',
    `type`              varchar(255) NOT NULL COMMENT '类型：npm, function',
    `content`           longtext     NOT NULL COMMENT '内容',
    `app_id`            int(11) NOT NULL COMMENT '关联app表Id',
    `category`          varchar(255) NOT NULL COMMENT '分类：utils,bridge',
    `created_by`        varchar(255) NOT NULL COMMENT '创建人',
    `last_updated_by`   varchar(255) NOT NULL COMMENT '最后修改人',
    `created_time`      timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `last_updated_time` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant_id`         varchar(255) NOT NULL COMMENT '租户id',
    `site_id`           varchar(255) COMMENT '站点id',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `u_idx_app_extension`(`app_id`, `name`, `category`, `type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC COMMENT '应用扩展';


DROP TABLE IF EXISTS `t_block_group`;
CREATE TABLE `t_block_group`
(
    `id`                int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `name`              varchar(255) NOT NULL COMMENT '分组名称',
    `app_id`            int(11) NOT NULL COMMENT '创建分组所在app',
    `platform_id`       int(11) NOT NULL COMMENT '设计器id',
    `description`       varchar(255) NULL DEFAULT NULL COMMENT '分组描述',
    `created_by`        varchar(255) NOT NULL COMMENT '创建人',
    `last_updated_by`   varchar(255) NOT NULL COMMENT '最后修改人',
    `created_time`      timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `last_updated_time` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant_id`         varchar(255) NOT NULL COMMENT '租户id',
    `site_id`           varchar(255) NOT NULL COMMENT '站点id',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `u_idx_block_group`(`tenant_id`,`platform_id`,`name`) USING BTREE
) ENGINE = InnoDB COMMENT '区块分组';

DROP TABLE IF EXISTS `t_block_history`;
CREATE TABLE `t_block_history`
(
    `id`                int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `ref_id`            int(11) NOT NULL COMMENT '关联主表id',
    `message`           varchar(255) NULL DEFAULT NULL COMMENT '历史记录描述消息',
    `version`           varchar(255) NOT NULL COMMENT '版本',
    `label`             varchar(255) NOT NULL COMMENT '区块编码',
    `name`              varchar(255) NOT NULL COMMENT '区块名称',
    `framework`         varchar(255) NULL DEFAULT NULL COMMENT '技术栈',
    `content`           longtext NULL COMMENT '区块内容',
    `assets`            longtext NULL COMMENT '区块构建资源',
    `build_info`        longtext NULL COMMENT '构建信息',
    `screenshot`        longtext NULL COMMENT '截图',
    `path`              varchar(255) NULL DEFAULT NULL COMMENT '区块路径',
    `description`       varchar(2000) NULL COMMENT '区块描述',
    `tags`              longtext NULL COMMENT '标签',
    `is_official`       tinyint(1) NULL DEFAULT NULL COMMENT '是否是官方',
    `public`            int(11) NULL DEFAULT NULL COMMENT '公开状态：0，1，2',
    `is_default`        tinyint(1) NULL DEFAULT NULL COMMENT '是否是默认',
    `tiny_reserved`     tinyint(1) NULL DEFAULT NULL COMMENT '是否是tiny专有',
    `mode`              varchar(255) NULL DEFAULT NULL COMMENT '模式：vscode',
    `platform_id`       int(11) NOT NULL COMMENT '区块id',
    `app_id`            int(11) NOT NULL COMMENT '创建区块appId',
    `npm_name`          varchar(255) NULL DEFAULT NULL COMMENT 'npm包名',
    `i18n`              longtext NULL COMMENT '国际化',
    `block_group_id`    int(11) NULL DEFAULT NULL COMMENT '区块分组关联Id',
    `content_blocks`    longtext NULL COMMENT '*暂不清楚*',
    `created_by`        varchar(255) NOT NULL COMMENT '创建人',
    `last_updated_by`   varchar(255) NOT NULL COMMENT '最后修改人',
    `created_time`      timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `last_updated_time` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant_id`         varchar(255) NOT NULL COMMENT '租户id',
    `site_id`           varchar(255) NOT NULL COMMENT '站点id',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `u_idx_block_history`(`app_id`,`ref_id`,`version`) USING BTREE
) ENGINE = InnoDB COMMENT '区块历史表';

DROP TABLE IF EXISTS `t_material_history`;
CREATE TABLE `t_material_history`
(
    `id`                      int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    `ref_id`                  int(11) NOT NULL COMMENT '关联主表id',
    `version`                 varchar(255) NOT NULL COMMENT '版本',
    `content`                 longtext NULL COMMENT '物料内容',
    `name`                    varchar(255) NOT NULL COMMENT '物料名称',
    `npm_name`                varchar(255) NULL DEFAULT NULL COMMENT 'npm包名',
    `framework`               varchar(255) NOT NULL COMMENT '技术栈',
    `assets_url`              longtext NULL COMMENT '物料构建资源',
    `image_url`               varchar(255) NULL COMMENT '封面图片地址',
    `description`             varchar(2000) NULL DEFAULT NULL COMMENT '描述',
    `material_size`           varchar(255) NULL COMMENT '物料包大小',
    `tgz_url`                 varchar(255) NULL COMMENT '物料压缩包地址',
    `unzip_tgz_root_path_url` varchar(255) NULL COMMENT '物料压缩包解压后根地址',
    `unzip_tgz_files`         varchar(255) NULL COMMENT '物料压缩包解压后文件地址',
    `created_by`              varchar(255) NOT NULL COMMENT '创建人',
    `last_updated_by`         varchar(255) NOT NULL COMMENT '最后修改人',
    `created_time`            timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `last_updated_time`       timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant_id`               varchar(255) NOT NULL COMMENT '租户id',
    `site_id`                 varchar(255) NOT NULL COMMENT '站点id',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `u_idx_material_history`(`tenant_id`,`ref_id`,`version`) USING BTREE
) ENGINE = InnoDB COMMENT '物料历史表';

DROP TABLE IF EXISTS `t_page_history`;
CREATE TABLE `t_page_history`
(
    `id`                int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    `ref_id`            int(11) NOT NULL COMMENT '关联主表id',
    `name`              varchar(255) NULL DEFAULT NULL COMMENT '名称',
    `app_id`            int(11) NOT NULL COMMENT '关联app表Id',
    `route`             varchar(255) NULL DEFAULT NULL COMMENT '页面路由',
    `page_content`      longtext NULL COMMENT '页面内容',
    `is_body`           tinyint(1) NULL DEFAULT NULL COMMENT '根元素是否是body',
    `parent_id`         bigint(20) NOT NULL COMMENT '父文件夹id',
    `group`             varchar(255) NULL DEFAULT NULL,
    `depth`             int(11) NULL DEFAULT NULL COMMENT '*页面/文件夹深度，更改层级时服务端校验用（校验可有可无）*',
    `is_page`           tinyint(1) NOT NULL COMMENT '是否为页面：分为页面和文件夹',
    `is_default`        tinyint(1) NOT NULL COMMENT '是否是默认页面',
    `message`           varchar(255) NULL DEFAULT NULL COMMENT '信息',
    `is_home`           tinyint(1) NULL DEFAULT NULL COMMENT '是否为主页',
    `content_blocks`    longtext NULL,
    `created_by`        varchar(255) NOT NULL COMMENT '创建人',
    `last_updated_by`   varchar(255) NOT NULL COMMENT '最后修改人',
    `created_time`      timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `last_updated_time` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant_id`         varchar(255) NOT NULL COMMENT '租户id',
    `site_id`           varchar(255) NOT NULL COMMENT '站点id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT '页面历史';

DROP TABLE IF EXISTS `t_component`;
CREATE TABLE `t_component`
(
    `id`                 int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    `version`            varchar(255) NOT NULL COMMENT '版本',
    `name`               longtext     NOT NULL COMMENT '名称',
    `name_en`            varchar(255) NOT NULL COMMENT '英文名称',
    `icon`               varchar(255) NULL DEFAULT NULL COMMENT '组件图标',
    `description`        varchar(255) NULL DEFAULT NULL COMMENT '描述',
    `doc_url`            varchar(255) NULL DEFAULT NULL COMMENT '文档链接',
    `screenshot`         varchar(255) NULL DEFAULT NULL COMMENT '缩略图',
    `tags`               varchar(255) NULL DEFAULT NULL COMMENT '标签',
    `keywords`           varchar(255) NULL DEFAULT NULL COMMENT '关键字',
    `dev_mode`           varchar(255) NOT NULL COMMENT '研发模式',
    `npm`                longtext     NOT NULL COMMENT 'npm属性对象',
    `group`              varchar(255) NULL DEFAULT NULL COMMENT '分组',
    `category`           varchar(255) NULL DEFAULT NULL COMMENT '分类',
    `priority`           int(11) NULL DEFAULT NULL COMMENT '排序',
    `snippets`           longtext NULL COMMENT 'schema片段',
    `schema_fragment`    longtext NULL COMMENT 'schema片段',
    `configure`          longtext NULL COMMENT '配置信息',
    `public`             int(11) NULL DEFAULT NULL COMMENT '公开状态：0，1，2',
    `framework`          varchar(255) NOT NULL COMMENT '技术栈',
    `is_official`        tinyint(1) NULL DEFAULT NULL COMMENT '标识官方组件',
    `is_default`         tinyint(1) NULL DEFAULT NULL COMMENT '标识默认组件',
    `tiny_reserved`      tinyint(1) NULL DEFAULT NULL COMMENT '是否tiny自有',
    `component_metadata` longtext NULL COMMENT '属性信息',
    `created_by`         varchar(255) NOT NULL COMMENT '创建人',
    `last_updated_by`    varchar(255) NOT NULL COMMENT '最后修改人',
    `created_time`       timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `last_updated_time`  timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant_id`          varchar(255) NOT NULL COMMENT '租户id',
    `site_id`            varchar(255) NOT NULL COMMENT '站点id',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `u_idx_component`(`name_en`,`created_by`, `framework`) USING BTREE
) ENGINE = InnoDB COMMENT '组件表';

DROP TABLE IF EXISTS `r_material_history_block`;
CREATE TABLE `r_material_history_block`
(
    `id`                  int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    `material_history_id` int(11) NOT NULL COMMENT '关联物料历史表id',
    `block_history_id`    int(11) NOT NULL COMMENT '关联区块历史表id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT '物料历史与区块历史关系表';

DROP TABLE IF EXISTS `r_material_component`;
CREATE TABLE `r_material_component`
(
    `id`           int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
    `material_id`  int(11) NOT NULL COMMENT '关联物料表id',
    `component_id` int(11) NOT NULL COMMENT '关联组件表id',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT '物料与组件关系表';

DROP TABLE IF EXISTS `t_i18n_entry`;
CREATE TABLE `t_i18n_entry`
(
    `id`                int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `key`               varchar(255) NOT NULL COMMENT '国际化词条key',
    `content`           varchar(255) NOT NULL COMMENT '词条内容',
    `host_id`           int(11) NOT NULL COMMENT '关联的hostid： appId或blockId',
    `host_type`         varchar(255) NOT NULL COMMENT 'app或者block',
    `lang_id`           int(11) NULL DEFAULT NULL COMMENT '关联语言id',
    `created_by`        varchar(255) NOT NULL COMMENT '创建人',
    `last_updated_by`   varchar(255) NOT NULL COMMENT '最后修改人',
    `created_time`      timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `last_updated_time` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant_id`         varchar(255) NOT NULL COMMENT '租户id',
    `site_id`           varchar(255) NOT NULL COMMENT '站点id',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `u_idx_i18n_entry`(`tenant_id`, `host_id`, `key`, `lang_id`) USING BTREE
) ENGINE = InnoDB COMMENT '国际化语言配置表';

DROP TABLE IF EXISTS `t_datasource`;
CREATE TABLE `t_datasource`
(
    `id`                int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `name`              varchar(255) NOT NULL COMMENT '数据源名称',
    `data`              longtext NULL COMMENT '数据源内容',
    `tpl`               bigint(20) NULL DEFAULT NULL COMMENT '*暂不清楚*',
    `app_id`            bigint(20) NULL DEFAULT NULL COMMENT '关联应用id',
    `description`       varchar(255) NULL DEFAULT NULL COMMENT '描述',
    `created_by`        varchar(255) NOT NULL COMMENT '创建人',
    `last_updated_by`   varchar(255) NOT NULL COMMENT '最后修改人',
    `created_time`      timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `last_updated_time` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant_id`         varchar(255) NOT NULL COMMENT '租户id',
    `site_id`           varchar(255) NOT NULL COMMENT '站点id',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `u_idx_datasource`(`tenant_id`,`app_id`, `name`) USING BTREE
) ENGINE = InnoDB COMMENT '数据源表';

DROP TABLE IF EXISTS `t_tenant`;
CREATE TABLE `t_tenant`
(
    `id`                int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `name_cn`           varchar(255) NULL DEFAULT NULL COMMENT '组织中文名',
    `name_en`           varchar(255) NULL DEFAULT NULL COMMENT '组织英文名',
    `description`       longtext NULL COMMENT '组织描述',
    `created_by`        varchar(255) NOT NULL COMMENT '创建人',
    `last_updated_by`   varchar(255) NOT NULL COMMENT '最后修改人',
    `created_time`      timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `last_updated_time` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant_id`         varchar(255) NOT NULL COMMENT '租户id',
    `site_id`           varchar(255) NOT NULL COMMENT '站点id',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `u_idx_tenant`(`name_cn`) USING BTREE
) ENGINE = InnoDB COMMENT '组织表';

-- ----------------------------
-- Table structure for t_platform
-- ----------------------------
DROP TABLE IF EXISTS `t_platform`;
CREATE TABLE `t_platform`
(
    `id`                   int          NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `name`                 varchar(255) NOT NULL COMMENT '名称',
    `published`            tinyint(1) COMMENT '是否发布：1是，0否',
    `last_build_info`      longtext COMMENT '最后构建信息',
    `description`          varchar(2000) COMMENT '描述',
    `latest_version`       varchar(255) COMMENT '当前历史记录表最新版本',
    `latest_history_id`    int COMMENT '当前历史记录表ID',
    `material_history_id`  int COMMENT '关联物料包历史ID',
    `image_url`            varchar(255) COMMENT '设计器截图地址',
    `sort_plugins`         longtext COMMENT '插件集合',
    `sort_toolbar`         longtext COMMENT '工具集合',
    `is_default`           tinyint(1) COMMENT '是否默认编辑器：1是，0否',
    `prettier_opts`        longtext COMMENT '*设计预留字段*',
    `set_default_by`       varchar(60) COMMENT '设置默认编辑器的人',
    `app_extend_config`    longtext COMMENT '应用扩展配置',
    `data_hash`            varchar(255) COMMENT '设计器数据Hash，验证数据一致性',
    `business_category_id` int COMMENT '业务类型',
    `theme_id`             int COMMENT '生态扩展使用，关联主题',
    `platform_url`         varchar(255) COMMENT '设计器静态资源托管地址URL',
    `vscode_url`           varchar(255) COMMENT '*设计预留字段*',
    `tenant_id`            varchar(60)  NOT NULL COMMENT '租户ID',
    `site_id`              varchar(60) COMMENT '站点ID',
    `created_by`           varchar(60)  NOT NULL COMMENT '创建人',
    `last_updated_by`      varchar(60)  NOT NULL COMMENT '最后修改人',
    `created_time`         timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `last_updated_time`    timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `u_idx_platform`(`tenant_id`,`name`) USING BTREE
) ENGINE = InnoDB COMMENT = '设计器表';


-- ----------------------------
-- Table structure for t_app
-- ----------------------------
DROP TABLE IF EXISTS `t_app`;
CREATE TABLE `t_app`
(
    `id`                  int          NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `name`                varchar(255) NOT NULL COMMENT '应用名称',
    `app_website`         varchar(255) COMMENT '*设计预留字段*',
    `platform_id`         int          NOT NULL COMMENT '设计器id',
    `platform_history_id` varchar(255) NOT NULL COMMENT '关联设计器的历史版本ID',
    `publish_url`         varchar(255) COMMENT '应用静态资源托管地址URL',
    `editor_url`          varchar(255) COMMENT '设计器地址',
    `visit_url`           varchar(255) COMMENT '访问地址',
    `image_url`           varchar(255) COMMENT '封面图地址',
    `assets_url`          varchar(255) COMMENT '应用资源url',
    `state`               int COMMENT '应用状态：1可用，0不可用',
    `published`           tinyint(1) COMMENT '是否发布：1是，0否',
    `home_page_id`        int COMMENT '主页面id，关联page表的id',
    `css`                 longtext COMMENT '*设计预留字段*',
    `config`              longtext COMMENT '*设计预留字段*',
    `constants`           longtext COMMENT '*设计预留字段*',
    `data_handler`        longtext COMMENT '数据源的拦截器',
    `description`         varchar(2000) COMMENT '描述',
    `latest`              varchar(255) COMMENT '应用最新历史记录id',
    `git_group`           varchar(255) COMMENT 'git仓库分组',
    `project_name`        varchar(255) COMMENT 'git仓库名称',
    `branch`              varchar(255) COMMENT '默认提交分支',
    `is_demo`             varchar(1) COMMENT '是否是demo应用',
    `is_default`          varchar(1) COMMENT '是否是默认应用',
    `template_type`       varchar(255) COMMENT '应用模板类型',
    `set_template_time`   timestamp COMMENT '设置模板时间',
    `set_template_by`     varchar(60) COMMENT '设置模板人id',
    `set_default_by`      varchar(60) COMMENT '设置为默认应用人id',
    `framework`           varchar(255) COMMENT '应用框架',
    `global_state`        longtext COMMENT '应用全局状态',
    `default_lang`        varchar(255) COMMENT '默认语言',
    `extend_config`       longtext COMMENT '应用扩展config',
    `data_hash`           varchar(255) COMMENT '应用内容哈希值',
    `can_associate`       varchar(1) COMMENT '*设计预留字段*',
    `data_source_global`  longtext COMMENT '数据源全局配置',
    `created_by`          varchar(60)  NOT NULL COMMENT '创建人',
    `last_updated_by`     varchar(60)  NOT NULL COMMENT '最后修改人',
    `created_time`        timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `last_updated_time`   timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant_id`           varchar(60)  NOT NULL COMMENT '租户ID',
    `site_id`             varchar(60) COMMENT '站点ID',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `u_idx_app`(`tenant_id`,`platform_id`,`name`) USING BTREE
) ENGINE = InnoDB COMMENT = '应用表';


-- ----------------------------
-- Table structure for t_business_category
-- ----------------------------
DROP TABLE IF EXISTS `t_business_category`;
CREATE TABLE `t_business_category`
(
    `id`                int          NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `code`              varchar(255) NOT NULL COMMENT '编码',
    `name`              varchar(255) NOT NULL COMMENT '名称',
    `created_by`        varchar(60)  NOT NULL COMMENT '创建人',
    `last_updated_by`   varchar(60)  NOT NULL COMMENT '最后修改人',
    `created_time`      timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `last_updated_time` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant_id`         varchar(60)  NOT NULL COMMENT '租户ID',
    `site_id`           varchar(60) COMMENT '站点ID',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `u_idx_business_category`(`tenant_id`,`code`) USING BTREE
) ENGINE = InnoDB COMMENT = '业务类型表，全局';


-- ----------------------------
-- Table structure for t_block
-- ----------------------------
DROP TABLE IF EXISTS `t_block`;
CREATE TABLE `t_block`
(
    `id`                int          NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `label`             varchar(255) NOT NULL COMMENT '区块显示名称，严格大小写格式',
    `name`              varchar(255) COMMENT '区块名称',
    `framework`         varchar(255) NOT NULL COMMENT '技术栈',
    `content`           longtext COMMENT '区块内容',
    `assets_url`            longtext COMMENT '构建资源',
    `last_build_info`   longtext COMMENT '最新一次构建信息',
    `description`       varchar(2000) COMMENT '描述',
    `tags`              longtext COMMENT '标签',
    `latest_version`    varchar(255) COMMENT '当前历史记录表最新版本',
    `latest_history_id` int COMMENT '当前历史记录表ID',
    `screenshot`        longtext COMMENT '截屏',
    `path`              varchar(255) COMMENT '区块路径',
    `occupier_by`       varchar(60) COMMENT '当前锁定人id',
    `is_official`       tinyint(1) COMMENT '是否是官方',
    `public`            int COMMENT '公开状态：0,1,2',
    `is_default`        tinyint(1) COMMENT '是否是默认',
    `tiny_reserved`     tinyint(1) COMMENT '是否是tiny专有',
    `npm_name`          varchar(255) COMMENT 'npm包名',
    `i18n`              longtext COMMENT '国际化内容',
    `platform_id`       int          NOT NULL COMMENT '设计器ID',
    `app_id`            int          NOT NULL COMMENT '创建区块时所在appId',
    `content_blocks`    longtext COMMENT '*设计预留字段用途*',
    `block_group_id`    int COMMENT '区块分组id,关联t_block_group表id',
    `created_by`        varchar(60)  NOT NULL COMMENT '创建人',
    `last_updated_by`   varchar(60)  NOT NULL COMMENT '最后修改人',
    `created_time`      timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `last_updated_time` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant_id`         varchar(60)  NOT NULL COMMENT '租户ID',
    `site_id`           varchar(60) COMMENT '站点ID',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `u_idx_block`(`platform_id`,`label`,`framework`) USING BTREE
) ENGINE = InnoDB COMMENT = '区块表';

-- ----------------------------
-- Table structure for t_material
-- ----------------------------
DROP TABLE IF EXISTS `t_material`;
CREATE TABLE `t_material`
(
    `id`                      int          NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `name`                    varchar(255) NOT NULL COMMENT '物料包名称',
    `npm_name`                varchar(255) COMMENT 'npm包名',
    `framework`               varchar(255) NOT NULL COMMENT '技术栈',
    `assets_url`              longtext COMMENT '资源地址',
    `image_url`               varchar(255) COMMENT '封面图地址',
    `published`               tinyint(1) COMMENT '是否发布：1是，0否',
    `latest_version`          varchar(255) COMMENT '当前历史记录表最新版本',
    `latest_history_id`       int COMMENT '当前历史记录表ID',
    `pulblic`                 int COMMENT '公开状态：0,1,2',
    `last_build_info`         longtext COMMENT '最新一次构建信息',
    `description`             varchar(2000) COMMENT '描述',
    `is_official`             tinyint(1) COMMENT '是否是官方',
    `is_default`              tinyint(1) COMMENT '是否默认',
    `tiny_reserved`           tinyint(1) COMMENT '是否是tiny专有',
    `component_library_id`    varchar(255) COMMENT '*设计预留字段*',
    `material_category_id`    varchar(255) COMMENT '物料包业务类型',
    `material_size`           int COMMENT '物料包大小',
    `tgz_url`                 varchar(255) COMMENT '物料包存储地址',
    `unzip_tgz_root_path_url` longtext COMMENT '物料包存储根路径',
    `unzip_tgz_files`         longtext COMMENT '物料包存储文件',
    `created_by`              varchar(60)  NOT NULL COMMENT '创建人',
    `last_updated_by`         varchar(60)  NOT NULL COMMENT '最后修改人',
    `created_time`            timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `last_updated_time`       timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant_id`               varchar(60)  NOT NULL COMMENT '租户ID',
    `site_id`                 varchar(60) COMMENT '站点ID',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `u_idx_material`(`tenant_id`,`name`,`framework`) USING BTREE
) ENGINE = InnoDB COMMENT = '物料包表';

-- ----------------------------
-- Table structure for t_page
-- ----------------------------
DROP TABLE IF EXISTS `t_page`;
CREATE TABLE `t_page`
(
    `id`                int          NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `name`              varchar(255) NOT NULL COMMENT '名称',
    `app_id`            bigint       NOT NULL COMMENT '关联appId',
    `route`             varchar(255) NOT NULL COMMENT '访问路由',
    `page_cotent`       longtext COMMENT '页面内容',
    `is_body`           tinyint(1) COMMENT '根元素是否是body',
    `parent_id`         int          NOT NULL COMMENT '父文件夹id',
    `group`             varchar(255) COMMENT '分组',
    `depth`             int COMMENT '页面/文件夹深度，更改层级时服务端校验用（校验可有可无）',
    `is_page`           tinyint(1) NOT NULL COMMENT '是否为页面：分为页面和文件夹',
    `occupier_by`       varchar(60) COMMENT '当前检出者id',
    `is_default`        tinyint(1) NOT NULL COMMENT '是否是默认页面',
    `content_blocks`    longtext COMMENT '*设计预留字段*',
    `latest_version`    varchar(255) COMMENT '当前历史记录表最新版本',
    `latest_history_id` int COMMENT '当前历史记录表ID',
    `created_by`        varchar(60)  NOT NULL COMMENT '创建人',
    `last_updated_by`   varchar(60)  NOT NULL COMMENT '最后修改人',
    `created_time`      timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `last_updated_time` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant_id`         varchar(60)  NOT NULL COMMENT '租户ID',
    `site_id`           varchar(60) COMMENT '站点ID',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `u_idx_page`(`app_id`,`name`) USING BTREE
) ENGINE = InnoDB COMMENT = '页面表';


-- ----------------------------
-- Table structure for page_template
-- ----------------------------
DROP TABLE IF EXISTS `page_template`;
CREATE TABLE `page_template`
(
    `id`                int          NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `name`              varchar(255) NOT NULL COMMENT '名称',
    `page_content`      longtext NULL COMMENT '模板页面内容，存储页面内容，数据源等',
    `framework`         varchar(255) NOT NULL COMMENT '技术栈',
    `published`         int COMMENT '是否发布：1是，0否',
    `public`            int COMMENT '公开状态：0,1,2',
    `type`              varchar(255) NOT NULL COMMENT '模版类型',
    `status`            varchar(255) NOT NULL COMMENT '模板状态',
    `is_preset`         int COMMENT '*设计预留字段*',
    `tpl_image`         varchar(255) COMMENT '模板截图',
    `description`       varchar(2000) COMMENT '描述',
    `platform_id`       int          NOT NULL COMMENT '设计器ID',
    `created_by`        varchar(60)  NOT NULL COMMENT '创建人',
    `last_updated_by`   varchar(60)  NOT NULL COMMENT '最后修改人',
    `created_time`      timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `last_updated_time` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant_id`         varchar(60)  NOT NULL COMMENT '租户ID',
    `site_id`           varchar(60) COMMENT '站点ID',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = '页面模板表';

-- ----------------------------
-- Table structure for r_material_category
-- ----------------------------
DROP TABLE IF EXISTS `r_material_category`;
CREATE TABLE `r_material_category`
(
    `id`          int NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `material_id` int COMMENT '物料包ID',
    `category_id` int COMMENT '业务分类ID',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = '物料包业务分类关系表';


-- ----------------------------
-- Table structure for r_material_history_component
-- ----------------------------
DROP TABLE IF EXISTS `r_material_history_component`;
CREATE TABLE `r_material_history_component`
(
    `id`                  int NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `material_history_id` int COMMENT '物料包历史ID',
    `component_id`        int COMMENT '组件ID',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = '物料包历史组件关系表';


-- ----------------------------
-- Table structure for r_material_block
-- ----------------------------
DROP TABLE IF EXISTS `r_material_block`;
CREATE TABLE `r_material_block`
(
    `id`          int NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `material_id` int COMMENT '物料包ID',
    `block_id`    int COMMENT '区块ID',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = '物料包区块关系表，编辑态';

-- ----------------------------
-- Table structure for t_i18n_lang
-- ----------------------------
DROP TABLE IF EXISTS `t_i18n_lang`;
CREATE TABLE `t_i18n_lang`
(
    `id`                int          NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `lang`              varchar(255) NOT NULL COMMENT '语言代码',
    `label`             varchar(255) NOT NULL COMMENT '语言',
    `created_by`        varchar(60)  NOT NULL COMMENT '创建人',
    `last_updated_by`   varchar(60)  NOT NULL COMMENT '最后修改人',
    `created_time`      timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `last_updated_time` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant_id`         varchar(60)  NOT NULL COMMENT '租户ID',
    `site_id`           varchar(60) COMMENT '站点ID',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `u_idx_i18n_lang`(`lang`) USING BTREE
) ENGINE = InnoDB COMMENT = '国际化语言表';

-- ----------------------------
-- Table structure for t_task_record
-- ----------------------------
DROP TABLE IF EXISTS `t_task_record`;
CREATE TABLE `t_task_record`
(
    `id`                int         NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `team_id`           int COMMENT '团队id, 默认0',
    `task_type_id`      int COMMENT '任务类型: 1 ASSETS_BUILD / 2 APP_BUILD / 3 PLATFORM_BUILD / 4 VSCODE_PLUGIN_BUILD/5 BLOCK_BUILD',
    `unique_id`         int COMMENT '构建资源id',
    `task_name`         varchar(255) COMMENT '构建任务名称',
    `task_status`       int COMMENT '任务状态：0 init / 1 running / 2 stopped / 3 finished',
    `task_result`       longtext NULL COMMENT '当前执行进度结果信息',
    `progress`          varchar(255) COMMENT '当前进行的子任务名',
    `ratio`             int COMMENT '无用字段',
    `progress_percent`  int COMMENT '构建进度百分比数',
    `indicator`         longtext NULL COMMENT '构建指标',
    `created_by`        varchar(60) NOT NULL COMMENT '创建人',
    `last_updated_by`   varchar(60) NOT NULL COMMENT '最后修改人',
    `created_time`      timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `last_updated_time` timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant_id`         varchar(60) NOT NULL COMMENT '租户ID',
    `site_id`           varchar(60) COMMENT '站点ID',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB COMMENT = '构建任务表';

-- ----------------------------
-- Table structure for t_users
-- ----------------------------
DROP TABLE IF EXISTS `t_users`;
CREATE TABLE `t_users`
(
    `id`                int          NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `username`          varchar(255) NOT NULL COMMENT '用户名',
    `email`             varchar(255) NOT NULL COMMENT '邮箱',
    `enable`            tinyint(1) COMMENT '账号是否可用',
    `created_by`        varchar(60)  NOT NULL COMMENT '创建人',
    `last_updated_by`   varchar(60)  NOT NULL COMMENT '最后修改人',
    `created_time`      timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `last_updated_time` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    `tenant_id`         varchar(60)  NOT NULL COMMENT '租户ID',
    `site_id`           varchar(60) COMMENT '站点ID',
    `is_admin`          tinyint(1),
    `is_public`         tinyint(1),
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `u_idx_users`(`username`,`email`) USING BTREE
) ENGINE = InnoDB COMMENT = '用户权限表';
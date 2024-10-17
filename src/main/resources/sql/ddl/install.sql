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
    `assets`                  longtext NULL COMMENT '物料构建资源',
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
    `isOfficial`         tinyint(1) NULL DEFAULT NULL COMMENT '标识官方组件',
    `isDefault`          tinyint(1) NULL DEFAULT NULL COMMENT '标识默认组件',
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

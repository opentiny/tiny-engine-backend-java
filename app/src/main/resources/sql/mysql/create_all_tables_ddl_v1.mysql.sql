drop table if exists `t_platform`;

create table `t_platform`
(
    `id`                   int          not null auto_increment comment '主键id',
    `name`                 varchar(255) not null comment '名称',
    `published`            tinyint(1) comment '是否发布：1是，0否',
    `last_build_info`      longtext comment '最后构建信息',
    `description`          varchar(2000) comment '描述',
    `latest_version`       varchar(255) comment '当前历史记录表最新版本',
    `latest_history_id`    int comment '当前历史记录表id',
    `material_history_id`  int comment '关联物料包历史id',
    `image_url`            varchar(255) comment '设计器截图地址',
    `sort_plugins`         longtext comment '插件集合',
    `sort_toolbar`         longtext comment '工具集合',
    `is_default`           tinyint(1) comment '是否默认编辑器：1是，0否',
    `prettier_opts`        longtext comment '设计预留字段',
    `set_default_by`       varchar(60) comment '设置默认编辑器的人',
    `app_extend_config`    longtext comment '应用扩展配置',
    `data_hash`            varchar(255) comment '设计器数据hash，验证数据一致性',
    `business_category_id` int comment '业务类型',
    `theme_id`             int comment '生态扩展使用，关联主题',
    `platform_url`         varchar(255) comment '设计器静态资源托管地址url',
    `vscode_url`           varchar(255) comment '设计预留字段',
    `tenant_id`            varchar(60)  not null comment '租户id',
    `renter_id`            varchar(60) comment '业务租户id',
    `site_id`              varchar(60) comment '站点id，设计预留字段',
    `created_by`           varchar(60)  not null comment '创建人',
    `last_updated_by`      varchar(60)  not null comment '最后修改人',
    `created_time`         timestamp    not null default current_timestamp comment '创建时间',
    `last_updated_time`    timestamp    not null default current_timestamp comment '更新时间',
    primary key (`id`) using btree,
    unique index `u_idx_platform` (`tenant_id`, `name`) using btree
) engine = innodb comment = '设计器表';

drop table if exists `t_platform_history`;

create table `t_platform_history`
(
    `id`                  int          not null auto_increment comment '主键id',
    `ref_id`              int          not null comment '关联主表id',
    `version`             varchar(255) not null comment '版本',
    `name`                varchar(255) not null comment '名称',
    `publish_url`         varchar(255) not null comment '设计器静态资源托管地址',
    `description`         varchar(2000) comment '描述',
    `vscode_url`          varchar(255) comment '设计预留字段',
    `material_history_id` int          not null comment '关联物料包历史id',
    `sub_count`           int          not null comment '设计预留字段',
    `material_pkg_name`   varchar(255) comment '物料包名称',
    `material_version`    varchar(255) comment '物料包版本',
    `image_url`           varchar(255) comment '封面图地址',
    `tenant_id`           varchar(60)  not null comment '租户id',
    `renter_id`           varchar(60) comment '业务租户id',
    `site_id`             varchar(60) comment '站点id，设计预留字段',
    `created_by`          varchar(60)  not null comment '创建人',
    `last_updated_by`     varchar(60)  not null comment '最后修改人',
    `created_time`        timestamp    not null default current_timestamp comment '创建时间',
    `last_updated_time`   timestamp    not null default current_timestamp comment '更新时间',
    primary key (`id`) using btree,
    unique index `u_idx_platform_history` (`ref_id`, `version`) using btree
) engine = innodb comment = '设计器历史表';

drop table if exists `t_app`;

create table `t_app`
(
    `id`                  int          not null auto_increment comment '主键id',
    `name`                varchar(255) not null comment '应用名称',
    `platform_id`         int          not null comment '关联设计器id',
    `platform_history_id` int          not null comment '关联设计器历史版本id',
    `publish_url`         varchar(255) comment '应用静态资源托管地址',
    `editor_url`          varchar(255) comment '设计器地址',
    `visit_url`           varchar(255) comment '访问地址',
    `image_url`           varchar(255) comment '封面图地址',
    `assets_url`          varchar(255) comment '应用资源地址',
    `state`               int comment '应用状态，1可用，0不可用',
    `published`           tinyint(1) comment '是否发布：1是，0否',
    `home_page_id`        int comment '主页id，关联page表的id',
    `app_website`         varchar(255) comment '设计预留字段',
    `css`                 longtext comment '设计预留字段',
    `config`              longtext comment '设计预留字段',
    `constants`           longtext comment '设计预留字段',
    `data_handler`        longtext comment '数据源的拦截器',
    `latest`              varchar(255) comment '应用最新历史记录id',
    `git_group`           varchar(255) comment 'git仓库分组',
    `project_name`        varchar(255) comment 'git仓库名称',
    `branch`              varchar(255) comment '默认提交分支',
    `is_demo`             tinyint(1) comment '是否是demo应用',
    `is_default`          tinyint(1) comment '是否是默认应用',
    `template_type`       varchar(255) comment '应用模板类型',
    `set_template_time`   timestamp comment '设置模板时间',
    `description`         varchar(2000) comment '描述',
    `set_template_by`     varchar(60) comment '设置模板人id',
    `set_default_by`      varchar(60) comment '设置为默认应用人id',
    `framework`           varchar(255) comment '应用框架',
    `global_state`        longtext comment '应用全局状态',
    `default_lang`        varchar(255) comment '默认语言',
    `extend_config`       longtext comment '应用扩展config',
    `data_hash`           varchar(255) comment '应用内容哈希值',
    `can_associate`       tinyint(1) comment '设计预留字段',
    `data_source_global`  longtext comment '数据源全局配置',
    `tenant_id`           varchar(60)  not null comment '租户id',
    `renter_id`           varchar(60) comment '业务租户id',
    `site_id`             varchar(60) comment '站点id，设计预留字段',
    `created_by`          varchar(60)  not null comment '创建人',
    `last_updated_by`     varchar(60)  not null comment '最后修改人',
    `created_time`        timestamp    not null default current_timestamp comment '创建时间',
    `last_updated_time`   timestamp    not null default current_timestamp comment '更新时间',
    primary key (`id`) using btree,
    unique index `u_idx_app` (`tenant_id`, `platform_id`, `name`) using btree
) engine = innodb comment = '应用表';

drop table if exists `t_app_extension`;

create table `t_app_extension`
(
    `id`                int          not null auto_increment comment '主键id',
    `name`              varchar(255) not null comment '名称',
    `type`              varchar(255) not null comment '类型：npm, function',
    `content`           longtext     not null comment '内容',
    `app_id`            int          not null comment '关联appid',
    `category`          varchar(255) not null comment '分类：utils,bridge',
    `tenant_id`         varchar(60)  not null comment '租户id',
    `renter_id`         varchar(60) comment '业务租户id',
    `site_id`           varchar(60) comment '站点id，设计预留字段',
    `created_by`        varchar(60)  not null comment '创建人',
    `last_updated_by`   varchar(60)  not null comment '最后修改人',
    `created_time`      timestamp    not null default current_timestamp comment '创建时间',
    `last_updated_time` timestamp    not null default current_timestamp comment '更新时间',
    primary key (`id`) using btree,
    unique index `u_idx_app_extension` (`app_id`, `name`) using btree
) engine = innodb comment = '应用扩展表';

drop table if exists `t_block_group`;

create table `t_block_group`
(
    `id`                int          not null auto_increment comment '主键id',
    `name`              varchar(255) not null comment '分组名称',
    `description`       varchar(2000) comment '描述',
    `app_id`            int          not null comment '关联app id',
    `platform_id`       int          not null comment '设计器id',
    `tenant_id`         varchar(60)  not null comment '租户id',
    `renter_id`         varchar(60) comment '业务租户id',
    `site_id`           varchar(60) comment '站点id，设计预留字段',
    `created_by`        varchar(60)  not null comment '创建人',
    `created_time`      timestamp    not null default current_timestamp comment '创建时间',
    `last_updated_by`   varchar(60)  not null comment '最后修改人',
    `last_updated_time` timestamp    not null default current_timestamp comment '更新时间',
    primary key (`id`) using btree,
    unique index `u_idx_block_group` (`tenant_id`, `platform_id`, `name`) using btree
) engine = innodb comment = '区块分组表，设计器内共享';

drop table if exists `t_business_category`;

create table `t_business_category`
(
    `id`                int          not null auto_increment comment '主键id',
    `code`              varchar(255) not null comment '编码',
    `name`              varchar(255) not null comment '名称',
    `tenant_id`         varchar(60)  not null comment '租户id',
    `renter_id`         varchar(60) comment '业务租户id',
    `site_id`           varchar(60) comment '站点id，设计预留字段',
    `created_by`        varchar(60)  not null comment '创建人',
    `created_time`      timestamp    not null default current_timestamp comment '创建时间',
    `last_updated_by`   varchar(60)  not null comment '最后修改人',
    `last_updated_time` timestamp    not null default current_timestamp comment '更新时间',
    primary key (`id`) using btree,
    unique index `u_idx_business_category` (`tenant_id`, `code`) using btree
) engine = innodb comment = '业务类型表，全局';



drop table if exists `t_block`;

create table `t_block`
(
    `id`                int          not null auto_increment comment '主键id',
    `label`             varchar(255) not null comment '区块编码',
    `name`              varchar(255) comment '名称',
    `framework`         varchar(255) comment '技术栈',
    `content`           longtext comment '区块内容',
    `assets`            longtext comment '构建资源',
    `last_build_info`   longtext comment '最新一次构建信息',
    `description`       varchar(2000) comment '描述',
    `tags`              longtext comment '标签',
    `latest_version`    varchar(255) comment '当前历史记录表最新版本',
    `latest_history_id` int comment '当前历史记录表id',
    `screenshot`        longtext comment '截屏',
    `path`              varchar(255) comment '区块路径',
    `occupier_by`       varchar(60) comment '当前检出者id',
    `is_official`       tinyint(1) comment '是否是官方',
    `public`            int comment '公开状态：0，1，2',
    `is_default`        tinyint(1) comment '是否是默认',
    `tiny_reserved`     tinyint(1) comment '是否是tiny自有',
    `npm_name`          varchar(255) comment 'npm包名',
    `i18n`              longtext NULL COMMENT '国际化',
    `platform_id`       int          not null comment '设计器id',
    `app_id`            int          not null comment '创建区块时所在appid',
    `content_blocks`    longtext comment '设计预留字段',
    `tenant_id`         varchar(60)  not null comment '租户id',
    `renter_id`         varchar(60) comment '业务租户id',
    `site_id`           varchar(60) comment '站点id，设计预留字段',
    `created_by`        varchar(60)  not null comment '创建人',
    `created_time`      timestamp    not null default current_timestamp comment '创建时间',
    `last_updated_by`   varchar(60)  not null comment '最后修改人',
    `last_updated_time` timestamp    not null default current_timestamp comment '更新时间',
    primary key (`id`) using btree,
    unique index `u_idx_block` (`platform_id`, `label`, `framework`) using btree
) engine = innodb comment = '区块表';

drop table if exists `t_block_history`;

create table `t_block_history`
(
    `id`                int          not null auto_increment comment '主键id',
    `ref_id`            int          not null comment '关联主表id',
    `version`           varchar(255) not null comment '版本',
    `message`           varchar(255) comment '历史记录描述消息',
    `label`             varchar(255) not null comment '显示标签',
    `name`              varchar(255) comment '名称',
    `framework`         varchar(255) comment '技术栈',
    `content`           longtext comment '区块内容',
    `assets`            longtext comment '构建资源',
    `build_info`        longtext comment '构建信息',
    `screenshot`        longtext comment '截屏',
    `path`              varchar(255) comment '区块路径',
    `description`       varchar(2000) comment '描述',
    `tags`              longtext comment '标签',
    `is_official`       tinyint(1) comment '是否是官方',
    `public`            int comment '公开状态：0，1，2',
    `is_default`        tinyint(1) comment '是否是默认',
    `tiny_reserved`     tinyint(1) comment '是否是tiny自有',
    `mode`              varchar(255) comment '模式：vscode',
    `platform_id`       int          not null comment '设计器id',
    `app_id`            int          not null comment '创建区块时所在appid',
    `npm_name`          varchar(255) comment 'npm包名',
    `i18n`              longtext NULL COMMENT '国际化',
    `content_blocks`    longtext comment '设计预留字段',
    `block_group_id`    int comment '区块分组id',
    `tenant_id`         varchar(60)  not null comment '租户id',
    `renter_id`         varchar(60) comment '业务租户id',
    `site_id`           varchar(60) comment '站点id，设计预留字段',
    `created_by`        varchar(60)  not null comment '创建人',
    `created_time`      timestamp    not null default current_timestamp comment '创建时间',
    `last_updated_by`   varchar(60)  not null comment '最后修改人',
    `last_updated_time` timestamp    not null default current_timestamp comment '更新时间',
    primary key (`id`) using btree,
    unique index `u_idx_block_history` (`app_id`, `ref_id`, `version`) using btree
) engine = innodb comment = '区块历史表';

drop table if exists `t_material`;

create table `t_material`
(
    `id`                      int          not null auto_increment comment '主键id',
    `name`                    varchar(255) not null comment '名称',
    `npm_name`                varchar(255) comment 'npm包名',
    `framework`               varchar(255) not null comment '技术栈',
    `assets_url`              longtext comment '资源地址',
    `image_url`               varchar(255) comment '封面图地址',
    `published`               tinyint(1) comment '是否发布：1是，0否',
    `latest_version`          varchar(255) comment '当前历史记录表最新版本',
    `latest_history_id`       int comment '当前历史记录表id',
    `public`                  int comment '公开状态：0，1，2',
    `last_build_info`         longtext comment '最新一次构建信息',
    `description`             varchar(2000) comment '描述',
    `is_official`             tinyint(1) comment '是否是官方',
    `is_default`              tinyint(1) comment '是否是默认',
    `tiny_reserved`           tinyint(1) comment '是否是tiny自有',
    `component_library_id`    int comment '设计预留字段',
    `material_category_id`    int comment '物料包业务类型',
    `material_size`           int comment '物料包大小',
    `tgz_url`                 varchar(255) comment '物料包存储地址',
    `unzip_tgz_root_path_url` longtext comment '物料包存储根路径',
    `unzip_tgz_files`         longtext comment '物料包存储文件',
    `tenant_id`               varchar(60)  not null comment '租户id',
    `renter_id`               varchar(60) comment '业务租户id',
    `site_id`                 varchar(60) comment '站点id，设计预留字段',
    `created_by`              varchar(60)  not null comment '创建人',
    `created_time`            timestamp    not null default current_timestamp comment '创建时间',
    `last_updated_by`         varchar(60)  not null comment '最后修改人',
    `last_updated_time`       timestamp    not null default current_timestamp comment '更新时间',
    primary key (`id`) using btree,
    unique index `u_idx_material` (`tenant_id`, `name`, `framework`) using btree
) engine = innodb comment = '物料包表';

drop table if exists `t_material_history`;

create table `t_material_history`
(
    `id`                      int          not null auto_increment comment '主键id',
    `ref_id`                  int          not null comment '关联主表id',
    `version`                 varchar(255) not null comment '版本',
    `content`                 longtext comment '物料内容',
    `name`                    varchar(255) not null comment '名称',
    `npm_name`                varchar(255) comment 'npm包名',
    `framework`               varchar(255) comment '技术栈',
    `assets_url`              longtext comment '资源地址',
    `image_url`               varchar(255) comment '封面图地址',
    `build_info`              longtext comment '构建信息',
    `description`             varchar(2000) comment '描述',
    `material_size`           int comment '物料包大小',
    `tgz_url`                 varchar(255) comment '物料包存储地址',
    `unzip_tgz_root_path_url` longtext comment '物料包存储根路径',
    `unzip_tgz_files`         longtext comment '物料包存储文件',
    `tenant_id`               varchar(60)  not null comment '租户id',
    `renter_id`               varchar(60) comment '业务租户id',
    `site_id`                 varchar(60) comment '站点id，设计预留字段',
    `created_by`              varchar(60)  not null comment '创建人',
    `created_time`            timestamp    not null default current_timestamp comment '创建时间',
    `last_updated_by`         varchar(60)  not null comment '最后修改人',
    `last_updated_time`       timestamp    not null default current_timestamp comment '更新时间',
    primary key (`id`) using btree,
    unique index `u_idx_material_history` (`tenant_id`, `ref_id`, `version`) using btree
) engine = innodb comment = '物料包历史表';

drop table if exists `t_page`;

create table `t_page`
(
    `id`                int          not null auto_increment comment '主键id',
    `name`              varchar(255) not null comment '名称',
    `app_id`            int          not null comment '关联appid',
    `route`             varchar(255) not null comment '访问路径',
    `page_content`      longtext comment '页面内容',
    `is_body`           tinyint(1) comment '根元素是否是body',
    `parent_id`         int          not null comment '父文件夹id',
    `group`             varchar(255) comment '分组',
    `depth`             int comment '页面/文件夹深度，更改层级时服务端校验用（校验可有可无）',
    `is_page`           tinyint(1) not null comment '是否为页面：分为页面和文件夹',
    `occupier_by`       varchar(60) comment '当前检出者id',
    `is_default`        tinyint(1) not null comment '是否是默认页面',
    `content_blocks`    longtext comment '设计预留字段',
    `latest_version`    varchar(255) comment '当前历史记录表最新版本',
    `latest_history_id` int comment '当前历史记录表id',
    `tenant_id`         varchar(60)  not null comment '租户id',
    `renter_id`         varchar(60) comment '业务租户id',
    `site_id`           varchar(60) comment '站点id，设计预留字段',
    `created_by`        varchar(60)  not null comment '创建人',
    `created_time`      timestamp    not null default current_timestamp comment '创建时间',
    `last_updated_by`   varchar(60)  not null comment '最后修改人',
    `last_updated_time` timestamp    not null default current_timestamp comment '更新时间',
    primary key (`id`) using btree,
    unique index `u_idx_page` (`app_id`,`parent_id`,`route`,`is_page`,`tenant_id`, `name`) using btree
) engine = innodb comment = '页面表';

drop table if exists `t_page_history`;

create table `t_page_history`
(
    `id`                int          not null auto_increment comment '主键id',
    `ref_id`            int          not null comment '关联主表id',
    `version`           varchar(255) comment '版本',
    `name`              varchar(255) not null comment '名称',
    `app_id`            int          not null comment '关联appid',
    `route`             varchar(255) not null comment '访问路径',
    `page_content`      longtext comment '页面内容',
    `is_body`           tinyint(1) comment '根元素是否是body',
    `parent_id`         int          not null comment '父文件夹id',
    `group`             varchar(255) comment '分组',
    `depth`             int comment '页面/文件夹深度，更改层级时服务端校验用（校验可有可无）',
    `is_page`           tinyint(1) not null comment '是否为页面：分为页面和文件夹',
    `is_default`        tinyint(1) not null comment '是否是默认页面',
    `message`           varchar(255) comment '历史记录消息描述',
    `is_home`           tinyint(1) not null default 0 comment '是否首页',
    `content_blocks`    longtext comment '设计预留字段',
    `tenant_id`         varchar(60)  not null comment '租户id',
    `renter_id`         varchar(60) comment '业务租户id',
    `site_id`           varchar(60) comment '站点id，设计预留字段',
    `is_published`      tinyint(1) not null comment '是否发布',
    `created_by`        varchar(60)  not null comment '创建人',
    `created_time`      timestamp    not null default current_timestamp comment '创建时间',
    `last_updated_by`   varchar(60)  not null comment '最后修改人',
    `last_updated_time` timestamp    not null default current_timestamp comment '更新时间',
    primary key (`id`) using btree
) engine = innodb comment = '页面历史表';

drop table if exists `t_page_template`;

create table `t_page_template`
(
    `id`                int          not null auto_increment comment '主键id',
    `name`              varchar(255) not null comment '名称',
    `page_content`      longtext comment '模板页面内容，存储页面内容、数据等',
    `framework`         varchar(255) not null comment '技术栈',
    `published`         tinyint(1) comment '是否发布：1是，0否',
    `public`            tinyint(1) comment '公开状态：0，1，2',
    `type`              varchar(255) not null comment '模板类型',
    `status`            varchar(255) not null comment '模板状态',
    `is_preset`         tinyint(1) comment '设计预留字段',
    `image_url`         longtext comment '封面图地址',
    `description`       varchar(2000) comment '描述',
    `platform_id`       int          not null comment '设计器id',
    `tenant_id`         varchar(60)  not null comment '租户id',
    `renter_id`         varchar(60) comment '业务租户id',
    `site_id`           varchar(60) comment '站点id，设计预留字段',
    `created_by`        varchar(60)  not null comment '创建人',
    `created_time`      timestamp    not null default current_timestamp comment '创建时间',
    `last_updated_by`   varchar(60)  not null comment '最后修改人',
    `last_updated_time` timestamp    not null default current_timestamp comment '更新时间',
    primary key (`id`) using btree
) engine = innodb comment = '页面模板表';

drop table if exists `t_component`;

create table `t_component`
(
    `id`                 int          not null auto_increment comment '主键id',
    `version`            varchar(255) not null comment '版本',
    `name`               varchar(255) not null comment '中文名称',
    `name_en`            varchar(255) not null comment '英文名称',
    `icon`               varchar(255) comment '图标',
    `description`        varchar(2000) comment '描述',
    `doc_url`            varchar(255) comment '文档链接',
    `screenshot`         varchar(255) comment '缩略图',
    `tags`               varchar(255) comment '标签',
    `keywords`           varchar(255) comment '关键字',
    `dev_mode`           varchar(255) not null comment '研发模式',
    `npm`                longtext     not null comment 'npm对象属性',
    `group`              varchar(255) comment '分组',
    `category`           varchar(255) comment '分类',
    `priority`           int comment '排序',
    `snippets`           longtext comment 'schema片段',
    `schema_fragment`    longtext comment 'schema片段',
    `configure`          longtext comment '配置信息',
    `public`             int comment '公开状态：0，1，2',
    `framework`          varchar(255) not null comment '技术栈',
    `is_official`        tinyint(1) comment '是否是官方',
    `is_default`         tinyint(1) comment '是否是默认',
    `tiny_reserved`      tinyint(1) comment '是否是tiny自有',
    `component_metadata` longtext comment '属性信息',
    `library_id`         int comment '设计预留字段',
    `tenant_id`          varchar(60)  not null comment '租户id',
    `renter_id`          varchar(60) comment '业务租户id',
    `site_id`            varchar(60) comment '站点id，设计预留字段',
    `created_by`         varchar(60)  not null comment '创建人',
    `created_time`       timestamp    not null default current_timestamp comment '创建时间',
    `last_updated_by`    varchar(60)  not null comment '最后修改人',
    `last_updated_time`  timestamp    not null default current_timestamp comment '更新时间',
    primary key (`id`) using btree,
    unique index `u_idx_component` (`tenant_id`, `name_en`, `version`) using btree
) engine = innodb comment = '组件表';

drop table if exists `r_material_category`;

create table `r_material_category`
(
    `id`          int not null auto_increment comment '主键id',
    `material_id` int not null comment '物料包id',
    `category_id` int not null comment '业务分类id',
    primary key (`id`) using btree
) engine = innodb comment = '物料包业务分类关系表';

drop table if exists `r_material_history_block`;

create table `r_material_history_block`
(
    `id`                  int not null auto_increment comment '主键id',
    `material_history_id` int not null comment '物料包历史id',
    `block_history_id`    int not null comment '区块历史id',
    primary key (`id`) using btree
) engine = innodb comment = '物料包历史区块关系表';

drop table if exists `r_material_history_component`;

create table `r_material_history_component`
(
    `id`                  int not null auto_increment comment '主键id',
    `material_history_id` int not null comment '物料包历史id',
    `component_id`        int not null comment '组件id',
    primary key (`id`) using btree
) engine = innodb comment = '物料包历史组件关系表';

drop table if exists `r_material_component`;

create table `r_material_component`
(
    `id`           int not null auto_increment comment '主键id',
    `material_id`  int not null comment '物料包id',
    `component_id` int not null comment '组件id',
    primary key (`id`) using btree
) engine = innodb comment = '物料包组件编辑态关系表';

drop table if exists `r_material_block`;

create table `r_material_block`
(
    `id`          int not null auto_increment comment '主键id',
    `material_id` int not null comment '物料包id',
    `block_id`    int not null comment '区块id',
    primary key (`id`) using btree
) engine = innodb comment = '物料包区块编辑态关系表';

drop table if exists `t_i18n_entry`;

create table `t_i18n_entry`
(
    `id`                int           not null auto_increment comment '主键id',
    `key`               varchar(255)  not null comment '国际化词条key',
    `content`           varchar(3000) not null comment '词条内容',
    `host_id`           int           not null comment '关联的hostid： appid或blockid',
    `host_type`         varchar(255)  not null comment 'app或者block',
    `lang_id`           int comment '关联语言id',
    `tenant_id`         varchar(60)   not null comment '租户id',
    `renter_id`         varchar(60) comment '业务租户id',
    `site_id`           varchar(60) comment '站点id，设计预留字段',
    `created_by`        varchar(60)   not null comment '创建人',
    `created_time`      timestamp     not null default current_timestamp comment '创建时间',
    `last_updated_by`   varchar(60)   not null comment '最后修改人',
    `last_updated_time` timestamp     not null default current_timestamp comment '更新时间',
    primary key (`id`) using btree,
    unique index `u_idx_i18n_entity` (`key`, `host_id`, `host_type`,`lang_id`) using btree
) engine = innodb comment = '国际化语言配置表';

drop table if exists `t_i18n_lang`;

create table `t_i18n_lang`
(
    `id`                int          not null auto_increment comment '主键id',
    `lang`              varchar(255) not null comment '语言代码',
    `label`             varchar(255) not null comment '语言',
    `created_by`        varchar(60)  not null comment '创建人',
    `created_time`      timestamp    not null default current_timestamp comment '创建时间',
    `last_updated_by`   varchar(60)  not null comment '最后修改人',
    `last_updated_time` timestamp    not null default current_timestamp comment '更新时间',
    primary key (`id`) using btree,
    unique index `u_idx_18n_lang` (`lang`) using btree
) engine = innodb comment = '国际化语言表，全局';

drop table if exists `t_datasource`;

create table `t_datasource`
(
    `id`                int          not null auto_increment comment '主键id',
    `name`              varchar(255) not null comment '数据源名称',
    `data`              longtext comment '数据源内容',
    `tpl`               int comment '设计预留字段',
    `app_id`            int comment '关联appId',
    `platform_id`       int comment '关联设计器id',
    `description`       varchar(2000) comment '描述',
    `tenant_id`         varchar(60)  not null comment '租户id',
    `renter_id`         varchar(60) comment '业务租户id',
    `site_id`           varchar(60) comment '站点id，设计预留字段',
    `created_by`        varchar(60)  not null comment '创建人',
    `created_time`      timestamp    not null default current_timestamp comment '创建时间',
    `last_updated_by`   varchar(60)  not null comment '最后修改人',
    `last_updated_time` timestamp    not null default current_timestamp comment '更新时间',
    primary key (`id`) using btree,
    unique index `u_idx_datasource` (`tenant_id`, `platform_id`, `name`) using btree
) engine = innodb comment = '数据源表';

drop table if exists `t_task_record`;

create table `t_task_record`
(
    `id`                int         not null auto_increment comment '主键id',
    `team_id`           int comment '团队id, 默认0',
    `task_type`         int comment '任务类型: 1 assets_build / 2 app_build / 3 platform_build / 4 vscode_plugin_build/5 block_build',
    `build_id`          int comment '构建资源id',
    `task_name`         varchar(255) comment '构建任务名称',
    `task_status`       int comment '任务状态：0 init / 1 running / 2 stopped / 3 finished',
    `task_result`       longtext comment '当前执行进度结果信息',
    `progress`          varchar(255) comment '当前进行的子任务名',
    `ratio`             int comment '无用字段',
    `progress_percent`  int comment '构建进度百分比数',
    `indicator`         longtext comment '构建指标',
    `tenant_id`         varchar(60) not null comment '租户id',
    `renter_id`         varchar(60) comment '业务租户id',
    `site_id`           varchar(60) comment '站点id，设计预留字段',
    `created_by`        varchar(60) not null comment '创建人',
    `created_time`      timestamp   not null default current_timestamp comment '创建时间',
    `last_updated_by`   varchar(60) not null comment '最后修改人',
    `last_updated_time` timestamp   not null default current_timestamp comment '更新时间',
    primary key (`id`) using btree
) engine = innodb comment = '构建任务表';

drop table if exists `t_tenant`;

create table `t_tenant`
(
    `id`                int          not null auto_increment comment '主键id',
    `org_code`          varchar(255) comment '组织唯一代码',
    `name_cn`           varchar(255) not null comment '组织中文名',
    `name_en`           varchar(255) comment '组织英文名',
    `description`       varchar(2000) comment '组织描述',
    `created_by`        varchar(60)  not null comment '创建人',
    `created_time`      timestamp    not null default current_timestamp comment '创建时间',
    `last_updated_by`   varchar(60)  not null comment '最后修改人',
    `last_updated_time` timestamp    not null default current_timestamp comment '更新时间',
    primary key (`id`) using btree,
    unique index `u_idx_tenant` (`name_cn`) using btree
) engine = innodb comment = '租户表';

drop table if exists `t_user`;

create table `t_user`
(
    `id`                int          not null auto_increment comment '主键id',
    `username`          varchar(255) not null comment '用户名',
    `email`             varchar(255) not null comment '邮箱',
    `role`              varchar(255) comment '用户角色',
    `enable`            tinyint(1) comment '账号是否可用',
    `is_admin`          tinyint(1) comment '是否管理员',
    `is_public`         tinyint(1) comment '是否公共账号',
    `tenant_id`         varchar(60)  not null comment '租户id',
    `renter_id`         varchar(60) comment '业务租户id',
    `site_id`           varchar(60) comment '站点id，设计预留字段',
    `created_by`        varchar(60)  not null comment '创建人',
    `created_time`      timestamp    not null default current_timestamp comment '创建时间',
    `last_updated_by`   varchar(60)  not null comment '最后修改人',
    `last_updated_time` timestamp    not null default current_timestamp comment '更新时间',
    primary key (`id`) using btree,
    unique index `u_idx_user` (`username`) using btree
) engine = innodb comment = '用户表';

drop table if exists `t_block_carriers_relation`;

create table `t_block_carriers_relation`
(
    `id`                int         not null auto_increment comment '主键id',
    `block_id`          int         not null comment '区块id',
    `host_id`              int         not null comment '类型id',
    `host_type`         varchar(60) comment '类型：blockGroup,materialHistory',
    `version`           varchar(60) not null comment '区块当前使用版本',
    `tenant_id`         varchar(60) not null comment '租户id',
    `renter_id`         varchar(60) comment '业务租户id',
    `site_id`           varchar(60) comment '站点id，设计预留字段',
    `created_by`        varchar(60) not null comment '创建人',
    `created_time`      timestamp   not null default current_timestamp comment '创建时间',
    `last_updated_by`   varchar(60) not null comment '最后修改人',
    `last_updated_time` timestamp   not null default current_timestamp comment '更新时间',
    primary key (`id`) using btree,
    unique index `u_idx_block_carriers_relation` (`host_id`, `host_type`, `block_id`) using btree
) engine = innodb comment = '区块分组与区块历史版本';

drop table if exists `r_block_group_block`;

create table `r_block_group_block`
(
    `id`             int not null auto_increment comment '主键id',
    `block_id`       int not null comment '区块id',
    `block_group_id` int not null comment '区块分组id',
    primary key (`id`) using btree,
    unique index `u_idx_block_group_block` (block_id, block_group_id) using btree
) engine = innodb comment = '区块分组和区块关系表';
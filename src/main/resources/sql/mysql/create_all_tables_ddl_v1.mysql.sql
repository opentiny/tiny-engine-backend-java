drop table
  if exists `t_platform`;

create table
  `t_platform` (
    `id` int not null auto_increment COMMENT '主键id',
    `name` varchar(255) not null COMMENT '名称',
    `published` tinyint(1) COMMENT '是否发布，1是，0否',
    `last_build_info` longtext COMMENT '最新构建信息',
    `description` varchar(2000) COMMENT '描述',
    `latest_version` varchar(255) COMMENT '当前历史记录表最新版本',
    `latest_history_id` int COMMENT '当前历史记录表ID',
    `material_history_id` int COMMENT '关联物料包历史ID',
    `image_url` varchar(255) COMMENT '封面图地址',
    `sort_plugins` longtext COMMENT '插件集合',
    `sort_toolbar` longtext COMMENT '工具集合',
    `is_default` tinyint(1) COMMENT '是否是默认编辑器，1是，0否',
    `prettier_opts` longtext COMMENT '设计预留字段',
    `set_default_by` varchar(60) COMMENT '设置为默认编辑器的人',
    `app_extend_config` longtext COMMENT '应用扩展配置',
    `data_hash` varchar(255) COMMENT '设计器数据HASH，验证数据一致性',
    `business_category_id` int COMMENT '业务类型',
    `theme_id` int COMMENT '生态扩展使用，关联主题',
    `platform_url` varchar(255) COMMENT '设计预留字段',
    `vscode_url` varchar(255) COMMENT '设计预留字段',
    `tenant_id` varchar(60) not null COMMENT '租户ID',
    `site_id` varchar(60) COMMENT '站点ID，设计预留字段',
    `created_by` varchar(60) not null COMMENT '创建人',
    `last_updated_by` varchar(60) not null COMMENT '最后修改人',
    `created_time` timestamp not null default CURRENT_TIMESTAMP COMMENT '创建时间',
    `last_updated_time` timestamp not null default CURRENT_TIMESTAMP COMMENT '更新时间',
    primary key (`id`) using BTREE,
    unique index `u_idx_platform` (`tenant_id`, `name`) using BTREE
  ) engine = InnoDB COMMENT = '设计器表';

drop table
  if exists `t_platform_history`;

create table
  `t_platform_history` (
    `id` int not null auto_increment COMMENT '主键id',
    `ref_id` int not null COMMENT '关联主表的ID',
    `version` varchar(255) not null COMMENT '版本',
    `name` varchar(255) not null COMMENT '名称',
    `publish_url` varchar(255) not null COMMENT '设计器静态资源托管地址',
    `description` varchar(2000) COMMENT '描述',
    `vscode_url` varchar(255) COMMENT '设计预留字段',
    `material_history_id` int COMMENT '关联物料包历史ID',
    `sub_count` int COMMENT '设计预留字段',
    `material_pkg_name` varchar(255) COMMENT '物料包名称',
    `material_version` varchar(255) COMMENT '物料包版本',
    `image_url` varchar(255) COMMENT '封面图地址',
    `tenant_id` varchar(60) not null COMMENT '租户ID',
    `site_id` varchar(60) COMMENT '站点ID，设计预留字段',
    `created_by` varchar(60) not null COMMENT '创建人',
    `last_updated_by` varchar(60) not null COMMENT '最后修改人',
    `created_time` timestamp not null default CURRENT_TIMESTAMP COMMENT '创建时间',
    `last_updated_time` timestamp not null default CURRENT_TIMESTAMP COMMENT '更新时间',
    primary key (`id`) using BTREE,
    unique index `u_idx_platform_history` (`ref_id`, `version`) using BTREE
  ) engine = InnoDB COMMENT = '设计器历史表';

DROP TABLE
  IF EXISTS `t_app`;

CREATE TABLE
  `t_app` (
    `id` int not null AUTO_INCREMENT COMMENT '主键id',
    `name` varchar(255) not null COMMENT '应用名称',
    `platform_id` int not null COMMENT '关联设计器id',
    `platform_history_id` int not null COMMENT '关联设计器历史版本id',
    `publish_url` varchar(255) COMMENT '应用静态资源托管地址',
    `editor_url` varchar(255) COMMENT '设计器地址',
    `visit_url` varchar(255) COMMENT '访问地址',
    `image_url` varchar(255) COMMENT '封面图地址',
    `assets_url` varchar(255) COMMENT '应用资源地址',
    `state` int COMMENT '应用状态，1可用，0不可用',
    `published` tinyint(1) COMMENT '是否发布，1是，0否',
    `home_page_id` int COMMENT '主页id，关联page表的id',
    `app_website` varchar(255) COMMENT '设计预留字段',
    `css` longtext COMMENT '设计预留字段',
    `config` longtext COMMENT '设计预留字段',
    `constants` longtext COMMENT '设计预留字段',
    `data_handler` longtext COMMENT '数据源的拦截器',
    `latest` varchar(255) COMMENT '应用最新历史记录id',
    `git_group` varchar(255) COMMENT 'git仓库分组',
    `project_name` varchar(255) COMMENT 'git仓库名称',
    `branch` varchar(255) COMMENT '默认提交分支',
    `is_demo` tinyint(1) COMMENT '是否是demo应用',
    `is_default` tinyint(1) COMMENT '是否是默认应用',
    `template_type` varchar(255) COMMENT '应用模板类型',
    `set_template_time` timestamp COMMENT '设置模板时间',
    `obs_url` varchar(255) COMMENT '上传至obs的地址',
    `description` varchar(2000) COMMENT '描述',
    `set_template_by` varchar(60) COMMENT '设置模板人id',
    `set_default_by` varchar(60) COMMENT '设置为默认应用人id',
    `framework` varchar(255) COMMENT '应用框架',
    `global_state` longtext COMMENT '应用全局状态',
    `default_lang` varchar(255) COMMENT '默认语言',
    `extend_config` longtext COMMENT '应用扩展config',
    `data_hash` varchar(255) COMMENT '应用内容哈希值',
    `can_associate` tinyint(1) COMMENT '设计预留字段',
    `data_source_global` longtext COMMENT '数据源全局配置',
    `tenant_id` varchar(60) not null COMMENT '租户ID',
    `site_id` varchar(60) COMMENT '站点ID，设计预留字段',
    `created_by` varchar(60) not null COMMENT '创建人',
    `last_updated_by` varchar(60) not null COMMENT '最后修改人',
    `created_time` timestamp not null default CURRENT_TIMESTAMP COMMENT '创建时间',
    `last_updated_time` timestamp not null default CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `u_idx_app` (`tenant_id`, `platform_id`, `name`) USING BTREE
  ) ENGINE = InnoDB COMMENT = '应用表';

DROP TABLE
  IF EXISTS `t_app_extension`;

CREATE TABLE
  `t_app_extension` (
    `id` int not null AUTO_INCREMENT COMMENT '主键id',
    `name` varchar(255) not null COMMENT '名称',
    `type` varchar(255) not null COMMENT '类型：npm, function',
    `content` longtext not null COMMENT '内容',
    `app_id` int not null COMMENT '关联appId',
    `category` varchar(255) not null COMMENT '分类：utils,bridge',
    `tenant_id` varchar(60) not null COMMENT '租户ID',
    `site_id` varchar(60) COMMENT '站点ID，设计预留字段',
    `created_by` varchar(60) not null COMMENT '创建人',
    `last_updated_by` varchar(60) not null COMMENT '最后修改人',
    `created_time` timestamp not null default CURRENT_TIMESTAMP COMMENT '创建时间',
    `last_updated_time` timestamp not null default CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `u_idx_app_extension` (`app_id`, `name`) USING BTREE
  ) ENGINE = InnoDB COMMENT = '应用扩展表';

DROP TABLE
  IF EXISTS `t_business_category`;

CREATE TABLE
  `t_business_category` (
    `id` int not null AUTO_INCREMENT COMMENT '主键id',
    `code` varchar(255) not null COMMENT '编码',
    `name` varchar(255) not null COMMENT '名称',
    `tenant_id` varchar(60) not null COMMENT '租户ID',
    `site_id` varchar(60) COMMENT '站点ID，设计预留字段',
    `created_by` varchar(60) not null COMMENT '创建人',
    `created_time` timestamp not null default CURRENT_TIMESTAMP COMMENT '创建时间',
    `last_updated_by` varchar(60) not null COMMENT '最后修改人',
    `last_updated_time` timestamp not null default CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `app_category_id` (`tenant_id`, `code`) USING BTREE
  ) ENGINE = InnoDB COMMENT = '业务类型表，全局';

DROP TABLE
  IF EXISTS `t_block_group`;

CREATE TABLE
  `t_block_group` (
    `id` int not null AUTO_INCREMENT COMMENT '主键id',
    `name` varchar(255) not null COMMENT '分组名称',
    `description` varchar(2000) COMMENT '描述',
    `app_id` int not null COMMENT '关联app id',
    `platform_id` int not null COMMENT '设计器id',
    `tenant_id` varchar(60) not null COMMENT '租户ID',
    `site_id` varchar(60) COMMENT '站点ID，设计预留字段',
    `created_by` varchar(60) not null COMMENT '创建人',
    `created_time` timestamp not null default CURRENT_TIMESTAMP COMMENT '创建时间',
    `last_updated_by` varchar(60) not null COMMENT '最后修改人',
    `last_updated_time` timestamp not null default CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `u_idx_block_group` (`tenant_id`, `platform_id`, `name`) USING BTREE
  ) ENGINE = InnoDB COMMENT = '区块分组表，设计器内共享';

DROP TABLE
  IF EXISTS `t_block`;

CREATE TABLE
  `t_block` (
    `id` int not null AUTO_INCREMENT COMMENT '主键id',
    `label` varchar(255) not null COMMENT '区块编码',
    `name` varchar(255) COMMENT '技术栈',
    `framework` varchar(255) COMMENT '技术栈',
    `content` longtext COMMENT '区块内容',
    `assets` longtext COMMENT '构建资源',
    `last_build_info` longtext COMMENT '最新一次构建信息',
    `description` varchar(2000) COMMENT '描述',
    `tags` longtext COMMENT '标签',
    `latest_version` varchar(255) COMMENT '当前历史记录表最新版本',
    `latest_history_id` int COMMENT '当前历史记录表ID',
    `screenshot` longtext COMMENT '截屏',
    `path` varchar(255) COMMENT '区块路径',
    `occupier_by` int COMMENT '当前锁定人id',
    `is_official` tinyint(1) COMMENT '是否是官方',
    `public` int COMMENT '公开状态：0，1，2',
    `is_default` tinyint(1) COMMENT '是否是默认',
    `tiny_reserved` tinyint(1) COMMENT '是否是tiny自有',
    `npm_name` varchar(255) COMMENT 'npm包名',
    `platform_id` int COMMENT '设计器ID',
    `app_id` int COMMENT '创建区块时所在appId',
    `content_blocks` longtext COMMENT '设计预留字段',
    `block_group_id` int COMMENT '区块分组id',
    `tenant_id` varchar(60) not null COMMENT '租户ID',
    `site_id` varchar(60) COMMENT '站点ID，设计预留字段',
    `created_by` varchar(60) not null COMMENT '创建人',
    `created_time` timestamp not null default CURRENT_TIMESTAMP COMMENT '创建时间',
    `last_updated_by` varchar(60) not null COMMENT '最后修改人',
    `last_updated_time` timestamp not null default CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `u_idx_block` (`platform_id`, `label`, `framework`) USING BTREE
  ) ENGINE = InnoDB COMMENT = '区块表';

DROP TABLE
  IF EXISTS `t_block_history`;
CREATE TABLE
  `t_block_history` (
    `id` int not null AUTO_INCREMENT COMMENT '主键id',
        `ref_id` int not null COMMENT '关联主表的ID',
        `version` varchar(255) not null COMMENT '版本',
        `message` varchar(255) COMMENT '历史记录描述消息',
        `label` varchar(255) not null COMMENT '显示标签',
        `name` varchar(255) COMMENT '名称',
        `framework` varchar(255) COMMENT '技术栈',
        `content` longtext COMMENT '区块内容',
        `assets` longtext COMMENT '构建资源',
        `build_info` longtext COMMENT '构建信息',
        `description` varchar(2000) COMMENT '描述',
        `tags` longtext COMMENT '标签',
        `screenshot` longtext COMMENT '截屏',
        `path` varchar(255) COMMENT '区块路径',
        `is_official` tinyint(1) COMMENT '是否是官方',
        `public` int COMMENT '公开状态：0，1，2',
        `is_default` tinyint(1) COMMENT '是否是默认',
        `tiny_reserved` tinyint(1) COMMENT '是否是tiny自有',
        `npm_name` varchar(255) COMMENT 'npm包名',
        `platform_id` int COMMENT '设计器ID',
        `app_id` int COMMENT '创建区块时所在appId',
        `content_blocks` longtext COMMENT '设计预留字段',
        `mode` varchar(255) COMMENT '模式：vscode',
        `block_group_id` int COMMENT '区块分组id',
        `tenant_id` varchar(60) not null COMMENT '租户ID',
        `site_id` varchar(60) COMMENT '站点ID，设计预留字段',
        `created_by` varchar(60) not null COMMENT '创建人',
        `created_time` timestamp not null default CURRENT_TIMESTAMP COMMENT '创建时间',
        `last_updated_by` varchar(60) not null COMMENT '最后修改人',
        `last_updated_time` timestamp not null default CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `u_idx_block_history` (`app_id`, `ref_id`, `version`) USING BTREE
  ) ENGINE = InnoDB COMMENT = '区块历史表';

DROP TABLE
  IF EXISTS `t_material`;

CREATE TABLE
  `t_material` (
    `id` int not null AUTO_INCREMENT COMMENT '主键id',
    `name` varchar(255) not null COMMENT '名称',
    `npm_name` varchar(255) COMMENT 'npm包名',
    `framework` varchar(255) COMMENT '技术栈',
    `assets_url` longtext COMMENT '资源地址',
    `image_url` varchar(255) COMMENT '封面图地址',
    `published` tinyint(1) COMMENT '是否发布，1是，0否',
    `latest_version` varchar(255) COMMENT '当前历史记录表最新版本',
    `latest_history_id` int COMMENT '当前历史记录表ID',
    `public` int COMMENT '公开状态：0，1，2',
    `last_build_info` longtext COMMENT '最新一次构建信息',
    `description` varchar(2000) COMMENT '描述',
    `is_official` tinyint(1) COMMENT '是否是官方',
    `is_default` tinyint(1) COMMENT '是否是默认',
    `tiny_reserved` tinyint(1) COMMENT '是否是tiny自有',
    `component_library_id` int COMMENT '设计预留字段',
    `material_category_id` int COMMENT '物料包业务类型',
    `material_size` varchar(255) COMMENT '物料包大小',
    `tgz_url` varchar(255) COMMENT '物料包存储地址',
    `unzip_tgz_root_path_url` longtext COMMENT '物料包存储根路径',
    `unzip_tgz_files` longtext COMMENT '物料包存储文件',
    `tenant_id` varchar(60) not null COMMENT '租户ID',
    `site_id` varchar(60) COMMENT '站点ID，设计预留字段',
    `created_by` varchar(60) not null COMMENT '创建人',
    `created_time` timestamp not null default CURRENT_TIMESTAMP COMMENT '创建时间',
    `last_updated_by` varchar(60) not null COMMENT '最后修改人',
    `last_updated_time` timestamp not null default CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `u_idx_material` (`tenant_id`, `name`, `framework`) USING BTREE
  ) ENGINE = InnoDB COMMENT = '物料包表';

DROP TABLE
  IF EXISTS `material_histories`;

CREATE TABLE
  `material_histories` (
    `id` int not null AUTO_INCREMENT COMMENT '主键id',
    `ref_id` int not null COMMENT '关联主表的ID',
    `version` varchar(255) not null COMMENT '版本',
    `name` varchar(255) not null COMMENT '名称',
    `npm_name` varchar(255) COMMENT 'npm包名',
    `framework` varchar(255) COMMENT '技术栈',
    `assets_url` longtext COMMENT '资源地址',
    `image_url` varchar(255) COMMENT '封面图地址',
    `public` int COMMENT '公开状态：0，1，2',
    `build_info` longtext COMMENT '构建信息',
    `description` varchar(2000) COMMENT '描述',
    `is_official` tinyint(1) COMMENT '是否是官方',
    `is_default` tinyint(1) COMMENT '是否是默认',
    `tiny_reserved` tinyint(1) COMMENT '是否是tiny自有',
    `app_id` int COMMENT '创建区块时所在appId',
    `tenant_id` varchar(60) not null COMMENT '租户ID',
    `site_id` varchar(60) COMMENT '站点ID，设计预留字段',
    `created_by` varchar(60) not null COMMENT '创建人',
    `created_time` timestamp not null default CURRENT_TIMESTAMP COMMENT '创建时间',
    `last_updated_by` varchar(60) not null COMMENT '最后修改人',
    `last_updated_time` timestamp not null default CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `u_idx_material_history` (`tenant_id`, `ref_id`, `version`) USING BTREE
  ) ENGINE = InnoDB COMMENT = '物料包历史表';

DROP TABLE
  IF EXISTS `t_page`;

CREATE TABLE
  `t_page` (
    `id` int not null AUTO_INCREMENT COMMENT '主键id',
    `name` varchar(255) not null COMMENT '名称',
    `app_id` int not null COMMENT '关联appId',
    `route` varchar(255) not null COMMENT '访问路径',
    `page_content` longtext COMMENT '页面内容',
    `is_body` tinyint(1) COMMENT '根元素是否是body',
    `parent_id` int not null COMMENT '父文件夹id',
    `group` varchar(255) COMMENT '分组',
    `depth` int COMMENT '*页面/文件夹深度，更改层级时服务端校验用（校验可有可无）*',
    `is_page` tinyint(1) not null COMMENT '是否为页面：分为页面和文件夹',
    `occupier_by` varchar(60) COMMENT '当前锁定人id',
    `is_default` tinyint(1) not null COMMENT '是否是默认页面',
    `content_blocks` longtext COMMENT '设计预留字段',
    `latest_version` varchar(255) COMMENT '当前历史记录表最新版本',
    `latest_history_id` int COMMENT '当前历史记录表ID',
    `tenant_id` varchar(60) not null COMMENT '租户ID',
    `site_id` varchar(60) COMMENT '站点ID，设计预留字段',
    `created_by` varchar(60) not null COMMENT '创建人',
    `created_time` timestamp not null default CURRENT_TIMESTAMP COMMENT '创建时间',
    `last_updated_by` varchar(60) not null COMMENT '最后修改人',
    `last_updated_time` timestamp not null default CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `u_idx_page` (`app_id`, `name`) USING BTREE
  ) ENGINE = InnoDB COMMENT = '页面表';

DROP TABLE
  IF EXISTS `t_pages_history`;

CREATE TABLE
  `t_pages_history` (
    `id` int not null AUTO_INCREMENT COMMENT '主键id',
    `ref_id` int not null COMMENT '关联主表id',
    `name` varchar(255) not null COMMENT '名称',
    `app_id` int not null COMMENT '关联appId',
    `version` varchar(255) not null COMMENT '版本',
    `route` varchar(255) not null COMMENT '访问路径',
    `page_content` longtext COMMENT '页面内容',
    `is_body` tinyint(1) COMMENT '根元素是否是body',
    `parent_id` int not null COMMENT '父文件夹id',
    `group` varchar(255) COMMENT '分组',
    `depth` int COMMENT '*页面/文件夹深度，更改层级时服务端校验用（校验可有可无）*',
    `is_page` tinyint(1) not null COMMENT '是否为页面：分为页面和文件夹',
    `is_default` tinyint(1) not null COMMENT '是否是默认页面',
    `message` varchar(255) not null COMMENT '历史记录消息描述',
    `is_home` tinyint(1) not null COMMENT '是否首页',
    `content_blocks` longtext COMMENT '设计预留字段',
    `tenant_id` varchar(60) not null COMMENT '租户ID',
    `site_id` varchar(60) COMMENT '站点ID，设计预留字段',
    `created_by` varchar(60) not null COMMENT '创建人',
    `created_time` timestamp not null default CURRENT_TIMESTAMP COMMENT '创建时间',
    `last_updated_by` varchar(60) not null COMMENT '最后修改人',
    `last_updated_time` timestamp not null default CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `u_idx_page_history` (`app_id`, `ref_id`, `version`) USING BTREE
  ) ENGINE = InnoDB COMMENT = '页面历史表';

DROP TABLE
  IF EXISTS `t_page_template`;

CREATE TABLE
  `t_page_template` (
    `id` int not null AUTO_INCREMENT COMMENT '主键id',
    `name` varchar(255) not null COMMENT '名称',
    `page_content` longtext COMMENT '模板页面内容，存储页面内容、数据等',
    `framework` varchar(255) not null COMMENT '技术栈',
    `published` tinyint(1) COMMENT '是否发布，1是，0否',
    `public` int COMMENT '公开状态：0，1，2',
    `type` varchar(255) not null COMMENT '模板类型',
    `status` varchar(255) not null COMMENT '模板状态',
    `is_preset` tinyint(1) COMMENT '设计预留字段',
    `image_url` varchar(255) COMMENT '封面图地址',
    `description` varchar(2000) COMMENT '描述',
    `platform_id` int not null COMMENT '设计器id',
    `tenant_id` varchar(60) not null COMMENT '租户ID',
    `site_id` varchar(60) COMMENT '站点ID，设计预留字段',
    `created_by` varchar(60) not null COMMENT '创建人',
    `created_time` timestamp not null default CURRENT_TIMESTAMP COMMENT '创建时间',
    `last_updated_by` varchar(60) not null COMMENT '最后修改人',
    `last_updated_time` timestamp not null default CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
  ) ENGINE = InnoDB COMMENT = '页面模板表';

DROP TABLE
  IF EXISTS `t_component`;

CREATE TABLE
  `t_component` (
    `id` int not null AUTO_INCREMENT COMMENT '主键id',
    `version` varchar(255) not null COMMENT '版本',
    `name` varchar(255) not null COMMENT '中文名称',
    `name_en` longtext not null COMMENT '英文名称',
    `icon` varchar(255) COMMENT '图标',
    `description` varchar(2000) COMMENT '描述',
    `doc_url` varchar(255) COMMENT '文档链接',
    `screenshot` varchar(255) COMMENT '缩略图',
    `tags` varchar(255) COMMENT '标签',
    `keywords` varchar(255) COMMENT '关键字',
    `dev_mode` varchar(255) not null COMMENT '研发模式',
    `npm` longtext not null COMMENT 'npm对象属性',
    `group` varchar(255) COMMENT '分组',
    `category` varchar(255) COMMENT '分类',
    `priority` int COMMENT '排序',
    `snippets` longtext COMMENT 'schema片段',
    `schema_fragment` longtext COMMENT 'schema片段',
    `configure` longtext COMMENT '配置信息',
    `public` int COMMENT '公开状态：0，1，2',
    `framework` varchar(255) not null COMMENT '技术栈',
    `is_official` tinyint(1) COMMENT '是否是官方',
    `is_default` tinyint(1) COMMENT '是否是默认',
    `tiny_reserved` tinyint(1) COMMENT '是否是tiny自有',
    `component_metadata` longtext COMMENT '属性信息',
    `library_id` int COMMENT '设计预留字段',
    `tenant_id` varchar(60) not null COMMENT '租户ID',
    `site_id` varchar(60) COMMENT '站点ID，设计预留字段',
    `created_by` varchar(60) not null COMMENT '创建人',
    `created_time` timestamp not null default CURRENT_TIMESTAMP COMMENT '创建时间',
    `last_updated_by` varchar(60) not null COMMENT '最后修改人',
    `last_updated_time` timestamp not null default CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `u_idx_component` (`tenant_id`, `name`, `version`) USING BTREE
  ) ENGINE = InnoDB COMMENT = '组件表';

DROP TABLE
  IF EXISTS `r_material_category`;

CREATE TABLE
  `r_material_category` (
    `id` int not null AUTO_INCREMENT COMMENT '主键id',
    `material_id` int not null COMMENT '物料包id',
    `category_id` int not null COMMENT '业务分类id',
    PRIMARY KEY (`id`) USING BTREE
  ) ENGINE = InnoDB COMMENT = '物料包业务分类关系表';

DROP TABLE
  IF EXISTS `r_material_history_block`;

CREATE TABLE
  `r_material_history_block` (
    `id` int not null AUTO_INCREMENT COMMENT '主键id',
    `material_history_id` int not null COMMENT '物料包历史id',
    `block_history_id` int not null COMMENT '区块历史id',
    PRIMARY KEY (`id`) USING BTREE
  ) ENGINE = InnoDB COMMENT = '物料包历史区块关系表';

DROP TABLE
  IF EXISTS `r_material_component`;

CREATE TABLE
  `r_material_component` (
    `id` int not null AUTO_INCREMENT COMMENT '主键id',
    `material_id` int not null COMMENT '物料包id',
    `component_id` int not null COMMENT '组件id',
    PRIMARY KEY (`id`) USING BTREE
  ) ENGINE = InnoDB COMMENT = '物料包组件编辑态关系表';

DROP TABLE
  IF EXISTS `r_material_block`;

CREATE TABLE
  `r_material_block` (
    `id` int not null AUTO_INCREMENT COMMENT '主键id',
    `material_id` int not null COMMENT '物料包id',
    `block_id` int not null COMMENT '区块id',
    PRIMARY KEY (`id`) USING BTREE
  ) ENGINE = InnoDB COMMENT = '物料包区块编辑态关系表';

DROP TABLE
  IF EXISTS `t_i18n_entity`;

CREATE TABLE
  `t_i18n_entity` (
    `id` int not null AUTO_INCREMENT COMMENT '主键id',
    `key` varchar(255) not null COMMENT '国际化词条key',
    `content` varchar(3000) not null COMMENT '词条内容',
    `host_id` int not null COMMENT '关联的hostid： appId或blockId',
    `host_type` varchar(255) not null COMMENT 'app或者block',
    `lang_id` int COMMENT '关联语言id',
    `tenant_id` varchar(60) not null COMMENT '租户ID',
    `site_id` varchar(60) COMMENT '站点ID，设计预留字段',
    `created_by` varchar(60) not null COMMENT '创建人',
    `created_time` timestamp not null default CURRENT_TIMESTAMP COMMENT '创建时间',
    `last_updated_by` varchar(60) not null COMMENT '最后修改人',
    `last_updated_time` timestamp not null default CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `u_idx_i18n_entity` (`tenant_id`, `key`, `lang_id`) USING BTREE
  ) ENGINE = InnoDB COMMENT = '国际化语言配置表';

DROP TABLE
  IF EXISTS `t_18n_lang`;

CREATE TABLE
  `t_18n_lang` (
    `id` int not null AUTO_INCREMENT COMMENT '主键id',
    `lang` varchar(255) not null COMMENT '语言代码',
    `label` varchar(255) not null COMMENT '语言',
    `created_by` varchar(60) not null COMMENT '创建人',
    `created_time` timestamp not null default CURRENT_TIMESTAMP COMMENT '创建时间',
    `last_updated_by` varchar(60) not null COMMENT '最后修改人',
    `last_updated_time` timestamp not null default CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `unique_lang` (`lang`) USING BTREE
  ) ENGINE = InnoDB COMMENT = '国际化语言表，全局';

DROP TABLE
  IF EXISTS `t_datasource`;

CREATE TABLE
  `t_datasource` (
    `id` int not null AUTO_INCREMENT COMMENT '主键id',
    `name` varchar(255) not null COMMENT '数据源名称',
    `data` longtext COMMENT '数据源内容',
    `tpl` int COMMENT '设计预留字段',
    `platform_id` int COMMENT '关联设计器id',
    `description` varchar(2000) COMMENT '描述',
    `tenant_id` varchar(60) not null COMMENT '租户ID',
    `site_id` varchar(60) COMMENT '站点ID，设计预留字段',
    `created_by` varchar(60) not null COMMENT '创建人',
    `created_time` timestamp not null default CURRENT_TIMESTAMP COMMENT '创建时间',
    `last_updated_by` varchar(60) not null COMMENT '最后修改人',
    `last_updated_time` timestamp not null default CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `u_idx_datasource` (`tenant_id`, `platform_id`, `name`) USING BTREE
  ) ENGINE = InnoDB COMMENT = '数据源表';

DROP TABLE
  IF EXISTS `t_task_record`;

CREATE TABLE
  `t_task_record` (
    `id` int not null AUTO_INCREMENT COMMENT '主键id',
    `team_id` int COMMENT '团队id, 默认0',
    `task_type` int COMMENT '任务类型: 1 ASSETS_BUILD / 2 APP_BUILD / 3 PLATFORM_BUILD / 4 VSCODE_PLUGIN_BUILD/5 BLOCK_BUILD',
    `build_id` int COMMENT '构建资源id',
    `task_name` varchar(255) COMMENT '构建任务名称',
    `task_status` int COMMENT '任务状态：0 init / 1 running / 2 stopped / 3 finished',
    `task_result` longtext COMMENT '当前执行进度结果信息',
    `progress` varchar(255) COMMENT '当前进行的子任务名',
    `ratio` int COMMENT '无用字段',
    `progress_percent` int COMMENT '构建进度百分比数',
    `indicator` longtext COMMENT '构建指标',
    `tenant_id` varchar(60) not null COMMENT '租户ID',
    `site_id` varchar(60) COMMENT '站点ID，设计预留字段',
    `created_by` varchar(60) not null COMMENT '创建人',
    `created_time` timestamp not null default CURRENT_TIMESTAMP COMMENT '创建时间',
    `last_updated_by` varchar(60) not null COMMENT '最后修改人',
    `last_updated_time` timestamp not null default CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
  ) ENGINE = InnoDB COMMENT = '构建任务表';

DROP TABLE
  IF EXISTS `t_tenant`;

CREATE TABLE
  `t_tenant` (
    `id` int not null AUTO_INCREMENT COMMENT '主键id',
    `org_code` varchar(255) COMMENT '组织唯一代码',
    `name_cn` varchar(255) not null COMMENT '组织中文名',
    `name_en` varchar(255) COMMENT '组织英文名',
    `description` varchar(2000) COMMENT '组织描述',
    `created_by` varchar(60) not null COMMENT '创建人',
    `created_time` timestamp not null default CURRENT_TIMESTAMP COMMENT '创建时间',
    `last_updated_by` varchar(60) not null COMMENT '最后修改人',
    `last_updated_time` timestamp not null default CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `u_idx_tenant` (`name_cn`) USING BTREE
  ) ENGINE = InnoDB COMMENT = '租户表';

DROP TABLE
  IF EXISTS `t_user`;

CREATE TABLE
  `t_user` (
    `id` int not null AUTO_INCREMENT COMMENT '主键id',
    `username` varchar(255) not null COMMENT '用户名',
    `email` varchar(255) not null COMMENT '邮箱',
    `role` varchar(255) COMMENT '用户角色',
    `enable` tinyint(1) not null COMMENT '账号是否可用',
    `is_admin` tinyint(1) COMMENT '是否管理员',
    `is_public` tinyint(1) COMMENT '是否公共账号',
    `tenant_id` varchar(60) not null COMMENT '租户ID',
    `site_id` varchar(60) COMMENT '站点ID，设计预留字段',
    `created_by` varchar(60) not null COMMENT '创建人',
    `created_time` timestamp not null default CURRENT_TIMESTAMP COMMENT '创建时间',
    `last_updated_by` varchar(60) not null COMMENT '最后修改人',
    `last_updated_time` timestamp not null default CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `u_idx_user` (`username`) USING BTREE
  ) ENGINE = InnoDB COMMENT = '用户表';
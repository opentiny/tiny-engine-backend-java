drop table if exists `t_resource`;

create table `t_resource`
(
    `id`                int          not null auto_increment comment '主键id',
    `app_id`            int          not null comment '关联appId',
    `platform_id`       int          not null comment '关联设计器id',
    `name`              varchar(255) not null comment '名称',
    `thumbnail_name`    varchar(255) not null comment '缩略图名称',
    `resource_url`      varchar(255) comment '资源url',
    `thumbnail_url`     varchar(255) comment '缩略图url',
    `category`          varchar(255) not null comment '分类',
    `description`       varchar(2000) comment '描述',
    `thumbnail_data`    longtext comment '缩略图数据',
    `resource_data`     longtext comment '资源数据',
    `public_status`     int comment '公开状态：0，1，2',
    `is_default`        tinyint(1) comment '是否是默认',
    `created_by`        varchar(60)  not null comment '创建人',
    `created_time`      timestamp    not null default current_timestamp comment '创建时间',
    `last_updated_by`   varchar(60)  not null comment '最后修改人',
    `last_updated_time` timestamp    not null default current_timestamp comment '更新时间',
    `tenant_id`         varchar(60) comment '租户id',
    `renter_id`         varchar(60) comment '业务租户id',
    `site_id`           varchar(60) comment '站点id，设计预留字段',
    primary key (`id`) using btree,
    unique index `u_idx_resource` (`category`,`name`, `tenant_id`) using btree
) engine = innodb comment = '资源表';

drop table if exists `t_resource_group`;

create table `t_resource_group`
(
    `id`                int          not null auto_increment comment '主键id',
    `name`              varchar(255) not null comment '中文名称',
    `app_id`            int          not null comment '关联appId',
    `platform_id`       int          not null comment '关联设计器id',
    `description`       varchar(2000) comment '描述',
    `created_by`        varchar(60)  not null comment '创建人',
    `created_time`      timestamp    not null default current_timestamp comment '创建时间',
    `last_updated_by`   varchar(60)  not null comment '最后修改人',
    `last_updated_time` timestamp    not null default current_timestamp comment '更新时间',
    `tenant_id`         varchar(60) comment '租户id',
    `renter_id`         varchar(60) comment '业务租户id',
    `site_id`           varchar(60) comment '站点id，设计预留字段',
    primary key (`id`) using btree,
    unique index `u_idx_resource_group` (`name`,`app_id`, `tenant_id`) using btree
) engine = innodb comment = '资源分组表';

drop table if exists `r_resource_group_resource`;

CREATE TABLE `r_resource_group_resource`
(
    `id`                int NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `resource_id`       int NOT NULL COMMENT '资源id',
    `resource_group_id` int NOT NULL COMMENT '资源分组id',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `u_idx_resource_group_resource` (`resource_id`,`resource_group_id`) USING BTREE
) engine = innodb comment = '资源及资源分组关系表';
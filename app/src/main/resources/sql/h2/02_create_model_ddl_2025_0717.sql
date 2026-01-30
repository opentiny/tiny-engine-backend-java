drop table if exists `t_model`;

create table `t_model`
(
    `id`                int  not null auto_increment comment '主键id',
    `name_cn`           varchar(255) not null comment '中文名称',
    `name_en`           varchar(255) not null comment '英文名称',
    `version`           varchar(255) comment '版本',
    `model_url`         varchar(255) comment '模型url',
    `parameters`        varchar(2000) not null comment '字段参数',
    `method`            longtext  comment '方法',
    `description`       varchar(2000) comment '描述',
    `created_by`        varchar(60)  not null comment '创建人',
    `created_time`      timestamp    not null default current_timestamp comment '创建时间',
    `last_updated_by`   varchar(60)  not null comment '最后修改人',
    `last_updated_time` timestamp    not null default current_timestamp comment '更新时间',
    `tenant_id`         varchar(60) comment '租户id',
    `renter_id`         varchar(60) comment '业务租户id',
    `site_id`           varchar(60) comment '站点id，设计预留字段',
    primary key (`id`) using btree,
    unique index `u_idx_model` (`name_en`,`version`) using btree
) engine = innodb comment = '模型表';

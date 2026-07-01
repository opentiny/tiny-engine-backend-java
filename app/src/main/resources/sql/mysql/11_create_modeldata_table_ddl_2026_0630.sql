drop table if exists `t_model_data`;

create table `t_model_data`
(
    `id`                int  not null auto_increment comment '主键id',
    `model_id`          varchar(255) not null comment '模型id',
    `data_json`         json NOT NULL COMMENT '模型数据',
    `version`           varchar(255) comment '版本',
    `created_by`        varchar(60)  not null comment '创建人',
    `created_time`      timestamp    not null default current_timestamp comment '创建时间',
    `last_updated_by`   varchar(60)  not null comment '最后修改人',
    `last_updated_time` timestamp    not null default current_timestamp comment '更新时间',
    `tenant_id`         varchar(60) comment '租户id',
    `renter_id`         varchar(60) comment '业务租户id',
    `site_id`           varchar(60) comment '站点id，设计预留字段',
    primary key (`id`) using btree,
    unique index `u_idx_model_data` (`id`,`model_id`) using btree
) engine = innodb comment = '模型数据表';


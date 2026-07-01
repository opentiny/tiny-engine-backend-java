drop table if exists `t_api_key`;

create table `t_api_key`
(
    `id`                int  not null auto_increment comment '主键id',
    `api_key`           varchar(255) not null comment 'api_key',
    `api_secret`        varchar(255) comment '秘钥',
    `expire_time`        timestamp   not null  comment '过期时间',
    `tenant_id`         varchar(60) comment '租户id',
    `status`         int comment '业务租户id',
    primary key (`id`) using btree,
    unique index `u_idx_api_key` (`api_key`,`api_secret`) using btree
) engine = innodb comment = 'api_key表';


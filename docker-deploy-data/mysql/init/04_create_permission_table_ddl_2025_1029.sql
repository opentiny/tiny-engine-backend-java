drop table if exists `t_permission_role`;

create table `t_permission_role`
(
    `id`                int          not null auto_increment comment '主键id',
    `name`           varchar(255) not null comment '名称',
    `description`       varchar(2000) comment '描述',
    `created_by`        varchar(60)  not null comment '创建人',
    `created_time`      timestamp    not null default current_timestamp comment '创建时间',
    `last_updated_by`   varchar(60)  not null comment '最后修改人',
    `last_updated_time` timestamp    not null default current_timestamp comment '更新时间',
    primary key (`id`) using btree,
    unique index `u_idx_permission_role` (`name`) using btree
) engine = innodb comment = '';

drop table if exists `r_auth_users_units_roles`;

create table `r_auth_users_units_roles`
(
    `id`                int          not null auto_increment comment '主键id',
    `user_id`           int          not null comment '用户',
    `unit_id`           int          not null comment '业务单元',
    `unit_type`         varchar(60)  not null comment '业务单元类型',
    `tenant_id`         int          not null comment '组织id',
    `role_id`           int          not null comment '角色id',
    `expired_time`      timestamp  comment '过期时间',
    `created_by`        varchar(60)  not null comment '创建人',
    `created_time`      timestamp    not null default current_timestamp comment '创建时间',
    `last_updated_by`   varchar(60)  not null comment '最后修改人',
    `last_updated_time` timestamp    not null default current_timestamp comment '更新时间',
    primary key (`id`) using btree,
    unique index `u_idx_auth_users_units_roles` (`user_id`, `unit_id`, `unit_type`) using btree
) engine = innodb comment = '';


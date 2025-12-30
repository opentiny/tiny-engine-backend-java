ALTER TABLE t_tenant DROP INDEX u_idx_tenant;
ALTER TABLE t_tenant ADD UNIQUE INDEX u_idx_tenant (`name_en`);

ALTER TABLE t_tenant MODIFY COLUMN name_en VARCHAR(255) NOT NULL COMMENT '组织英文名';
ALTER TABLE t_tenant MODIFY COLUMN name_cn VARCHAR(255) NULL COMMENT '组织中文名';
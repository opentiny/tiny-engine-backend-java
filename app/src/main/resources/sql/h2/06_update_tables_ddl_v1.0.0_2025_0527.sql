ALTER TABLE t_block_group DROP INDEX u_idx_block_group;
ALTER TABLE t_block_group ADD INDEX u_idx_block_group (`tenant_id`, `platform_id`, `name`, `app_id`);
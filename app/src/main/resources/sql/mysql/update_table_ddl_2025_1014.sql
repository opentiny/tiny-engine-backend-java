ALTER TABLE t_app ADD COLUMN is_template VARCHAR(100) AFTER is_default;
ALTER TABLE t_app ADD COLUMN business_id INT AFTER platform_id;
ALTER TABLE t_business_category ADD COLUMN business_group VARCHAR(100) AFTER `name`;

ALTER TABLE t_app DROP INDEX u_idx_app;
ALTER TABLE t_app ADD INDEX u_idx_app (`tenant_id`, `platform_id`, `name`, `is_template`);


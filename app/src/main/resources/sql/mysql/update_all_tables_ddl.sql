ALTER TABLE t_component DROP INDEX u_idx_component;
ALTER TABLE t_component ADD INDEX u_idx_component (tenant_id, name_en, version, library_id);

ALTER TABLE t_datasource DROP INDEX u_idx_datasource;
ALTER TABLE t_datasource ADD INDEX u_idx_datasource (`tenant_id`, `platform_id`, `name`, `app_id`);

ALTER TABLE t_platform_history MODIFY sub_count int NULL;
ALTER TABLE t_platform_history MODIFY publish_url varchar(255) NULL;
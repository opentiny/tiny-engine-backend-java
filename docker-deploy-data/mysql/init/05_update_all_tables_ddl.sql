ALTER TABLE t_component DROP INDEX u_idx_component;
ALTER TABLE t_component ADD INDEX u_idx_component (tenant_id, name_en, version, library_id);

ALTER TABLE t_datasource DROP INDEX u_idx_datasource;
ALTER TABLE t_datasource ADD INDEX u_idx_datasource (`tenant_id`, `platform_id`, `name`, `app_id`);

ALTER TABLE t_platform_history MODIFY sub_count int NULL;
ALTER TABLE t_platform_history MODIFY publish_url varchar(255) NULL;

ALTER TABLE t_app MODIFY tenant_id varchar(60) NULL;
ALTER TABLE t_app_extension MODIFY tenant_id varchar(60) NULL;
ALTER TABLE t_block MODIFY tenant_id varchar(60) NULL;
ALTER TABLE t_block_carriers_relation MODIFY tenant_id varchar(60) NULL;
ALTER TABLE t_block_group MODIFY tenant_id varchar(60) NULL;
ALTER TABLE t_block_history MODIFY tenant_id varchar(60) NULL;
ALTER TABLE t_business_category MODIFY tenant_id varchar(60) NULL;
ALTER TABLE t_component MODIFY tenant_id varchar(60) NULL;
ALTER TABLE t_component_library MODIFY tenant_id varchar(60) NULL;
ALTER TABLE t_datasource MODIFY tenant_id varchar(60) NULL;
ALTER TABLE t_i18n_entry MODIFY tenant_id varchar(60) NULL;
ALTER TABLE t_material MODIFY tenant_id varchar(60) NULL;
ALTER TABLE t_material_history MODIFY tenant_id varchar(60) NULL;
ALTER TABLE t_page MODIFY tenant_id varchar(60) NULL;
ALTER TABLE t_page_history MODIFY tenant_id varchar(60) NULL;
ALTER TABLE t_page_template MODIFY tenant_id varchar(60) NULL;
ALTER TABLE t_platform MODIFY tenant_id varchar(60) NULL;
ALTER TABLE t_platform_history MODIFY tenant_id varchar(60) NULL;
ALTER TABLE t_task_record MODIFY tenant_id varchar(60) NULL;
ALTER TABLE t_user MODIFY tenant_id varchar(60) NULL;


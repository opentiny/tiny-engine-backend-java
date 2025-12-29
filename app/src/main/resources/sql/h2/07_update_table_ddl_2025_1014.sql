ALTER TABLE t_app
    ADD COLUMN is_template VARCHAR(100) AFTER is_default;
ALTER TABLE t_app
    ADD COLUMN industry_id INT AFTER platform_id;
ALTER TABLE t_app
    ADD COLUMN scene_id INT AFTER industry_id;

ALTER TABLE t_app DROP INDEX u_idx_app;
ALTER TABLE t_app
    ADD INDEX u_idx_app (`tenant_id`, `platform_id`, `name`, `is_template`);

ALTER TABLE t_business_category
    ADD COLUMN business_group VARCHAR(100) AFTER `name`;

ALTER TABLE t_datasource DROP INDEX u_idx_datasource;
ALTER TABLE t_datasource
    ADD INDEX u_idx_datasource (`tenant_id`, `platform_id`, `name`, `app_id`);

ALTER TABLE t_app_extension
    ADD COLUMN platform_id INT AFTER app_id;
ALTER TABLE t_app_extension DROP INDEX u_idx_app_extension;
ALTER TABLE t_app_extension
    ADD INDEX u_idx_app_extension (`tenant_id`, `platform_id`, `name`, `app_id`);

ALTER TABLE t_model
    ADD COLUMN app_id INT AFTER id;
ALTER TABLE t_model
    ADD COLUMN platform_id INT AFTER app_id;
ALTER TABLE t_model DROP INDEX u_idx_model;
ALTER TABLE t_model
    ADD INDEX u_idx_model (`tenant_id`, `platform_id`, `app_id`, `name_cn`,`version`);

ALTER TABLE t_user DROP COLUMN email, DROP COLUMN tenant_id, DROP COLUMN site_id,DROP COLUMN renter_id,DROP COLUMN created_by,DROP COLUMN last_updated_by;


ALTER TABLE t_user
    ADD COLUMN password VARCHAR(200) AFTER username;
ALTER TABLE t_user
    ADD COLUMN email VARCHAR(200) AFTER password;
ALTER TABLE t_user
    ADD COLUMN salt VARCHAR(200) AFTER password;
ALTER TABLE t_user
    ADD COLUMN public_key VARCHAR(200) AFTER salt;
ALTER TABLE t_user
    ADD COLUMN private_key VARCHAR(200) AFTER public_key;


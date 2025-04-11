ALTER TABLE t_component DROP INDEX u_idx_component;
ALTER TABLE t_component ADD INDEX u_idx_component (tenant_id, name_en, version, library_id);
/**
 * Copyright (c) 2023 - present TinyEngine Authors.
 * Copyright (c) 2023 - present Huawei Cloud Computing Technologies Co., Ltd.
 *
 * Use of this source code is governed by an MIT-style license.
 *
 * THE OPEN SOURCE SOFTWARE IN THIS PRODUCT IS DISTRIBUTED IN THE HOPE THAT IT WILL BE USEFUL,
 * BUT WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS FOR
 * A PARTICULAR PURPOSE. SEE THE APPLICABLE LICENSES FOR MORE DETAILS.
 *
 */

package com.tinyengine.it.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * SchemaDto
 *
 * @since 2024-10-20
 */
@Getter
@Setter
public class SchemaDto {
    private List<SchemaUtils> bridge;
    private List<Map<String, Object>> componentsMap;
    private List<ComponentTree> componentsTree;
    private Map<String, Object> config;
    private String constants;
    private String css;
    private SchemaDataSource dataSource;
    private SchemaI18n i18n;
    private SchemaMeta meta;
    private List<SchemaUtils> utils;
    private String version;
}

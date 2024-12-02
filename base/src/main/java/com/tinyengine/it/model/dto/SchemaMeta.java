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
 * SchemaMeta
 *
 * @since 2024-10-20
 */
@Getter
@Setter
public class SchemaMeta {
    private String appId;
    private String branch;
    private String creator;
    private String description;
    private String name;
    private String gitGroup;
    private List<Map<String, Object>> globalState;
    private String projectName;
    private Integer tenant;
    private String gmtCreate;
    private String gmtModified;
    private String isDemo;
}

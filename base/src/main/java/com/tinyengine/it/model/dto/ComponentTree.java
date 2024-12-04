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
 * ComponentTree
 *
 * @since 2024-10-20
 */
@Getter
@Setter
public class ComponentTree {
    private List<Object> children;
    private String css;
    private String componentName;
    private String fileName;
    private Map<String, Object> lifeCycles;
    private Map<String, Object> meta;
    private Map<String, Object> methods;
    private Map<String, Object> props;
    private Map<String, Object> state;
}

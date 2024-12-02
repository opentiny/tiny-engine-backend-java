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

import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The type Schema config.
 *
 * @since 2024-10-20
 */
@Data
public class SchemaConfig {
    // AppConfig
    private Map<String, String> appConvert;
    private List<String> appInclude;
    private Map<String, String> appFormat;

    // PageMetaConfig
    private Map<String, String> pageMetaConvert;
    private List<String> pageMetaInclude;
    private Map<String, String> pageMetaFormat;

    // PageContentConfig
    private List<String> pageContentInclude;

    // FolderConfig
    private Map<String, String> folderConvert;
    private List<String> folderInclude;
    private Map<String, String> folderFormat;

    /**
     * Instantiates a new Schema config.
     */
    // Corrected constructor
    public SchemaConfig() {
        // Initialize AppConfig
        this.appConvert = Stream
                .of(new Object[][]{{"id", "appId"}, {"createdBy", "creator"}, {"created_at", "gmt_create"},
                        {"updated_at", "gmt_modified"}})
                .filter(data -> data[0] instanceof String && data[1] instanceof String) // 使用 instanceof 过滤
                .collect(Collectors.toMap(data -> (String) data[0], data -> (String) data[1]));

        this.appInclude = Arrays.asList("id", "name", "tenant", "git_group", "project_name", "is_demo", "description",
                "createdBy", "created_at", "updated_at", "branch", "global_state");

        this.appFormat = Stream.of(new Object[][]{{"id", "toFormatString"},
                        {"created_at", "toLocalTimestamp"},
                        {"updated_at", "toLocalTimestamp"},
                        {"createdBy", "toCreatorName"},
                        {"global_state", "toArrayValue"}})
                .filter(data -> data[0] instanceof String && data[1] instanceof String) // 使用 instanceof 过滤
                .collect(Collectors.toMap(data -> (String) data[0], data -> (String) data[1]));

        // Initialize PageMetaConfig
        this.pageMetaConvert = Stream
                .of(new Object[][]{{"page_desc", "description"}, {"route", "router"}, {"isBody", "rootElement"},
                        {"createdBy", "creator"}, {"created_at", "gmt_create"}, {"updated_at", "gmt_modified"}})
                .filter(data -> data[0] instanceof String && data[1] instanceof String) // 使用 instanceof 过滤
                .collect(Collectors.toMap(data -> (String) data[0], data -> (String) data[1]));

        this.pageMetaInclude = Arrays.asList("id", "title", "page_desc", "createdBy", "parentId", "created_at",
                "updated_at", "isHome", "isBody", "group", "route", "occupier");

        this.pageMetaFormat = Stream
                .of(new Object[][]{{"created_at", "toLocalTimestamp"}, {"updated_at", "toLocalTimestamp"},
                        {"isBody", "toRootElement"}, {"group", "toGroupName"}, {"createdBy", "toCreatorName"}})
                .filter(data -> data[0] instanceof String && data[1] instanceof String) // 使用 instanceof 过滤
                .collect(Collectors.toMap(data -> (String) data[0], data -> (String) data[1]));

        // Initialize PageContentConfig
        this.pageContentInclude = Arrays.asList("fileName", "componentName", "props", "css", "children", "methods",
                "state", "lifeCycles");

        // Initialize FolderConfig
        this.folderConvert = Stream
                .of(new Object[][]{{"name", "folderName"}, {"route", "router"}, {"created_at", "gmt_create"},
                        {"updated_at", "gmt_modified"}})
                .filter(data -> data[0] instanceof String && data[1] instanceof String) // 使用 instanceof 过滤
                .collect(Collectors.toMap(data -> (String) data[0], data -> (String) data[1]));

        this.folderInclude = Arrays.asList("name", "route", "created_at", "updated_at", "id", "parentId", "depth");

        this.folderFormat = Stream
                .of(new Object[][]{{"created_at", "toLocalTimestamp"}, {"updated_at", "toLocalTimestamp"}})
                .filter(data -> data[0] instanceof String && data[1] instanceof String) // 使用 instanceof 过滤
                .collect(Collectors.toMap(data -> (String) data[0], data -> (String) data[1]));
    }
}


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
                .collect(Collectors.toMap(data -> (String) data[0], data -> (String) data[1]));

        this.appInclude = Arrays.asList("id", "name", "tenant", "git_group", "project_name", "is_demo", "description",
                "createdBy", "created_at", "updated_at", "branch", "global_state");

        this.appFormat = Stream.of(new Object[][]{{"id", "toFormatString"}, {"created_at", "toLocalTimestamp"},
                {"updated_at", "toLocalTimestamp"}, {"createdBy", "toCreatorName"}, {"global_state", "toArrayValue"}})
                .collect(Collectors.toMap(data -> (String) data[0], data -> (String) data[1]));

        // Initialize PageMetaConfig
        this.pageMetaConvert = Stream
                .of(new Object[][]{{"page_desc", "description"}, {"route", "router"}, {"isBody", "rootElement"},
                        {"createdBy", "creator"}, {"created_at", "gmt_create"}, {"updated_at", "gmt_modified"}})
                .collect(Collectors.toMap(data -> (String) data[0], data -> (String) data[1]));

        this.pageMetaInclude = Arrays.asList("id", "title", "page_desc", "createdBy", "parentId", "created_at",
                "updated_at", "isHome", "isBody", "group", "route", "occupier");

        this.pageMetaFormat = Stream
                .of(new Object[][]{{"created_at", "toLocalTimestamp"}, {"updated_at", "toLocalTimestamp"},
                        {"isBody", "toRootElement"}, {"group", "toGroupName"}, {"createdBy", "toCreatorName"}})
                .collect(Collectors.toMap(data -> (String) data[0], data -> (String) data[1]));

        // Initialize PageContentConfig
        this.pageContentInclude = Arrays.asList("fileName", "componentName", "props", "css", "children", "methods",
                "state", "lifeCycles");

        // Initialize FolderConfig
        this.folderConvert = Stream
                .of(new Object[][]{{"name", "folderName"}, {"route", "router"}, {"created_at", "gmt_create"},
                        {"updated_at", "gmt_modified"}})
                .collect(Collectors.toMap(data -> (String) data[0], data -> (String) data[1]));

        this.folderInclude = Arrays.asList("name", "route", "created_at", "updated_at", "id", "parentId", "depth");

        this.folderFormat = Stream
                .of(new Object[][]{{"created_at", "toLocalTimestamp"}, {"updated_at", "toLocalTimestamp"}})
                .collect(Collectors.toMap(data -> (String) data[0], data -> (String) data[1]));
    }
}

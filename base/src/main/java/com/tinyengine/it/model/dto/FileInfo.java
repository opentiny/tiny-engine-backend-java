package com.tinyengine.it.model.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * The type I18n file dto.
 *
 * @since 2024-10-20
 */
@Getter
@Setter
public class FileInfo {
    private String name;
    private String content;
    private Boolean isDirectory;

    /**
     * Instantiates a new i18n file  body dto.
     *
     * @param name the name
     * @param isDirectory the isDirectory
     * @param content the content
     */
    public FileInfo(String name, String content, boolean isDirectory) {
        this.name = name;
        this.content = content;
        this.isDirectory = isDirectory;
    }
}
package com.tinyengine.it.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileInfo {
    private String name;
    private String content;
    private boolean isDirectory;

    public FileInfo(String name, String content, boolean isDirectory) {
        this.name = name;
        this.content = content;
        this.isDirectory = isDirectory;
    }

}
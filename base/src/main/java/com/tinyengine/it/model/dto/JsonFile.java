package com.tinyengine.it.model.dto;

import lombok.Data;

import java.util.Map;

/**
 * JsonFile
 *
 * @since 2024-11-13
 */
@Data
public class JsonFile {
    private String fileName;
    private Map<String,Object> fileContent;
}

package com.tinyengine.it.rag.entity;

import lombok.Data;

import java.util.Map;

@Data
public class DocumentMetadata {
    private String docName;
    private String content;
    private Map<String, Object> metadata;
    private long timestamp;


    public DocumentMetadata() {

    }
}
